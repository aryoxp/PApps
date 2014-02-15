package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Matakuliah;
import id.ac.ub.ptiik.papps.interfaces.MatakuliahIndexInterface;
import id.ac.ub.ptiik.papps.parsers.MatakuliahListParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class MatakuliahIndexTask extends AsyncTask<Void, Void, ArrayList<Matakuliah>> {
	
	private MatakuliahIndexInterface mCallback;
	private String error;
	private Context c;
		
	public MatakuliahIndexTask(Context c, MatakuliahIndexInterface mCallback) {
		this.mCallback = mCallback;
		this.c = c;
	}

	@Override
	protected ArrayList<Matakuliah> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onMatakuliahRetrievingStart();
			}
		});
		try {
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.c)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/matakuliah/index"; 
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<Matakuliah> matakuliahList = MatakuliahListParser.Parse(result);
			return matakuliahList;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Matakuliah> matakuliahList) {
		if(matakuliahList != null) {
			this.mCallback.onMatakuliahRetrieveComplete(matakuliahList);
		} else {
			this.mCallback.onMatakuliahRetrieveFail(this.error);
		}
	}
}
