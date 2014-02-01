package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.AgendaKaryawanAdapter;
import id.ac.ub.ptiik.papps.base.CalendarCell;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DayCalendarFragment extends DialogFragment {
	View v;
	CalendarCell cell;
	ListView agendaList;
	AgendaKaryawanAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		this.v = inflater.inflate(R.layout.fragment_agenda, container, false);
		this.adapter = new AgendaKaryawanAdapter(getActivity(), this.cell.getListAgenda());
		this.agendaList = (ListView) this.v.findViewById(R.id.agendaList);
		this.agendaList.setAdapter(this.adapter);
		
		TextView tanggal = (TextView) this.v.findViewById(R.id.agendaDateText);
		TextView numAgenda = (TextView) this.v.findViewById(R.id.agendaNumAgendaText);
		
		numAgenda.setText(this.cell.getListAgenda().size() + " Events");
		tanggal.setText(this.cell.getDateMonthString());
		
		return v;
	}
	
	public void putCalendarCell(CalendarCell cell) {
		this.cell = cell;
	}
}
