package id.ac.ub.ptiik.papps.tasks;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.News;
import id.ac.ub.ptiik.papps.interfaces.NewsIndexInterface;
import id.ac.ub.ptiik.papps.parsers.NewsListParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import ap.mobile.utils.Rest;

public class NewsIndexTask extends AsyncTask<Void, Void, ArrayList<News>> {
	
	private NewsIndexInterface mCallback;
	private String error;
	private int page;
	private int perpage;
	private Context c;
		
	public NewsIndexTask(Context c, NewsIndexInterface mCallback, Integer page, Integer perpage) {
		this.mCallback = mCallback;
		this.page = page;
		this.perpage = perpage;
		this.c = c;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public void setPerpage(int perpage)
	{
		this.perpage = perpage;
	}
	
	@Override
	protected ArrayList<News> doInBackground(Void... params) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(mCallback !=null)
					mCallback.onRetrievingStart();
			}
		});
		try {
			String host = PreferenceManager
					.getDefaultSharedPreferences(this.c)
					.getString("host", "175.45.187.246");
			String url = "http://"+host+"/service/index.php/news/index/" 
					+ String.valueOf(page) + "/"
					+ String.valueOf(perpage);
			String result = Rest.getInstance().get(url).getResponseText();
			ArrayList<News> newsList = NewsListParser.Parse(result);
			return newsList;
		} catch(Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<News> newsList) {
		if(newsList != null) {
			this.mCallback.onRetrieveComplete(newsList);
		} else {
			this.mCallback.onRetrieveFail(this.error);
		}
	}
}
