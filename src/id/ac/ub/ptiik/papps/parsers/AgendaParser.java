package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Agenda;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgendaParser {
	public static ArrayList<Agenda> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);

			ArrayList<Agenda> agendaList = new ArrayList<Agenda>();
			
			JSONArray newsArray = object.getJSONArray("agenda");
			for(int i=0; i<newsArray.length(); i++) {
				Agenda a = new Agenda();
				JSONObject aJSON = (JSONObject) newsArray.get(i);
				
				a.agenda_id = aJSON.getString("jenis_kegiatan_id");
				a.jenis_kegiatan_id = aJSON.getString("jenis_kegiatan_id");
				a.lokasi = aJSON.getString("lokasi");
				a.ruang = aJSON.getString("ruang");
				a.judul = aJSON.getString("judul");
				a.keterangan = aJSON.getString("keterangan");
				a.penyelenggara = aJSON.getString("penyelenggara");
				a.tgl_mulai = aJSON.getString("tgl_mulai");
				a.tgl_selesai = aJSON.getString("tgl_selesai");
				a.is_publish = aJSON.getString("is_publish");
				a.is_private = aJSON.getString("is_private");
				
				agendaList.add(a);
			}
			return agendaList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
