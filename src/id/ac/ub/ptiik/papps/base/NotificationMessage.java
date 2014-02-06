package id.ac.ub.ptiik.papps.base;

public class NotificationMessage {
	
	public static int AGENDA_NEW = 0;
	public static int MESSAGE = 1;
	
	public String message;
	public int type;
	
	public NotificationMessage() {}
	public NotificationMessage(int type, String message) {
		this.message = message;
		this.type = type;
	}
	
}
