package id.ac.ub.ptiik.papps.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarCell {
	public GregorianCalendar calendar;
	public boolean currentMonth = true;
	public boolean today = false;

	private ArrayList<AgendaKaryawan> listAgenda;
	
	public CalendarCell(GregorianCalendar calendar) {
		this.calendar = calendar;
		this.listAgenda = new ArrayList<AgendaKaryawan>();
	}
	
	public CalendarCell(GregorianCalendar calendar, boolean current) {
		this.calendar = calendar;
		this.currentMonth = current;
		this.listAgenda = new ArrayList<AgendaKaryawan>();
	}
	
	public void setToday() {
		this.today = true;
	}
	
	public void addAgenda(AgendaKaryawan agenda) {
		this.listAgenda.add(agenda);
	}
	
	public ArrayList<AgendaKaryawan> getListAgenda(){
		return this.listAgenda;
	}
	
	public int getAgendaCount() {
		return this.listAgenda.size();
	}

	public String getDateMonthString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.US);
		return sdf.format(this.calendar.getTime());
	}
	
	public String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.US);
		return sdf.format(this.calendar.getTime());
	}
	
	public String getFormattedString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		return sdf.format(this.calendar.getTime());
	}
	
	public boolean isSunday() {
		if(this.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return true;
		return false;
	}
}
