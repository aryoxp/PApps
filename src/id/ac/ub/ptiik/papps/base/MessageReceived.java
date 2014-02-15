package id.ac.ub.ptiik.papps.base;

public class MessageReceived extends Message {
	
	public MessageReceived(int type, String message, String sent, 
			String received, String sender, String receiver) {
		this.message = message;
		this.type = Message.TYPE_RECEIVED;
		this.sent = sent;
		this.received = received;
		this.sender = sender;
		this.receiver = receiver;
		this.readStatus = MessageReceived.STATUS_NEW;
	}
	
}
