package id.ac.ub.ptiik.papps;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.adapters.ComposeToAdapter;
import id.ac.ub.ptiik.papps.base.UserOnline;
import id.ac.ub.ptiik.papps.interfaces.SendtoInterface;
import id.ac.ub.ptiik.papps.interfaces.UserOnlineCheckInterface;
import id.ac.ub.ptiik.papps.tasks.UserOnlineTask;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SendtoFragment extends DialogFragment 
	implements View.OnClickListener, UserOnlineCheckInterface, OnItemClickListener {
	
	private View v;
	private ListView usersListView;
	private SendtoInterface mCallback;
	private ComposeToAdapter adapter;
	private ArrayList<UserOnline> users;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		View v = inflater.inflate(R.layout.fragment_sendto, container, false);
		this.v = v;
		
		TextView buttonCancel = (TextView) this.v.findViewById(R.id.sendtoButtonCancelSelect);
		this.usersListView = (ListView) this.v.findViewById(R.id.sendtoUserListView);
		this.usersListView.setOnItemClickListener(this);
		
		buttonCancel.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		LayoutParams lp = getDialog().getWindow().getAttributes();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		lp.width = (int) (dm.widthPixels * 0.9);
		//lp.height = (int) (dm.heightPixels * 0.9);
		getDialog().getWindow().setAttributes(lp);
		
		this.users = new ArrayList<UserOnline>();
		this.adapter = new ComposeToAdapter(getActivity(), users);
		this.usersListView.setAdapter(this.adapter);
		
		UserOnlineTask userOnlineTask = new UserOnlineTask(getActivity(), this);
		userOnlineTask.execute();
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.sendtoButtonCancelSelect:
		default:
			this.dismiss();
			break;
		}
	}
	
	public void setOnUserSelectedCallback(SendtoInterface mCallback) {
		this.mCallback = mCallback;
	}

	@Override
	public void onUserOnlineCheckStarted() {
		this.v.findViewById(R.id.sendToListContainer).animate().alpha(0)
		.setDuration(200).start();
	}

	@Override
	public void onUserOnlineCheckSuccess(ArrayList<UserOnline> users) {
		
		this.users.clear();
		this.users.addAll(users);
		this.adapter.notifyDataSetChanged();
		
		this.v.findViewById(R.id.sendToListContainer).animate().alpha(1)
		.setDuration(200).start();
		
	}

	@Override
	public void onUserOnlineCheckFail(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserOnline user = this.users.get(position);
		this.mCallback.onUserSelected(user);
		this.dismiss();
	}
	
}
