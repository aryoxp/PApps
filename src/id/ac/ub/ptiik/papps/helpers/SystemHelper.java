package id.ac.ub.ptiik.papps.helpers;

import com.google.gson.Gson;

import android.content.Context;
import android.preference.PreferenceManager;
import id.ac.ub.ptiik.papps.base.User;

public class SystemHelper {
	public static User getSystemUser(Context context) {
		String userGson = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user", null);
		Gson gson = new Gson();
		User user = gson.fromJson(userGson, User.class);
		return user;
	}
	
	public static void saveSystemUser(Context context, User user) {
		Gson gson = new Gson();
		String userGson = gson.toJson(user);
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.putString("user", userGson)
		.commit();
	}
	
	public static void setPreference(Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit().putString(key, value)
		.commit();
	}
	
	public static String getPreferenceString(Context context, String key) {
		String value = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, null);
		return value;
	}
	
	public static Long getPreferenceLong(Context context, String key) {
		Long value = PreferenceManager.getDefaultSharedPreferences(context)
				.getLong(key, 0);
		return value;
	}
	
	public static Integer getPreferenceInteger(Context context, String key) {
		Integer value = PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(key, 0);
		return value;
	}
	
}
