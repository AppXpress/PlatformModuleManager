var TERMINATING_CHAR = "|";
var OLD_NEW_SPLIT = ":";
var LINE_CHILD_SPLIT = ":";
var KEY_VALUE_SPLIT = "\"=";
var ADDED = "M\"";
var MODIFIED = "M\"";
var DELETED = "M\"";
var FULLY_ADDED = "A\"";
var FULLY_DELETED = "D\"";
var FULL_DELETE = "*D*";
var FULL_ADD = "*A*";
var FORMAT_EMPTY = "null";
var LEVEL_HEADER = "Header";
var LEVEL_LINE = "Line";
var LEVEL_SUBLINE = "Subline";
var ITEM_TYPE_CODE_COMPONENT = "Component";
var debugString = "";
var shouldSave = false;
var xPathToNamer = {};
var MAX_CUSTOM_REF_LENGTH = 4096;
var MAX_CUSTOM_REF_ERROR = "ERROR:MXLNGTH";
var reportName = "CriticalField"; //Default report name and populating variable

function populateCriticalFields(orderFolder, eventName, params) {
   
   if (hasValue(orderFolder.orderTerms) && hasValue(orderFolder.orderTerms.reference)) {
      //Gets either the current Active PO if this is a POA, or gets the yet-to-be-active PO document if this is a new PO
      var poFetch = Providers.getPersistenceProvider().createFetchRequest("Order400", orderFolder.uid);
      var poFetchResults;
      if (hasValue(orderFolder.uid)) {
         poFetchResults = Providers.getPersistenceProvider().execute(poFetch); 
      }
      
      /*
      var isAmend = true;
      var poaQuery = Providers.getQueryProvider().createQuery("OrderXml400");
      poaQuery.setOQL("uid=" + orderFolder.uid + " AND version=amendment");
      poaQuery.setExpansion("detail");
      var poaQueryResults = Providers.getQueryProvider().execute(poaQuery); 
      if (!hasValue(poaQuery)) {
         isAmend = false;
      }
      //debugString = debugString.concat("isAmend = " + isAmend + ";");
      */
      
      var criticalFieldsQuery = Providers.getQueryProvider().createQuery("$CriticalFieldsQ1");
      criticalFieldsQuery.setOQL("ReportStatus = 'Active'");
      var criticalFieldsResults = Providers.getQueryProvider().execute(criticalFieldsQuery);
      
      if (hasElements(criticalFieldsResults)) {
         var activeOrder = poFetchResults;
         var criticalFieldResult = criticalFieldsResults[0];
         if (hasValue(activeOrder) && hasValue(criticalFieldResult)) {
            if (hasValue(criticalFieldResult.ReportName)) {
               reportName = criticalFieldResult.ReportName;
            }
            //Null out existing fields
            orderFolder.orderTerms.reference[reportName] = "";
            
            var headerXpaths = new Array();
            var lineXpaths = new Array();
            var sublineXpaths = new Array();
            var headerDifferences = new Array();
            var lineDifferences = new Array();
            var sublineDifferences = new Array();
            var wholeLineAddsDeletes = new Array();
            var anyCriticalChanges = false;
            
            //Process all the XPaths so that they are relative path xPaths
            if (hasElements(criticalFieldResult.XPaths)) {
               var xPathText = null;
               var xPathObj = null;
               var critFieldResultCtr = 0;
               for (critFieldResultCtr = 0; critFieldResultCtr < criticalFieldResult.XPaths.length; critFieldResultCtr++) {
                  
                  xPathObj = criticalFieldResult.XPaths[critFieldResultCtr];
                  if (hasValue(xPathObj)) {
                     xPathText = xPathObj.XPath;
                     if (hasValue(xPathText) && typeof(xPathText) == 'string') {
                        var relativeXpath = "";
                        
                        if (xPathText.indexOf("orderItem/orderItem/") == 0) { //Subline
                           relativeXpath = xPathText.substring(20);
                           sublineXpaths.push(relativeXpath);
                           //debugString = debugString.concat("sublineXpaths.push("+relativeXpath+");");
                        } else if (xPathText.indexOf("orderItem/") == 0) { //Main line
                           relativeXpath = xPathText.substring(10);
                           lineXpaths.push(relativeXpath);
                           //debugString = debugString.concat("lineXpaths.push("+relativeXpath+");");
                        } else { //Header
                           relativeXpath = xPathText;
                           headerXpaths.push(relativeXpath);
                           //debugString = debugString.concat("headerXpaths.push("+relativeXpath+");");
                        }
                        //Generate map of xPaths to namers
                        var xPathNamer = makeNamerString(xPathObj);
                        if (hasValue(xPathNamer)) {
                           xPathToNamer[relativeXpath] = xPathNamer;
                           //debugString = debugString.concat(relativeXpath + "=" + xPathNamer + ", ");
                        }
                     }
                  }
               } //end criticalFieldResult.XPaths loop
            }
            
            var headerDifferences = analyzeFields(activeOrder, orderFolder, headerXpaths, "", LEVEL_HEADER);
            var headerDifferenceText = "";
            if (hasElements(headerDifferences)) {
               anyCriticalChanges = true;
               //debugString = debugString.concat("Found header differences {" + headerDifferences.length + "};");
            }
            
            var headerDiffCtr = 0;
            for (headerDiffCtr; headerDiffCtr < headerDifferences.length; headerDiffCtr++) {
               var headerDifference = headerDifferences[headerDiffCtr];
               var textToAdd = arrayToCritString(headerDifference);
               if (hasValue(textToAdd)) {
                  headerDifferenceText = headerDifferenceText.concat(textToAdd);
               }
            } //end headerDifferences loop
            
            
            //Find full adds and deletes
            var fullChanges = analyzeLineAddDeletes(activeOrder, orderFolder);
            //debugString = debugString.concat("fullChanges.length = {" + fullChanges.length + "};");
            
            var fullChangesCtr = 0;
            for (fullChangesCtr; fullChangesCtr < fullChanges.length; fullChangesCtr++) {
               var fullChangeDifference = fullChanges[fullChangesCtr];
               var fullChangeText = arrayToCritString(fullChangeDifference);
               if (hasValue(fullChangeText)) {
                  headerDifferenceText = headerDifferenceText.concat(fullChangeText);
                  //debugString = debugString.concat("Found full diff: " + fullChangeText +";");
               }
            } //end fullChanges loop
            
            //Remove trailing terminating character
            if (hasValue(headerDifferenceText)) {
               headerDifferenceText = headerDifferenceText.slice(0, (headerDifferenceText.length-1));
            }
            //Set header level field if needed
            if (headerDifferenceText != orderFolder.orderTerms.reference[reportName]) {
               shouldSave = true;
               orderFolder.orderTerms.reference[reportName] = truncate(headerDifferenceText, MAX_CUSTOM_REF_LENGTH, MAX_CUSTOM_REF_ERROR);
            }
            
            //Process line items
            var lineItems = orderFolder.orderItem;
            var activeLineItems = activeOrder.orderItem;
            var lineItemCtr = 0;
            if (!hasElements(lineItems)) {
               lineItems = new Array();
            }
            for (lineItemCtr; lineItemCtr < lineItems.length; lineItemCtr++) {
               lineItem = lineItems[lineItemCtr];
               
               if (hasValue(lineItem) && hasValue(lineItem.baseItem.itemIdentifier) && hasValue(lineItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
                  var activeLine = getMatchingLine(lineItem, activeLineItems);
                  var lineDifferences = analyzeFields(activeLine, lineItem, lineXpaths, "", LEVEL_LINE);
                  var lineDifferenceText = "";
                  
                  if (hasElements(lineDifferences)) {
                     anyCriticalChanges = true;
                     //debugString = debugString.concat("Found line differences {" + lineDifferences.length + "};");
                  }
                  var lineDiffCtr = 0;
                  for (lineDiffCtr; lineDiffCtr < lineDifferences.length; lineDiffCtr++) {
                     var lineDifference = lineDifferences[lineDiffCtr];
                     var textToAdd = arrayToCritString(lineDifference);
                     if (hasValue(textToAdd)) {
                        lineDifferenceText = lineDifferenceText.concat(textToAdd);
                     }
                  } //end lineDifferences loop
                  
                  //Remove trailing terminating character
                  if (hasValue(lineDifferenceText)) {
                     lineDifferenceText = lineDifferenceText.slice(0, (lineDifferenceText.length-1));
                  }
                  //Set line level field if needed
                  if (hasValue(lineItem.baseItem) && hasValue(lineItem.baseItem.itemIdentifier)) {
                     if (lineDifferenceText != lineItem.baseItem.itemIdentifier[reportName]) {
                        shouldSave = true;
                        lineItem.baseItem.itemIdentifier[reportName] = truncate(lineDifferenceText, MAX_CUSTOM_REF_LENGTH, MAX_CUSTOM_REF_ERROR);
                     }
                  }
                  
                  //Subline items
                  var sublineItems = lineItem.orderItem;
                  if (!hasElements(sublineItems)) {
                     sublineItems = new Array();
                  }
                  var activeSublineItems = new Array();
                  if (hasValue(activeLine) && hasElements(activeLine.orderItem)) {
                     activeSublineItems = activeLine.orderItem;
                  }
                  var sublineItemCtr = 0;
                  for (sublineItemCtr; sublineItemCtr < sublineItems.length; sublineItemCtr++) {
                     sublineItem = sublineItems[sublineItemCtr];
                     if (hasValue(sublineItem) && hasValue(sublineItem.baseItem) && !ITEM_TYPE_CODE_COMPONENT.equals(sublineItem.baseItem.itemTypeCode)) {
                        var activeSubline = getMatchingLine(sublineItem, activeSublineItems);
                        var sublineDifferences = analyzeFields(activeSubline, sublineItem, sublineXpaths, lineItem.baseItem.itemIdentifier.ItemSequenceNumber, LEVEL_SUBLINE);
                        var sublineDifferenceText = "";
                        
                        if (hasElements(sublineDifferences)) {
                           anyCriticalChanges = true;
                           //debugString = debugString.concat("Found subline differences {" + sublineDifferences.length + "};");
                        }
                        var sublineDiffCtr = 0;
                        for (sublineDiffCtr; sublineDiffCtr < sublineDifferences.length; sublineDiffCtr++) {
                           var sublineDifference = sublineDifferences[sublineDiffCtr];
                           var textToAdd = arrayToCritString(sublineDifference);
                           if (hasValue(textToAdd)) {
                              sublineDifferenceText = sublineDifferenceText.concat(textToAdd);
                           }
                        } //end sublineDifferences loop
                        
                        
                        //Remove trailing terminating character
                        if (hasValue(sublineDifferenceText)) {
                           sublineDifferenceText = sublineDifferenceText.slice(0, (sublineDifferenceText.length-1));
                        }
                        //Set line level field if needed
                        if (hasValue(sublineItem.baseItem) && hasValue(sublineItem.baseItem.itemIdentifier)) {
                           if (sublineDifferenceText != sublineItem.baseItem.itemIdentifier[reportName]) {
                              shouldSave = true;
                              sublineItem.baseItem.itemIdentifier[reportName] = truncate(sublineDifferenceText, MAX_CUSTOM_REF_LENGTH, MAX_CUSTOM_REF_ERROR);
                           }
                        }
                     }
                  } //end sublineItems loop
               }
            } //end lineItems loop
         } else {
            if (!hasValue(activeOrder)) {
               throw ("activeOrder can't be found");
            }
            if (!hasValue(criticalFieldResult)) {
               throw ("criticalFieldResult is empty");
            }
         }
      } else {
         throw("No critical fields custom object runtime found!");
      }
      orderFolder.orderTerms.reference.Debug = debugString;
      if (shouldSave) {
         Providers.getPersistenceProvider().save(orderFolder);
      }
      //throw("DEBUG - Not an actual exception");
   }
   Providers.getPersistenceProvider().save(orderFolder);
} //end populateCriticalFields

function makeNamerString(xPathObj) {
   var namerString = null;
   if (hasValue(xPathObj) && hasValue(xPathObj.XPath)) {
      //debugString = debugString.concat("makeNamerString{1};");
      var xPathText = xPathObj.XPath;
      namerString = xPathObj.DisplayValue;
      if (!hasValue(namerString)) {
         //debugString = debugString.concat("makeNamerString{2};");
         //Use default formatting
         var xPathSplit = xPathText.split("/");
         //Get last segment and capitalize the first letter
         namerString = xPathSplit[(xPathSplit.length - 1)];
         namerString = namerString.charAt(0).toUpperCase() + namerString.slice(1);
         //debugString = debugString.concat("makeNamerString{3}="+namerString+";");
      }
   }
   return namerString;
}

function analyzeLineAddDeletes(activeOrder, newOrder) {
   var differences = new Array();
   var activeItemNumbers = getAllItemKeys(activeOrder);
   var newItemNumbers = getAllItemKeys(newOrder);
   var activeItemNumbersSet = getItemKeyToLevel(activeOrder);
   var newItemNumbersSet = getItemKeyToLevel(newOrder);
   
   //Find delete items
   var itemCtr;
   for (itemCtr = 0; itemCtr < activeItemNumbers.length; itemCtr++) {
      var activeKey = activeItemNumbers[itemCtr];
      
      if (!hasValue(newItemNumbersSet[activeKey])) {
         var diffObject = new Object();
         
         diffObject.xPath = FULL_DELETE;
         diffObject.existingValue = activeKey;
         diffObject.newValue = null;
         diffObject.level = activeItemNumbersSet[activeKey];
         diffObject.fullReplace = true;
         differences.push(diffObject);
         //debugString = debugString.concat("Found Full Delete: " + activeKey + ";");
      }
   } //end end removed item loop
   
   //Find added items
   for (itemCtr = 0; itemCtr < newItemNumbers.length; itemCtr++) {
      var newKey = newItemNumbers[itemCtr];
      
      if (!hasValue(activeItemNumbersSet[newKey])) {
         var diffObject = new Object();
         
         diffObject.xPath = FULL_ADD;
         diffObject.existingValue = null;
         diffObject.newValue = newKey;
         diffObject.level = newItemNumbersSet[newKey];
         diffObject.fullReplace = true;
         differences.push(diffObject);
         //debugString = debugString.concat("Found Full Add: " + newKey + ";");
      }
   } //end end added item loop
   
   return differences;
}

function getItemKeyToLevel(order) {
   var returnValue = {};
   
   if (hasValue(order) && hasElements(order.orderItem)) {
      var lineCtr = 0;
      
      for (lineCtr; lineCtr < order.orderItem.length; lineCtr++) {
         var lineItem = order.orderItem[lineCtr];
         var lineKey = "";
         
         if (hasValue(lineItem) && hasValue(lineItem.baseItem.itemIdentifier) && hasValue(lineItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
            lineKey = lineItem.baseItem.itemIdentifier.ItemSequenceNumber;
            returnValue[lineKey] = LEVEL_LINE;
         }
         if (hasValue(lineItem) && hasElements(lineItem.orderItem)) {
            var sublineItems = lineItem.orderItem;
            var sublineCtr = 0;
            
            for (sublineCtr; sublineCtr < sublineItems.length; sublineCtr++) {
               var sublineItem = sublineItems[sublineCtr];
               var sublineKey = "";
               
               if (hasValue(sublineItem) && hasValue(sublineItem.baseItem.itemIdentifier) && hasValue(sublineItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
                  sublineKey = lineKey + LINE_CHILD_SPLIT + sublineItem.baseItem.itemIdentifier.ItemSequenceNumber;
                  returnValue[sublineKey] = LEVEL_SUBLINE;
               }
            } //end sublineItems loop
         }
      } //end lineItems loop
   }
   
   return returnValue;
}

/**
 * Gets all item keys - ignores any component items
 */
function getAllItemKeys(order) {
   var itemKeys = new Array();
   
   if (hasValue(order) && hasElements(order.orderItem)) {
      var lineCtr = 0;
      
      for (lineCtr; lineCtr < order.orderItem.length; lineCtr++) {
         var lineItem = order.orderItem[lineCtr];
         var lineKey = "";
         
         if (hasValue(lineItem) && hasValue(lineItem.baseItem.itemIdentifier) && hasValue(lineItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
            lineKey = lineItem.baseItem.itemIdentifier.ItemSequenceNumber;
            itemKeys.push(lineKey);
            //debugString = debugString.concat("getAllItemKeys push line: " + lineKey +";");
         }
         if (hasValue(lineItem) && hasElements(lineItem.orderItem)) {
            var sublineItems = lineItem.orderItem;
            var sublineCtr = 0;
            
            for (sublineCtr; sublineCtr < sublineItems.length; sublineCtr++) {
               var sublineItem = sublineItems[sublineCtr];
               var sublineKey = "";
               
               if (hasValue(sublineItem) && hasValue(sublineItem.baseItem) && hasValue(sublineItem.baseItem.itemIdentifier) && 
                   !ITEM_TYPE_CODE_COMPONENT.equals(sublineItem.baseItem.itemTypeCode) &&
                   hasValue(sublineItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
                  sublineKey = lineKey + LINE_CHILD_SPLIT + sublineItem.baseItem.itemIdentifier.ItemSequenceNumber;
                  itemKeys.push(sublineKey);
                  //debugString = debugString.concat("getAllItemKeys push subline: " + sublineKey +";");
               }
            } //end sublineItems loop
         }
      } //end lineItems loop
   }
   
   return itemKeys;
} //end getAllItemKeys

function arrayToCritString(diffObject) {
   var critString = "";
   //Separate logic for full change
   if (hasValue(diffObject)) {
      var activeField = diffObject.existingValue;
      var newValue = diffObject.newValue;
      var xPath = diffObject.xPath;
      var xPathNamer = xPathToNamer[xPath];
      
      //Full replace (add or removed lines) will be treated differently
      if (diffObject.fullReplace == true) {
         var changeCode = null;
         var itemKey = null;
         var level = diffObject.level;
         if (xPath == FULL_ADD) {
            changeCode = FULLY_ADDED;
            itemKey = diffObject.newValue;
         } else if (xPath == FULL_DELETE) {
            changeCode = FULLY_DELETED;
            itemKey = diffObject.existingValue;
         }
         critString = changeCode + level + KEY_VALUE_SPLIT + itemKey + TERMINATING_CHAR;
      } else if (hasValue(activeField) || hasValue(newValue)) {
         var changeCode = null;
         //Figure out what kind of change it was
         if (!hasValue(activeField) && hasValue(newValue)) {
            changeCode = ADDED;
         } else if (hasValue(activeField) && !hasValue(newValue)) {
            changeCode = DELETED;
         } else {
            changeCode = MODIFIED;
         }
         
         if (!hasValue(activeField)) {
            activeField = FORMAT_EMPTY;
         }
         if (!hasValue(newValue)) {
            newValue = FORMAT_EMPTY;
         }
         critString = changeCode + xPathNamer + KEY_VALUE_SPLIT + activeField + OLD_NEW_SPLIT + newValue + TERMINATING_CHAR;
      }
   }
   return critString;

} //end arrayToCritString;

/**
 * Returns an array of fields with differences
 */
function analyzeFields(activeDoc, currentDoc, xPaths, fullReplacePrefix, objectLevel) {
   var criticalDifferences = new Array();
   /*The code commented out checks if the line was just newly added or deleted
   if (!hasValue(fullReplacePrefix)) {
      fullReplacePrefix = "";
   }
   
   //debugString = debugString.concat("xPaths.length = " + xPaths.length + ";");
   if (!hasValue(activeDoc) && hasValue(currentDoc)) { //New object
      var diffObject = new Object();
      diffObject.xPath = FULL_ADD;
      diffObject.existingValue = null;
      if (hasValue(fullReplacePrefix)) {
         diffObject.newValue = fullReplacePrefix + LINE_CHILD_SPLIT + currentDoc.itemKey;
      } else {
         diffObject.newValue = currentDoc.itemKey;
      }
      diffObject.level = objectLevel;
      diffObject.fullReplace = true;
      criticalDifferences.push(diffObject);
   } else if (hasValue(activeDoc) && !hasValue(currentDoc)) { //Deleted object
      var diffObject = new Object();
      diffObject.xPath = FULL_DELETE;
      if (hasValue(fullReplacePrefix)) {
         diffObject.existingValue = fullReplacePrefix + LINE_CHILD_SPLIT + activeDoc.itemKey;
      } else {
         diffObject.existingValue = currentDoc.itemKey;
      }
      diffObject.newValue = null;
      diffObject.level = objectLevel;
      diffObject.fullReplace = true;
      criticalDifferences.push(diffObject);
   } else */
   if (hasValue(currentDoc) && hasElements(xPaths)) {
      var analyzeFieldCtr = 0;
      for (analyzeFieldCtr; analyzeFieldCtr < xPaths.length; analyzeFieldCtr++) {
         var xPath = xPaths[analyzeFieldCtr];
         var existingValue = getFieldForRelativeXPath(xPath, activeDoc);
         var newValue = getFieldForRelativeXPath(xPath, currentDoc);
         var diffObject = new Object();
         
         if (existingValue != newValue) {
            diffObject.xPath = xPath;
            diffObject.existingValue = existingValue;
            diffObject.newValue = newValue;
            diffObject.level = objectLevel;
            diffObject.fullReplace = false;
            criticalDifferences.push(diffObject);
         }
      } //end xPaths loop
   }
   
   return criticalDifferences;
} //end analyzeFields

function getMatchingLine(knownItem, listOfItems) {
   var matchingLine = null;
   //debugString = debugString.concat("getMatchingLine{1} " + knownItem.baseItem.itemIdentifier.ItemSequenceNumber + ";");
   if (hasValue(knownItem) && hasElements(listOfItems) && hasValue(knownItem.baseItem.itemIdentifier) && hasValue(knownItem.baseItem.itemIdentifier.ItemSequenceNumber)) {
      var knownItemKey = knownItem.baseItem.itemIdentifier.ItemSequenceNumber;
      
      //debugString = debugString.concat("getMatchingLine{2};");
      var matchingLineCtr = 0;
      for (matchingLineCtr = 0; matchingLineCtr < listOfItems.length; matchingLineCtr++) {
         //debugString = debugString.concat("getMatchingLine{3} " + matchingLineCtr + ";");
         var itemToCompare = listOfItems[matchingLineCtr];
         if (hasValue(itemToCompare) && hasValue(itemToCompare.baseItem.itemIdentifier)) {
            
            if (knownItemKey == itemToCompare.baseItem.itemIdentifier.ItemSequenceNumber) {
               //Match found
               //debugString = debugString.concat("getMatchingLine{4} " + knownItemKey + ":" + itemToCompare.baseItem.itemIdentifier.ItemSequenceNumber + ";");
               matchingLine = itemToCompare;
               break;
            }
         }
      } //end listOfItems loop
   }
   return matchingLine;
}

function getFieldForRelativeXPath(xPath, object) {
   var objectPointer = null;
   if (hasValue(xPath) && hasValue(object)) {
      var splitXpath = xPath.split("/");
      
      var splitPathCtr = 0;
      //debugString = debugString.concat("xPath = " + xPath + " object = " + object.toString() + ";");
      for (splitPathCtr = 0; splitPathCtr < splitXpath.length; splitPathCtr++) {
         var nextPath = splitXpath[splitPathCtr];
         //debugString = debugString.concat("nextPath = " + nextPath + ";");
         if (hasValue(nextPath)) {
            if (objectPointer == null) { //Purposefully not using hasValue
               objectPointer = object;
            }
            objectPointer = objectPointer[nextPath];
            //debugString = debugString.concat("objectPointer[" + nextPath + "] = " + objectPointer + ";");
            //Don't keep going through the xPath if the precursor element doesn't exist
            if (!hasValue(objectPointer)) {
               break;
            }
         }
      } //end splitXpath loop
   }
   return objectPointer;
} //end getFieldForRelativeXPath

function hasValue(input) {
   return (input != null && input != undefined && "" != input);
}

function hasElements(input) {
   return (input != null && input != undefined && input.length > 0);
}

function truncate(text, length, errorMessage) {
   var returnValue = text;
   if (hasValue(text) && hasValue(length)) {
      if (hasValue(errorMessage)) {
         if (text.length > length) {
            returnValue = errorMessage;
         } else {
            returnValue = text;
         }
      } else {
         returnValue = text.substring(0, Math.min(length, text.length));
      }
   }
   return returnValue;
}









