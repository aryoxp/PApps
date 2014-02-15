package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.Matakuliah;

import java.util.ArrayList;

public interface MatakuliahIndexInterface {
	public void onMatakuliahRetrievingStart();
	public void onMatakuliahRetrieveComplete(ArrayList<Matakuliah> matakuliahList);
	public void onMatakuliahRetrieveFail(String error);
}
