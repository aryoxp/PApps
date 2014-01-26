package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.interfaces.AgendaKaryawanIndexInterface;
import id.ac.ub.ptiik.papps.parsers.AgendaKaryawanParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class AgendaKaryawanTask extends AsyncTask<Void, Void, ArrayList<AgendaKaryawan>> {
	
	private AgendaKaryawanIndexInterface mCallback;
	private String error;
	private int page;
	private int perpage;
	private Context c;
	private String idKaryawan;
	private String tanggal;
		
	public AgendaKaryawanTask(Context c, AgendaKaryawanIndexInterface mCallback, 
			String idKaryawan, String tanggal,
			Integer page, Integer perpage) {
		this.mCallback = mCallback;
		this.page = page;
		this.perpage = perpage;
		this.c = c;
		this.idKaryawan = idKaryawan;
		this.tanggal = tanggal;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public void setPerpage(int perpage)
	{
		this.perpage = perpage;
	}
	
	@Override
	protected ArrayList<AgendaKaryawan> doInBackground(Void... params) {
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
			String url = "http://"+host+"/service/index.php/agenda/karyawan/" 
					+ this.idKaryawan + "/"
					+ this.tanggal + "/"
					+ String.valueOf(page) + "/"
					+ String.valueOf(perpage);
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<AgendaKaryawan> AgendaKaryawanList = AgendaKaryawanParser.Parse(result);
			return AgendaKaryawanList;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<AgendaKaryawan> AgendaKaryawanList) {
		if(AgendaKaryawanList != null) {
			this.mCallback.onRetrieveComplete(AgendaKaryawanList);
		} else {
			if(this.error == null) {
				this.mCallback.onRetrieveComplete(new ArrayList<AgendaKaryawan>());
				return;
			}
			this.mCallback.onRetrieveFail(this.error);
		}
	}
}
