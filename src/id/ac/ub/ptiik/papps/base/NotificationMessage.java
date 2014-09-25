package id.ac.ub.ptiik.papps.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationMessage {
	
	public static final int NEW_AGENDA = 0;
	public static final int MESSAGE = 1;
	
	public static final int STATUS_NEW = 2;
	public static final int STATUS_READ = 3;
	
	public static final int SENT = 100;
	public static final int RECEIVED = 101;
	
	public int id, type;
	public String message;
	public String sent;
	public String received;
	public String from;
	public int status;
	
	private Date sentDate;
	private Date receivedDate;
	
	public NotificationMessage(int id, int type, String message, String sent, String received, String from, int status) {
		this.id = id;
		this.message = message;
		this.type = type;
		this.sent = sent;
		this.received = received;
		this.from = from;
		this.status = status;
	}
	public NotificationMessage(int type, String message, String sent, String received, String from) {
		this.message = message;
		this.type = type;
		this.sent = sent;
		this.received = received;
		this.from = from;
		this.status = NotificationMessage.STATUS_NEW;
	}
	
	public NotificationMessage(String from, String message, String sent, String received, int status) {
		this.from = from;
		this.sent = sent;
		this.message = message;
		this.received = received;
		this.status = status;
	}
	
	public String getDateTimeString(String format, int kind) {
		SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		try {
			this.sentDate = sdfParser.parse(this.sent);
			this.receivedDate = sdfParser.parse(this.received);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		switch (kind) {
		case RECEIVED:
			return sdf.format(this.receivedDate);
		default:
			return sdf.format(this.sentDate);
		}
	}
	
	public void setRead() {
		this.status = STATUS_READ;
	}
	
	
	
}
