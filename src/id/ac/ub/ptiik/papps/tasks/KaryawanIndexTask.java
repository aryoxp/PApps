package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Karyawan;
import id.ac.ub.ptiik.papps.interfaces.KaryawanIndexInterface;
import id.ac.ub.ptiik.papps.parsers.KaryawanListParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class KaryawanIndexTask extends AsyncTask<Void, Void, ArrayList<Karyawan>> {
	
	private KaryawanIndexInterface mCallback;
	private String error;
	private Context c;
		
	public KaryawanIndexTask(Context c, KaryawanIndexInterface mCallback) {
		this.mCallback = mCallback;
		this.c = c;
	}

	@Override
	protected ArrayList<Karyawan> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onRetrievingStart();
			}
		});
		try {
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.c)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/karyawan/index"; 
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<Karyawan> newsList = KaryawanListParser.Parse(result);
			return newsList;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Karyawan> karyawanList) {
		if(karyawanList != null) {
			this.mCallback.onRetrieveComplete(karyawanList);
		} else {
			this.mCallback.onRetrieveFail(this.error);
		}
	}
}
