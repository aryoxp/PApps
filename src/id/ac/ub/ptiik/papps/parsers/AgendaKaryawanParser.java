package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgendaKaryawanParser {
	public static ArrayList<AgendaKaryawan> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			/*
			{"karyawan_id":"130109132054","tgl":"2013-12-03","jam_mulai":"09:30:00",
			"jam_selesai":"12:00:00","tgl_selesai":"2013-12-03","ruang":"5.3",
			"jenis_kegiatan":"kuliah","kegiatan":"Perkuliahan","keterangan":"Aryo Pinandito(Selasa)",
			"namamk":"","id":"KU2013010020042"}
			*/
			
			ArrayList<AgendaKaryawan> agendaList = new ArrayList<AgendaKaryawan>();
			
			JSONArray newsArray = object.getJSONArray("agendakaryawan");
			for(int i=0; i<newsArray.length(); i++) {
				AgendaKaryawan u = new AgendaKaryawan();
				JSONObject news = (JSONObject) newsArray.get(i);
				u.id = news.getString("id");
				u.karyawan_id = news.getString("karyawan_id");
				u.jam_mulai = news.getString("jam_mulai");
				u.jam_selesai = news.getString("jam_selesai");
				u.tanggal_mulai = news.getString("tgl");
				u.tanggal_selesai = news.getString("tgl_selesai");
				u.ruang = news.getString("ruang");
				u.jenis_kegiatan = news.getString("jenis_kegiatan");
				u.kegiatan = news.getString("kegiatan");
				u.keterangan = news.getString("keterangan");
				u.namamk = news.getString("namamk");
				agendaList.add(u);
			}
			return agendaList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
