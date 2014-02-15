package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import id.ac.ub.ptiik.papps.helpers.GCMHelper;
import id.ac.ub.ptiik.papps.interfaces.CheckinInterface;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import ap.mobile.utils.Rest;

public class CheckinTask extends AsyncTask<Void, Void, Boolean> {
	
	private Context context;
	private CheckinInterface mCallback;
	private String error;
	private String username;
	private int status;
		
	public CheckinTask(Context context, CheckinInterface mCallback, String username, int status) {
		this.context = context;
		this.mCallback = mCallback;
		this.username = username;
		this.status = status;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onCheckinStarted();
			}
		});
		try {
			
			GCMHelper gcmHelper = new GCMHelper(this.context);
			String registrationId = gcmHelper.getRegistrationId();
			if(registrationId != null && !registrationId.trim().equals(""))
			{
				String host = PreferenceManager.getDefaultSharedPreferences(context)
						.getString("host", "175.45.187.246");
				String url = "http://" + host + "/service/index.php/gcm/checkin";
				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("username", this.username));
				data.add(new BasicNameValuePair("regid", registrationId));
				data.add(new BasicNameValuePair("api", String.valueOf(Build.VERSION.SDK_INT)));
				data.add(new BasicNameValuePair("status", String.valueOf(this.status)));
				String result = Rest.getInstance().post(url, data).getResponseText();
				Log.d("Check-In", result);
				return true;
			}
			this.error = "Invalid GCM registration ID.";
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean checkinStatus) {
		if(checkinStatus) {
			this.mCallback.onCheckinSuccess();
		} else {
			this.mCallback.onCheckinFail(this.error);
		}
	}
}
