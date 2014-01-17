package ap.mobile.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtils {
	private Context c;
	
	public TypefaceUtils(Context c) {
		this.c = c;
	}
	
	public static TypefaceUtils NewInstance(Context c) {
		return new TypefaceUtils(c);
	}
	
	public Typeface normalCondensed() {
		return Typeface.createFromAsset(c.getAssets(), "fonts/Helvetica-Condensed.ttf");
	}
}
