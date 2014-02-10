package id.ac.ub.ptiik.papps.interfaces;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.UserOnline;

public interface UserOnlineCheckInterface {
	public void onUserOnlinceCheckStarted();
	public void onUserOnlinceCheckSuccess(ArrayList<UserOnline> users);
	public void onUserOnlinceCheckFail(String error);
}
