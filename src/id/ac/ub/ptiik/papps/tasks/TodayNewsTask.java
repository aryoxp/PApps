package id.ac.ub.ptiik.papps.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import ap.mobile.base.Weather;
import ap.mobile.interfaces.WeatherInterface;
import ap.mobile.jsonparser.WeatherParser;
import ap.mobile.utils.Rest;

public class TodayNewsTask extends AsyncTask<String, Void, Weather> {
	
	private ap.mobile.interfaces.WeatherInterface mCallback;
	private String error;
		
	public TodayNewsTask(WeatherInterface mCallback) {
		this.mCallback = mCallback;
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
