package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.AgendaKaryawan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class AgendaKaryawanAdapter extends BaseAdapter {
	
	private ArrayList<AgendaKaryawan> agendaList;
	private Context c;
	
	public AgendaKaryawanAdapter(Context c, ArrayList<AgendaKaryawan> agendaList){
		this.agendaList = agendaList;
		this.c = c;
	}
	
	@Override
	public int getCount() {
		return this.agendaList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.agendaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(position);
	}

	private class ViewHolder {
		public TextView date;
		public TextView kegiatan;
		public TextView info;
		public TextView keterangan;
		public View row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_agenda_karyawan, null);
			ViewHolder vh = new ViewHolder();
			vh.date = (TextView) rowView.findViewById(R.id.itemAgendaKaryawanDate);
			vh.kegiatan = (TextView) rowView.findViewById(R.id.itemAgendaKaryawanKegiatan);
			vh.info = (TextView) rowView.findViewById(R.id.itemAgendaKaryawanInfo);
			vh.keterangan = (TextView) rowView.findViewById(R.id.itemAgendaNamamk);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		AgendaKaryawan agenda = this.agendaList.get(position); 
		Calendar tanggalMulai = agenda.getCalendarTanggalMulai();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm", Locale.US);

		String keterangan = "";
		if(agenda.keterangan != "") keterangan += agenda.keterangan + " ";
		if(agenda.namamk != "") keterangan += agenda.namamk + " ";
		
		vh.date.setText(sdf.format(tanggalMulai.getTime()));
		vh.info.setText(agenda.ruang);
		vh.kegiatan.setText(agenda.kegiatan);
		vh.keterangan.setText(keterangan);
		vh.row.setVisibility(View.VISIBLE);
		
		return rowView;
	}

	

}
