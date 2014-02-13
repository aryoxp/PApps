package id.ac.ub.ptiik.papps.base;

public class MessageReceived extends Message {
	
	public MessageReceived(
			int id, int type, String message, String sent, 
			String received, String sender, String receiver, int status) {
		this.id = id;
		this.message = message;
		this.type = type;
		this.sent = sent;
		this.received = received;
		this.sender = sender;
		this.receiver = receiver;
		this.status = status;
	}
	public MessageReceived(int type, String message, String sent, 
			String received, String sender, String receiver) {
		this.message = message;
		this.type = type;
		this.sent = sent;
		this.received = received;
		this.sender = sender;
		this.receiver = receiver;
		this.status = MessageReceived.STATUS_NEW;
	}
	
	public void setRead() {
		this.status = STATUS_READ;
	}
	
}
