package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.UserOnline;
import id.ac.ub.ptiik.papps.interfaces.UserOnlineCheckInterface;
import id.ac.ub.ptiik.papps.parsers.OnlineUserParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class UserOnlineTask extends AsyncTask<Void, Void, ArrayList<UserOnline>> {
	
	private Context context;
	private UserOnlineCheckInterface mCallback;
	private String error;
		
	public UserOnlineTask(Context context, UserOnlineCheckInterface mCallback) {
		this.context = context;
		this.mCallback = mCallback;
	}
	
	@Override
	protected ArrayList<UserOnline> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.onUserOnlinceCheckStarted();
			}
		});
		try {
			ArrayList<UserOnline> users = new ArrayList<UserOnline>();
		
			String host = PreferenceManager.getDefaultSharedPreferences(context)
					.getString("host", "175.45.187.246");
			String url = "http://" + host + "/service/index.php/gcm/getonlineuser";
			String result = Rest.getInstance().post(url).getResponseText();
			users = OnlineUserParser.Parse(result);
			return users;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<UserOnline> users) {
		if(users != null && users.size() > 0) {
			this.mCallback.onUserOnlinceCheckSuccess(users);
		} else {
			this.mCallback.onUserOnlinceCheckFail(this.error);
		}
	}
}
