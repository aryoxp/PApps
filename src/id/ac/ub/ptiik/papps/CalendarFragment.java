package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.CalendarAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class CalendarFragment extends Fragment {
	
	View v;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_calendar, container, false);
		this.v = v;
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
}
