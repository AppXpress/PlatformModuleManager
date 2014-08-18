var GLTYPE_FIELD_VLD_HOLDER = "$PoValidationRulesQ1";
var REGEX_ESCAPE_CHAR = "\\";
var ERR_MSG_REPLACE_START = "\\{";
var ERR_MSG_REPLACE_END = "\\}";
var ERR_MSG_FIELD_VALUE = "value";
var ERR_MSG_FIELD_ITEMKEY = "itemKey";
var ARRAY_INDICATOR = "*";
var debugString = "";

function validatePoFields(orderFolder, eventName, params) {
   //var po = Providers.getPersistenceProvider().createFetchRequest("Order", orderFolder.uid).execute();
   
   var partyKeys = getPartyKeys(orderFolder);
   //debugString = debugString.concat("partyKeys = " + partyKeys + ";");
   var fieldValidationHolderResult = getMultipleGlobalObjectTypes(GLTYPE_FIELD_VLD_HOLDER, partyKeys);
   
   if (hasValue(fieldValidationHolderResult)) {
      var vldResultCtr = 0;
      
      for (vldResultCtr = 0; vldResultCtr < fieldValidationHolderResult.length; vldResultCtr++) {
         var fieldValidation = fieldValidationHolderResult[vldResultCtr];
         var headerValidations = fieldValidation.HeaderErrors;
         var lineValidations = fieldValidation.LineErrors;
         var sublineValidations = fieldValidation.SublineErrors;
         
         //debugString = debugString.concat("header;");
         examineErrorsOnLevel(headerValidations, orderFolder); //Examine header level fields
         
         var lineItems = orderFolder.orderItem;
         if (hasValue(lineItems)) {
            var lineItemCtr = 0;
            
            for (lineItemCtr = 0; lineItemCtr < lineItems.length; lineItemCtr++) {
               //debugString = debugString.concat("lineItem #" + (lineItemCtr+1) + "/" + lineItems.length + ";");
               var lineItem = lineItems[lineItemCtr];
               if (hasValue(lineItem)) {
                  examineErrorsOnLevel(lineValidations, lineItem); //Examine line level fields
                  
                  var sublineItems = lineItem.orderItem;
                  if (hasValue(sublineItems)) {
                     var sublineItemCtr = 0;
                     
                     for (sublineItemCtr = 0; sublineItemCtr < sublineItems.length; sublineItemCtr++) {
                        //debugString = debugString.concat("sublineItem #" + (sublineItemCtr+1) + "/" + sublineItems.length + ";");
                        var sublineItem = sublineItems[sublineItemCtr];
                        
                        if (hasValue(sublineItem )) {
                           examineErrorsOnLevel(sublineValidations, sublineItem); //Examine subline level fields
                        }
                     } //end sublineItems loop
                  }
               }
            } //end lineItems loop
         }
      } //end fieldValidationHolderResult loop
   }
   
   //orderFolder.orderTerms.reference.Debug = debugString;
   //throw("DEBUG - Not an actual exception.       debugString =  " + debugString);
} //end validatePoFields

function examineErrorsOnLevel(levelValidations, documentToExamine) {
   if (hasValue(levelValidations)) {
      var vldCtr = 0;
      for (vldCtr = 0; vldCtr < levelValidations.length; vldCtr++) {
         var validation = levelValidations[vldCtr];
         var pattern = new RegExp(validation.SystemRegex);
         var fieldsToExamine = getFieldsForRelativeXPath(validation.XPath, documentToExamine); //Possible multiple outputs
         var errorWhenMatch = validation.ErrorWhenMatch == "true" || validation.ErrorWhenMatch === true; //if true, only error when the regexp matches the field
         //debugString = debugString.concat("documentToExamine[" + validation.XPath +"] = " + fieldToExamine + ";");
         
         if (hasValue(fieldsToExamine)) {
            var fieldsToExamineCtr = 0;
            for (fieldsToExamineCtr = 0; fieldsToExamineCtr < fieldsToExamine.length; fieldsToExamineCtr++) {
               var fieldToExamine = fieldsToExamine[fieldsToExamineCtr];
               if (hasValue(fieldToExamine) == false) { //Null prevention, but allow empty string if the regex allows it
                  fieldToExamine = "";
               }
               var matchResults = fieldToExamine.match(pattern);
               
               if ((matchResults == null !== errorWhenMatch) && hasValue(validation.ErrorMessage)) {
                  //Replace {value}
                  var errorMessage = validation.ErrorMessage.replace(new RegExp(ERR_MSG_REPLACE_START + ERR_MSG_FIELD_VALUE + ERR_MSG_REPLACE_END), fieldToExamine);
                  //Replace {itemKey}
                  errorMessage = errorMessage.replace(new RegExp(ERR_MSG_REPLACE_START + ERR_MSG_FIELD_ITEMKEY + ERR_MSG_REPLACE_END), getFieldsForRelativeXPath(ERR_MSG_FIELD_ITEMKEY, documentToExamine));
                  Providers.getMessageProvider().error(documentToExamine, errorMessage);
               }
            } //end fieldsToExamine loop
         } else { //no elements, check against empty
               var matchResults = "".match(pattern);
               
               if ((matchResults == null !== errorWhenMatch) && hasValue(validation.ErrorMessage)) {
                  //Replace {value}
                  var errorMessage = validation.ErrorMessage.replace(new RegExp(ERR_MSG_REPLACE_START + ERR_MSG_FIELD_VALUE + ERR_MSG_REPLACE_END), fieldToExamine);
                  //Replace {itemKey}
                  errorMessage = errorMessage.replace(new RegExp(ERR_MSG_REPLACE_START + ERR_MSG_FIELD_ITEMKEY + ERR_MSG_REPLACE_END), getFieldsForRelativeXPath(ERR_MSG_FIELD_ITEMKEY, documentToExamine));
                  Providers.getMessageProvider().error(documentToExamine, errorMessage);
               }
         }
      } //end levelValidations loop
   }
} //end examineErrorsOnLevel

function getMultipleGlobalObjectTypes(globalObjectType, partyKeys) {
   var returnValue = null;
   if (hasValue(partyKeys) && partyKeys.length > 1) {
      //Check the buyer is actually the buyer - per Mark, not sure why
      var customObjectQuery = Providers.getQueryProvider().createQuery(globalObjectType);
      customObjectQuery.setOQL("FieldValidationStatus = 'Active' AND (RuleKey = '" + partyKeys[0] + "' OR RuleKey = '" + partyKeys[1] + "')");
      var customObjectQueryResult = Providers.getQueryProvider().execute(customObjectQuery);
      
      if (hasValue(customObjectQueryResult.result) || hasValue(customObjectQueryResult)) {
         returnValue = customObjectQueryResult.result || customObjectQueryResult;
      }
   }
   //May have an issue when returning single element vs array - may not be in an array on the return
   return returnValue;
} //end validatePoFields

/**
 * Generates the identifying keys to find the set of rules needed for this document.
 * Includes just the buyer or the buyer and seller.
 */
function getPartyKeys(orderFolder) {
   var returnValue = new Array();
   var buyerId = "";
   var sellerId = "";
   
   if (orderFolder.buyer) {
      buyerId = orderFolder.buyer.organizationUid;
   }
   if (orderFolder.seller) {
      sellerId = orderFolder.seller.organizationUid;
   }
   returnValue.push(buyerId + "-");
   returnValue.push(buyerId + "-" + sellerId);
   
   return returnValue;
} //end getPartyKeys

function getFieldForRelativeXPath(xPath, field) {
   var fieldPointer = null;
   if (hasValue(xPath) && hasValue(field)) {
      var splitXpath = xPath.split("/");
      
      var splitPathCtr = 0;
      //debugString = debugString.concat("xPath = " + xPath + " field = " + field.toString() + ";");
      for (splitPathCtr = 0; splitPathCtr < splitXpath.length; splitPathCtr++) {
         var nextPath = parseInt(splitXpath[splitPathCtr]) || splitXpath[splitPathCtr]; //will parse if integer for array
         //debugString = debugString.concat("nextPath = " + nextPath + ";");
         if (hasValue(nextPath)) {
            if (fieldPointer == null) { //Purposefully not using hasValue
               fieldPointer = field;
            }
            fieldPointer = fieldPointer[nextPath];
            //debugString = debugString.concat("fieldPointer[" + nextPath + "] = " + fieldPointer + ";");
            //Don't keep going through the xPath if the precursor element doesn't exist
            if (!hasValue(fieldPointer)) {
               break;
            }
         }
      } //end splitXpath loop
   }
   return fieldPointer;
} //end getFieldForRelativeXPath

function getFieldsForRelativeXPath(xPath, field) {
   var fieldPointers = new Array();
   if (hasValue(xPath) && hasValue(field)) {
      var indexOfSplit = xPath.indexOf("/*/");
      if (indexOfSplit == -1) {
         //Not possible to contain multiple values, just return the single value
         fieldPointers.push(getFieldForRelativeXPath(xPath, field));
      } else {
         //debugString = debugString.concat("Detected * in " + xPath + ";");
         //Possible to have multiple values, run through loops, only assumes one star.
         //var xPathSplitByArray = xPath.split(/\/\*\/(.+)?/); //Greedy split JSLint doesn't like this
         var xPathSplitByArray = new Array();
         xPathSplitByArray[0] = xPath.substring(0, indexOfSplit);
         xPathSplitByArray[1] = xPath.substring(indexOfSplit+3);

         var listOfObjects = getFieldForRelativeXPath(xPathSplitByArray[0], field);
         //debugString = debugString.concat("xPathSplitByArray = (" + xPathSplitByArray[0] + ", " + xPathSplitByArray[1] + ");listOfObjects = " + listOfObjects.length + ";");
         //Always verify at least one value, if there's more than one verify those
         if (hasValue(listOfObjects) && hasValue(xPathSplitByArray[1])) {
            var listOfObjectsCtr = 0;
            do {
               var singleElement = listOfObjects[listOfObjectsCtr];
               if (hasValue(singleElement)) {
                  var singleField = getFieldForRelativeXPath(xPathSplitByArray[1], singleElement);
                  //debugString = debugString.concat("singleField = " + singleField + ";");
                  //Regardless of whether or not the field was found, add it to the array
                  fieldPointers.push(singleField);
               }
               listOfObjectsCtr += 1;
            } while (listOfObjectsCtr < listOfObjects.length);
            //end listOfObjects
         }
      }
   }
   return fieldPointers;
} //end getFieldsForRelativeXPath

/**************** UTILITY FUNCTIONS ****************/

function validDouble(initValue) {
   var returnValue = false;
   if (hasValue(initValue)) {
      returnValue = !isNaN(parseFloat(initValue));
   }
   return returnValue;
}

function validInteger(initValue) {
   var returnValue = false;
   if (hasValue(initValue)) {
      returnValue = !isNaN(parseInt(initValue));
   }
   return returnValue;
}

function validDate(initValue) {
   var returnValue = false;
   if (hasValue(initValue)) {
      var dateRegex = new RegExp("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
      returnValue = initValue.match(dateRegex) != null;
   }
   return returnValue;
}

function isBoolean(initValue) {
   var returnValue = false;
   if (hasValue(initValue)) {
      returnValue = ("true" == initValue || "false" == initValue);
   }
   return returnValue;
}

function hasValue(input) {
   var returnValue = false;
   if (input != null && input != undefined) {
      if (typeof input === "string") { //String
         returnValue = ("" != input);
      } else if (typeof input === "object" && input instanceof Array && typeof input.length === "number") { //Array
         returnValue = (input.length > 0);
      } else {
         returnValue = true;
      }
   }
   return returnValue;
}

function hasValue(input) {
   var returnValue = false;
   if (input != null && input != undefined) {
      if (typeof input === "string") { //String
         returnValue = ("" != input);
      } else if (typeof input === "object" && input instanceof Array && typeof input.length === "number") { //Array
         returnValue = (input.length > 0);
      } else {
         returnValue = true;
      }
   }
   return returnValue;
}

/**
 * JSON related scripts
 * JSON stringify taken from: https://github.com/douglascrockford/JSON-js/blob/master/json2.js
 * JSON parse taken from: https://github.com/douglascrockford/JSON-java
 * Code was modified to fit into the GT Nexus usage (especially parse)
 */
var JSON_IGNORE_SET = { //ignore these fields in any object globally when processing JSON
   "uid" : true
};
var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
    escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
    gap,
    indent,
    meta = {    // table of character substitutions
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
    },
    rep;

function jsonStringify(value) {

   // The stringify method takes a value and an optional replacer, and an optional
   // space parameter, and returns a JSON text. The replacer can be a function
   // that can replace values, or an array of strings that will select the keys.
   // A default replacer method can be provided. Use of the space parameter can
   // produce text that is more easily readable.

   gap = '';
   indent = '';

   // Make a fake root object containing our value under the key of ''.
   // Return the result of stringifying the value.

   return jsonStr('', {
      '': value
   });
}

function jsonStr(key, holder) {

   // Produce a string from holder[key].

   var i, // The loop counter.
      k, // The member key.
      v, // The member value.
      length,
      mind = gap,
      partial,
      value = holder[key];

   // If the value has a toJSON method, call it to obtain a replacement value.

   if (value) {
      if (typeof value === "Date") {
         value = isFinite(this.valueOf())
                   ? this.getUTCFullYear()     + '-' +
                       jsonF(this.getUTCMonth() + 1) + '-' +
                       jsonF(this.getUTCDate())      + 'T' +
                       jsonF(this.getUTCHours())     + ':' +
                       jsonF(this.getUTCMinutes())   + ':' +
                       jsonF(this.getUTCSeconds())   + 'Z'
                   : null;
      } else if (typeof value === "boolean" || typeof value === "string" || typeof value === "number") {
         value = value.valueOf();
      }
   }

   // What happens next depends on the value's type.

   switch (typeof value) {
   case 'string':
      return jsonQuote(value);

   case 'number':

      // JSON numbers must be finite. Encode non-finite numbers as null.

      return isFinite(value) ? String(value) : 'null';

   case 'boolean':
   case 'null':

      // If the value is a boolean or null, convert it to a string. Note:
      // typeof null does not produce 'null'. The case is included here in
      // the remote chance that this gets fixed someday.

      return String(value);

      // If the type is 'object', we might be dealing with an object or an array or
      // null.

   case 'object':

      // Due to a specification blunder in ECMAScript, typeof null is 'object',
      // so watch out for that case.

      if (!value) {
         return 'null';
      }

      // Make an array to hold the partial results of stringifying this object value.

      gap += indent;
      partial = [];

      // Is the value an array?

      if (Object.prototype.toString.apply(value) === '[object Array]') {

         // The value is an array. Stringify every element. Use null as a placeholder
         // for non-JSON values.

         length = value.length;
         for (i = 0; i < length; i += 1) {
            partial[i] = jsonStr(i, value) || 'null';
         }

         // Join all of the elements together, separated with commas, and wrap them in
         // brackets.

         v = partial.length === 0 ? '[]' : gap ? '[' + gap + partial.join(',' + gap) + mind + ']' : '[' + partial.join(',') + ']';
         gap = mind;
         return v;
      }


      // Iterate through all of the keys in the object.

      for (k in value) {
         if (Object.prototype.hasOwnProperty.call(value, k) && !JSON_IGNORE_SET.hasOwnProperty(k)) {
            v = jsonStr(k, value);
            if (v) {
               //partial.push(jsonQuote(k) + (gap ? ': ' : ':') + v);
               partial.push(jsonQuote(k) + ':' + v);
            }
         }
      }

      // Join all of the member texts together, separated with commas,
      // and wrap them in braces.

      v = partial.length === 0 ? '{}' : gap ? '{' + gap + partial.join(',' + gap) + mind + '}' : '{' + partial.join(',') + '}';
      gap = mind;
      return v;
   }
}

function jsonF(n) {
   // Format integers to have at least two digits.
   return n < 10 ? '0' + n : n;
}

function jsonQuote(string) {

   // If the string contains no control characters, no quote characters, and no
   // backslash characters, then we can safely slap some quotes around it.
   // Otherwise we must also replace the offending characters with safe escape
   // sequences.

   escapable.lastIndex = 0;
   return escapable.test(string) ? '"' + string.replace(escapable, function (a) {
      var c = meta[a];
      return typeof c === 'string' ? c : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
   }) + '"' : '"' + string + '"';
}


/**
 * JSON Parse
 */
var jsonFullString;
var jsonEof;
var jsonIndex;
var jsonUsePrevious;
var jsonLine;
var jsonCharacter;
var jsonPrevious;

/**
 * Moves the pointer back one position
 * @throws Exception if you are trying to go back twice or we're at the beginning of the file already.
 */
function jsonBack() {
   if (jsonUsePrevious === true) {
      throw ("jsonBack(): Stepping back two steps is not supported. line=" + jsonLine+ "; char=" + jsonCharacter);
   }
   if (jsonIndex <= 0) {
      throw ("jsonBack(): Can't step any further back. line=" + jsonLine+ "; char=" + jsonCharacter);
   }
   
   jsonIndex--;
   jsonCharacter--;
   jsonUsePrevious = true;
   jsonEof = false;
}

/**
 * Returns the next character
 * @return character that's next, or thei nt 0 if there's nothing left
 */
function jsonNext() {
   var c;
   
   if (jsonUsePrevious === true) {
      jsonUsePrevious = false;
      c = jsonPrevious;
   } else {
      c = jsonGetChar(jsonIndex);
      if (c == null) {
         c = 0;
      } else {
         jsonPrevious = c;
      }
      
      if (jsonIndex >= jsonFullString.length) {
         jsonEof = true;
         c = 0;
      }
   }
   
   jsonIndex++;
   
   if (jsonPeekBack() == '\r') {
      jsonLine++;
      jsonCharacter = c == '\n' ? 0 : 1; //If the current character is \n then don't consider it the first yet
   } else if (c == '\n') {
      jsonLine++;
      jsonCharacter = 0;
   } else {
      jsonCharacter++;
   }
   
   return c;
}

/**
 * Get the next indicated characters.
 *
 * @param strLength The number of characters to take.
 * @return A string of n characters.
 * @throws Exception
 *   Substring bounds error if there are not
 *   n characters remaining in the source string.
 */
function jsonNextFew(strLength) {
   if (strLength == 0) {
      return "";
   }
   
   var chars = "";
   var pos = 0;
   
   while (pos < strLength) {
      chars = chars.concat(jsonNext());
      if (jsonEof === true) {
         pos = strLength; //terminate while loop
      }
      pos++;
   }
   
   return chars;
}

/**
 * Get the next char in the string, skipping whitespace.
 * @return
 */
function jsonNextClean() {
   var c = jsonNext();
   while (c !== 0 && c <= ' ') {
      c = jsonNext();
   }
   return c;
}

/**
 * Gets the next object, string or array
 * @return javascript object, string or array that is next
 * @throws Exception if the text is malformed.
 */
function jsonNextValue() {
   var c = jsonNextClean();
   var returnValue = null;
   switch (c) {
   case '"':
   case '\'':
      returnValue = jsonNextString(c);
      break;
   case '{':
      jsonBack();
      returnValue = jsonNextObject();
      break;
   case '[':
      jsonBack();
      returnValue = jsonNextList();
      break;
   }
   
   /*
    * Handle unquoted text. This could be the values true, false, or
    * null, or it can be a number. An implementation (such as this one)
    * is allowed to also accept non-standard forms.
    *
    * Accumulate characters until we reach the end of the text or a
    * formatting character.
    */
   if (returnValue == null) {
      returnValue = "";
      
      while (c <= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
         returnValue = returnValue.concat(c);
         c = jsonNext();
      }
      jsonBack();
      
      var oldReturnValue = returnValue;
      returnValue = returnValue.trim(); //trim
      if (returnValue.length == 0) {
         var charCodeAscii = "";
         var i = 0;
         for (i = 0; i < oldReturnValue.length; i++) {
            charCodeAscii = charCodeAscii.concat(oldReturnValue.charCodeAt(i) + ",");
         }
         throw ("jsonNextValue(): returnValue is empty. [" + oldReturnValue + "] {" + debugString + "} (" + charCodeAscii + ") line=" + jsonLine+ "; char=" + jsonCharacter);
      }
   }
   return returnValue;
} //end jsonNextValue

/**
 * Return the characters up to the next close quote character.
 * Backslash processing is done. The formal JSON format does not
 * allow strings in single quotes, but an implementation is allowed to
 * accept them.
 * @param quote The quoting character, either with double quote or single quote
 * @return A String.
 * @throws Exception Unterminated string.
 */
function jsonNextString(quote) {
   var returnValue = "";
   var c;
   var closedQuote = false;
   
   while (closedQuote === false) {
      c = jsonNext();
      switch (c) {
      case 0:
      case '\n':
      case '\r':
         throw ("jsonNextValue(): Unterminated string. line=" + jsonLine+ "; char=" + jsonCharacter);
      case '\\':
         c = jsonNext();
         switch(c) {
         case 'b':
            returnValue = returnValue.concat('\b');
            break;
         case 't':
            returnValue = returnValue.concat('\t');
            break;
         case 'n':
            returnValue = returnValue.concat('\n');
            break;
         case 'f':
            returnValue = returnValue.concat('\f');
            break;
         case 'r':
            returnValue = returnValue.concat('\r');
            break;
         case 'u':
            returnValue = returnValue.concat(String.fromCharCode(parseInt(jsonNextFew(4), 16))); //Not sure if this will work
            break;
         case '"':
         case '\'':
         case '\\':
         case '/':
            returnValue = returnValue.concat(c);
            break;
         default:
            throw ("jsonNextValue(): Illegal escape. line=" + jsonLine+ "; char=" + jsonCharacter);
         }
         break;
      default:
         if (c == quote) {
            closedQuote = true;
         } else {
            returnValue = returnValue.concat(c);
         }
         break;
      }
   }
   return returnValue;
} //end jsonNextString

/**
 * Gets the character from theo riginal input string at a certain position
 * @param int value of the location
 * @return character at the location or null
 */
function jsonGetChar(fieldIndex) {
   var c = null;
   if (hasValue(fieldIndex) && typeof fieldIndex === 'number' && 
       fieldIndex >= 0 && fieldIndex < jsonFullString.length) {
      c = jsonFullString.charAt(fieldIndex);
   }
   return c;
}

function jsonEnd() {
   return (jsonEof && !jsonUsePrevious);
}

function jsonPeekBack() {
   return jsonGetChar(jsonIndex - 1);
}

/**
 * Gets the next javascript object
 * @return Javascript object representing JSON
 * @throws Exception if the text is malformed
 */
function jsonNextObject() {
   var returnValue = new Object();
   var c;
   var key = "ERROR";
   var completed = false;
   
   if (jsonNextClean() != '{') {
      throw ("jsonNextObject(): A JSON object text must begin with '{'. line=" + jsonLine+ "; char=" + jsonCharacter);
   }
   
   while (completed === false) {
      c = jsonNextClean();
      switch (c) {
      case 0:
         throw ("jsonNextObject(): A JSON object text must end with '}'. line=" + jsonLine+ "; char=" + jsonCharacter);
      case '}':
         completed = true;
         break;
      default:
         jsonBack();
         key = jsonNextValue().toString();
         break;
      }
      
      if (completed === false) {
         c = jsonNextClean();
         if (c != ':') {
            throw ("jsonNextObject(): Expected a ':' after a key. line=" + jsonLine+ "; char=" + jsonCharacter);
         }
         returnValue[key] = jsonNextValue();
         
         switch (jsonNextClean()) {
         case ';':
         case ',':
            if (jsonNextClean() == '}') {
               completed =true;
            }
            jsonBack();
            break;
         case '}':
            completed = true;
            break;
         default:
            throw ("jsonNextObject(): Expected a ',' or '}'. line=" + jsonLine+ "; char=" + jsonCharacter);
         }
      }
   }
   
   return returnValue;
} //end jsonNextObject

/**
 * Gets the next javascript array
 * @return Javascript array representing JSON
 * @throws Exception if the text is malformed
 */
function jsonNextList() {
   var returnValue = new Array();
   var completed = false;
   
   if (jsonNextClean() != '[') {
      throw ("jsonNextList(): A JSON array text must begin with '['. line=" + jsonLine+ "; char=" + jsonCharacter);
   }
   if (jsonNextClean() != ']') {
      jsonBack();
      while (completed === false) {
         if (jsonNextClean() == ',') {
            jsonBack();
         } else {
            jsonBack();
            returnValue.push(jsonNextValue());
         }
         switch (jsonNextClean()) {
         case ',':
            if (jsonNextClean() == ']') {
               completed = true;
            }
            jsonBack();
            break;
         case ']':
            completed = true;
            break;
         default:
            throw ("jsonNextList(): Expected a ',' or ']'. line=" + jsonLine+ "; char=" + jsonCharacter);
         }
      }
   } else {
      completed = true;
   }
   return returnValue;
} //end jsonNextList

/**
 * The kickstart function to parse a string into a javascript object
 * @param Text representing the javascript object
 * @throws Exception if the text is malformed
 */
function jsonParse(input) {
   var returnValue = new Object();
   if (input != null && input !== "") {
      //var escapedNewLine = new RegExp("(?<!\\)\\[rn]","g");
      jsonFullString = input; //.replace(escapedNewLine,""); //Get rid of platform escaped characters for new lines
      jsonEof = false;
      jsonIndex = 0;
      jsonUsePrevious = false;
      jsonLine = 0;
      jsonCharacter = 0;
      jsonPrevious = null;
      
      returnValue = jsonNextObject();
   }
   
   return returnValue;
}



