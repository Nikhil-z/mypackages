package com.konka.upgrade.service;

import com.konka.upgrade.service.UpgradeDesc;

interface IProcedureListener {

	void onStart();
	void onProcess(in int type);
	void onError(in int type,in int errMsg);
}