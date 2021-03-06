package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import com.google.gson.Gson;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.base.News;
import id.ac.ub.ptiik.papps.base.PreferenceKey;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.DashboardInfoInterface;
import id.ac.ub.ptiik.papps.parsers.AgendaKaryawanParser;
import id.ac.ub.ptiik.papps.parsers.NewsListParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.base.Weather;
import ap.mobile.jsonparser.WeatherParser;
import ap.mobile.utils.Rest;

public class DashboardInfoTask extends AsyncTask<String, Void, Void> {
	
	private DashboardInfoInterface mCallback;
	private String error;
	private Context context;
		
	public DashboardInfoTask(Context context, DashboardInfoInterface mCallback) {
		this.mCallback = mCallback;
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.dashboardInfoStart();
			}
		});
		try {
			final Weather w;
			int weatherTimestamp = SystemHelper.getPreferenceInteger(this.context, PreferenceKey.WEATHER_TIMESTAMP);
			int selisih = (int) ((System.currentTimeMillis()/1000)-weatherTimestamp);
			if(weatherTimestamp == 0 || selisih > 3600) {
				String city = params[0];
				String url = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + city;
				String result = Rest.getInstance().get(url).getResponseText();
				w = WeatherParser.Parse(result);
				
				// cache the weather data...
				Gson gson = new Gson();
				String jsonWeather = gson.toJson(w);
				int timestamp = (int) System.currentTimeMillis()/1000;
				PreferenceManager.getDefaultSharedPreferences(this.context)
				.edit()
				.putString(PreferenceKey.WEATHER_JSON, jsonWeather)
				.putInt(PreferenceKey.WEATHER_TIMESTAMP, timestamp)
				.commit();
				
			} else {
				String jsonWeather = SystemHelper.getPreferenceString(this.context, PreferenceKey.WEATHER_JSON);
				Gson gson = new Gson();
				w = gson.fromJson(jsonWeather, Weather.class);
			}
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					mCallback.setWeather(w);
				}
			});
			
			MessageDBHelper messageDB = new MessageDBHelper(this.context);
			final int count = messageDB.getUnreadCount();
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					mCallback.setUnreadMessages(count);
				}
			});
			
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.context)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/news/today";
			String result = Rest.getInstance().get(url).getResponseText();
			final ArrayList<News> newsList = NewsListParser.Parse(result);
			
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					mCallback.setTodayNews(newsList);
				}
			});
			
			User user = SystemHelper.getSystemUser(this.context);
			if(user != null) {
				url = "http://"+host+"/service/index.php/agenda/today/" + user.karyawan_id;
				result = Rest.getInstance().get(url).getResponseText();
				final ArrayList<AgendaKaryawan> agendaKaryawanList = AgendaKaryawanParser.Parse(result);
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						mCallback.setTodayAgenda(agendaKaryawanList);
					}
				});
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute() {
		if(this.error != null) {
			mCallback.dashboardInfoError(this.error);
		}
	}
}
