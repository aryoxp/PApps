package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.Karyawan;
import id.ac.ub.ptiik.papps.base.User;

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

public class KaryawanAdapter extends BaseAdapter {

	private ArrayList<Karyawan> karyawanList;
	private Context context;
	
	public KaryawanAdapter(Context context, ArrayList<Karyawan> karyawanList) {
		this.context = context;
		this.karyawanList = karyawanList;
	}
	
	public int findItem(User u) {
		if(u != null) {
			for (int i = 0; i<this.karyawanList.size(); i++) {
				if(u.karyawan_id != null && u.karyawan_id.equals(this.karyawanList.get(i).id)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	@Override
	public int getCount() {
		return karyawanList.size();
	}

	@Override
	public Object getItem(int position) {
		return karyawanList.get(position);
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
		vh.scheduleSelectedText.setText(this.karyawanList.get(position).getNamaGelar());
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
		vh.scheduleOptionsText.setText(this.karyawanList.get(position).getNamaGelar());
		return rowView;
	}

}
