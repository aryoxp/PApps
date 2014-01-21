package id.ac.ub.ptiik.papps.parsers;

import id.ac.ub.ptiik.papps.base.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserParser {
	public static User Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			/*
			{"status":"OK","user":[{"nama":"Aryo Pinandito","gelar_awal":null,
			"gelar_akhir":"ST, M.MT","karyawan_id":"130109132054","level":"2",
			"status":"1","role":"Akademik"}]}
			*/

			User u = new User();
			JSONArray userArray = object.getJSONArray("user");
			JSONObject user = (JSONObject) userArray.get(0);
			u.nama = user.getString("nama");
			u.gelar_awal = user.getString("gelar_awal");
			u.gelar_akhir = user.getString("gelar_akhir");
			u.karyawan_id = user.getString("karyawan_id");
			u.level = user.getInt("level");
			u.status = user.getInt("status");
			u.role = user.getString("role");
			
			return u;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
