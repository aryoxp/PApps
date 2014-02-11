package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.NotificationMessage;

import java.util.ArrayList;

public interface MessageThreadInterface {
	public void onMessageThreadRetrievingStart();
	public void onMessageThreadRetrieveComplete(ArrayList<NotificationMessage> messages);
	public void onMessageThreadRetrieveFail(String error);
}
