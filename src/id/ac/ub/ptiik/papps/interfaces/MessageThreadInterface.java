package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.Message;
import java.util.ArrayList;

public interface MessageThreadInterface {
	public void onMessageThreadRetrievingStart();
	public void onMessageThreadRetrieveComplete(ArrayList<Message> messages);
	public void onMessageThreadRetrieveFail(String error);
}
