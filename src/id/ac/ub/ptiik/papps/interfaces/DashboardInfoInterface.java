package id.ac.ub.ptiik.papps.interfaces;

import ap.mobile.base.Weather;

public interface DashboardInfoInterface {
	
	public void weatherLoaded(Weather weather);
	public void eventSummaryLoaded();
	public void weatherLoadError(String error);
	public void weatherLoadStart();
	
}
