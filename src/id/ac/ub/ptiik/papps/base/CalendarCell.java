package id.ac.ub.ptiik.papps.base;

import java.util.ArrayList;

public class CalendarCell {
	public int date;
	public boolean currentMonth = true;
	public boolean today = false;
	private ArrayList<AgendaKaryawan> listAgenda;
	
	public CalendarCell(int date) {
		this.date = date;
		this.listAgenda = new ArrayList<AgendaKaryawan>();
	}
	
	public CalendarCell(int date, boolean current) {
		this.date = date;
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
}
