package id.ac.ub.ptiik.papps.base;

public class MessageSent extends Message {
	
	public MessageSent(
			int id, int type, String message, String sent, 
			String received, String sender, String receiver, int status) {
		this.id = id;
		this.message = message;
		this.type = Message.TYPE_SENT;
		this.sent = sent;
		this.received = received;
		this.sender = sender;
		this.receiver = receiver;
		this.readStatus = status;
	}
	
	public MessageSent(String message, String sent, String sender, String receiver) {
		this.message = message;
		this.type = Message.TYPE_SENT;
		this.sent = sent;
		this.received = "";
		this.sender = sender;
		this.receiver = receiver;
		this.readStatus = MessageSent.STATUS_READ;
	}
	
	public void setRead() {
		this.readStatus = STATUS_READ;
	}
	
}
