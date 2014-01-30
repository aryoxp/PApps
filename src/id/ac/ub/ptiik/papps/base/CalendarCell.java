package id.ac.ub.ptiik.papps.base;

public class CalendarCell {
	public int date;
	public boolean currentMonth = true;
	public boolean today = false;
	
	public CalendarCell(int date) {
		this.date = date;
	}
	
	public CalendarCell(int date, boolean current) {
		this.date = date;
		this.currentMonth = current;
	}
	
	public void setToday() {
		this.today = true;
	}
}
