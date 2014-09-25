package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.User;

public interface LoginInterface {
	public void onLoginStarted();
	public void onLoginSuccess(User user);
	public void onLoginFail(String error);
	public void onLoginProgress(int progress, String message);
}
