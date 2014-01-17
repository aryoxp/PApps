package id.ac.ub.ptiik.papps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ap.mobile.utils.TypefaceUtils;

public class ScheduleFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_schedule, container, false);
		TextView titleText = (TextView) v.findViewById(R.id.fragmentScheduleTitle);
		titleText.setTypeface(TypefaceUtils.NewInstance(getActivity()).normalCondensed());
		return v;
	}
}
