package com.konka.upgrade.service;

interface IDownloadListener {
	void onDownloadProgress(in int type,in long downloadSize,in long totalSize);
}