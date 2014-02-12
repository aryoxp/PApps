package id.ac.ub.ptiik.papps;

import id.ac.ub.ptiik.papps.adapters.MessageIndexAdapter;
import id.ac.ub.ptiik.papps.base.AppFragment;
import id.ac.ub.ptiik.papps.base.NotificationMessage;
import id.ac.ub.ptiik.papps.base.UserOnline;
import id.ac.ub.ptiik.papps.helpers.NotificationMessageHandler;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.SendtoInterface;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MessagesFragment extends Fragment 
	implements OnItemClickListener, android.content.DialogInterface.OnClickListener, 
	OnItemLongClickListener, SendtoInterface {

	View v;
	ProgressBar refreshProgressBar;
	ListView messagesListView;
	TextView refreshText;
	
	private ArrayList<NotificationMessage> messageList;
	private NotificationMessageHandler handler;
	private MessageIndexAdapter adapter;
	
	private NotificationMessage selectedMessage;
	
	private AppInterface mCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.v = inflater.inflate(R.layout.fragment_messages, container, false);
		this.refreshProgressBar = (ProgressBar) this.v.findViewById(R.id.messagesRefreshProgress);
		this.refreshProgressBar.setAlpha(0);
		this.messagesListView = (ListView) this.v.findViewById(R.id.messagesList);
		this.messagesListView.setAlpha(0);
		this.messagesListView.setOnItemClickListener(this);
		
		this.messagesListView.setOnItemLongClickListener(this);
		this.refreshText = (TextView) this.v.findViewById(R.id.messagesRefreshText);
		this.refreshText.setAlpha(0);
		this.registerForContextMenu(this.messagesListView);
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onStart() {
		this.messageList = new ArrayList<NotificationMessage>();
		this.handler = new NotificationMessageHandler(getActivity());
		this.messageList.clear();
		this.messageList.addAll(this.handler.getAll());
		this.adapter = new MessageIndexAdapter(getActivity(), this.messageList);
		this.messagesListView.setAdapter(this.adapter);
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.messagesListView.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_messages, menu);
	}

	@Override
	public void onItemClick(AdapterView<?> container, View view, int position, long id) {
		//this.getActivity().openContextMenu(container);
		this.selectedMessage = this.messageList.get(position);
		MessagesThreadFragment fragment = new MessagesThreadFragment();
		Bundle args = new Bundle();
		args.putString(MessagesThreadFragment.MESSAGE_FROM, this.selectedMessage.from);
		fragment.setArguments(args);
		fragment.setOnNavigationCallback(this.mCallback);
		this.mCallback.setContentFragment(fragment, AppFragment.FRAGMENT_TAG_MESSAGE_THREAD);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = this.getActivity().getMenuInflater();
		menu.setHeaderTitle("Message Options");
		inflater.inflate(R.menu.menu_context_message, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.message_context_delete:
			new AlertDialog.Builder(getActivity())
			.setMessage("Delete this message?")
			.setTitle("Confirmation")
			.setPositiveButton("Yes", this)
			.setNegativeButton("Cancel", this)
			.show();	
			break;
		case R.id.message_context_detail:
			String detailText = "from: " + this.selectedMessage.from + "\n"
					+ "sent: " + this.selectedMessage.sent + "\n"
					+ "received: " + this.selectedMessage.received;
			new AlertDialog.Builder(getActivity())
			.setMessage(detailText)
			.setTitle("Message Detail")
			.show();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case Dialog.BUTTON_POSITIVE:
			NotificationMessageHandler handler = new NotificationMessageHandler(getActivity());
			if(handler.delete(this.selectedMessage) > 0)
			{
				Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
				this.messageList.remove(this.selectedMessage);
				this.adapter.notifyDataSetChanged();
			}
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_messages_refresh:
			this.messageList.clear();
			this.messageList.addAll(this.handler.getAll());
			this.adapter = new MessageIndexAdapter(getActivity(), this.messageList);
			this.messagesListView.setAdapter(this.adapter);
			return true;
		case R.id.action_messages_new:
			
			SendtoFragment sendtoFragment = new SendtoFragment();
			sendtoFragment.setOnUserSelectedCallback(this);
			sendtoFragment.show(getFragmentManager(), AppFragment.FRAGMENT_TAG_SENDTO);
			
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> container, View view, int position,
			long id) {
		this.getActivity().openContextMenu(container);
		this.selectedMessage = this.messageList.get(position);
		return true;
	}
	
	public void setOnNavigationCallback(AppInterface mCallback) {
		this.mCallback = mCallback;
	}

	@Override
	public void onUserSelected(UserOnline user) {
		if(this.mCallback != null) {
			MessagesThreadFragment fragment = new MessagesThreadFragment();
			Bundle args = new Bundle();
			args.putString(MessagesThreadFragment.MESSAGE_FROM, user.username);
			args.putString(MessagesThreadFragment.MESSAGE_FROM_NAME, user.nama);
			fragment.setArguments(args);
			fragment.setOnNavigationCallback(this.mCallback);
			this.mCallback.setContentFragment(fragment, AppFragment.FRAGMENT_TAG_MESSAGE_THREAD);
		}
	}
}
