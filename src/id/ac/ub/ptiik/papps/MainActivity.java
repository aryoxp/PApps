package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.base.AppFragment;
import id.ac.ub.ptiik.papps.base.GCM;
import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.GCMHelper;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.CheckinInterface;
import id.ac.ub.ptiik.papps.interfaces.ContentFragmentInterface;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;
import id.ac.ub.ptiik.papps.tasks.CheckinTask;

import java.util.ArrayList;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity 
	implements NavigationInterface, CheckinInterface, ContentFragmentInterface {

	private ArrayList<NavMenu> menus;
	private NavigationFragment navFragment;
	private SlidingMenu navigationMenu;
	
	private NavMenu menuHome;
	private NavMenu menuNews;
	private NavMenu menuSchedule;
	private NavMenu menuAgenda;
	private NavMenu menuMessages;
	
	private GCMHelper gcmHelper;
	private String registrationId;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the application default values for the first time
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);		
		
		setContentView(R.layout.activity_main); // show main activity layout
		setBehindContentView(R.layout.menu_container); // add sliding menu container layout
		
		initiateNavigationMenu();		
		configureNavigationMenu();
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null)
		{
			switch (bundle.getInt("fragment")) {
			case AppFragment.FRAGMENT_MESSAGES:
					this.OnNavigationMenuSelected(menuMessages);
				break;
			default:
				this.OnNavigationMenuSelected(menuHome);
				break;
			}
		} else
			this.OnNavigationMenuSelected(menuHome);
		this.gcmHelper = new GCMHelper(this);
	}
		
	private void configureNavigationMenu() {
		
		// configuring Action Bar
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setSlidingActionBarEnabled(false);
		
		// navigation fragment initialization
		this.navFragment = new NavigationFragment();
		this.navFragment.setMenu(this.menus);
		
		// configure navigation menu
		this.navigationMenu = this.getSlidingMenu();
		this.navigationMenu.setFadeEnabled(true);
		this.navigationMenu.setFadeDegree(0.3f);
		this.navigationMenu.setBehindOffset(
			(int) (this.getResources().getDisplayMetrics().widthPixels * 0.2));
		this.navigationMenu.setMode(SlidingMenu.LEFT);
		this.navigationMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		this.navigationMenu.setShadowDrawable(R.drawable.menu_shadow);
		this.navigationMenu.setShadowWidth(
			(int) (this.getResources().getDisplayMetrics().widthPixels * 0.05));

		// show navigation fragment to the configured menu
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fragmentNavigationContainer, navFragment)
		.commit();
	}

	private void initiateNavigationMenu() {
		this.menus = new ArrayList<NavMenu>();
		this.menuHome = new NavMenu(NavMenu.MENU_HOME, "Home", "Apps home screen", R.drawable.ic_places_1, "home");
		this.menuNews = new NavMenu(NavMenu.MENU_NEWS, "News", "News and Information", R.drawable.ic_communication_99, "news");
		this.menuSchedule = new NavMenu(NavMenu.MENU_SCHEDULE, "Schedule", "Lecturing Schedules", R.drawable.ic_time_4, "schedule");
		this.menuAgenda = new NavMenu(NavMenu.MENU_AGENDA, "Agenda", "My Agenda", R.drawable.ic_time_3, "agenda");
		this.menuMessages = new NavMenu(NavMenu.MENU_MESSAGES, "Messages", "Incoming messages", R.drawable.ic_communication_63, "messages");
		this.menus.add(menuHome);
		this.menus.add(menuNews);
		this.menus.add(menuSchedule);
		this.menus.add(menuAgenda);
		this.menus.add(menuMessages);
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    
	    this.registrationId = this.gcmHelper.getRegistrationId();
		Log.d("c2dm ID", "Device is registered with ID: " + this.registrationId);
		String username = SystemHelper.getSystemUser(getApplicationContext()).username;
		if(username != null) {
			CheckinTask checkinTask = new CheckinTask(getApplicationContext(), 
					this, username, GCM.USER_ONLINE);
			checkinTask.execute();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				this.startActivity(i);
				return true;
			case android.R.id.home:
				if(this.getSlidingMenu().isShown())
					this.getSlidingMenu().toggle();
				return true;
		}
		return false;
	}

	@Override
	public void OnNavigationMenuSelected(NavMenu menu) {
		Boolean isShown = this.getSlidingMenu().isMenuShowing();
		if(isShown)
			this.getSlidingMenu().toggle();
		Fragment newFragment = getSupportFragmentManager().findFragmentByTag(menu.tag);
		switch(menu.id) {
			case NavMenu.MENU_SCHEDULE:
				if(newFragment == null) {
					newFragment = new ScheduleFragment();
				}
				break;
			case NavMenu.MENU_NEWS:
				if(newFragment == null) {
					newFragment = new NewsFragment();
				}
				break;
			case NavMenu.MENU_AGENDA:
				if(newFragment == null) {
					User u = SystemHelper.getSystemUser(this);
					if(u == null) {
						Toast.makeText(this, "You have to login to use this feature", 
								Toast.LENGTH_SHORT).show();
						return;
					}
					newFragment = new CalendarFragment();
					Bundle args = new Bundle();
					args.putString("idKaryawan", u.karyawan_id);
					newFragment.setArguments(args);
				}
				break;
			case NavMenu.MENU_MESSAGES:
				if(newFragment == null) {
					MessagesFragment messagesFragment = new MessagesFragment();
					messagesFragment.setOnNavigationCallback(this);
					newFragment = messagesFragment;
				}
				break;
			default:
				if(newFragment == null) {
					DashboardFragment fragment = new DashboardFragment();
					fragment.setNavigationCallback(this);
					newFragment = fragment;
				}
		}
		
		this.setContentFragment(newFragment, menu.tag);
		
		this.navFragment.activateMenu(menu);

	}

	@Override
	public void onBackPressed() {
		Fragment fragment = getSupportFragmentManager()
				.findFragmentByTag(this.menuHome.tag);
		if(fragment == null) {
			this.OnNavigationMenuSelected(this.menuHome);
		} else {
			super.onBackPressed();
			return;
		}
		
		
	}

	@Override
	public void onCheckinStarted() {}

	@Override
	public void onCheckinSuccess() {
		Log.i("Checkin", "User checkin - OK");
	}

	@Override
	public void onCheckinFail(String error) {
		Log.i("Checkin", "User checkin - NOK");
	}

	@Override
	public void setContentFragment(Fragment fragment, String tag) {
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
		.replace(R.id.contentContainer, fragment, tag)
		.commit();
	}
	
	
}
