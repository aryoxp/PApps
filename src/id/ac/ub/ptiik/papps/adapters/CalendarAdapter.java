package id.ac.ub.ptiik.papps.adapters;

import id.ac.ub.ptiik.papps.R;
import id.ac.ub.ptiik.papps.base.CalendarCell;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<CalendarCell> dayList;
	int month, year;
	int todayDate;
	//Calendar currentCalendar;
	
	public CalendarAdapter(Context c, int month, int year) {
		this.context = c;
		this.month = month;
		this.year = year;
		this.calculateMonth(month, year);
	}
	
	public void calculateMonth(int month, int year) {
		GregorianCalendar currentMonth = new GregorianCalendar(year, month-1, 1);
		Log.d("current month", currentMonth.getTime().toString());
		
		int pMonth, pYear, nMonth, nYear;
		
		nYear = year;
		pYear = year;
		
		if(month == 1) {
			pMonth = 11; pYear = year-1;
		} else pMonth = month-1;
		
		if(month == 12) {
			nMonth = 0; nYear = year+1;
		} else nMonth = month;
		
		GregorianCalendar prevMonth = new GregorianCalendar(pYear, pMonth, 1);
		Log.d("p month", prevMonth.getTime().toString());
		GregorianCalendar nextMonth = new GregorianCalendar(nYear, nMonth, 1);
		Log.d("n month", nextMonth.getTime().toString());
		
		int prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int currentDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);
		this.todayDate = Calendar.getInstance(Locale.US).get(Calendar.DAY_OF_MONTH);
		
		this.dayList = new ArrayList<CalendarCell>();
		
		for(int i = currentDayOfWeek-3; i>=0; i--) {
			CalendarCell cell = new CalendarCell(prevMonthDays-i, false);
			this.dayList.add(cell);
		}
		for(int i = 1; i<=currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			CalendarCell cell = new CalendarCell(i);
			if(i == this.todayDate) cell.setToday();
			this.dayList.add(cell);
		}
		currentMonth.set(Calendar.DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);
		int d = 1;
		for(int i=lastDayOfWeek; i<=7; i++) {
			CalendarCell cell = new CalendarCell(d, false);
			this.dayList.add(cell);
			d++;
		}
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
		vh.dayText.setText(String.valueOf(c.date));
		if(!c.currentMonth)
			vh.dayText.setTextColor(context.getResources().getColor(R.color.silver));
		if(c.today) {
			vh.cell.setBackgroundColor(context.getResources().getColor(R.color.petermann));
			vh.dayText.setTextColor(context.getResources().getColor(R.color.white));
		}
		
		return v;

	}

}
