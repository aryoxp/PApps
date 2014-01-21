package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.News;

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

public class NewsIndexAdapter extends BaseAdapter {
	
	private ArrayList<News> newsList;
	private Context c;
	
	public NewsIndexAdapter(Context c, ArrayList<News> newsList){
		this.newsList = newsList;
		this.c = c;
	}
	
	@Override
	public int getCount() {
		return this.newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(this.newsList.get(position).ID);
	}

	private class ViewHolder {
		public TextView date;
		public TextView title;
		//public TextView description;
		public View row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_news_index, null);
			ViewHolder vh = new ViewHolder();
			vh.title = (TextView) rowView.findViewById(R.id.itemNewsTitle);
			//vh.description = (TextView) rowView.findViewById(R.id.menuDescription);
			vh.date = (TextView) rowView.findViewById(R.id.itemNewsDate);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		vh.title.setText(this.newsList.get(position).post_title);
		vh.date.setText(this.newsList.get(position).post_date);
		vh.row.setVisibility(View.VISIBLE);
		return rowView;
	}

	

}
