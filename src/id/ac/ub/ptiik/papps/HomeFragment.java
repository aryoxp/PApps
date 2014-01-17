package id.ac.ub.ptiik.papps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import ap.mobile.utils.TypefaceUtils;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		ImageView cloud9 = (ImageView) v.findViewById(R.id.cloud_9);
		ImageView cloud2 = (ImageView) v.findViewById(R.id.cloud_2);
		TextView cityName = (TextView) v.findViewById(R.id.cityName);
		Animation cloudAnimationShow = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_move);
		Animation cloudAnimationShowRev = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_move_rev);
		cloud9.startAnimation(cloudAnimationShow);
		cloud2.startAnimation(cloudAnimationShowRev);
		cityName.setTypeface(TypefaceUtils.NewInstance(getActivity()).normalCondensed());
		return v;
	}
}
