var PASTED_LINE_DELIM = "\t";

function importPasted(customObject, eventName, params) {
   var requireSave = false;
   if (hasValue(customObject)) {
      var newElements = new Array();
      
      if (hasValue(customObject.PastedText) && hasValue(customObject.PastedText.Text)) {
         var lines = customObject.PastedText.Text.split(/[\n\r]+/); //Split by new line
         var linesCtr = 0;
         for (linesCtr = 0; linesCtr < lines.length; linesCtr++) {
            var line = lines[linesCtr];
            if (hasValue(line) && line.indexOf(PASTED_LINE_DELIM) != -1) {
               var keyValue = new Object();
               var splitPair = line.split(PASTED_LINE_DELIM);
               keyValue.Code = splitPair[0];
               keyValue.XPath = splitPair[1];
               newElements.push(keyValue);
            }
         } //end lines loop
         
         //Overlay elements, wipe out pasted text
         customObject.PastedText.Text = "";
         customObject.Elements = newElements;
         Providers.getPersistenceProvider().save(customObject);
      }
      //throw("DEBUG - Not an actual exception [processPastedText]");
   }
} //end importPasted

function exportPasted(customObject, eventName, params) {
   //TODO : export and overwrite to pasted text
}

function codeToXpathCreate(customObject, eventName, params) {
   if (!hasValue(customObject.PastedText) || !hasValue(customObject.PastedText.Name)) {
      customObject.PastedText = new Object();
      customObject.PastedText.Name = "CodeToXpath";
      Providers.getPersistenceProvider().save(customObject);
   }
}
