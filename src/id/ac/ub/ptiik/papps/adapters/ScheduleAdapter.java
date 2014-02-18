package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.JadwalMengajar;
import java.util.ArrayList;
import java.util.Locale;
//import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//import ap.mobile.utils.TypefaceUtils;

public class ScheduleAdapter extends BaseAdapter {

	private ArrayList<JadwalMengajar> jadwalMengajarList;
	private Context context;
	
	public ScheduleAdapter(Context context, ArrayList<JadwalMengajar> jadwalMengajarList) {
		this.context = context;
		this.jadwalMengajarList = jadwalMengajarList;
	}
	
	@Override
	public int getCount() {
		return jadwalMengajarList.size();
	}

	@Override
	public Object getItem(int position) {
		return jadwalMengajarList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public TextView scheduleDateTimeText;
		public TextView scheduleLecturerText;
		public TextView scheduleLocationText;
		public TextView scheduleSubjectText;
		public TextView scheduleClassText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_schedule, null);
			ViewHolder vh = new ViewHolder();
			vh.scheduleDateTimeText = (TextView) rowView.findViewById(R.id.scheduleDateTimeText);
			vh.scheduleLecturerText = (TextView) rowView.findViewById(R.id.scheduleLecturerText);
			vh.scheduleLocationText = (TextView) rowView.findViewById(R.id.scheduleLocationText);
			vh.scheduleSubjectText = (TextView) rowView.findViewById(R.id.scheduleMatakuliahText);
			vh.scheduleClassText = (TextView) rowView.findViewById(R.id.scheduleClassText);
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		JadwalMengajar jadwal = this.jadwalMengajarList.get(position);
		vh.scheduleSubjectText.setText(jadwal.matakuliah.getNama());
		String dateTime = jadwal.jam_mulai + "-" + jadwal.jam_selesai;
		dateTime = jadwal.hari.substring(0,1).toUpperCase(Locale.US) + jadwal.hari.substring(1) 
				+ " " + dateTime;
		vh.scheduleDateTimeText.setText(dateTime);
		vh.scheduleLocationText.setText(jadwal.ruang);
		vh.scheduleLecturerText.setText(jadwal.dosen.getNamaGelar());
		vh.scheduleClassText.setText(jadwal.prodi + "-" + jadwal.kelas);
		if(jadwal.prodi.equals("TIF/ILKOM"))
			vh.scheduleClassText.setBackgroundColor(this.context.getResources().getColor(R.color.petermann));
		else if(jadwal.prodi.equals("SI"))
			vh.scheduleClassText.setBackgroundColor(this.context.getResources().getColor(R.color.sunflower));
		else if(jadwal.prodi.equals("SISKOM"))
			vh.scheduleClassText.setBackgroundColor(this.context.getResources().getColor(R.color.emerald));
		return rowView;
	}

}
