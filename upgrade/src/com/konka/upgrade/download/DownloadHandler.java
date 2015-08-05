package com.konka.upgrade.download;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

public class DownloadHandler {

    private static final String TAG = "DownloadHandler";

    private final Map<Long, DownloadInfo> mDownloadsQueue =
            new LinkedHashMap<Long, DownloadInfo>();
    
    private static final DownloadHandler sDownloadHandler = new DownloadHandler();
    
    private DownloadHandler(){}
    
    public static DownloadHandler getInstance() {
        return sDownloadHandler;
    }
    
    public synchronized void enqueueDownload(DownloadInfo info) {
        if (!mDownloadsQueue.containsKey(info.mId)) {
            if (Constants.LOGV) {
                Log.i(TAG, "enqueued download. id: " + info.mId + ", uri: " + info.mUri);
            }
            mDownloadsQueue.put(info.mId, info);
            info.startDownloadThread();
        }
    }
    
    public synchronized boolean hasDownloadInQueue(long id) {
        return mDownloadsQueue.containsKey(id);
    }

    public synchronized void dequeueDownload(long id) {
        mDownloadsQueue.remove(id);
        if (mDownloadsQueue.size() == 0) {
            notifyAll();
        }
    }
    
    public synchronized void stopAll() {
        Iterator<Long> keys = mDownloadsQueue.keySet().iterator();
        while(keys.hasNext()) {
            DownloadInfo info = mDownloadsQueue.remove(keys.next());
            info.mControl = Downloads.Impl.CONTROL_PAUSED;
        }
    }
}
