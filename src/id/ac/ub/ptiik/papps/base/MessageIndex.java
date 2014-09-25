package id.ac.ub.ptiik.papps.base;

public class MessageIndex extends Message {
	
	public MessageIndex(String sender, String receiver, String message, String sent, String received, int status) {
		this.sender = sender;
		this.receiver = receiver;
		this.sent = sent;
		this.message = message;
		this.received = received;
		this.readStatus = status;
	}
	
}
