package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.ac.ub.ptiik.papps.adapters.CalendarAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarFragment extends Fragment
	implements OnClickListener {
	
	View v;
	TextView monthYearText;
	Calendar currentCalendar;
	
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
		View nextMonthButton = this.v.findViewById(R.id.calendarNextMonthButton);
		View prevMonthButton = this.v.findViewById(R.id.calendarPrevMonthButton);
		nextMonthButton.setOnClickListener(this);
		prevMonthButton.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		CalendarAdapter adapter = new CalendarAdapter(getActivity(), 1, 2014);
		GridView calendarGrid = (GridView) this.v.findViewById(R.id.calendarGrid);
		calendarGrid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int month = this.currentCalendar.get(Calendar.MONTH);
		int year = this.currentCalendar.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
		Log.d("month before", String.valueOf(this.currentCalendar.get(Calendar.MONTH)));
		
		switch(v.getId()) {
			case R.id.calendarNextMonthButton:
				month++;
				break;
			case R.id.calendarPrevMonthButton:
				month--;
				break;
		}
		
		this.currentCalendar.set(Calendar.MONTH, month);

		Log.d("month after", String.valueOf(this.currentCalendar.get(Calendar.MONTH)));
		this.monthYearText = (TextView) this.v.findViewById(R.id.calendarMonthYearText);
		this.monthYearText.setText(sdf.format(this.currentCalendar.getTime()));
	}
}
