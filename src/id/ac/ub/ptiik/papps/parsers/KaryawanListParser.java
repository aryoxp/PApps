package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Karyawan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KaryawanListParser {
	public static ArrayList<Karyawan> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			ArrayList<Karyawan> karyawanList = new ArrayList<Karyawan>();
			
			JSONArray karyawanArray = object.getJSONArray("karyawan");
			for(int i=0; i<karyawanArray.length(); i++) {
				JSONObject karyawan = (JSONObject) karyawanArray.get(i);
				String id = karyawan.getString("karyawan_id");
				String nama = karyawan.getString("nama");
				String gelar_awal = karyawan.getString("gelar_awal");
				String gelar_akhir = karyawan.getString("gelar_akhir");
				Karyawan u = new Karyawan(id, nama, gelar_awal, gelar_akhir);
				karyawanList.add(u);
			}
			return karyawanList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
