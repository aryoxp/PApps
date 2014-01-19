package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.tasks.WeatherTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import ap.mobile.base.Weather;
import ap.mobile.interfaces.WeatherInterface;

public class DashboardFragment extends Fragment 
	implements WeatherInterface, View.OnClickListener {
	
	private ImageView iconView;
	private TextView temperatureView;
	private TextView humidityView;
	private TextView descriptionView;
	private TextView buttonSignIn;
	private Weather weather;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.iconView = (ImageView) v.findViewById(R.id.imageWeatherIcon);
		this.temperatureView = (TextView) v.findViewById(R.id.dashboardTemperatureText);
		this.humidityView = (TextView) v.findViewById(R.id.dashboardHumidityText);
		this.descriptionView = (TextView) v.findViewById(R.id.dashboardTemperatureDescriptionText);
		this.buttonSignIn = (TextView) v.findViewById(R.id.buttonDashboardSignIn);
		this.buttonSignIn.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onStart() {
		
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels/3);
		int[] resId = {
			R.id.imageWeatherContainer,
			R.id.agendaSummaryContainer,
			R.id.newsSummaryContainer,
			R.id.notificationSummaryContainer
		};
		
		for(int i=0; i<resId.length; i++) {
		
			View v = this.getView().findViewById(resId[i]);
			LayoutParams layoutParams = v.getLayoutParams();
			layoutParams.width = width;
			if(resId[i] != R.id.imageWeatherContainer )
				layoutParams.height = (int) (width - (40*metrics.density));
			v.setLayoutParams(layoutParams);
		
		}
		
		super.onStart();
		
		if(this.weather == null) {
			WeatherTask weatherTask = new WeatherTask(this);
			weatherTask.execute("Malang");
		}
		
	}

	@Override
	public void weatherLoaded(Weather weather) {
		this.weather = weather;
		switch(weather.id){
			case 200:
			case 210:
			case 201:
			case 211:
			case 202:
			case 212:
			case 221:
			case 230:
			case 231:
			case 232:
				this.iconView.setImageResource(R.drawable.ic_lightning);
				break;
			case 500:
			case 520:
			case 501:
			case 521:
			case 502:
			case 522:
			case 503:
			case 504:
			case 511:
			case 531:
				this.iconView.setImageResource(R.drawable.ic_rain);
				break;
			case 701:
			case 711:
			case 731:
			case 741:
			case 751:
			case 761:
			case 721:
			case 762:
			case 771:				
				this.iconView.setImageResource(R.drawable.ic_cloud_fog);
				break;			
			case 781:
			case 900: //"tornado");
			case 901: //"tropical storm");
			case 902: //"hurricane");
			case 960: //"Storm");
			case 961: //"Violent Storm");
			case 962: //"Hurricane");
				this.iconView.setImageResource(R.drawable.ic_tornado);
				break;
			case 800:
			case 903: //"cold");
			case 904: //"hot");
				this.iconView.setImageResource(R.drawable.ic_sun);
				break;
			case 801:
			case 802:
			case 803:
			case 804:
				this.iconView.setImageResource(R.drawable.ic_cloud_sun);
				break;
			case 905: //"windy");
			case 906: //"hail");
			case 950: //"Setting");
			case 951: //"Calm");
			case 952: //"Light breeze");
			case 953: //"Gentle Breeze");
			case 954: //"Moderate breeze");
			case 955: //"Fresh Breeze");
			case 956: //"Strong breeze");
			case 957: //"High wind: //near gale");
			case 958: //"Gale");
			case 959: //"Severe Gale");
				this.iconView.setImageResource(R.drawable.ic_cloud_wind);
				break;
			default:
				this.iconView.setImageResource(R.drawable.ic_cloud_fog);
		}
		
		this.temperatureView.setText(String.valueOf(weather.temperature) + (char) 0x00B0 + "C");
		this.humidityView.setText(weather.humidity + "%");
		this.descriptionView.setText(weather.city + ", " + weather.main + " - " + weather.description);
	}

	@Override
	public void weatherLoadError(String error) {
		this.iconView.setImageResource(R.drawable.ic_cloud_fog);
		this.temperatureView.setText("--" + (char) 0x00B0 + "C");
		this.humidityView.setText("--%");
		this.descriptionView.setText("Unable to read weather data.");
	}

	@Override
	public void weatherLoadStart() {
		this.iconView.setImageResource(R.drawable.ic_cloud_refresh);
		this.temperatureView.setText("--" + (char) 0x00B0 + "C");
		this.humidityView.setText("--%");
		this.descriptionView.setText("Loading weather data...");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonDashboardSignIn:
			LoginFragment loginFragment = new LoginFragment();
			loginFragment.show(getFragmentManager(), "login");
			break;
		}
	}
}
