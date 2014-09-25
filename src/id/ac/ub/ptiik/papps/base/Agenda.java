package id.ac.ub.ptiik.papps.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Agenda {
	
	public String agenda_id, jenis_kegiatan_id, lokasi, ruang, judul, keterangan, 
		penyelenggara, tgl_mulai, tgl_selesai, is_publish, is_private;
	
	public Calendar getCalendarTanggalMulai() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		try {
			Date tanggal = format.parse(this.tgl_mulai);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(tanggal);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getTanggal() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		try {
			Date tanggal = format.parse(this.tgl_mulai);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(tanggal);
			return cal.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Calendar getCalendarTanggalSelesai() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		try {
			Date tanggal = format.parse(this.tgl_selesai);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(tanggal);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDayOfWeekTanggalMulai() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd k:m:s", Locale.US);
		try {
			Date tanggal = format.parse(this.tgl_mulai);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(tanggal);
			return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDayOfWeekTanggalSelesai() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd k:m:s", Locale.US);
		try {
			Date tanggal = format.parse(this.tgl_selesai);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(tanggal);
			return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
