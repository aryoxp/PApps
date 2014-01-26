package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.AgendaKaryawan;

import java.util.ArrayList;

public interface AgendaKaryawanIndexInterface {
	public void onRetrievingStart();
	public void onRetrieveComplete(ArrayList<AgendaKaryawan> AgendaKaryawanList);
	public void onRetrieveFail(String error);
}
