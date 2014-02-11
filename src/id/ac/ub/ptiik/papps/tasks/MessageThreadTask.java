package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.NotificationMessage;
import id.ac.ub.ptiik.papps.helpers.NotificationMessageHandler;
import id.ac.ub.ptiik.papps.interfaces.MessageThreadInterface;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class MessageThreadTask extends AsyncTask<String, Void, ArrayList<NotificationMessage>> {
	
	private Context context;
	private MessageThreadInterface mCallback;
	private String error;
		
	public MessageThreadTask(Context context, MessageThreadInterface mCallback) {
		this.context = context;
		this.mCallback = mCallback;
	}
	
	@Override
	protected ArrayList<NotificationMessage> doInBackground(String... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onMessageThreadRetrievingStart();
			}
		});
		try {
			ArrayList<NotificationMessage> messages = new ArrayList<NotificationMessage>();
			NotificationMessageHandler handler = new NotificationMessageHandler(this.context);
			messages = handler.getAll(params[0]);
			return messages;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<NotificationMessage> messages) {
		if(messages != null && messages.size() > 0) {
			this.mCallback.onMessageThreadRetrieveComplete(messages);
		} else {
			this.mCallback.onMessageThreadRetrieveFail(this.error);
		}
	}
}
