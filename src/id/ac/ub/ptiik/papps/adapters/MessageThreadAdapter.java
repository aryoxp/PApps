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

	private class ViewHolder {
		public TextView messageDateTime;
		public TextView messageMessage;
		public View messageStatus;
		public View messageDeleteButton;
		public View messageContainer;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_message, null);
			ViewHolder vh = new ViewHolder();
			vh.messageDateTime = (TextView) rowView.findViewById(R.id.messageTanggal);
			vh.messageMessage = (TextView) rowView.findViewById(R.id.messageMessage);
			vh.messageStatus = rowView.findViewById(R.id.messageStatus);
			vh.messageDeleteButton = rowView.findViewById(R.id.messageButtonDelete);
			vh.messageContainer = rowView.findViewById(R.id.messageContainer);
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		Message message = this.messageList.get(position);
		if(message.readStatus == MessageReceived.STATUS_NEW)
			vh.messageStatus.setAlpha(1);
		else vh.messageStatus.setAlpha(0);
		//message.setRead();
		vh.messageDateTime.setText(message.getDateTimeString("dd MMM yyy HH:mm", Message.DATE_SENT));
		vh.messageMessage.setText(message.message);
		vh.messageDeleteButton.setTag(position);
		vh.messageDeleteButton.setOnClickListener(this);
		if(message.type == Message.TYPE_SENT) {
			vh.messageDeleteButton.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.button_clouds));
			vh.messageContainer.setBackgroundColor(this.context.getResources().getColor(R.color.clouds));
		} else {
			vh.messageDeleteButton.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.button_white));
			vh.messageContainer.setBackgroundColor(this.context.getResources().getColor(R.color.white));
		}
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
