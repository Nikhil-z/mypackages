package android.com.konka.dvb.search;

import android.com.konka.dvb.search.SearchManager.SearchResultInfo;
import android.com.konka.dvb.tuner.TunerManager.TunerInfo;

public interface SearchEventListener {
	void onUpdateSingalInfo(TunerInfo tunerInfo);
	void onUpdateProgInfoOfCurrvernFreq(int freq,SearchResultInfo searchResultInfo);
	void onUpdateSearchProgress(int step, int max_step);
	void onReceiveNitTable();
	void onSearchNitFailed();
	void onSearchAllDataFinish();
}
