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

	private ArrayList<MessageReceived> notificationMessageList;
	private Context context;
	
	public MessageThreadAdapter(Context context, ArrayList<MessageReceived> notificationMessageList) {
		this.context = context;
		this.notificationMessageList = notificationMessageList;
	}
	
	public MessageThreadAdapter updateMessages(ArrayList<MessageReceived> messages) {
		this.notificationMessageList = messages;
		return this;
	}
	
	public int findItem(MessageReceived u) {
		if(u != null) {
			for (int i = 0; i<this.notificationMessageList.size(); i++) {
				if(u.id != 0 && u.id == this.notificationMessageList.get(i).id) {
					return i;
				}
			}
		}
		return -1;
	}
	
	@Override
	public int getCount() {
		return notificationMessageList.size();
	}

	@Override
	public Object getItem(int position) {
		return notificationMessageList.get(position);
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
	}

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
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		MessageReceived message = this.notificationMessageList.get(position);
		if(message.status == MessageReceived.STATUS_NEW)
			vh.messageStatus.setAlpha(1);
		else vh.messageStatus.setAlpha(0);
		//message.setRead();
		vh.messageDateTime.setText(message.getDateTimeString("dd MMM yyy HH:mm", Message.DATE_SENT));
		vh.messageMessage.setText(message.message);
		vh.messageDeleteButton.setTag(position);
		vh.messageDeleteButton.setOnClickListener(this);
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
					MessageReceived message = notificationMessageList.get(position);
					MessageDBHelper handler = new MessageDBHelper(context);
					handler.delete(message);
					notificationMessageList.remove(message);
					notifyDataSetChanged();
					Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("No", null)
			.show();

	}
	

}
