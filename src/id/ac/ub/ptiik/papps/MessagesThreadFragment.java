package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.MessageThreadAdapter;
import id.ac.ub.ptiik.papps.base.NotificationMessage;
import id.ac.ub.ptiik.papps.helpers.NotificationMessageHandler;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.MessageThreadInterface;
import id.ac.ub.ptiik.papps.tasks.MessageThreadTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesThreadFragment extends Fragment 
implements MessageThreadInterface, OnScrollListener {

	private View progressContainerView;
	private ListView messageThreadListView;
	private TextView messageThreadFrom;
	private ArrayList<NotificationMessage> messages;
	private MessageThreadAdapter messageThreadAdapter;
	
	public static final String MESSAGE_FROM = "messageFrom";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_messages_thread, container, false);
		this.progressContainerView = v.findViewById(R.id.messagesThreadProgressContainer);
		this.progressContainerView.setAlpha(0);
		this.messages = new ArrayList<NotificationMessage>();
		this.messageThreadAdapter = new MessageThreadAdapter(getActivity(), this.messages);
		this.messageThreadListView = (ListView) v.findViewById(R.id.messagesThreadList);
		this.messageThreadListView.setAdapter(messageThreadAdapter);
		this.messageThreadListView.setDivider(null);
		this.messageThreadListView.setOnScrollListener(this);
		this.messageThreadFrom = (TextView) v.findViewById(R.id.messagesThreadFrom);
		//this.v = v;
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		try {
			String from = this.getArguments().getString(MESSAGE_FROM, "");
			this.messageThreadFrom.setText(from);
			MessageThreadTask task = new MessageThreadTask(getActivity(), this);
			task.execute(from);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setOnNavigationCallback(AppInterface mCallback) {}

	@Override
	public void onMessageThreadRetrievingStart() {
		this.progressContainerView.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onMessageThreadRetrieveComplete(
			ArrayList<NotificationMessage> messages) {
		this.progressContainerView.animate().alpha(0).setDuration(200).start();
		this.messages.clear();
		this.messages.addAll(messages);
		this.messageThreadAdapter.notifyDataSetChanged();
		this.messageThreadListView.smoothScrollToPosition(this.messageThreadAdapter.getCount());
	}

	@Override
	public void onMessageThreadRetrieveFail(String error) {
		this.messages.clear();
		this.messageThreadAdapter.notifyDataSetChanged();
		this.progressContainerView.animate().alpha(0).setDuration(200).start();
	}

	private int firstVisibleIndex;
	private int lastVisibleIndex;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleIndex = firstVisibleItem;
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE)
		{
			this.lastVisibleIndex = this.messageThreadListView.getLastVisiblePosition();
			NotificationMessageHandler handler = new NotificationMessageHandler(getActivity());
			for(int i = this.firstVisibleIndex; i <= this.lastVisibleIndex; i++)
			{
				NotificationMessage notificationMessage = this.messages.get(i);
				if(notificationMessage.status == NotificationMessage.STATUS_NEW) {
					notificationMessage.setRead();
					handler.update(notificationMessage);
				}
			}
		}
	}
	
}