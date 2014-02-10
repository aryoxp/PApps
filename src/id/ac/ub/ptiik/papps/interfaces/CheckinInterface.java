package id.ac.ub.ptiik.papps.interfaces;

public interface CheckinInterface {
	public void onCheckinStarted();
	public void onCheckinSuccess();
	public void onCheckinFail(String error);
}
