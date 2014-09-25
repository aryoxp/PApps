package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.NewsIndexAdapter;
import id.ac.ub.ptiik.papps.base.News;
import id.ac.ub.ptiik.papps.interfaces.NewsIndexInterface;
import id.ac.ub.ptiik.papps.tasks.NewsIndexTask;
import android.app.Fragment;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
	
	private TextView newsRefrestText;
	private ProgressBar newsRefreshProgress;
	private View refreshContainer;
		
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
		
		this.newsRefreshProgress = (ProgressBar) v.findViewById(R.id.newsRefreshProgress);
		this.newsRefrestText = (TextView) v.findViewById(R.id.newsRefreshText);
		this.refreshContainer = v.findViewById(R.id.newsRefreshContainer);
		
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		
		this.newsIndexTask = new NewsIndexTask(getActivity(), this, page, perpage);
		this.newsIndexTask.execute();
		
		//this.refreshContainerHeight = this.refreshContainer.getMeasuredHeight();
		this.newsRefreshProgress.setAlpha(0);
		this.newsRefrestText.setText("Pull to refresh");
		this.refreshContainer.setAlpha(0);
		
	}

	@Override
	public void onRetrievingStart() {
		if(page == 1) {
			this.newsRefreshProgress.animate()
			.alpha(1)
			.setDuration(300)
			.start();
			return;
		} 
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
		if(page == 1) this.newsList.clear();
		this.newsList.addAll(newsList);
		this.adapter.notifyDataSetChanged();
		if(this.newsRefreshProgress.getAlpha() > 0)
			this.newsRefreshProgress.animate().alpha(0).setDuration(100).start();
		if(page == 1) {
			this.newsListView.smoothScrollToPosition(0);
		}
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_news, menu);
		//super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_news_refresh:
			if(this.newsIndexTask != null && this.newsIndexTask.getStatus() != Status.RUNNING) {
				this.page = 1;
				this.newsIndexTask = new NewsIndexTask(getActivity(), this, page, perpage);
				this.newsIndexTask.execute();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
}
