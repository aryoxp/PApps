package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.base.CalendarCell;
import id.ac.ub.ptiik.papps.interfaces.AgendaKaryawanIndexInterface;
import id.ac.ub.ptiik.papps.parsers.AgendaKaryawanParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import ap.mobile.utils.Rest;

public class AgendaKaryawanTask extends AsyncTask<Void, Void, ArrayList<CalendarCell>> {
	
	private AgendaKaryawanIndexInterface mCallback;
	private String error;
	//private int page, perpage;
	private int bulan, tahun;
	private Context c;
	private String idKaryawan;
	
	public AgendaKaryawanTask(Context c, AgendaKaryawanIndexInterface mCallback,
			String idKaryawan, int bulan, int tahun) {
		this.bulan = bulan;
		this.tahun = tahun;
		this.c = c;
		this.mCallback = mCallback;
		this.idKaryawan = idKaryawan;
	}
	
	public void setPage(int page) {
		//this.page = page;
	}
	
	public void setPerpage(int perpage)
	{
		//this.perpage = perpage;
	}
	
	@Override
	protected ArrayList<CalendarCell> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onRetrievingStart();
			}
		});
		try {
			String bulan = String.valueOf(this.bulan+1);
			if(this.bulan < 10)
				bulan = "0" + bulan;
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.c)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/agenda/karyawan/" 
					+ this.idKaryawan + "/"
					+ bulan + "/"
					+ this.tahun;
					//+ String.valueOf(page) + "/"
					//+ String.valueOf(perpage);
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<AgendaKaryawan> AgendaKaryawanList = AgendaKaryawanParser.Parse(result);
			ArrayList<CalendarCell> cells = this.calculateMonth(AgendaKaryawanList);
			return cells;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<CalendarCell> calendarCells) {
		if(calendarCells != null) {
			this.mCallback.onRetrieveComplete(calendarCells);
		} else {
			if(this.error == null) {
				this.mCallback.onRetrieveComplete(new ArrayList<CalendarCell>());
				return;
			}
			this.mCallback.onRetrieveFail(this.error);
		}
	}
	
	private ArrayList<CalendarCell> calculateMonth(ArrayList<AgendaKaryawan> agendaList) {
		GregorianCalendar currentMonth = new GregorianCalendar(tahun, bulan, 1);
		Log.d("current month", currentMonth.getTime().toString());
		
		int pMonth, pYear, nMonth, nYear;
		
		nYear = tahun;
		pYear = tahun;
		
		if(bulan == 0) {
			pMonth = 11; pYear = tahun-1;
		} else pMonth = bulan-1;
		
		if(bulan == 11) {
			nMonth = 0; nYear = tahun+1;
		} else nMonth = bulan;
		
		GregorianCalendar prevMonth = new GregorianCalendar(pYear, pMonth, 1);
		Log.d("p month", prevMonth.getTime().toString());
		GregorianCalendar nextMonth = new GregorianCalendar(nYear, nMonth, 1);
		Log.d("n month", nextMonth.getTime().toString());
		
		int prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int currentDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);
		
		int todayDate = Calendar.getInstance(Locale.US).get(Calendar.DAY_OF_MONTH);
		
		ArrayList<CalendarCell> dayList = new ArrayList<CalendarCell>();
		
		for(int i = currentDayOfWeek-3; i>=0; i--) {
			CalendarCell cell = new CalendarCell(prevMonthDays-i, false);
			dayList.add(cell);
		}
		for(int i = 1; i<=currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			CalendarCell cell = new CalendarCell(i);
			if(i == todayDate) cell.setToday();
			for(int j=0; j<agendaList.size();j++)
			{
				if(agendaList.get(j).getTanggal() == i)
					cell.addAgenda(agendaList.get(j));
			}
			dayList.add(cell);
		}
		currentMonth.set(Calendar.DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);
		int d = 1;
		for(int i=lastDayOfWeek; i<=7; i++) {
			CalendarCell cell = new CalendarCell(d, false);
			dayList.add(cell);
			d++;
		}
		
		return dayList;
	}
}
