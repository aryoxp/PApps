package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.NotificationMessage;

import java.util.ArrayList;
//import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//import ap.mobile.utils.TypefaceUtils;

public class NotificationMessageAdapter extends BaseAdapter {

	private ArrayList<NotificationMessage> notificationMessageList;
	private Context context;
	
	public NotificationMessageAdapter(Context context, ArrayList<NotificationMessage> notificationMessageList) {
		this.context = context;
		this.notificationMessageList = notificationMessageList;
	}
	
	public NotificationMessageAdapter updateMessages(ArrayList<NotificationMessage> messages) {
		this.notificationMessageList = messages;
		return this;
	}
	
	public int findItem(NotificationMessage u) {
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
		public TextView messageFrom;
		public TextView messageType;
		public TextView messageMessage;
		//public View row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_message, null);
			ViewHolder vh = new ViewHolder();
			vh.messageFrom = (TextView) rowView.findViewById(R.id.messageFrom);
			vh.messageDateTime = (TextView) rowView.findViewById(R.id.messageTanggal);
			vh.messageMessage = (TextView) rowView.findViewById(R.id.messageMessage);
			vh.messageType = (TextView) rowView.findViewById(R.id.messageType);
			//vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		NotificationMessage message = this.notificationMessageList.get(position);
		vh.messageFrom.setText(message.from);
		vh.messageDateTime.setText(message.dateTime);
		vh.messageType.setText(String.valueOf(message.type));
		vh.messageMessage.setText(message.message);
		return rowView;
	}

}
