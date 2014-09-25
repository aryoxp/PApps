package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import id.ac.ub.ptiik.papps.interfaces.MessageSendInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class MessageSendTask extends AsyncTask<String, Void, Boolean> {
	
	private Context context;
	private MessageSendInterface mCallback;
	private String username;
	private String message;
	private String senderUsername;
	private String error;
		
	public MessageSendTask(Context context, MessageSendInterface mCallback, String senderUsername) {
		this.senderUsername = senderUsername;
		this.context = context;
		this.mCallback = mCallback;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		this.username = params[0];
		this.message = params[1];
		
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onMessageSendStart();
			}
		});
		try {
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("username", this.username));
			data.add(new BasicNameValuePair("sender", this.senderUsername));
			data.add(new BasicNameValuePair("message", this.message));
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			String host = pref.getString("host", "175.45.187.246");
			String url = "http://" + host + "/service/index.php/gcm/send";
			String result = Rest.getInstance().post(url, data).getResponseText();
			if(result != null)
				return true;
			else return false;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean status) {
		if(status) {
			this.mCallback.onMessageSendComplete(status);
		} else 
			this.mCallback.onMessageSendFail(this.error);
	}
}
