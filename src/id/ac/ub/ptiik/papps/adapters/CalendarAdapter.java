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
	
	int month, year;
	
	
	public CalendarAdapter(Context c, int month, int year) {
		this.dayList = new ArrayList<CalendarCell>();
		this.context = c;
		this.month = month;
		this.year = year;
		//this.agendaList = new ArrayList<AgendaKaryawan>();
		//this.calculateMonth(month, year);
	}
	
	public void fillCalendar(ArrayList<CalendarCell> dayList) {
		this.dayList = dayList;
		//this.agendaList.clear();
		//this.agendaList.addAll(agendaList);
		//this.calculateMonth(month, year);
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
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarCell cell = this.dayList.get(position);
		
		View v = convertView;
		if(v == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_calendar_cell, parent, false);
			ViewHolder vh = new ViewHolder();
			vh.dayText = (TextView) v.findViewById(R.id.calendarDayText);
			vh.cell = v.findViewById(R.id.calendarCell);
			v.setTag(vh);
		} 
		ViewHolder vh = (ViewHolder) v.getTag();
		
		CalendarCell c = (CalendarCell) cell;
		vh.dayText.setText(String.valueOf(c.date) + " - " + c.getAgendaCount());
		if(!c.currentMonth)
			vh.dayText.setTextColor(context.getResources().getColor(R.color.silver));
		if(c.today) {
			vh.cell.setBackgroundColor(context.getResources().getColor(R.color.petermann));
			vh.dayText.setTextColor(context.getResources().getColor(R.color.white));
		}
		
		return v;

	}

}
