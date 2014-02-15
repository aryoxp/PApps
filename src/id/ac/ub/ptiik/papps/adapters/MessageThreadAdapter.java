package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.Message;
import id.ac.ub.ptiik.papps.base.MessageReceived;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;

import java.util.ArrayList;
//import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageThreadAdapter extends BaseAdapter 
	implements OnClickListener {

	private ArrayList<Message> messageList;
	private Context context;
	
	public MessageThreadAdapter(Context context, ArrayList<Message> messageList) {
		this.context = context;
		this.messageList = messageList;
	}
	
	public MessageThreadAdapter updateMessages(ArrayList<Message> messages) {
		this.messageList = messages;
		return this;
	}
	
	public int findItem(MessageReceived u) {
		if(u != null) {
			for (int i = 0; i<this.messageList.size(); i++) {
				if(u.id != 0 && u.id == this.messageList.get(i).id) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public void add(Message message) {
		this.messageList.add(message);
	}
	
	public void setItems(ArrayList<Message> messages) {
		this.messageList = messages;
	}
	
	public void clearItems() {
		this.messageList.clear();
	}
	
	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = this.messageList.get(position);
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		TextView messageDateTime;
		TextView messageMessage;
		View messageStatus;
		View rowView;
		if(message.type == Message.TYPE_SENT)
		{
			rowView = inflater.inflate(R.layout.item_message_alt, null, false);
			messageDateTime = (TextView) rowView.findViewById(R.id.messageAltTanggal);
			messageMessage = (TextView) rowView.findViewById(R.id.messageAltMessage);
			messageStatus = rowView.findViewById(R.id.messageAltStatus);
		} else {
			rowView = inflater.inflate(R.layout.item_message, null);
			messageDateTime = (TextView) rowView.findViewById(R.id.messageTanggal);
			messageMessage = (TextView) rowView.findViewById(R.id.messageMessage);
			messageStatus = rowView.findViewById(R.id.messageStatus);
		}
		
		if(message.readStatus == MessageReceived.STATUS_NEW)
			messageStatus.setAlpha(1);
		else messageStatus.setAlpha(0);
		messageDateTime.setText(message.getDateTimeString("dd MMM yyy HH:mm", Message.DATE_SENT));
		messageMessage.setText(message.message);
		return rowView;
	}

	@Override
	public void onClick(View v) {
		final int position = (Integer) v.getTag();
		
		new AlertDialog.Builder(this.context)
			.setMessage("Delete this message?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Message message = messageList.get(position);
					MessageDBHelper handler = new MessageDBHelper(context);
					handler.delete(message);
					messageList.remove(message);
					notifyDataSetChanged();
					Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("No", null)
			.show();

	}
	

}
