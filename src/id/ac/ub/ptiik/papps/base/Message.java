package id.ac.ub.ptiik.papps.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
	public static final int NEW_AGENDA = 0;
	public static final int MESSAGE = 1;
	
	public static final int STATUS_NEW = 2;
	public static final int STATUS_READ = 3;
	
	public static final int DATE_SENT = 100;
	public static final int DATE_RECEIVED = 101;
	
	public static final int TYPE_SENT = 90;
	public static final int TYPE_RECEIVED = 91;
	
	public int id, type, readStatus;
	public String message;
	public String sent;
	public String received;
	public String sender;
	public String receiver;
	
	public Message(){}
	
	public Message(
			int id, int type, String message, String sent, 
			String received, String sender, String receiver, int status) {
		this.id = id;
		this.message = message;
		this.type = type;
		this.sent = sent;
		this.received = received;
		this.sender = sender;
		this.receiver = receiver;
		this.readStatus = status;
	}
	
	public String getDateTimeString(String format, int dateKind) {
		
		SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		
		try {
			switch (dateKind) {
				case DATE_RECEIVED:
					Date receivedDate = sdfParser.parse(this.received);
					return sdf.format(receivedDate);
				default:
					Date sentDate = sdfParser.parse(this.sent);
					return sdf.format(sentDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public void setRead() {
		this.readStatus = STATUS_READ;
	}
}
