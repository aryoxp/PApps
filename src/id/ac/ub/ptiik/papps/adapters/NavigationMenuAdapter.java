package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.NavMenu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		public View row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.menu_navigation, null);
			ViewHolder vh = new ViewHolder();
			vh.title = (TextView) rowView.findViewById(R.id.menuTitle);
			vh.description = (TextView) rowView.findViewById(R.id.menuDescription);
			vh.icon = (ImageView) rowView.findViewById(R.id.menuIcon);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		vh.title.setText(this.listMenu.get(position).title);
		vh.description.setText(this.listMenu.get(position).description);
		if(this.listMenu.get(position).imageResourceId != 0)
			vh.icon.setImageResource(this.listMenu.get(position).imageResourceId);
		if(this.listMenu.get(position).isActive) vh.row.setBackgroundColor(0xffe4e4e4);
		else vh.row.setBackgroundColor(Color.TRANSPARENT);
		return rowView;
	}

}