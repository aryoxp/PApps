package id.ac.ub.ptiik.papps.interfaces;

import ap.mobile.base.Weather;

public interface DashboardInfoInterface {
	
	public void setWeather(Weather weather);
	public void setUnreadMessages(int count);
	public void dashboardInfoError(String error);
	public void dashboardInfoStart();
	
}
