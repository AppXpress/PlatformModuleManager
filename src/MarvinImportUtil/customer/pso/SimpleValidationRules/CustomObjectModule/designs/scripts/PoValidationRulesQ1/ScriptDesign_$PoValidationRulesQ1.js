/**
 *   C H A N G E    L  O G
 * 
 *  (B)ug/(E)nh/(I)DB #    Date      Who  Description
 *  -------------------  ----------  ---  ---------------------------------------------------------------
 *   CORE-FIXES          04/22/2013  EH   Core fixes
 */

var REQUIRED = true;
var NOT_REQUIRED = false;
var IS_KEY = true;
var IS_VALUE = false;
var HEADER_REF_PREFIX = "orderTerms/reference/";
var HEADER_REF_SUFFIX = "";
var LINE_REF_PREFIX = "baseItem/itemIdentifier/";
var LINE_REF_SUFFIX = "";
var SUBLINE_REF_PREFIX = "baseItem/itemIdentifier/";
var SUBLINE_REF_SUFFIX = "";
var CODE_TO_XPATH = new Object(); //Cached when needed
var CODE_TO_XPATH_GLTYPE = "$CodeToXpathQ2";
var DOCUMENT_TYPE = "Order";
var EXPORT_IMPORT_DELIM = "\t";

function populateCort(customObject, eventName, params) {
   var requireSave = false;
   if (hasValue(customObject)) {
      //Rule Key
      var newRuleKey = generateRuleKey(customObject);
      if (newRuleKey != customObject.RuleKey) {
         customObject.RuleKey = newRuleKey;
         requireSave = true;
      }
      
      var headerErrors = customObject.HeaderErrors;
      var lineErrors = customObject.LineErrors;
      var sublineErrors = customObject.SublineErrors;
      
      if (populateErrorConfigs(headerErrors, HEADER_REF_PREFIX, HEADER_REF_SUFFIX) == true) {
         requireSave = true;
      }
      if (populateErrorConfigs(lineErrors, LINE_REF_PREFIX, LINE_REF_SUFFIX) == true) {
         requireSave = true;
      }
      if (populateErrorConfigs(sublineErrors, SUBLINE_REF_PREFIX, SUBLINE_REF_SUFFIX) == true) {
         requireSave = true;
      }
      
      if (requireSave == true) {
         Providers.getPersistenceProvider().save(customObject);
      }
      //throw("DEBUG - Not an actual exception Population");
   }
} //end populateCort

function populateErrorConfigs(levelErrors, refPrefix, refSuffix) {
   var requireSave = false;
   
   if (hasValue(levelErrors)) {
      var levelCtr = 0;
      for (levelCtr = 0; levelCtr < levelErrors.length; levelCtr++) {
         var levelError = levelErrors[levelCtr];
         
         //XPath
         var xPath = buildXPath(levelError, refPrefix, refSuffix);
         if (xPath != levelError.XPath) {
            levelError.XPath = xPath;
            requireSave = true;
         }
         //System Regex
         var systemRegex = buildRegex(levelError);
         if (systemRegex != levelError.SystemRegex) {
            levelError.SystemRegex = systemRegex;
            requireSave = true;
         }
      } //end headerErrors loop
   }
   
   return requireSave;
} //end populateErrorConfigs

/**
 * Validates the object
 */
function validateCort(customObject, eventName, params) {

   //Duplicate tables
   if (hasActiveTable(customObject) == true) {
      Providers.getMessageProvider().error(customObject, "There is already an Active instance of this table for the same buyer/seller combination. Please deactivate it before proceeding.");
      //throw("There is already an Active instance of this table for the same buyer/seller combination. Please deactivate it before proceeding.");
   }
   
   if (!hasValue(customObject.BuyerParty)) {
      Providers.getMessageProvider().error(customObject, "You must select an appropriate Buyer party for this rule");
   }
   
   var headerErrors = customObject.HeaderErrors;
   var headerHasErrors = validateErrorConfigs("Header", headerErrors, customObject);
   
   var lineErrors = customObject.LineErrors;
   var lineHasErrors = validateErrorConfigs("Line", lineErrors, customObject);
   
   var sublineErrors = customObject.SublineErrors;
   var sublineHasErrors = validateErrorConfigs("Subline", sublineErrors, customObject);
   
   
   var returnValue = "noErrors";
   if (headerHasErrors == true || lineHasErrors || sublineHasErrors) {
      returnValue = "errors";
   }
   //throw("DEBUG - Not an actual exception - Validation");
   return returnValue;

} //end validateCort

function validateErrorConfigs(levelName, errorConfigs, errorObject) {
   var hasErrors = false;
   
   if (hasValue(errorConfigs)) {
      var errorCtr = 0;
      for (errorCtr = 0; errorCtr < errorConfigs.length; errorCtr++) {
         var levelError = errorConfigs[errorCtr];
         if (hasValue(levelError)) {
            var errorIndex = "Element #" + (errorCtr + 1);
            
            //Regex
            if (!validRegex(levelError.SystemRegex)) {
               Providers.getMessageProvider().error(errorObject, levelName + " error [" + errorIndex + "]: This doesn't have a valid regular expression. Reconfigure the Custom Regex.");
               hasErrors = true;
            }
            if (!hasValue(levelError.SystemRegex)) {
               Providers.getMessageProvider().error(errorObject, levelName + " error [" + errorIndex + "]: There is no value in the System Regex. If you used the standard and custom regex, please choose one or the other.");
               hasErrors = true;
            }
            
            //XPath
            if (!hasValue(levelError.XPath)) {
               Providers.getMessageProvider().error(errorObject, levelName + " error [" + errorIndex + "]: Please make sure either Standard Field or a Custom Reference Key has a value. XPath can\'t be determined.");
               hasErrors = true;
            }
            
            //Error Message
            if (!hasValue(levelError.ErrorMessage)) {
               Providers.getMessageProvider().error(errorObject, levelName + " error [" + errorIndex + "]: Please enter an Error Message.");
               hasErrors = true;
            }
         }
      } //end headerErrors loop
   }
   
   return hasErrors;
} //end validateErrorConfigs

/**
 * Runs on save
 */
function validateOnSave(customObject, eventName, params) {
   //Run Validations
   if ("Active" == customObject.Status && "errors" == validateCort(customObject, eventName, params)) {
      customObject.Status = "FailedValidate";
      Providers.getPersistenceProvider().save(customObject);
   }
}


/**
 * Renders the keys for the rule key, uses the buyer org concatenated with the seller org and 
 * hyphen in between.
 */
function generateRuleKey(customObject) {
   var buyerOrgId = "";
   var sellerOrgId = "";
   if (hasValue(customObject.BuyerParty) && hasValue(customObject.BuyerParty.organization)) {
      buyerOrgId = customObject.BuyerParty.organization;
   } else {
      throw("BuyerParty had no organization");
   }
   if (hasValue(customObject.SellerParty) && hasValue(customObject.SellerParty.organization)) {
      sellerOrgId = customObject.SellerParty.organization;
   }
   return buyerOrgId + "-" + sellerOrgId;
} //end generateRuleKey

/**
 * Builds the xPath for the appropriate field, whether it's a custom reference or a standard field.
 */
function buildXPath(levelError, prefix, suffix) {
   var returnValue = "";
   if (hasValue(levelError.StandardField) && !hasValue(levelError.CustomReferenceKey)) { //standard field
      returnValue = getXpathForCode(levelError.StandardField);
   } else if (!hasValue(levelError.StandardField) && hasValue(levelError.CustomReferenceKey)) { //custom ref
      returnValue = prefix + levelError.CustomReferenceKey + suffix;
   }
   
   return returnValue;
} //buildXPath

function buildRegex(levelError) {
   var returnValue = "";
   
   var hasCustomRegex = hasValue(levelError.CustomRegex);
   var hasStandardRegex = (hasValue(levelError.MinFieldLength) || hasValue(levelError.MaxFieldLength) || 
                          "true" == levelError.TextAllowed || "true" == levelError.NumbersAllowed || 
                          "true" == levelError.MatchAnywhere || "true" == levelError.ErrorWhenMatch ||
                          hasValue(levelError.CharactersAllowed));
   var newRegex = "";
   if (hasCustomRegex && !hasStandardRegex) {
      newRegex = levelError.CustomRegex;
      
   } else if (!hasCustomRegex && hasStandardRegex) {
      var matchAnywhere = levelError.MatchAnywhere === true || levelError.MatchAnywhere == "true";
      if (!matchAnywhere) {
         newRegex = "^";
      }
      newRegex = newRegex.concat("[");
      if (levelError.TextAllowed == "true") {
         newRegex = newRegex.concat("a-zA-Z");
      }
      if (levelError.NumbersAllowed == "true") {
         newRegex = newRegex.concat("0-9");
      }
      if (hasValue(levelError.CharactersAllowed)) {
         //Escape any regular expression fields when using characters allowed
         var charactersAllowed = levelError.CharactersAllowed.replace(/[\"\'\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
         newRegex = newRegex.concat(charactersAllowed);
      }
      newRegex = newRegex.concat("]{");
      if (hasValue(levelError.MinFieldLength)) { //Min
         newRegex = newRegex.concat(levelError.MinFieldLength + ",");
      } else {
         newRegex = newRegex.concat("0,");
      }
      if (hasValue(levelError.MaxFieldLength)) { //Max
         newRegex = newRegex.concat(levelError.MaxFieldLength);
      }
      newRegex = newRegex.concat("}");
      if (!matchAnywhere) {
         newRegex = newRegex.concat("$");
      }
   }
   returnValue = newRegex;
   
   return returnValue;
} //buildRegex

/**
 * Check if the string for the regular expression is valid
 */
function validRegex(regex) {
   var returnValue = false;

   try {
      var regexTest = new RegExp(regex);
      returnValue = true;
   } catch (e) {
      //Do not set if there's an error
   }
   
   return returnValue;
}

/**
 * Checks if there's an existing Active Custom Table
 */
function hasActiveTable(customObject) {
   var status = customObject.FieldValidationStatus;
   var ruleKey = customObject.RuleKey;
   var tableName = customObject.type;
   var returnValue = false;
   
   if ( hasValue(tableName) && hasValue(status) && hasValue(ruleKey) ) {
      //Look for other Active tables
      var query = Providers.getQueryProvider().createQuery(tableName);
      query.setOQL("FieldValidationStatus = 'Active' AND RuleKey = '" + ruleKey + "'");
      var otherActiveTable = Providers.getQueryProvider().execute(query);
      
      if (hasValue(otherActiveTable)) {
         var matchingTables = otherActiveTable.result || otherActiveTable;
         if (hasValue(matchingTables)) {
            var matchingTableCtr = 0;
            for (matchingTableCtr = 0; matchingTableCtr < matchingTables.length; matchingTableCtr++) {
               var matchingTable = matchingTables[matchingTableCtr];
               if (hasValue(matchingTable) && hasValue(matchingTable.uid) && customObject.uid != matchingTable.uid) {
                  returnValue = true;
               }
            } //end matchingTables loop
         } else if (hasValue(matchingTables) && hasValue(matchingTables.uid) && customObject.uid != matchingTables.uid) { //Only one element returned
            returnValue = true;
         }
      }
   }
   
   return returnValue;
} //end hasActiveTable


function getXpathForCode(code) {
   var returnValue = null;
   
   //Query the table and put key/value pairs into a map if not instantiated
   if (CODE_TO_XPATH.lookedUp !== true) { //typed comparison
      var query = Providers.getQueryProvider().createQuery(CODE_TO_XPATH_GLTYPE);
      query.setOQL("TableStatus = 'Active' AND DocumentType = '" + DOCUMENT_TYPE + "'");
      var queryResult = Providers.getQueryProvider().execute(query);
      
      if (hasValue(queryResult)) {
         queryResult = queryResult.result || queryResult;
         if (hasValue(queryResult)) { //In case results were in an array, get first element
            queryResult = queryResult[0];
         }
         if (hasValue(queryResult.Elements)) {
            var elements = queryResult.Elements;
            var elementsCtr = 0;
            
            for (elementsCtr = 0; elementsCtr < elements.length; elementsCtr++) {
               var codeToXpath = elements[elementsCtr];
               if (hasValue(codeToXpath) && hasValue(codeToXpath.Code)) {
                  CODE_TO_XPATH[codeToXpath.Code] = codeToXpath.XPath;
               }
            } //end elements loop
         }
      }
      
      CODE_TO_XPATH.lookedUp = true;
   }
   
   if (hasValue(code) && hasValue(CODE_TO_XPATH[code])) {
      returnValue = CODE_TO_XPATH[code];
   }
   
   return returnValue;
} //end getXpathForCode

function poValidationRuleCreate(customObject, eventName, params) {
   if (hasValue(customObject)) {
      if (!hasValue(customObject.PastedText) || !hasValue(customObject.PastedText.Name)) {
         customObject.PastedText = new Object();
         customObject.PastedText.Name = "PoValidationRules";
         Providers.getPersistenceProvider().save(customObject);
      }
   }
}

function importPasted(customObject, eventName, params) {
   var newObjects = null;
   if (hasValue(customObject.PastedText)) {
      try {
         newObjects = jsonParse(customObject.PastedText.Text);
      } catch (err) {
         customObject.PastedText.Text = "ERROR! " + err;
         Providers.getPersistenceProvider().save(customObject);
      }
   }
   if (hasValue(newObjects)) {
      if (hasValue(newObjects.HeaderErrors)) {
         customObject.HeaderErrors = newObjects.HeaderErrors;
      }
      if (hasValue(newObjects.LineErrors)) {
         customObject.LineErrors = newObjects.LineErrors;
      }
      if (hasValue(newObjects.SublineErrors)) {
         customObject.SublineErrors = newObjects.SublineErrors;
      }
      customObject.PastedText.Text = "";
      Providers.getPersistenceProvider().save(customObject);
   }
}

function exportPasted(customObject, eventName, params) {
   if (hasValue(customObject)) {
      if (!hasValue(customObject.PastedText)) {
         customObject.PastedText = new Object();
      }
   }
   customObject.PastedText.Text = "";
   var jsonText ="{";
   if (customObject.HeaderErrors) {
      var potentialComma = customObject.LineErrors ? "," : "";
      jsonText = jsonText.concat("\"HeaderErrors\": " + jsonStringify(customObject.HeaderErrors) + potentialComma);
   }
   if (customObject.LineErrors) {
      var potentialComma = customObject.SublineErrors ? "," : "";
      jsonText = jsonText.concat("\"LineErrors\": " + jsonStringify(customObject.LineErrors) + potentialComma);
   }
   if (customObject.SublineErrors) {
      jsonText = jsonText.concat("\"SublineErrors\": " + jsonStringify(customObject.SublineErrors));
   }
   jsonText = jsonText.concat("}");
   customObject.PastedText.Text = jsonText;
   
   Providers.getPersistenceProvider().save(customObject);
}


