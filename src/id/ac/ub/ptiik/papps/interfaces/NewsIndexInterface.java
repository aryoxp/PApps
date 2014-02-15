package id.ac.ub.ptiik.papps.interfaces;

import id.ac.ub.ptiik.papps.base.News;

import java.util.ArrayList;

public interface NewsIndexInterface {
	public void onRetrievingStart();
	public void onRetrieveComplete(ArrayList<News> newsList);
	public void onRetrieveFail(String error);
}
