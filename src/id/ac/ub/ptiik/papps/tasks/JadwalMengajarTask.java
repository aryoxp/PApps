package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.JadwalMengajar;
import id.ac.ub.ptiik.papps.base.Karyawan;
import id.ac.ub.ptiik.papps.interfaces.JadwalMengajarIndexInterface;
import id.ac.ub.ptiik.papps.parsers.JadwalMengajarParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class JadwalMengajarTask extends AsyncTask<Void, Void, ArrayList<JadwalMengajar>> {
	
	private JadwalMengajarIndexInterface mCallback;
	private String error;
	private Context c;
	private Karyawan karyawan;
		
	public JadwalMengajarTask(Context c, JadwalMengajarIndexInterface mCallback, Karyawan karyawan) {
		this.mCallback = mCallback;
		this.c = c;
		this.karyawan = karyawan;
	}

	@Override
	protected ArrayList<JadwalMengajar> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onJadwalRetrievingStart();
			}
		});
		try {
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.c)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/karyawan/jadwal/" + this.karyawan.id;  
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<JadwalMengajar> jadwalMengajarList = JadwalMengajarParser.Parse(result);
			return jadwalMengajarList;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(ArrayList<JadwalMengajar> jadwalMengajarList) {
		if(jadwalMengajarList != null) {
			this.mCallback.onJadwalRetrieveComplete(jadwalMengajarList);
		} else {
			this.mCallback.onJadwalRetrieveFail(this.error);
		}
	}
}
