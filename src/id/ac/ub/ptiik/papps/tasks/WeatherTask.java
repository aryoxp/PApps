package id.ac.ub.ptiik.papps.tasks;

import com.google.gson.Gson;

import id.ac.ub.ptiik.papps.base.PreferenceKey;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.base.Weather;
import ap.mobile.interfaces.WeatherInterface;
import ap.mobile.jsonparser.WeatherParser;
import ap.mobile.utils.Rest;

public class WeatherTask extends AsyncTask<String, Void, Weather> {
	
	private WeatherInterface mCallback;
	private String error;
	private Context context;
		
	public WeatherTask(Context context, WeatherInterface mCallback) {
		this.mCallback = mCallback;
		this.context = context;
	}
	
	@Override
	protected Weather doInBackground(String... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mCallback.weatherLoadStart();
			}
		});
		try {
			String city = params[0];
			String url = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + city;
			String result = Rest.getInstance().get(url).getResponseText();
			Weather w = WeatherParser.Parse(result);
			
			// cache the weather data...
			Gson gson = new Gson();
			String jsonWeather = gson.toJson(w);
			
			int timestamp = (int)(System.currentTimeMillis()/1000);
			
			PreferenceManager.getDefaultSharedPreferences(this.context)
			.edit()
			.putString(PreferenceKey.WEATHER_JSON, jsonWeather)
			.putInt(PreferenceKey.WEATHER_TIMESTAMP, timestamp)
			.commit();
			
			return w;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Weather w) {
		if(w != null) {
			this.mCallback.weatherLoaded(w);
		} else {
			this.mCallback.weatherLoadError(this.error);
		}
	}
}
