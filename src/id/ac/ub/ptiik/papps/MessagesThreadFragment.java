package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.ac.ub.ptiik.papps.adapters.MessageThreadAdapter;
import id.ac.ub.ptiik.papps.base.Message;
import id.ac.ub.ptiik.papps.base.MessageReceived;
import id.ac.ub.ptiik.papps.base.MessageSent;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.MessageSendInterface;
import id.ac.ub.ptiik.papps.interfaces.MessageThreadInterface;
import id.ac.ub.ptiik.papps.tasks.MessageSendTask;
import id.ac.ub.ptiik.papps.tasks.MessageThreadTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
implements MessageThreadInterface, OnScrollListener, OnClickListener, MessageSendInterface, DialogInterface.OnClickListener {

	private View progressContainerView, messageThreadSendButton;
	private ListView messageThreadListView;
	private TextView messageThreadFrom;
	private EditText messageThreadMessageText;
	private ArrayList<Message> messages;
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
		this.messages = new ArrayList<Message>();
		this.messageThreadAdapter = new MessageThreadAdapter(getActivity(), this.messages);
		this.messageThreadListView = (ListView) v.findViewById(R.id.messagesThreadList);
		this.messageThreadListView.setAdapter(this.messageThreadAdapter);
		this.messageThreadListView.setDivider(null);
		this.messageThreadListView.setOnScrollListener(this);
		this.messageThreadListView.setStackFromBottom(true);
		this.messageThreadListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
		this.setHasOptionsMenu(true);
	}

	private void readMessages() {
		MessageThreadTask task = new MessageThreadTask(getActivity(), this);
		task.execute(this.sender, this.user.username);
	}
	
	public void addMessage(MessageReceived message) {
		this.messageThreadAdapter.add(message);
		this.messageThreadAdapter.notifyDataSetChanged();
	}
	
	public void setOnNavigationCallback(AppInterface mCallback) {}

	@Override
	public void onMessageThreadRetrievingStart() {
		this.progressContainerView.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onMessageThreadRetrieveComplete(
			ArrayList<Message> messages) {
		this.progressContainerView.animate().alpha(0).setDuration(200).start();
		this.messageThreadAdapter.setItems(messages);
		this.messageThreadAdapter.notifyDataSetChanged();
	}

	@Override
	public void onMessageThreadRetrieveFail(String error) {
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
				Message message = (Message) this.messageThreadAdapter.getItem(i);
				if(message.readStatus == MessageReceived.STATUS_NEW) {
					message.setRead();
					handler.update(message);
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
				this.messageThreadMessageText.setText("");
				
				// instantiating sent message to save
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
				String sent = sdf.format(Calendar.getInstance(Locale.US).getTime());
				MessageSent messageSent = new MessageSent(message, sent, this.sender, this.receiver);
				this.messageThreadAdapter.add(messageSent);
				this.messageThreadAdapter.notifyDataSetChanged();

				// save to db
				MessageDBHelper handler = new MessageDBHelper(getActivity());
				handler.add(messageSent);
				
				// send to receiver via GCM
				MessageSendTask sendTask = new MessageSendTask(getActivity(), this, this.user.username);
				sendTask.execute(this.receiver, message);
				
				
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_messages_thread, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_messages_thread_trash:
			new AlertDialog.Builder(getActivity())
			.setTitle("Clear Messages")
			.setMessage("Do you want to remove all messages on current message thread?")
			.setPositiveButton("Yes", this)
			.setNegativeButton("Cancel", this)
			.show();
			return true;
		case R.id.action_messages_thread_refresh:
			this.readMessages();
			return true;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			MessageDBHelper handler = new MessageDBHelper(getActivity());
			handler.clear(this.sender, this.receiver);
			MessageThreadAdapter adapter = (MessageThreadAdapter) this.messageThreadListView.getAdapter();
			adapter.clearItems();
			adapter.notifyDataSetChanged();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
		default:
			break;
		}
	}
	
}