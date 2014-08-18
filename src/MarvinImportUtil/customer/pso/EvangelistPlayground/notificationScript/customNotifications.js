function exampleNotificationFn(co, eventName, params) {

	Providers.getNotificationProvider().setData('subjectkey', 'Subject is being passed');
	Providers.getNotificationProvider().setData('bodykey', "CHECK THIS OUT.");
	Providers.getNotificationProvider().setData('signaturekey', "signature");
	//if (runtime.SampleTextField.equals('CANCEL_SEND')) {
	//	Providers.getNotificationProvider().cancelNotification();
	//}


}
