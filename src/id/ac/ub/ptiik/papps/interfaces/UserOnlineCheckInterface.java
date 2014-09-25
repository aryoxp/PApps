package id.ac.ub.ptiik.papps.interfaces;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.UserOnline;

public interface UserOnlineCheckInterface {
	public void onUserOnlineCheckStarted();
	public void onUserOnlineCheckSuccess(ArrayList<UserOnline> users);
	public void onUserOnlineCheckFail(String error);
}
