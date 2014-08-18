/**
 *   C H A N G E    L  O G
 * 
 *  (B)ug/(E)nh/(I)DB #    Date      Who  Description
 *  -------------------  ----------  ---  ---------------------------------------------------------------
 *   ENH-????            11/01/2012  EH   new
 */

/**
 * Generate Unique IDs for the e-mail list.
 * If there are e-mails
 */
function generateEmailId(customObject, eventName, params) {
  var emails = customObject.Subscribers;
  var i = 0;
  var uniqueId = 0;
  var foundEmails = false;  
  
  if (isntNullOrEmpty(emails) == true) {
    //Find the last Unique Id
    for (i = emails.length - 1; i >= 0; i--) {
      if (isntNullOrEmpty(emails[i]) == true && isntNullOrEmpty(emails[i].UniqueId) == true) {
        uniqueId = parseInt(emails[i].UniqueId) + 1;
        foundEmails = true;
        break;
      }
    } //end for loop to find last unique id

    if (foundEmails == true) {    
      //Starting from the element after the last unique id found, populate any additional missing unique IDs
      for (i = i+1; i < emails.length; i++) {
        if (isntNullOrEmpty(emails[i]) == true && isNullOrEmpty(emails[i].UniqueId) == true) {
          emails[i].UniqueId = uniqueId;
          uniqueId = uniqueId + 1;
        }                                      
      } //end loop through all e-mails
    } else { //If no existing elements were found, start from the top.
      for (i = 0; i < emails.length; i++) {
        if (isntNullOrEmpty(emails[i]) == true && isNullOrEmpty(emails[i].UniqueId) == true) {
          emails[i].UniqueId = uniqueId;
          uniqueId = uniqueId + 1;
        }                                      
      } //end loop through all elements
    }
  }
  
  Providers.getPersistenceProvider().save(customObject);
} //end generateEmailId

function fieldMasking(customObject, eventName, params) {
	var fieldMasksMap;
	
	if (isntNullOrEmpty(params[0]) == true) {
		fieldMasksMap = params[0];
		
		//If the user is buyer, don't hide anything
		var roles = Providers.getSessionProvider().getCurrentOrgRoles();
		var orgId = Providers.getSessionProvider().getCurrentOrgId();
		
		if (roles.Buyer != true) {
			var emails = customObject.Subscribers;
			var i = 0;
			
			if (isntNullOrEmpty(emails) == true) {
				//Find the last Unique Id
				for (i = 0; i < emails.length; i++) {
					if (emails[i].OrganizationID != orgId) {
						fieldMasksMap.put("Parent.Subscribers."+i, "Hidden");
						//fieldMasksMap.put("myObject.Subscribers."+i, "Hidden");
					}
				} //end loop through emails
			}
			
		}
	}
	
	return fieldMasksMap;
}

function generateOrgId(customObject, eventName, params) {
	var emails = customObject.Subscribers;
	var i = 0;
	var madeChange = false;
	
	if (isntNullOrEmpty(emails) == true) {
		//Find the last Unique Id
		for (i = 0; i < emails.length; i++) {
			
			//Fill in any empty e-mails' organization ids
			if (isntNullOrEmpty(emails[i]) == true &&
			    isntNullOrEmpty(emails[i].Email) == true &&
			    isNullOrEmpty(emails[i].OrganizationId) == true) {
				madeChange = true;
				emails[i].OrganizationId = Providers.getSessionProvider().getCurrentOrgId();
			}
		}
		
	}
	
	if (madeChange == true) {
		Providers.getPersistenceProvider().save(customObject);
	}
	
} //end generatedOrgId

/**
 * Validates the e-mail address entered is valid.
 */
function validateEmail(customObject, eventName, params) {
   
   var hasErrors = false;
   var entries = customObject.PortOfDischargeNonUsEntries;
   var i = 0;
   //var emailRegex = /^.+\@.+\..{2,4}$/; Our javascript compiler didn't understand this because of the periods
   var emailRegex = /^[\w\W]+\@[\w\W]+\.[\w\W]{2,4}$/;
   
   if (entries != undefined && entries != null) {
      for (i = 0; i < entries.length; i++) {
         var entry = entries[i];
         
         if (entry != undefined && entry != null) {
         	if (!filter.test(entry.EMail)) {
            	Providers.getMessageProvider().error(customObject, "Column ["+i+"]: e-mail ["+entry.EMail+"] is not a valid e-mail address.");
         	}
         	
         }
         
      }
   }
}

/**
 * Run all validations on each save.
 */
function validateOnSave(customObject, eventName, params) {
   //Run Validations
   validateEmail(customObject, eventName, params);
}

function isntNullOrEmpty(input) {
  return ("" != input && input != undefined && input != null);
}

function isNullOrEmpty(input) {
  return (input == undefined || "" == input || input == null);
}



