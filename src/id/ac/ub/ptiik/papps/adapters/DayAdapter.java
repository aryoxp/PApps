package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import java.util.ArrayList;
//import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//import ap.mobile.utils.TypefaceUtils;

public class DayAdapter extends BaseAdapter {

	private ArrayList<String> options;
	private Context context;
	
	public DayAdapter(Context context, ArrayList<String> options) {
		this.context = context;
		this.options = options;
	}
	
	@Override
	public int getCount() {
		return options.size();
	}

	@Override
	public Object getItem(int position) {
		return options.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class SelectedViewHolder {
		public TextView scheduleSelectedText;
	}
	
	private class OptionsViewHolder {
		public TextView scheduleOptionsText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_schedule_selected, null);
			SelectedViewHolder vh = new SelectedViewHolder();
			vh.scheduleSelectedText = (TextView) rowView.findViewById(R.id.scheduleSelectedText);
			//vh.row = rowView;
			rowView.setTag(vh);
		}
		SelectedViewHolder vh = (SelectedViewHolder) rowView.getTag();
		vh.scheduleSelectedText.setText(this.options.get(position));
		return rowView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_schedule_options, null);
			OptionsViewHolder vh = new OptionsViewHolder();
			vh.scheduleOptionsText = (TextView) rowView.findViewById(R.id.scheduleOptionsText);
			//vh.row = rowView;
			rowView.setTag(vh);
		}
		OptionsViewHolder vh = (OptionsViewHolder) rowView.getTag();
		vh.scheduleOptionsText.setText(this.options.get(position));
		return rowView;
	}

}
