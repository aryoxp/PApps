package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.ac.ub.ptiik.papps.adapters.AgendaKaryawanAdapter;
import id.ac.ub.ptiik.papps.adapters.NewsTodayAdapter;
import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.base.News;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.DashboardInfoInterface;
import id.ac.ub.ptiik.papps.interfaces.LoginDialogFinishInterface;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;
import id.ac.ub.ptiik.papps.tasks.DashboardInfoTask;
import android.app.AlertDialog;
<<<<<<< HEAD
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
=======
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
>>>>>>> masteronline
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ap.mobile.base.Weather;

public class DashboardFragment extends Fragment 
	implements DashboardInfoInterface, View.OnClickListener, LoginDialogFinishInterface,
	DialogInterface.OnClickListener {
	
	private ImageView iconView;
	private TextView temperatureView;
	private TextView humidityView;
	private TextView descriptionView;
	private TextView buttonSignIn;
	private View v;
	
	private User user;
	
	private int weatherTimestamp;
	
	private NavigationInterface navigationCallback;
	private ViewGroup contentContainer;
	private ListView todayNewsListView;
	private ListView todayAgendaListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.v = v;
		this.iconView = (ImageView) v.findViewById(R.id.imageWeatherIcon);
		this.temperatureView = (TextView) v.findViewById(R.id.dashboardTemperatureText);
		this.humidityView = (TextView) v.findViewById(R.id.dashboardHumidityText);
		this.descriptionView = (TextView) v.findViewById(R.id.dashboardTemperatureDescriptionText);
		this.contentContainer = (ViewGroup) v.findViewById(R.id.dashboardContentContainer);
		this.todayNewsListView = (ListView) v.findViewById(R.id.dashboardTodayNewsListView);
		this.todayAgendaListView = (ListView) v.findViewById(R.id.dashboardTodayAgendaListView);
		
		v.findViewById(R.id.newsSummaryContainer).setOnClickListener(this);
		v.findViewById(R.id.agendaSummaryContainer).setOnClickListener(this);
		v.findViewById(R.id.notificationSummaryContainer).setOnClickListener(this);
		
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
			
			//if(resId[i] != R.id.imageWeatherContainer )
			//	layoutParams.height = (int) (width);
			v.setLayoutParams(layoutParams);
		
		}
		
		super.onStart();
		DashboardInfoTask dashboardInfoTask = new DashboardInfoTask(getActivity(), this);
		dashboardInfoTask.execute("Malang");
		
		try {
			this.user = SystemHelper.getSystemUser(getActivity());
		} catch (Exception e) {
			Log.e("User", e.getMessage());
			this.user = null;
		}
		updateUserView();
		
	}

	public void setNavigationCallback(NavigationInterface navCallback) {
		this.navigationCallback = navCallback;
	}

	@Override
	public void setWeather(Weather weather) {
		
		
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
		this.descriptionView.setText(weather.city + ", " + weather.main); //  + " - " + weather.description
		TextView timestampText = (TextView) this.v.findViewById(R.id.dashboardTimestampText);
		Long timestampLong = (long) this.weatherTimestamp*1000;
		Date timestamp = new Date(timestampLong);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM HH:mm", Locale.US);
		timestampText.setText(sdf.format(timestamp).toUpperCase(Locale.US));
	}

	@Override
	public void dashboardInfoError(String error) {
		this.iconView.setImageResource(R.drawable.ic_cloud_fog);
		this.temperatureView.setText("--" + (char) 0x00B0 + "C");
		this.humidityView.setText("--%");
		this.descriptionView.setText("Unable to read weather data.");
	}

	@Override
	public void dashboardInfoStart() {
		this.iconView.setImageResource(R.drawable.ic_cloud_refresh);
		this.temperatureView.setText("--" + (char) 0x00B0 + "C");
		this.humidityView.setText("--%");
		this.descriptionView.setText("Loading weather data...");
	}

	@Override
	public void onClick(View v) {
		NavMenu menu = new NavMenu(NavMenu.MENU_HOME, "Home", "Apps home screen", R.drawable.ic_places_1, "home");
		switch(v.getId()) {
		case R.id.newsSummaryContainer:
			menu = new NavMenu(NavMenu.MENU_NEWS, "News", "News and Information", R.drawable.ic_communication_99, "news");
			break;
		case R.id.agendaSummaryContainer:
			menu = new NavMenu(NavMenu.MENU_AGENDA, "Agenda", "My Agenda", R.drawable.ic_time_3, "agenda"); 
			break;
		case R.id.notificationSummaryContainer:
			menu = new NavMenu(NavMenu.MENU_MESSAGES, "Messages", "Incoming messages", R.drawable.ic_communication_63, "messages");
			break;
		case R.id.buttonDashboardSignIn:
			LoginFragment loginFragment = new LoginFragment();
			loginFragment.setOnFinishCallback(this);
			loginFragment.show(getFragmentManager(), "login");
			break;
		case R.id.dashboardButtonSignOut:
			new AlertDialog.Builder(getActivity())
			.setTitle("Confirmation")
			.setMessage("Sign out from this mobile app?")
			.setPositiveButton("OK", this)
			.setNegativeButton("Cancel", this)
			.show();
			break;
		}
		if(menu.id != NavMenu.MENU_HOME && this.navigationCallback != null)
			this.navigationCallback.OnNavigationMenuSelected(menu);
	}

	@Override
	public void onLoginFinished(User user) {
		
		SystemHelper.saveSystemUser(getActivity(), user);
		this.user = user;
		updateUserView();
		
	}

	private void updateUserView() {
		if(this.user != null) {
			if(user.karyawan_id != null) {
				TextView userText = (TextView) this.v.findViewById(R.id.dashboardUsernameText);
				if(this.user.gelar_awal.equals("null")) this.user.gelar_awal = "";
				if(this.user.gelar_akhir.equals("null")) this.user.gelar_akhir = "";
				String namaLengkap = this.user.gelar_awal + " " + this.user.nama + ", " + this.user.gelar_akhir;
				userText.setText(namaLengkap.trim());
				
				loginView();
				return;
			}
		}
		logoutView();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_POSITIVE) {
			Log.d("Logout", String.valueOf(which));
			
			PreferenceManager.getDefaultSharedPreferences(getActivity())
			.edit().remove("user").commit();
			
			this.user = null;
			
			logoutView();
		}
	}

	private void loginView() {
		
		View viewLogin = this.v.findViewById(R.id.dashboardLoginContainer);
		ImageView buttonSignOut = (ImageView) this.v.findViewById(R.id.dashboardButtonSignOut);
		buttonSignOut.setOnClickListener(this);
		if(viewLogin != null)
			((ViewManager)viewLogin.getParent()).removeView(viewLogin);
		
		View userContainer = this.v.findViewById(R.id.dashboardUserContainer);
		userContainer.animate()
		.translationX(0)
		.setDuration(300)
		.start();
		
	}	
	
	private void logoutView() {
		View dashboardLogin = getActivity().getLayoutInflater().inflate(R.layout.view_dashboard_login, (ViewGroup) this.contentContainer, false);
		dashboardLogin.setTag("loginView");
		if(this.contentContainer.findViewWithTag("loginView") == null)
			this.contentContainer.addView(dashboardLogin, 0);
		
		dashboardLogin.setAlpha(0);
		dashboardLogin.animate()
		.alpha(1f)
		.setDuration(500)
		.start();
		this.buttonSignIn = (TextView) dashboardLogin.findViewById(R.id.buttonDashboardSignIn);
		this.buttonSignIn.setOnClickListener(this);
		
		
		View userContainer = this.v.findViewById(R.id.dashboardUserContainer);
		userContainer.animate()
		.translationX(this.getResources().getDisplayMetrics().widthPixels)
		.setDuration(300)
		.start();
	}

	@Override
	public void setUnreadMessages(int count) {
		TextView unreadMessagesText = (TextView) this.v.findViewById(R.id.dashboardNumNotificationText);
		unreadMessagesText.setText(String.valueOf(count));
	}

	@Override
	public void setTodayNews(ArrayList<News> newsList) {
		if(newsList == null)
			return;
		NewsTodayAdapter newsTodayAdapter = new NewsTodayAdapter(this.getActivity(), newsList);
		if(newsList.size() > 0) {
			this.v.findViewById(R.id.dashboardTodayNewsNoNewsText).setAlpha(0);
			((TextView)this.v.findViewById(R.id.dashboardNumNews)).setText(String.valueOf(newsList.size()));
		}
		if(this.todayNewsListView != null) 
			this.todayNewsListView.setAdapter(newsTodayAdapter);
	}

	@Override
	public void setTodayAgenda(ArrayList<AgendaKaryawan> agendaKaryawanList) {
		if(agendaKaryawanList == null)
			return;
		AgendaKaryawanAdapter agendaKaryawanAdapter = new AgendaKaryawanAdapter(this.getActivity(), agendaKaryawanList);
		if(agendaKaryawanAdapter.getCount() > 0) {
			this.v.findViewById(R.id.dashboardTodayAgendaNoAgendaText).setAlpha(0);
			((TextView)this.v.findViewById(R.id.dashboardNumAgenda)).setText(String.valueOf(agendaKaryawanList.size()));
		}
		if(this.todayAgendaListView != null)
			this.todayAgendaListView.setAdapter(agendaKaryawanAdapter);
		
	}
}
