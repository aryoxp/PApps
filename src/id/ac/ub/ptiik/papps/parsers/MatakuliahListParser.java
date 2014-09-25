package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Matakuliah;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatakuliahListParser {
	public static ArrayList<Matakuliah> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			ArrayList<Matakuliah> matakuliahList = new ArrayList<Matakuliah>();
			
			JSONArray matakuliahArray = object.getJSONArray("matakuliah");
			for(int i=0; i<matakuliahArray.length(); i++) {
				
				JSONObject matakuliah = (JSONObject) matakuliahArray.get(i);
				String kode = matakuliah.getString("kode_mk");
				String nama = matakuliah.getString("namamk");
				int sks = Integer.parseInt(matakuliah.getString("sks"));
				Matakuliah u = new Matakuliah(kode, nama, sks);
				matakuliahList.add(u);
			}
			return matakuliahList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
