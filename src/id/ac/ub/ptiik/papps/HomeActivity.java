package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.NavigationMenuAdapter;
import id.ac.ub.ptiik.papps.base.AppFragment;
import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends Activity implements AppInterface, NavigationInterface {

	private CharSequence mTitle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavMenu> menus;
	private NavMenu menuHome;
	private NavMenu menuNews;
	private NavMenu menuSchedule;
	private NavMenu menuAgenda;
	private NavMenu menuMessages;
	private NavigationMenuAdapter navigationMenuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_home);
		
		
		mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        this.initiateNavigationMenu();
        
        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //        R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
     // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        
	}

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        /*
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        */
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        new Bundle();
        NavMenu menu = this.menus.get(position);
        
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = getFragmentManager().findFragmentByTag(menu.tag);
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
		
        fragmentManager.beginTransaction()
        	.replace(R.id.content_frame, newFragment, menu.tag).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        this.setTitle(this.menus.get(position).title);
        NavMenu.activate(this.menus, position);
        this.navigationMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    /*
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = "Sapi";//getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }
    */
    
	private void initiateNavigationMenu() {
		this.menus = new ArrayList<NavMenu>();
		this.menuHome = new NavMenu(NavMenu.MENU_HOME, "Home", "Apps home screen", R.drawable.ic_places_1, AppFragment.FRAGMENT_TAG_HOME);
		this.menuNews = new NavMenu(NavMenu.MENU_NEWS, "News", "News and Information", R.drawable.ic_communication_99, AppFragment.FRAGMENT_TAG_NEWS);
		this.menuSchedule = new NavMenu(NavMenu.MENU_SCHEDULE, "Schedule", "Lecturing Schedules", R.drawable.ic_time_4, AppFragment.FRAGMENT_TAG_SCHEDULE);
		this.menuAgenda = new NavMenu(NavMenu.MENU_AGENDA, "Agenda", "My Agenda", R.drawable.ic_time_3, AppFragment.FRAGMENT_TAG_AGENDA);
		this.menuMessages = new NavMenu(NavMenu.MENU_MESSAGES, "Messages", "Incoming messages", R.drawable.ic_communication_61, AppFragment.FRAGMENT_TAG_MESSAGES);
		this.menus.add(menuHome);
		this.menus.add(menuNews);
		this.menus.add(menuSchedule);
		this.menus.add(menuAgenda);
		this.menus.add(menuMessages);
		
		this.navigationMenuAdapter = new NavigationMenuAdapter(this, this.menus);
        this.mDrawerList.setAdapter(this.navigationMenuAdapter);
	}

	@Override
	public void setContentFragment(android.support.v4.app.Fragment fragment,
			String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActionBarTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnNavigationMenuSelected(NavMenu menu) {
		// TODO Auto-generated method stub
		
	}
}
