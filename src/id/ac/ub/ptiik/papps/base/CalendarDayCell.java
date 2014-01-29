package id.ac.ub.ptiik.papps.base;

public class CalendarDayCell extends CalendarCell {
	public int date;
	public boolean currentMonth = true;
	public boolean today = false;
	
	public CalendarDayCell(int date) {
		this.date = date;
	}
	
	public CalendarDayCell(int date, boolean current) {
		this.date = date;
		this.currentMonth = current;
	}
	
	public void setToday() {
		this.today = true;
	}
}
