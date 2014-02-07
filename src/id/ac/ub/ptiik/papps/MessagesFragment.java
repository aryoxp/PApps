package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.NotificationMessageAdapter;
import id.ac.ub.ptiik.papps.base.NotificationMessage;
import id.ac.ub.ptiik.papps.helpers.NotificationMessageHandler;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MessagesFragment extends Fragment 
	implements OnClickListener {

	View v;
	ProgressBar refreshProgressBar;
	ListView messagesListView;
	TextView refreshText;
	
	private ArrayList<NotificationMessage> messageList;
	private NotificationMessageHandler handler;
	private NotificationMessageAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.v = inflater.inflate(R.layout.fragment_messages, container, false);
		this.refreshProgressBar = (ProgressBar) this.v.findViewById(R.id.messagesRefreshProgress);
		this.refreshProgressBar.setAlpha(0);
		this.messagesListView = (ListView) this.v.findViewById(R.id.messagesList);
		this.messagesListView.setAlpha(0);
		this.refreshText = (TextView) this.v.findViewById(R.id.messagesRefreshText);
		this.refreshText.setAlpha(0);
		
		this.v.findViewById(R.id.messagesButtonRefresh).setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onStart() {
		this.messageList = new ArrayList<NotificationMessage>();
		this.handler = new NotificationMessageHandler(getActivity());
		this.messageList.clear();
		this.messageList.addAll(this.handler.getAll());
		this.adapter = new NotificationMessageAdapter(getActivity(), this.messageList);
		this.messagesListView.setAdapter(this.adapter);
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.messagesListView.animate().alpha(1).setDuration(200).start();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.messagesButtonRefresh:
			this.messageList.clear();
			this.messageList.addAll(this.handler.getAll());
			this.adapter.updateMessages(this.messageList).notifyDataSetChanged();
		}
	}

}
