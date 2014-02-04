package id.ac.ub.ptiik.papps;

import java.util.ArrayList;
import java.util.Arrays;

import id.ac.ub.ptiik.papps.adapters.DayAdapter;
import id.ac.ub.ptiik.papps.adapters.KaryawanAdapter;
import id.ac.ub.ptiik.papps.adapters.MatakuliahAdapter;
import id.ac.ub.ptiik.papps.adapters.ScheduleAdapter;
import id.ac.ub.ptiik.papps.base.JadwalMengajar;
import id.ac.ub.ptiik.papps.base.Karyawan;
import id.ac.ub.ptiik.papps.base.Matakuliah;
import id.ac.ub.ptiik.papps.base.User;
import id.ac.ub.ptiik.papps.helpers.SystemHelper;
import id.ac.ub.ptiik.papps.interfaces.JadwalMengajarIndexInterface;
import id.ac.ub.ptiik.papps.interfaces.KaryawanIndexInterface;
import id.ac.ub.ptiik.papps.interfaces.MatakuliahIndexInterface;
import id.ac.ub.ptiik.papps.tasks.JadwalMengajarTask;
import id.ac.ub.ptiik.papps.tasks.KaryawanIndexTask;
import id.ac.ub.ptiik.papps.tasks.MatakuliahIndexTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class ScheduleFragment extends Fragment 
	implements OnItemSelectedListener, KaryawanIndexInterface, 
	MatakuliahIndexInterface, JadwalMengajarIndexInterface, OnClickListener {
	
	Spinner categoryList;
	Spinner scheduleOptions;
	ListView scheduleList;
	ProgressBar scheduleProgressBar;
	View v;
	
	String[] category = new String[] {
			"Lecturer","Day","Subject"
		};
		
	String[] days = new String[] {
		"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
	};
		
	private KaryawanAdapter karyawanAdapter;
	private DayAdapter dayAdapter;
	private MatakuliahAdapter matakuliahAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_schedule, container, false);
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_schedule_category, category);
		this.categoryList = (Spinner) v.findViewById(R.id.scheduleCategory);
		this.categoryList.setAdapter(categoryAdapter);
		this.scheduleOptions = (Spinner) v.findViewById(R.id.scheduleOptions);
		this.scheduleOptions.setOnItemSelectedListener(this);
		this.categoryList.setOnItemSelectedListener(this);
		this.scheduleList = (ListView) v.findViewById(R.id.scheduleList);
		this.scheduleList.setAlpha(0);
		this.scheduleProgressBar = (ProgressBar) v.findViewById(R.id.scheduleProgressBar);
		this.scheduleProgressBar.setAlpha(0);
		this.v = v;
		
		this.v.findViewById(R.id.scheduleButtonRefresh).setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch(parent.getId()) {
			case R.id.scheduleCategory:
				switch(position) {
					case 0:
						if(this.karyawanAdapter == null) {
							KaryawanIndexTask task = new KaryawanIndexTask(getActivity(), this);
							task.execute();
						} else this.scheduleOptions.setAdapter(this.karyawanAdapter);
						break;
					case 1:
						this.dayAdapter = new DayAdapter(getActivity(), 
								new ArrayList<String>(Arrays.asList(this.days)));
						this.scheduleOptions.setAdapter(this.dayAdapter);
						break;
					case 2:
						if(this.matakuliahAdapter == null) {
							MatakuliahIndexTask task = new MatakuliahIndexTask(getActivity(), this);
							task.execute();
						} else this.scheduleOptions.setAdapter(this.matakuliahAdapter);
						break;
				}
				break;
			case R.id.scheduleOptions:
			loadSchedule();
				break;
		}
	}

	private void loadSchedule() {
		JadwalMengajarTask task = new JadwalMengajarTask(getActivity(), this);
		if(this.scheduleOptions.getAdapter().getClass() == KaryawanAdapter.class)
		{
			Karyawan karyawan = (Karyawan) this.scheduleOptions.getSelectedItem();					
			task.setByKaryawan(karyawan);
		} else if(this.scheduleOptions.getAdapter().getClass() == DayAdapter.class){
			String day = (String) this.scheduleOptions.getSelectedItem();
			task.setByDay(day);
		} else if(this.scheduleOptions.getAdapter().getClass() == MatakuliahAdapter.class){
			String kodeSubject = ((Matakuliah)this.scheduleOptions.getSelectedItem()).getKode();
			task.setBySubject(kodeSubject);
		}
			task.execute();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	@Override
	public void onRetrievingStart() {
		this.scheduleOptions.setPrompt("Loading...");
	}

	@Override
	public void onRetrieveComplete(ArrayList<Karyawan> karyawanList) {
		this.karyawanAdapter = new KaryawanAdapter(getActivity(), karyawanList);
		User u = SystemHelper.getSystemUser(getActivity());
		int index = this.karyawanAdapter.findItem(u);
		this.scheduleOptions.setAdapter(this.karyawanAdapter);
		if(index >= 0)
			this.scheduleOptions.setSelection(index);
	}

	@Override
	public void onRetrieveFail(String error) {}

	@Override
	public void onMatakuliahRetrievingStart() {}

	@Override
	public void onMatakuliahRetrieveComplete(
		ArrayList<Matakuliah> matakuliahList) {
		this.matakuliahAdapter = new MatakuliahAdapter(getActivity(), matakuliahList);
		this.scheduleOptions.setAdapter(this.matakuliahAdapter);
	}

	@Override
	public void onMatakuliahRetrieveFail(String error) {}

	@Override
	public void onJadwalRetrievingStart() {
		if(this.scheduleList.getAlpha() > 0)
			this.scheduleList.animate().alpha(0).setDuration(300).start();
		this.scheduleProgressBar.animate().alpha(1).setDuration(200).start();
	}

	@Override
	public void onJadwalRetrieveComplete(
			ArrayList<JadwalMengajar> jadwalMengajarList) {
		ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), jadwalMengajarList);
		this.scheduleList.setAdapter(adapter);
		this.scheduleList.animate().alpha(1).setDuration(300).start();
		this.scheduleProgressBar.animate().alpha(0).setDuration(200).start();
	}

	@Override
	public void onJadwalRetrieveFail(String error) {}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.scheduleButtonRefresh:
			if(this.scheduleOptions.getAdapter() != null)
				this.loadSchedule();
			break;
		}
	}

}
