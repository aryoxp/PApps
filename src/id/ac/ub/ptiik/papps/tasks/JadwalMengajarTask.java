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
	private int jadwalMengajarBy;
	private String day, kodeSubject;
		
	public JadwalMengajarTask(Context c, JadwalMengajarIndexInterface mCallback) {
		this.mCallback = mCallback;
		this.c = c;
	}
	
	public void setByKaryawan(Karyawan karyawan) {
		this.karyawan = karyawan;
		this.jadwalMengajarBy = JadwalMengajar.DOSEN;
	}
	
	public void setByDay(String day) {
		this.day = day;
		this.jadwalMengajarBy = JadwalMengajar.DAY;
	}
	
	public void setBySubject(String kodeSubject){
		this.kodeSubject = kodeSubject;
		this.jadwalMengajarBy = JadwalMengajar.SUBJECT;
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
			String url = null;
			switch(this.jadwalMengajarBy) {
			case JadwalMengajar.DOSEN:
				url = "http://"+host+"/service/index.php/jadwal/karyawan/" + this.karyawan.id;
				break;
			case JadwalMengajar.DAY:
				url = "http://"+host+"/service/index.php/jadwal/day/" + this.getDay();
				break;
			case JadwalMengajar.SUBJECT:
				url = "http://"+host+"/service/index.php/jadwal/subject/" + this.kodeSubject;
			}
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
	
	private String getDay(){
		if(this.day.equals("Monday")) return "senin";
		if(this.day.equals("Tuesday")) return "selasa";
		if(this.day.equals("Wednesday")) return "rabu";
		if(this.day.equals("Thursday")) return "kamis";
		if(this.day.equals("Friday")) return "jumat";
		if(this.day.equals("Saturday")) return "sabtu";
		if(this.day.equals("Sunday")) return "minggu";
		return null;
	}
}
