package id.ac.ub.ptiik.papps;

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
	ListView messagesList;
	TextView refreshText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.v = inflater.inflate(R.layout.fragment_messages, container, false);
		this.refreshProgressBar = (ProgressBar) this.v.findViewById(R.id.messagesRefreshProgress);
		this.refreshProgressBar.setAlpha(0);
		this.messagesList = (ListView) this.v.findViewById(R.id.messagesList);
		this.messagesList.setAlpha(0);
		this.refreshText = (TextView) this.v.findViewById(R.id.messagesRefreshText);
		this.refreshText.setAlpha(0);
		return v;
	}
	
	@Override
	public void onClick(View v) {
	}

}
