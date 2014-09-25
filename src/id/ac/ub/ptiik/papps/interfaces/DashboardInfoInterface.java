package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.base.News;

import java.util.ArrayList;

import ap.mobile.base.Weather;

public interface DashboardInfoInterface {
	
	public void setWeather(Weather weather);
	public void setUnreadMessages(int count);
	public void setTodayNews(ArrayList<News> newses);
	public void setTodayAgenda(ArrayList<AgendaKaryawan> agendaKaryawanList);
	public void dashboardInfoError(String error);
	public void dashboardInfoStart();
	
}
