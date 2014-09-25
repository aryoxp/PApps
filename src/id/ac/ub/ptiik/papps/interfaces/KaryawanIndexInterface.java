package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.Karyawan;

import java.util.ArrayList;

public interface KaryawanIndexInterface {
	public void onRetrievingStart();
	public void onRetrieveComplete(ArrayList<Karyawan> karyawanList);
	public void onRetrieveFail(String error);
}
