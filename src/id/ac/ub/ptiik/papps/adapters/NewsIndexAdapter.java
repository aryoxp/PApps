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
		public TextView excerpt;
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
			vh.excerpt = (TextView) rowView.findViewById(R.id.itemNewsExcerpt);
			vh.date = (TextView) rowView.findViewById(R.id.itemNewsDate);
			vh.row = rowView;
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		
		String date = this.newsList.get(position).post_date;
		String tanggal = (date.split(" "))[0];
		String jam = (date.split(" "))[1];
		String tanggalParts[] = tanggal.split("-");
		
		vh.title.setText(this.newsList.get(position).post_title);
		vh.date.setText(tanggalParts[2] + "/" + tanggalParts[1] + "/" + tanggalParts[0]
				+ " " + jam.substring(0, jam.length()-3));
		String post_content = this.newsList.get(position).post_content;
		if(position == 0) {
			vh.excerpt.setText(this.newsList.get(position).post_content + " ...");
		} else {
			int cutPosition = 300;
			if(post_content.length() <= 300)
				cutPosition = post_content.length();
			vh.excerpt.setText(post_content.substring(0, cutPosition) + " ...");
		}
		vh.row.setVisibility(View.VISIBLE);
		return rowView;
	}

	

}
