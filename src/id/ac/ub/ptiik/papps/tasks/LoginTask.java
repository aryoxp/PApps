package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.GCMHelper;
import id.ac.ub.ptiik.papps.interfaces.LoginInterface;
import id.ac.ub.ptiik.papps.parsers.UserParser;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import ap.mobile.utils.Rest;

public class LoginTask extends AsyncTask<Void, Void, User> {
	
	private Context context;
	private LoginInterface mCallback;
	private String error;
	private String username;
	private String password;
		
	public LoginTask(Context context, LoginInterface mCallback, String username, String password) {
		this.context = context;
		this.mCallback = mCallback;
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected User doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onLoginStarted();
			}
		});
		try {
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("u", this.username));
			data.add(new BasicNameValuePair("p", this.password));
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			String host = pref.getString("host", "175.45.187.246");
			String url = "http://" + host + "/service/index.php/auth/login";
			String result = Rest.getInstance().post(url, data).getResponseText();
			Log.d("Login", result);
			User u = UserParser.Parse(result);
			
			GCMHelper gcmHelper = new GCMHelper(this.context);
			String registrationId = gcmHelper.getRegistrationId();
			if(registrationId != null && !registrationId.trim().equals(""))
			{
				url = "http://" + host + "/service/index.php/gcm/checkin";
				data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("username", this.username));
				data.add(new BasicNameValuePair("regid", registrationId));
				data.add(new BasicNameValuePair("api", String.valueOf(Build.VERSION.SDK_INT)));
				data.add(new BasicNameValuePair("status", "1"));
				result = Rest.getInstance().post(url, data).getResponseText();
				Log.d("Check-In", result);
			}
			
			return u;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(User u) {
		if(u != null) {
			u.username = this.username;
			u.password = this.password;
			this.mCallback.onLoginSuccess(u);
		} else {
			this.mCallback.onLoginFail(this.error);
		}
	}
}
