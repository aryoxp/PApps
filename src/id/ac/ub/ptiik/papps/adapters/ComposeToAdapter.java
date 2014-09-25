package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.UserOnline;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComposeToAdapter extends BaseAdapter {

	private ArrayList<UserOnline> userList;
	private Context context;
	
	public ComposeToAdapter(Context context, ArrayList<UserOnline> userList) {
		this.context = context;
		this.userList = userList;
	}
	
	public int findItem(UserOnline u) {
		if(u != null) {
			for (int i = 0; i<this.userList.size(); i++) {
				if(u != null && u.username.equals(this.userList.get(i).username)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class OptionsViewHolder {
		public TextView userOptionsText;
		public TextView userOptionsStatusText;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_user_options, null);
			OptionsViewHolder vh = new OptionsViewHolder();
			vh.userOptionsText = (TextView) rowView.findViewById(R.id.userOptionsText);
			vh.userOptionsStatusText = (TextView) rowView.findViewById(R.id.userOptionsStatusText);
			//vh.row = rowView;
			rowView.setTag(vh);
		}
		OptionsViewHolder vh = (OptionsViewHolder) rowView.getTag();
		vh.userOptionsText.setText(this.userList.get(position).getNamaGelar());
		if(this.userList.get(position).status == 0) {
			vh.userOptionsStatusText.setTextColor(
					this.context.getResources().getColor(R.color.pomegranate));
			vh.userOptionsStatusText.setText("OFFLINE");
		}
		return rowView;
	}

}
