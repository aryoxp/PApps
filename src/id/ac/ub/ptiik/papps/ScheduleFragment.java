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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ScheduleFragment extends Fragment 
	implements OnItemSelectedListener, KaryawanIndexInterface, 
	MatakuliahIndexInterface, JadwalMengajarIndexInterface {
	
	Spinner categoryList;
	Spinner scheduleOptions;
	ListView scheduleList;
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
		this.v = v;
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
				if(this.scheduleOptions.getAdapter().getClass() == KaryawanAdapter.class)
				{
					Karyawan karyawan = (Karyawan) this.scheduleOptions.getSelectedItem();
					JadwalMengajarTask task = new JadwalMengajarTask(getActivity(), this, karyawan);
					task.execute();
				}
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	@Override
	public void onRetrievingStart() {}

	@Override
	public void onRetrieveComplete(ArrayList<Karyawan> karyawanList) {
		this.karyawanAdapter = new KaryawanAdapter(getActivity(), karyawanList);
		this.scheduleOptions.setAdapter(this.karyawanAdapter);
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
	public void onJadwalRetrievingStart() {}

	@Override
	public void onJadwalRetrieveComplete(
			ArrayList<JadwalMengajar> jadwalMengajarList) {
		ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), jadwalMengajarList);
		this.scheduleList.setAdapter(adapter);
	}

	@Override
	public void onJadwalRetrieveFail(String error) {}

}
