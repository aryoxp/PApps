package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.MessageReceived;

import java.util.ArrayList;

public interface MessageThreadInterface {
	public void onMessageThreadRetrievingStart();
	public void onMessageThreadRetrieveComplete(ArrayList<MessageReceived> messages);
	public void onMessageThreadRetrieveFail(String error);
}
