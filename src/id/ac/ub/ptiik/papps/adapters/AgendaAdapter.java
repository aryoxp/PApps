package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.Agenda;

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

public class AgendaAdapter extends BaseAdapter {
	
	private ArrayList<Agenda> agendaList;
	private Context c;
	
	public AgendaAdapter(Context c, ArrayList<Agenda> agendaList){
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
		public TextView tanggal;
		public TextView judul;
		public TextView lokasi;
		public TextView keterangan;
		public View row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_agenda, null);
			ViewHolder vh = new ViewHolder();
			vh.tanggal = (TextView) rowView.findViewById(R.id.itemAgendaTanggal);
			vh.judul = (TextView) rowView.findViewById(R.id.itemAgendaJudul);
			vh.lokasi = (TextView) rowView.findViewById(R.id.itemAgendaLokasi);
			vh.keterangan = (TextView) rowView.findViewById(R.id.itemAgendaKeterangan);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		Agenda agenda = this.agendaList.get(position); 
		Calendar tanggalMulai = agenda.getCalendarTanggalMulai();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm", Locale.US);

		String keterangan = "";
		if(agenda.keterangan != "") keterangan += agenda.keterangan + " ";
		
		vh.tanggal.setText(sdf.format(tanggalMulai.getTime()));
		vh.lokasi.setText(agenda.lokasi);
		vh.judul.setText(agenda.judul);
		vh.keterangan.setText(keterangan);
		vh.row.setVisibility(View.VISIBLE);
		
		return rowView;
	}

	

}
