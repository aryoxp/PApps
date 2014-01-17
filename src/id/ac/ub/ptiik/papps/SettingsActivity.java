package id.ac.ub.ptiik.papps;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(0xffffffff);
		// Display the fragment as the main content.
	    this.getActionBar().setLogo(R.drawable.app_icon);
	    this.getActionBar().setDisplayHomeAsUpEnabled(true);
	    this.getActionBar().setHomeButtonEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
			onBackPressed();
		return true;
	}
}