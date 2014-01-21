package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends SlidingFragmentActivity 
	implements NavigationInterface {

	private ArrayList<NavMenu> menus;
	private NavigationFragment navFragment;
	private SlidingMenu navigationMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the application default values for the first time
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);		
		
		setContentView(R.layout.activity_main); // show main activity layout
		setBehindContentView(R.layout.menu_container); // add sliding menu container layout
		
		initiateNavigationMenu();		
		configureNavigationMenu();
		showDashboardFragments();
		
	}

	private void configureNavigationMenu() {
		
		// configuring Action Bar
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setSlidingActionBarEnabled(false);
		
		// navigation fragment initialization
		navFragment = new NavigationFragment();
		navFragment.setMenu(this.menus);
		
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
		this.navFragment.getMenu().get(0).activate();
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fragmentNavigationContainer, navFragment)
		.commit();
	}

	private void showDashboardFragments() {
		DashboardFragment fragment = new DashboardFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.contentContainer, fragment, "dashboard");
		transaction.commit();
	}

	private void initiateNavigationMenu() {
		this.menus = new ArrayList<NavMenu>();
		this.menus.add(new NavMenu("Home", "Apps home screen", R.drawable.places_1));
		this.menus.add(new NavMenu("News", "News and Information", R.drawable.communication_99));
		this.menus.add(new NavMenu("Class", "My lecturing classes", R.drawable.users_17));
		this.menus.add(new NavMenu("Schedule", "Lecturing Schedules", R.drawable.time_4));
		this.menus.add(new NavMenu("Agenda", "My Agenda", R.drawable.time_3));
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
	public void OnNavigationMenuSelected(int position) {
		this.getSlidingMenu().toggle();
		Fragment newFragment;
		String tag = null;
		switch(position) {
			case 3:
				newFragment = getSupportFragmentManager()
					.findFragmentByTag("schedule");
				if(newFragment == null) {
					newFragment = new ScheduleFragment();
					tag = "schedule";
				}
				else if(position == 3) {
					//this.navAnimation.toggle();
					return;
				}
				break;
			case 1:
			case 2:
			case 4:
			default:
				newFragment = getSupportFragmentManager()
					.findFragmentByTag("dashboard");
				if(newFragment == null) {
					newFragment = new DashboardFragment();
					tag = "dashboard" ;
				}
				else if(position == 0 || position == 1 || position == 2) {
					//this.navAnimation.toggle();
					return;
				}
		}
		
		getSupportFragmentManager()
			.beginTransaction()
			.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
			.replace(R.id.contentContainer, newFragment, tag)
			.commit();
		
		//this.navAnimation.toggle();
	}

	

}
