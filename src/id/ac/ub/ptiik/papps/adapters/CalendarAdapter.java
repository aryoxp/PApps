package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.CalendarCell;

import java.util.ArrayList;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<CalendarCell> dayList;
		
	public CalendarAdapter(Context c) {
		this.dayList = new ArrayList<CalendarCell>();
		this.context = c;
	}
	
	public void fillCalendar(ArrayList<CalendarCell> dayList) {
		this.dayList.clear();
		this.dayList.addAll(dayList);
	}
	
	
	@Override
	public int getCount() {
		return this.dayList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.dayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		View cell;
		TextView dayText;
		TextView calendarItems;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarCell c = this.dayList.get(position);
		
		View v = convertView;
		if(v == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_calendar_cell, parent, false);
			ViewHolder vh = new ViewHolder();
			vh.dayText = (TextView) v.findViewById(R.id.calendarDayText);
			vh.cell = v.findViewById(R.id.calendarCell);
			vh.calendarItems = (TextView) v.findViewById(R.id.calendarDayNumEvents);
			v.setTag(vh);
		} 
		ViewHolder vh = (ViewHolder) v.getTag();
		
		int agendaCount = c.getAgendaCount();
		vh.dayText.setText(String.valueOf(c.getDateString()));
		String s = "";
		if(agendaCount > 1) s = "S"; 
		if(agendaCount > 0) vh.calendarItems.setText(agendaCount + " EVT" + s);
		else vh.calendarItems.setText("");
		if(c.today) {
			vh.cell.setBackgroundColor(context.getResources().getColor(R.color.petermann));
			vh.dayText.setTextColor(context.getResources().getColor(R.color.white));
			vh.calendarItems.setTextColor(context.getResources().getColor(R.color.white));
		} else {
			if(c.isSunday()) {
				vh.dayText.setTextColor(context.getResources().getColor(R.color.white));
				vh.calendarItems.setTextColor(context.getResources().getColor(R.color.white));
				vh.cell.setBackgroundDrawable(context.getResources().getDrawable(R.color.alizarinlight));
			} else {
				vh.cell.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_white));
				if(!c.currentMonth)
					vh.dayText.setTextColor(context.getResources().getColor(R.color.silver));
				else vh.dayText.setTextColor(context.getResources().getColor(R.color.asbestos));
			}
		}
		
		return v;

	}

}
