package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.ComposeToAdapter;
import id.ac.ub.ptiik.papps.base.UserOnline;
import id.ac.ub.ptiik.papps.interfaces.AppInterface;
import id.ac.ub.ptiik.papps.interfaces.UserOnlineCheckInterface;
import id.ac.ub.ptiik.papps.tasks.UserOnlineTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class MessagesNewFragment extends Fragment implements UserOnlineCheckInterface {
	View v;
	View progressContainerView;
	Spinner usersSpinner;
	AppInterface mCallback;
	ComposeToAdapter composeToAdapter;
	ArrayList<UserOnline> onlineUsers;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_messages_compose, container, false);
		this.progressContainerView = v.findViewById(R.id.messagesComposeProgressContainer);
		this.progressContainerView.setAlpha(0);
		this.usersSpinner = (Spinner) v.findViewById(R.id.messageComposeToSpinner);
		this.onlineUsers = new ArrayList<UserOnline>();
		this.composeToAdapter = new ComposeToAdapter(getActivity(), this.onlineUsers);
		this.usersSpinner.setAdapter(this.composeToAdapter);
		this.v = v;
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		UserOnlineTask userOnlineTask = new UserOnlineTask(getActivity(), this);
		userOnlineTask.execute();
	}
	
	public void setOnNavigationCallback(AppInterface mCallback) {
		this.mCallback = mCallback;
	}

	@Override
	public void onUserOnlinceCheckStarted() {
		this.progressContainerView.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onUserOnlinceCheckSuccess(ArrayList<UserOnline> users) {
		this.progressContainerView.animate().alpha(0).setDuration(200).start();
		this.onlineUsers.clear();
		this.onlineUsers.addAll(users);
		this.composeToAdapter.notifyDataSetChanged();
	}

	@Override
	public void onUserOnlinceCheckFail(String error) {
		this.onlineUsers.clear();
		this.progressContainerView.animate().alpha(0).setDuration(200).start();
	}
	
}