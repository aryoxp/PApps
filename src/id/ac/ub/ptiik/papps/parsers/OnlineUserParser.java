package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.UserOnline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineUserParser {
	public static ArrayList<UserOnline> Parse(String JSONString){
		ArrayList<UserOnline> users = new ArrayList<UserOnline>();
		try {
			JSONObject object = new JSONObject(JSONString);
			JSONArray userArray = object.getJSONArray("users");
			for(int i=0;i<userArray.length();i++) {
				UserOnline u = new UserOnline();
				JSONObject user = (JSONObject) userArray.get(i);
				u.nama = user.getString("nama");
				u.gelar_akhir = user.getString("gelar_akhir");
				u.gelar_awal = user.getString("gelar_awal");
				u.username = user.getString("username");
				u.regId = user.getString("regid");
				u.api = Integer.parseInt(user.getString("api"));
				u.timestamp = user.getString("timestamp");
				u.status = user.getInt("status");
				users.add(u);
			}
			return users;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
