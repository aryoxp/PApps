package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.NewsIndexAdapter;
import id.ac.ub.ptiik.papps.base.News;
import id.ac.ub.ptiik.papps.interfaces.NewsIndexInterface;
import id.ac.ub.ptiik.papps.tasks.NewsIndexTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

public class NewsFragment extends Fragment 
	implements NewsIndexInterface, OnScrollListener {

	private View v;
	private View footerView;
	private ListView newsListView;
	private NewsIndexAdapter adapter;
	private int page = 1;
	private int perpage = 20;
	private ArrayList<News> newsList;
	private NewsIndexTask newsIndexTask;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_news, container, false);
		this.v = v;
		this.newsListView = (ListView) v.findViewById(R.id.newsList);
		this.footerView = inflater.inflate(R.layout.view_listview_footer, null, false);
		this.newsListView.addFooterView(this.footerView);
		this.newsList = new ArrayList<News>();
		this.adapter = new NewsIndexAdapter(getActivity(), this.newsList);
		this.newsListView.setAdapter(this.adapter);
		this.newsListView.setOnScrollListener(this);
		return v;
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		
		this.newsIndexTask = new NewsIndexTask(getActivity(), this, page, perpage);
		newsIndexTask.execute();
		
	}

	@Override
	public void onRetrievingStart() {
		View progressBar = this.v.findViewById(R.id.loadingMoreProgressBar);
		if(progressBar != null) {
			progressBar.animate()
			.alpha(1)
			.setDuration(500)
			.start();
		}
	}

	@Override
	public void onRetrieveComplete(ArrayList<News> newsList) {
		this.newsList.addAll(newsList);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onRetrieveFail(String error) {
		if(error != null)
			Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
		else {
			View progressBar = this.v.findViewById(R.id.loadingMoreProgressBar);
			if(progressBar != null) {
				progressBar.animate()
				.alpha(0)
				.setDuration(500)
				.start();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
        int lastInScreen = firstVisibleItem + visibleItemCount;
        if(this.newsIndexTask != null) {
            if(lastInScreen == totalItemCount && this.newsIndexTask.getStatus() != Status.RUNNING) {
                    Log.d("onScroll", "loading started...");
                    this.page++;
                    this.newsIndexTask = new NewsIndexTask(getActivity(), this, page, perpage);
                    this.newsIndexTask.execute();
                    
            }
        }

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

}
