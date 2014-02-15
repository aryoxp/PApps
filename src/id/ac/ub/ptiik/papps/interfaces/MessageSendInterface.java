package id.ac.ub.ptiik.papps.interfaces;

public interface MessageSendInterface {
	public void onMessageSendStart();
	public void onMessageSendComplete(Boolean status);
	public void onMessageSendFail(String error);
}
