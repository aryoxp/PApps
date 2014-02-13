package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.MessageThreadAdapter;
import id.ac.ub.ptiik.papps.base.MessageReceived;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.MessageSendInterface;
import id.ac.ub.ptiik.papps.interfaces.MessageThreadInterface;
import id.ac.ub.ptiik.papps.tasks.MessageSendTask;
import id.ac.ub.ptiik.papps.tasks.MessageThreadTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagesThreadFragment extends Fragment 
implements MessageThreadInterface, OnScrollListener, OnClickListener, MessageSendInterface {

	private View progressContainerView, messageThreadSendButton;
	private ListView messageThreadListView;
	private TextView messageThreadFrom;
	private EditText messageThreadMessageText;
	private ArrayList<MessageReceived> messages;
	private MessageThreadAdapter messageThreadAdapter;
	
	private String sender;
	private String receiver;
	private User user;
	
	public static final String MESSAGE_FROM = "messageFrom";
	public static final String MESSAGE_FROM_NAME = "messageFromName";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_messages_thread, container, false);
		this.progressContainerView = v.findViewById(R.id.messagesThreadProgressContainer);
		this.progressContainerView.setAlpha(0);
		this.messages = new ArrayList<MessageReceived>();
		this.messageThreadAdapter = new MessageThreadAdapter(getActivity(), this.messages);
		this.messageThreadListView = (ListView) v.findViewById(R.id.messagesThreadList);
		this.messageThreadListView.setAdapter(messageThreadAdapter);
		this.messageThreadListView.setDivider(null);
		this.messageThreadListView.setOnScrollListener(this);
		this.messageThreadFrom = (TextView) v.findViewById(R.id.messagesThreadFrom);
		this.messageThreadMessageText = (EditText) v.findViewById(R.id.messagesThreadMessageText);
		this.messageThreadSendButton = v.findViewById(R.id.messagesThreadSendButton);
		this.messageThreadSendButton.setOnClickListener(this);
		//this.v = v;
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		try {
			this.user = SystemHelper.getSystemUser(getActivity());
			
			this.sender = this.getArguments().getString(MESSAGE_FROM, "");
			String senderName = this.getArguments().getString(MESSAGE_FROM_NAME, "");
			
			String nameToDisplay = senderName;
			if(senderName.equals(""))
				nameToDisplay = sender;
			else nameToDisplay = senderName + " (" + sender + ")";
			
			this.receiver = sender;
			this.messageThreadFrom.setText(nameToDisplay);
			this.readMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readMessages() {
		MessageThreadTask task = new MessageThreadTask(getActivity(), this);
		task.execute(this.sender, this.user.username);
	}
	
	public void addMessage(MessageReceived message) {
		this.messages.add(message);
		this.messageThreadAdapter.notifyDataSetChanged();
		this.messageThreadListView.smoothScrollToPosition(this.messageThreadAdapter.getCount());
	}
	
	public void setOnNavigationCallback(AppInterface mCallback) {}

	@Override
	public void onMessageThreadRetrievingStart() {
		this.progressContainerView.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onMessageThreadRetrieveComplete(
			ArrayList<MessageReceived> messages) {
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
			MessageDBHelper handler = new MessageDBHelper(getActivity());
			for(int i = this.firstVisibleIndex; i <= this.lastVisibleIndex; i++)
			{
				MessageReceived notificationMessage = this.messages.get(i);
				if(notificationMessage.status == MessageReceived.STATUS_NEW) {
					notificationMessage.setRead();
					handler.update(notificationMessage);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.messagesThreadSendButton:
			
			String message = this.messageThreadMessageText.getText().toString();
			if(message.trim().equals(""))
				return;
			if(this.user != null) {
				MessageSendTask sendTask = new MessageSendTask(getActivity(), this, this.user.username);
				sendTask.execute(this.receiver, message);
				this.messageThreadMessageText.setText("");
			} else
				Toast.makeText(getActivity(), "You have to login to send messages", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onMessageSendStart() {
		
	}

	@Override
	public void onMessageSendComplete(Boolean status) {
		Toast.makeText(getActivity(), "complete", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMessageSendFail(String error) {
		
	}
	
}