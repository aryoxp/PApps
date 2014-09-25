package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.AgendaAdapter;
import id.ac.ub.ptiik.papps.adapters.AgendaKaryawanAdapter;
import id.ac.ub.ptiik.papps.base.CalendarCell;
<<<<<<< HEAD
import android.app.DialogFragment;
import android.os.Bundle;
=======
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
>>>>>>> masteronline
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AgendaFragment extends DialogFragment implements OnClickListener {
	View v;
	CalendarCell cell;
	ListView agendaList;
	ListView agendaKaryawanList;
	AgendaKaryawanAdapter agendaKaryawanAdapter;
	AgendaAdapter agendaAdapter;
	TextView tabPersonal;
	TextView tabAll;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		this.v = inflater.inflate(R.layout.fragment_agenda, container, false);
		
		this.agendaKaryawanAdapter = new AgendaKaryawanAdapter(getActivity(), this.cell.getListAgendaKaryawan());
		this.agendaAdapter = new AgendaAdapter(getActivity(), this.cell.getListAgenda());
		
		this.agendaList = (ListView) this.v.findViewById(R.id.agendaList);
		this.agendaKaryawanList = (ListView) this.v.findViewById(R.id.agendaKaryawanList);
		this.tabAll = (TextView) this.v.findViewById(R.id.agendaTabAll);
		this.tabPersonal = (TextView) this.v.findViewById(R.id.agendaTabPersonal);
		
		this.tabAll.setOnClickListener(this);
		this.tabPersonal.setOnClickListener(this);
		
		if(this.cell.getListAgendaKaryawan().size() == 0) {
			this.agendaKaryawanList.setAlpha(0);
			this.v.findViewById(R.id.agendaTabAll).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_white));
			this.v.findViewById(R.id.agendaNoEventsText).setAlpha(0);
		} else {
			this.agendaList.setAlpha(0);
			this.v.findViewById(R.id.agendaTabPersonal).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_white));
			this.v.findViewById(R.id.agendaNoEventsText).setAlpha(0);
		}
		
		if(this.cell.getListAgendaKaryawan().size() > 0 || this.cell.getListAgenda().size() > 0)
			this.v.findViewById(R.id.agendaNoEventsText).setAlpha(0);
		
		this.agendaList.setAdapter(this.agendaAdapter);
		this.agendaKaryawanList.setAdapter(this.agendaKaryawanAdapter);
		
		TextView tanggal = (TextView) this.v.findViewById(R.id.agendaDateText);
		TextView numAgenda = (TextView) this.v.findViewById(R.id.agendaNumAgendaText);
		
		numAgenda.setText(this.cell.getAgendaCount() + " All, " + this.cell.getAgendaKaryawanCount() + " Personal Events");
		tanggal.setText(this.cell.getFormattedString("d MMM yyyy"));
		
		return v;
	}
	
	public void putCalendarCell(CalendarCell cell) {
		this.cell = cell;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.agendaTabAll:
			if(this.agendaKaryawanList.getAlpha()>0)
				this.agendaKaryawanList.animate().alpha(0).setDuration(200).start();
			this.agendaList.animate().alpha(1).setDuration(200).start();
			this.v.findViewById(R.id.agendaTabAll).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_white));
			this.v.findViewById(R.id.agendaTabPersonal).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_clouds));
			if(this.cell.getAgendaCount() == 0)
				this.v.findViewById(R.id.agendaNoEventsText).setAlpha(1);
			else this.v.findViewById(R.id.agendaNoEventsText).setAlpha(0);
			break;
		case R.id.agendaTabPersonal:
			if(this.agendaList.getAlpha()>0)
				this.agendaList.animate().alpha(0).setDuration(200).start();
			this.agendaKaryawanList.animate().alpha(1).setDuration(200).start();
			this.v.findViewById(R.id.agendaTabPersonal).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_white));
			this.v.findViewById(R.id.agendaTabAll).setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.button_clouds));
			if(this.cell.getAgendaKaryawanCount() == 0)
				this.v.findViewById(R.id.agendaNoEventsText).setAlpha(1);
			else this.v.findViewById(R.id.agendaNoEventsText).setAlpha(0);
		}
	}
}
