package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Message;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;
import id.ac.ub.ptiik.papps.interfaces.MessageThreadInterface;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class MessageThreadTask extends AsyncTask<String, Void, ArrayList<Message>> {
	
	private Context context;
	private MessageThreadInterface mCallback;
	private String error;
		
	public MessageThreadTask(Context context, MessageThreadInterface mCallback) {
		this.context = context;
		this.mCallback = mCallback;
	}
	
	@Override
	protected ArrayList<Message> doInBackground(String... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onMessageThreadRetrievingStart();
			}
		});
		try {
			ArrayList<Message> messages = new ArrayList<Message>();
			MessageDBHelper handler = new MessageDBHelper(this.context);
			messages = handler.getAll(params[0], params[1]);
			return messages;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Message> messages) {
		if(messages != null && messages.size() > 0) {
			this.mCallback.onMessageThreadRetrieveComplete(messages);
		} else {
			this.mCallback.onMessageThreadRetrieveFail(this.error);
		}
	}
}
