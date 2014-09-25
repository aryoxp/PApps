package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.interfaces.LoginDialogFinishInterface;
import id.ac.ub.ptiik.papps.interfaces.LoginInterface;
import id.ac.ub.ptiik.papps.tasks.LoginTask;
import android.app.AlertDialog;
<<<<<<< HEAD
import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
=======
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
>>>>>>> masteronline
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends DialogFragment 
	implements View.OnClickListener, LoginInterface {
	
	private View v;
	private TextView loginProgressText;
	private String usernameKeySharedPreferences = "appUsername";
	private String karyawanIdKeySharedPreferences = "appKaryawanId";
	
	private LoginDialogFinishInterface mCallback;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		this.v = v;
		
		TextView buttonOK = (TextView) v.findViewById(R.id.buttonLoginOK);
		TextView buttonCancel = (TextView) v.findViewById(R.id.buttonLoginCancel);
		this.loginProgressText = (TextView) v.findViewById(R.id.textLoginProgress);
		
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
		String username = 
				PreferenceManager.getDefaultSharedPreferences(
						getActivity()).getString(this.usernameKeySharedPreferences, "");
		//Toast.makeText(getActivity(), "Current user: " + username, Toast.LENGTH_SHORT).show();
		
		EditText usernameEditText = (EditText)this.v.findViewById(R.id.textLoginUsername);
		usernameEditText.setText(username);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonLoginOK:
			
			String username = ((EditText) this.v.findViewById(R.id.textLoginUsername)).getEditableText().toString();
			String password = ((EditText) this.v.findViewById(R.id.textLoginPassword)).getEditableText().toString();
			LoginTask loginTask = new LoginTask(this.getActivity(), this, username, password);
			loginTask.execute();
			
			break;
		case R.id.buttonLoginCancel:
		default:
			this.dismiss();
			break;
		}
	}

	@Override
	public void onLoginStarted() {
		this.v.findViewById(R.id.loginFormContainer)
		.animate()
		.alpha(0)
		.setDuration(500)
		.start();
		this.v.findViewById(R.id.buttonLoginOK).setClickable(false);
		this.v.findViewById(R.id.buttonLoginCancel).setClickable(false);
		this.loginProgressText.setText("Please wait, logging you in...");
	}

	@Override
	public void onLoginSuccess(User user) {
		
		PreferenceManager.getDefaultSharedPreferences(getActivity())
			.edit()
			.putString(this.karyawanIdKeySharedPreferences, user.karyawan_id)
			.commit();
		
		PreferenceManager.getDefaultSharedPreferences(getActivity())
			.edit()
			.putString(this.usernameKeySharedPreferences, user.username)
			.commit();
		
		new AlertDialog.Builder(getActivity()).setTitle("Information")
			.setMessage("Hello " + user.nama + "!\nWelcome to PTIIK mobile app for Android.")
			.setPositiveButton("OK", null)
			.show();
		
		if(this.mCallback != null)
			this.mCallback.onLoginFinished(user);
		
		this.dismiss();
	}

	@Override
	public void onLoginFail(String error) {
		this.v.findViewById(R.id.loginFormContainer)
		.animate()
		.alpha(1f)
		.setDuration(500)
		.start();
		
		new AlertDialog.Builder(this.getActivity())
	    .setTitle("Error")
	    .setMessage("Sorry, we are unable to log you in with PTIIK server.\nPossibly wrong username or password, please try again.")
	    .setPositiveButton("OK", null)
	    .show();
		
		this.v.findViewById(R.id.buttonLoginOK).setClickable(true);
		this.v.findViewById(R.id.buttonLoginCancel).setClickable(true);
	}
	
	public void setOnFinishCallback(LoginDialogFinishInterface mCallback) {
		this.mCallback = mCallback;
	}

	@Override
	public void onLoginProgress(int progress, String message) {
		
	}

}
