package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.adapters.NavigationMenuAdapter;
import id.ac.ub.ptiik.papps.base.NavMenu;
import id.ac.ub.ptiik.papps.interfaces.NavigationInterface;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class NavigationFragment extends Fragment implements OnItemClickListener {
	
	private ArrayList<NavMenu> menus;
	private NavigationMenuAdapter menuAdapter;
	private ListView listMenu;
	private NavigationInterface navInterface;
	
	public NavigationFragment(){
		this.menus = new ArrayList<NavMenu>();
		
	}
	
	public void setMenu(ArrayList<NavMenu> menus) {
		this.menus = menus;
	}
	
	public ArrayList<NavMenu> getMenu() {
		return this.menus;
	}
	
	public NavigationMenuAdapter getMenuAdapter() {
		return this.menuAdapter;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = (View) inflater.inflate(R.layout.fragment_navigation, container, false);
		this.listMenu = (ListView) v.findViewById(R.id.menuList);
		this.menuAdapter = new NavigationMenuAdapter(getActivity(), this.menus);
		this.listMenu.setAdapter(this.menuAdapter);
		this.listMenu.setOnItemClickListener(this);
		
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		for(int i=0; i<this.menus.size(); i++)
			this.menus.get(i).deactivate();
		this.menus.get(position).activate();
		this.menuAdapter.notifyDataSetChanged();
		this.listMenu.invalidate();
		this.navInterface.OnNavigationMenuSelected(position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		this.navInterface = (NavigationInterface) activity;
		super.onAttach(activity);
	}
}
