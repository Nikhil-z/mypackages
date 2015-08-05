package com.konka.upgrade.service;

import com.konka.upgrade.service.IProcedureListener;
import com.konka.upgrade.service.IDownloadListener;
import com.konka.upgrade.service.UpgradeDesc;

interface IUpgradeService {
	
	void checkUpgrade();
	int getCurrentState();
	UpgradeDesc getUpgradeDesc();
	void startDownload();
	void installUpgrade();
	void cancel();
	void neverRemind();
	boolean registProcedureListener(IProcedureListener cb);
	boolean unregistProcedureListener(IProcedureListener cb);
	boolean registDownloadListener(IDownloadListener cb);
	boolean unregistDownloadListener(IDownloadListener cb);
}