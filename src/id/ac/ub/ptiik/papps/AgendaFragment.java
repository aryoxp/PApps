package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.AgendaKaryawanAdapter;
import id.ac.ub.ptiik.papps.base.AgendaKaryawan;
import id.ac.ub.ptiik.papps.interfaces.AgendaKaryawanIndexInterface;
import id.ac.ub.ptiik.papps.tasks.AgendaKaryawanTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AgendaFragment extends Fragment 
	implements AgendaKaryawanIndexInterface, OnScrollListener, OnClickListener {

	private View v;
	private View footerView;
	private ListView agendaKaryawanListView;
	private AgendaKaryawanAdapter adapter;
	private int page = 1;
	private int perpage = 20;
	private Calendar calendar;
	private String idKaryawan;
	private ArrayList<AgendaKaryawan> agendaKaryawanList;
	private AgendaKaryawanTask agendaKaryawanTask;
	
	private TextView AgendaKaryawanRefrestText;
	private ProgressBar AgendaKaryawanRefreshProgress;
	private View refreshContainer;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_agenda, container, false);
		this.v = v;
		this.agendaKaryawanListView = (ListView) v.findViewById(R.id.agendaList);
		this.footerView = inflater.inflate(R.layout.view_listview_footer, null, false);
		this.agendaKaryawanListView.addFooterView(this.footerView);
		this.agendaKaryawanList = new ArrayList<AgendaKaryawan>();
		this.adapter = new AgendaKaryawanAdapter(getActivity(), this.agendaKaryawanList);
		this.agendaKaryawanListView.setAdapter(this.adapter);
		this.agendaKaryawanListView.setOnScrollListener(this);
		
		this.AgendaKaryawanRefreshProgress = (ProgressBar) v.findViewById(R.id.agendaRefreshProgress);
		this.AgendaKaryawanRefrestText = (TextView) v.findViewById(R.id.agendaRefreshText);
		this.refreshContainer = v.findViewById(R.id.agendaRefreshContainer);
		
		this.idKaryawan = getArguments().getString("idKaryawan");
		
		v.findViewById(R.id.agendaButtonRefresh).setOnClickListener(this);
		v.findViewById(R.id.loadingMoreProgressBar).setAlpha(0);
		
		return v;
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		
		
		//this.refreshContainerHeight = this.refreshContainer.getMeasuredHeight();
		this.AgendaKaryawanRefreshProgress.setAlpha(0);
		this.AgendaKaryawanRefrestText.setText("Pull to refresh");
		this.refreshContainer.setAlpha(0);
		this.calendar = Calendar.getInstance();
		
		this.idKaryawan = this.getArguments().getString("idKaryawan");
		this.calendar = Calendar.getInstance(Locale.US);
		
		String tanggal = this.getBulanTahunString();
		
		this.agendaKaryawanTask = new AgendaKaryawanTask(getActivity(), this, this.idKaryawan, tanggal, page, perpage);
		this.agendaKaryawanTask.execute();
	}

	@Override
	public void onRetrievingStart() {
		if(page == 1) {
			this.AgendaKaryawanRefreshProgress.animate()
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
	public void onRetrieveComplete(ArrayList<AgendaKaryawan> AgendaKaryawanList) {
		this.hideProgressIndicator();
		this.agendaKaryawanList.addAll(AgendaKaryawanList);
		this.adapter.notifyDataSetChanged();
		if(this.AgendaKaryawanRefreshProgress.getAlpha() > 0)
			this.AgendaKaryawanRefreshProgress.animate().alpha(0).setDuration(100).start();
		if(page == 1)
			this.agendaKaryawanListView.smoothScrollToPosition(0);
		if(AgendaKaryawanList.size() > 0)
			this.page++;
	}	

	@Override
	public void onRetrieveFail(String error) {
		if(error != "null")
			Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
		else {
			hideProgressIndicator();
		}
	}

	private void hideProgressIndicator() {
		View pagingProgressBar = this.v.findViewById(R.id.loadingMoreProgressBar);
		View loadingProgressBar = this.v.findViewById(R.id.agendaRefreshProgress);
		
		if(pagingProgressBar != null) {
			pagingProgressBar.animate()
			.alpha(0)
			.setDuration(500)
			.start();
		}
		
		if(loadingProgressBar != null) {
			loadingProgressBar.animate()
			.alpha(0)
			.setDuration(500)
			.start();
		}
	}

	private int visibleItemCount;
	private int totalItemCount;
	private int lastInScreen;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
		this.lastInScreen = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(this.visibleItemCount > 0 && scrollState == SCROLL_STATE_IDLE) {

			//int lastInScreen = firstVisibleItem + visibleItemCount;
	        if(this.agendaKaryawanTask != null) {
	            //if(lastInScreen == totalItemCount && this.agendaKaryawanTask.getStatus() != Status.RUNNING) {
	        	if(lastInScreen == totalItemCount && this.agendaKaryawanTask.getStatus() != Status.RUNNING) {
                    Log.d("onScroll", "loading started...");
            		String tanggal = this.getBulanTahunString();
            		this.agendaKaryawanTask = new AgendaKaryawanTask(getActivity(), this, this.idKaryawan, tanggal, page+1, perpage);
            		this.agendaKaryawanTask.execute();
	            }
	        }
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.agendaButtonRefresh:
			if(this.agendaKaryawanTask != null && this.agendaKaryawanTask.getStatus() != Status.RUNNING) {
				this.agendaKaryawanList.clear();
				this.page = 1;
				String tanggal = this.getBulanTahunString();
				this.agendaKaryawanTask = new AgendaKaryawanTask(getActivity(), this, this.idKaryawan, tanggal, page, perpage);
				this.agendaKaryawanTask.execute();
			}
			break;
		}
		
	}	

	private String getBulanTahunString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);
		String bulanTahun = sdf.format(this.calendar.getTime());
		return bulanTahun;
	}
	
}
