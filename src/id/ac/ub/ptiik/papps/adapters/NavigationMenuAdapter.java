package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.NavMenu;

import java.util.ArrayList;
//import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//import ap.mobile.utils.TypefaceUtils;

public class NavigationMenuAdapter extends BaseAdapter {

	private ArrayList<NavMenu> listMenu;
	private Context context;
	
	public NavigationMenuAdapter(Context context, ArrayList<NavMenu> listMenu) {
		this.context = context;
		this.listMenu = listMenu;
	}
	
	@Override
	public int getCount() {
		return listMenu.size();
	}

	@Override
	public Object getItem(int position) {
		return listMenu.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public ImageView icon;
		public TextView title;
		public TextView description;
		public TextView count;
		public View row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_menu_navigation, null);
			ViewHolder vh = new ViewHolder();
			vh.title = (TextView) rowView.findViewById(R.id.menuTitle);
			vh.description = (TextView) rowView.findViewById(R.id.menuSubTitle);
			vh.icon = (ImageView) rowView.findViewById(R.id.menuIcon);
			vh.count = (TextView) rowView.findViewById(R.id.menuCount);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		
		NavMenu menu = this.listMenu.get(position);
		
		ViewHolder vh = (ViewHolder) rowView.getTag();
		vh.title.setText(menu.title);
		//vh.title.toUpperCase(Locale.US));
		//vh.title.setTypeface(TypefaceUtils.NewInstance(this.context).normalCondensed());
		vh.description.setText(menu.description);
		vh.count.setText(String.valueOf(menu.notificationCount));
		
		if(this.listMenu.get(position).imageResourceId != 0)
			vh.icon.setImageResource(menu.imageResourceId);
		if(this.listMenu.get(position).isActive) vh.row.setBackgroundColor(0xff2980b9);
		else vh.row.setBackgroundColor(Color.TRANSPARENT);
		int isCountVisible = (menu.notificationCount == 0)?View.GONE:View.VISIBLE;
		vh.count.setVisibility(isCountVisible);
		
		return rowView;
	}

}
