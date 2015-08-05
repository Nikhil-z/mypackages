package com.konka.upgrade.download;

public interface DownloadListener {
    public void onDownloadStart(String url);
    public void onDownloadSize(String url,long size,long totalSize);
    public void onDownloadComplete(String url,String saveFile);
    public void onError(Throwable error);
}
