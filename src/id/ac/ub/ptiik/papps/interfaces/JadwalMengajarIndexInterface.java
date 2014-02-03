package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.JadwalMengajar;

import java.util.ArrayList;

public interface JadwalMengajarIndexInterface {
	public void onJadwalRetrievingStart();
	public void onJadwalRetrieveComplete(ArrayList<JadwalMengajar> jadwalMengajarList);
	public void onJadwalRetrieveFail(String error);
}
