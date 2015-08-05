package com.konka.upgrade.download;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class DownloadManager {
    
    private Context mContext;
    
    public DownloadManager(Context context) {
        mContext = context;
    }
    
    public void startDownload(String url,DownloadListener listener) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(
                Downloads.Impl.CONTENT_URI, 
                null, 
                "( "+
                Downloads.Impl.COLUMN_URI + "='"+url + "')", null, null);
        
        
        try {
            if (!cursor.moveToFirst()) {
                DownloadInfo.Request request = new DownloadInfo.Request(url);
                resolver.insert(Downloads.Impl.CONTENT_URI, request.toContentValues());
            }
        } finally {
            cursor.close();
        }
        /*
         * to do this in case of exception
         */
        try {
            cursor = resolver.query(
                    Downloads.Impl.CONTENT_URI, 
                    null, 
                    Downloads.Impl.COLUMN_URI + "='"+url + "'", null, null);
            if (cursor.moveToFirst()) {
                DownloadInfo.Reader reader = new DownloadInfo.Reader(resolver, cursor,listener);
                DownloadHandler.getInstance().enqueueDownload(reader.newDownloadInfo(mContext));
            }
        } finally {
            cursor.close();
        }
    }
}
