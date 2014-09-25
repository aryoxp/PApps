package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.ac.ub.ptiik.papps.adapters.CalendarAdapter;
import id.ac.ub.ptiik.papps.base.CalendarCell;
import id.ac.ub.ptiik.papps.interfaces.AgendaKaryawanIndexInterface;
import id.ac.ub.ptiik.papps.tasks.AgendaKaryawanTask;
import android.app.Fragment;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarFragment extends Fragment
	implements OnClickListener, AgendaKaryawanIndexInterface, OnItemClickListener {
	
	View v;
	TextView monthYearText;
	Calendar currentCalendar;
	String userId;
	AgendaKaryawanTask agendaKaryawanTask;
	CalendarAdapter adapter;
	GridView calendarGrid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_calendar, container, false);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
		this.v = v;
		this.currentCalendar = Calendar.getInstance(Locale.US);
		this.currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
		this.monthYearText = (TextView) v.findViewById(R.id.calendarMonthYearText);
		this.monthYearText.setText(sdf.format(this.currentCalendar.getTime()));
		this.v.findViewById(R.id.calendarTableContainer).setAlpha(0);
		View nextMonthButton = this.v.findViewById(R.id.calendarNextMonthButton);
		View prevMonthButton = this.v.findViewById(R.id.calendarPrevMonthButton);
		nextMonthButton.setOnClickListener(this);
		prevMonthButton.setOnClickListener(this);
		this.userId = this.getArguments().getString("idKaryawan");
		
		this.setHasOptionsMenu(true);
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		this.adapter = new CalendarAdapter(getActivity());
		this.calendarGrid = (GridView) this.v.findViewById(R.id.calendarGrid);
		this.calendarGrid.setAdapter(this.adapter);
		this.calendarGrid.setOnItemClickListener(this);
		int month = this.currentCalendar.get(Calendar.MONTH);
		int year = this.currentCalendar.get(Calendar.YEAR);
		this.agendaKaryawanTask = 
				new AgendaKaryawanTask(getActivity(), this, this.userId, month, year);
		this.agendaKaryawanTask.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.calendarButtonRefresh:
		case R.id.calendarNextMonthButton:
		case R.id.calendarPrevMonthButton:
			if(this.agendaKaryawanTask.getStatus() != Status.RUNNING) {
				int month = this.currentCalendar.get(Calendar.MONTH);
				SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);		
				switch(v.getId()) {
					case R.id.calendarNextMonthButton:
						month++;
						break;
					case R.id.calendarPrevMonthButton:
						month--;
						break;
				}
				reloadCalendar(month, sdf);
			}
			break;
		}
	}

	private void reloadCalendar(int month, SimpleDateFormat sdf) {
		this.currentCalendar.set(Calendar.MONTH, month);
		this.monthYearText = (TextView) this.v.findViewById(R.id.calendarMonthYearText);
		this.monthYearText.setText(sdf.format(this.currentCalendar.getTime()));
		this.agendaKaryawanTask = 
				new AgendaKaryawanTask(getActivity(), 
						(AgendaKaryawanIndexInterface) this, 
						this.userId, this.currentCalendar.get(Calendar.MONTH), 
						this.currentCalendar.get(Calendar.YEAR));
		this.agendaKaryawanTask.execute();
	}

	@Override
	public void onRetrievingStart() {
		this.v.findViewById(R.id.calendarTableContainer).animate()
		.alpha(0)
		.setDuration(300)
		.start();
	}

	@Override
	public void onRetrieveComplete(ArrayList<CalendarCell> calendarCells) {
		this.v.findViewById(R.id.calendarTableContainer).animate()
		.alpha(1)
		.setDuration(300)
		.start();
		this.adapter.fillCalendar(calendarCells);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onRetrieveFail(String error) {		
		
	}

	@Override
	public void onRetrieveProgress(int percent, String status) {
		TextView statusText = (TextView) this.v.findViewById(R.id.calendarLoadingStatusText);
		statusText.setText(status);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CalendarCell cell = (CalendarCell) this.adapter.getItem(position);
		AgendaFragment fragment = new AgendaFragment();
		fragment.putCalendarCell(cell);
		fragment.show(this.getFragmentManager(), "dayCalendar");
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_agenda, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_agenda_refresh:
			if(this.agendaKaryawanTask.getStatus() != Status.RUNNING) {
				int month = this.currentCalendar.get(Calendar.MONTH);
				SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);		
				reloadCalendar(month, sdf);
			}
			break;
		}
		return true;
	}
}
