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
	private int bulan, tahun;
	private Context c;
	private String idKaryawan;
	private Calendar todayCalendar;
	
	public AgendaKaryawanTask(Context c, AgendaKaryawanIndexInterface mCallback,
			String idKaryawan, int bulan, int tahun) {
		this.bulan = bulan;
		this.tahun = tahun;
		this.c = c;
		this.mCallback = mCallback;
		this.idKaryawan = idKaryawan;
		this.todayCalendar = Calendar.getInstance(Locale.US);
	}
	
	@Override
	protected ArrayList<CalendarCell> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onRetrievingStart();
					mCallback.onRetrieveProgress(10, "Connecting to server...");
			}
		});
		try {
			String bulan = String.valueOf(this.bulan+1);
			if(this.bulan < 9)
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
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					if(mCallback !=null)
						mCallback.onRetrieveProgress(50, "Parsing XML data...");
				}
			});
			ArrayList<AgendaKaryawan> AgendaKaryawanList = AgendaKaryawanParser.Parse(result);
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					if(mCallback !=null)
						mCallback.onRetrieveProgress(90, "Visualizing calendar...");
				}
			});
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
		this.mCallback.onRetrieveProgress(100, "Loading complete");
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
		ArrayList<AgendaKaryawan> agendaListTemp = agendaList;
		GregorianCalendar currentCalendar = new GregorianCalendar(tahun, bulan, 1);
		Log.d("current month", currentCalendar.getTime().toString());
		
		int pMonth, pYear, nMonth, nYear;
		
		nYear = tahun;
		pYear = tahun;
		
		if(bulan == 0) {
			pMonth = 11; pYear = tahun-1;
		} else pMonth = bulan-1;
		
		if(bulan == 11) {
			nMonth = 0; nYear = tahun+1;
		} else nMonth = bulan+1;
		
		GregorianCalendar prevMonth = new GregorianCalendar(pYear, pMonth, 1);
		Log.d("p month", prevMonth.getTime().toString());
		GregorianCalendar nextMonth = new GregorianCalendar(nYear, nMonth, 1);
		Log.d("n month", nextMonth.getTime().toString());
		
		int prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDayInMonth = currentCalendar.get(Calendar.DAY_OF_WEEK);
		
		int todayDate = todayCalendar.get(Calendar.DAY_OF_MONTH);
		int todayMonth = todayCalendar.get(Calendar.MONTH);
		int todayYear = todayCalendar.get(Calendar.YEAR);
		
		ArrayList<CalendarCell> dayList = new ArrayList<CalendarCell>();
		// 1 - 6
		// 2 - 0
		// 3 - 1
		int add = 0;
		if(firstDayInMonth>1) add = firstDayInMonth-2;
		else add = 6;
		for(int i = add-1; i>=0; i--) {
			GregorianCalendar cal = new GregorianCalendar(pYear, pMonth, prevMonthDays-i);
			CalendarCell cell = new CalendarCell(cal, false);
			dayList.add(cell);
		}
		for(int i = 1; i<=currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			int tahun = currentCalendar.get(Calendar.YEAR);
			int bulan = currentCalendar.get(Calendar.MONTH);
			GregorianCalendar cal = new GregorianCalendar(tahun, bulan, i);
			CalendarCell cell = new CalendarCell(cal);
			if(i == todayDate && bulan == todayMonth && tahun == todayYear) 
				cell.setToday();
			if(agendaList != null) {
				for(int j=0; j<agendaList.size();j++)
				{
					AgendaKaryawan agenda = agendaListTemp.get(j);
					if(agenda.getTanggal() == i) {
						cell.addAgenda(agenda);
						agendaListTemp.remove(j);
					}
				}
			}
			dayList.add(cell);
		}
		currentCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
		int d = 1;
		for(int i=lastDayOfWeek; i<=7; i++) {
			GregorianCalendar cal = new GregorianCalendar(nYear, nMonth, d);
			CalendarCell cell = new CalendarCell(cal, false);
			dayList.add(cell);
			d++;
		}
		
		return dayList;
	}
}
