package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.CalendarCell;

import java.util.ArrayList;

public interface AgendaKaryawanIndexInterface {
	public void onRetrievingStart();
	public void onRetrieveComplete(ArrayList<CalendarCell> calendarCells);
	public void onRetrieveFail(String error);
}
