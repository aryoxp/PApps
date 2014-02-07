package id.ac.ub.ptiik.papps.base;

public class NotificationMessage {
	
	public static int NEW_AGENDA = 0;
	public static int MESSAGE = 1;
	
	public int id, type;
	public String message;
	public String dateTime;
	public String from;
	
	public NotificationMessage(int id, int type, String message, String dateTime, String from) {
		this.id = id;
		this.message = message;
		this.type = type;
		this.dateTime = dateTime;
		this.from = from;
	}
	public NotificationMessage(int type, String message, String dateTime, String from) {
		this.message = message;
		this.type = type;
		this.dateTime = dateTime;
		this.from = from;
	}
	
}
