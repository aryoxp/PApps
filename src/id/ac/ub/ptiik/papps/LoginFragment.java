package id.ac.ub.ptiik.papps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends DialogFragment 
	implements View.OnClickListener {
	
	private View v;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		this.v = v;
		
		TextView buttonOK = (TextView) v.findViewById(R.id.buttonLoginOK);
		TextView buttonCancel = (TextView) v.findViewById(R.id.buttonLoginCancel);
		
		buttonOK.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		LayoutParams lp = getDialog().getWindow().getAttributes();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		lp.width = (int) (dm.widthPixels * 0.9);
		getDialog().getWindow().setAttributes(lp);
		
		// Restore preferences
		SharedPreferences settings = this.getActivity().getSharedPreferences("papps", Context.MODE_PRIVATE);
		String username = settings.getString("appUsername", "guest");
		Toast.makeText(getActivity(), "Current user: " + username, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonLoginOK:
			this.v.findViewById(R.id.loginFormContainer)
			.animate()
			.alpha(0)
			.setDuration(500)
			.start();
			this.v.findViewById(R.id.buttonLoginOK).setClickable(false);
			this.v.findViewById(R.id.buttonLoginCancel).setClickable(false);
			break;
		case R.id.buttonLoginCancel:
		default:
			this.dismiss();
			break;
		}
	}
}
