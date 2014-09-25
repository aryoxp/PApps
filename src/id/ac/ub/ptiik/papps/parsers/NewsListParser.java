package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsListParser {
	public static ArrayList<News> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			/*
			{"status":"OK","user":[{"nama":"Aryo Pinandito","gelar_awal":null,
			"gelar_akhir":"ST, M.MT","karyawan_id":"130109132054","level":"2",
			"status":"1","role":"Akademik"}]}
			*/
			ArrayList<News> newsList = new ArrayList<News>();
			
			JSONArray newsArray = object.getJSONArray("news");
			for(int i=0; i<newsArray.length(); i++) {
				News u = new News();
				JSONObject news = (JSONObject) newsArray.get(i);
				u.ID = news.getString("ID");
				u.post_date = news.getString("post_date");
				u.post_title = news.getString("post_title");
				u.post_content = news.getString("post_content");
				u.post_name = news.getString("post_name");
				u.post_excerpt = news.getString("post_excerpt");
				u.guid = news.getString("guid");
				newsList.add(u);
			}
			return newsList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
