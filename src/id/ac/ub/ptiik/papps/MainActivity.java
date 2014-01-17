package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.animations.NavigationAnimation;
import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity 
	implements NavigationInterface {

	private ArrayList<NavMenu> menus;
	private View navView;
	private NavigationAnimation navAnimation;
	private NavigationFragment navFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the default values for the first time
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);		
		
		setContentView(R.layout.activity_main);
		this.navView = this.findViewById(R.id.navigationContainer);
		this.navView.setVisibility(View.INVISIBLE);
		
		initiateNavigationMenu();		
		navFragment = new NavigationFragment();
		navFragment.setMenu(this.menus);
		
		showFragments();
		
		this.navAnimation = new NavigationAnimation(this, this.navView);
		getActionBar().setIcon(R.drawable.app_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		this.navFragment.getMenu().get(0).activate();
	}

	private void showFragments() {
		HomeFragment homeFragment = new HomeFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.contentContainer, homeFragment, "home");
		transaction.add(R.id.navigationContainer, navFragment, "nav");
		transaction.commit();
	}

	private void initiateNavigationMenu() {
		this.menus = new ArrayList<NavMenu>();
		this.menus.add(new NavMenu("Home", "Apps home screen", R.drawable.places_1));
		this.menus.add(new NavMenu("News", "News and Information", R.drawable.communication_99));
		this.menus.add(new NavMenu("My Class", "My lecturing classes", R.drawable.users_17));
		this.menus.add(new NavMenu("Schedule", "Lecturing Schedules", R.drawable.time_3));
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
			case R.id.action_menu:
				this.navAnimation.toggle();
				return true;
			case R.id.action_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				this.startActivity(i);
			case android.R.id.home:
				this.navAnimation.toggle();
				return true;
		}
		return false;
	}

	@Override
	public void OnNavigationMenuSelected(int position) {
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
					this.navAnimation.toggle();
					return;
				}
				break;
			case 1:
			case 2:
			default:
				newFragment = getSupportFragmentManager()
					.findFragmentByTag("home");
				if(newFragment == null) {
					newFragment = new HomeFragment();
					tag = "home";
				}
				else if(position == 0 || position == 1 || position == 2) {
					this.navAnimation.toggle();
					return;
				}
		}
		
		getSupportFragmentManager()
			.beginTransaction()
			.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
			.replace(R.id.contentContainer, newFragment, tag)
			.commit();
		
		this.navAnimation.toggle();
	}

	

}
