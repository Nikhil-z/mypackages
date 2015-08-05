package com.konka.upgrade.download;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.android.internal.util.IndentingPrintWriter;

public class DownloadInfo {
    public static class Reader {
        private DownloadListener mDownloadListener;
        private Cursor mCursor;
        
        public Reader(ContentResolver resolver,Cursor cursor,DownloadListener listener) {
            mCursor = cursor;
            mDownloadListener = listener;
        }
        
        public DownloadInfo newDownloadInfo(Context context) {
            DownloadInfo info = new DownloadInfo(context,mDownloadListener);
            updateFromDatabase(info);
            return info;
        }
        
        public void updateFromDatabase(DownloadInfo info) {
            info.mId = getLong(Downloads.Impl._ID);
            info.mUri = getString(Downloads.Impl.COLUMN_URI);
            info.mHint = getString(Downloads.Impl.COLUMN_FILE_NAME_HINT);
            info.mFileName = getString(Downloads.Impl._DATA);
            info.mMimeType = getString(Downloads.Impl.COLUMN_MIME_TYPE);
            info.mStatus = getInt(Downloads.Impl.COLUMN_STATUS);
            info.mNumFailed = getInt(Constants.FAILED_CONNECTIONS);
            info.mLastMod = getLong(Downloads.Impl.COLUMN_LAST_MODIFICATION);
            info.mTotalBytes = getLong(Downloads.Impl.COLUMN_TOTAL_BYTES);
            info.mCurrentBytes = getLong(Downloads.Impl.COLUMN_CURRENT_BYTES);
            info.mETag = getString(Constants.ETAG);
            info.mControl = Downloads.Impl.CONTROL_RUN;
        }

        private String getString(String column) {
            int index = mCursor.getColumnIndexOrThrow(column);
            String s = mCursor.getString(index);
            return (TextUtils.isEmpty(s)) ? null : s;
        }

        private Integer getInt(String column) {
            return mCursor.getInt(mCursor.getColumnIndexOrThrow(column));
        }

        private Long getLong(String column) {
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(column));
        }
    }
    
    public static class Request {
        private String mUrl;
        
        public Request(String uri) {
            mUrl = uri;
        }
        
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(Downloads.Impl.COLUMN_URI, mUrl);
            return values;
        }
    }

    /**
     * The network is usable for the given download.
     */
    public static final int NETWORK_OK = 1;

    /**
     * There is no network connectivity.
     */
    public static final int NETWORK_NO_CONNECTION = 2;

    /**
     * Current network is blocked.
     */
    public static final int NETWORK_BLOCKED = 3;


    public long mId;
    public String mUri;
    public String mHint;
    public String mFileName;
    public String mMimeType;
    public int mDestination;
    public int mControl;
    public int mStatus;
    public int mNumFailed;
    public long mLastMod;
    public String mUserAgent;
    public long mTotalBytes;
    public long mCurrentBytes;
    public String mETag;

    public int mFuzz;
    
    private DownloadListener mDownloadListener;

    private List<Pair<String, String>> mRequestHeaders = new ArrayList<Pair<String, String>>();
    private Context mContext;

    private DownloadInfo(Context context,DownloadListener listener) {
        mContext = context;
        mDownloadListener = listener;
        mFuzz = Helpers.sRandom.nextInt(1001);
    }

    public Collection<Pair<String, String>> getHeaders() {
        return Collections.unmodifiableList(mRequestHeaders);
    }

    /**
     * @return a non-localized string appropriate for logging corresponding to one of the
     * NETWORK_* constants.
     */
    public String getLogMessageForNetworkError(int networkError) {
        switch (networkError) {
            case NETWORK_NO_CONNECTION:
                return "no network connection available";

            case NETWORK_BLOCKED:
                return "network is blocked for requesting application";

            default:
                return "unknown error with network connectivity";
        }
    }
    
    public int checkCanUseNetwork() {
        ConnectivityManager connectivity =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(Constants.TAG, "couldn't get connectivity manager");
            return NETWORK_NO_CONNECTION;
        }

        final NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
        if (activeInfo == null && Constants.LOGVV) {
            Log.v(Constants.TAG, "network is not available");
            return NETWORK_NO_CONNECTION;
        } else if(activeInfo.isConnected()) {
            return NETWORK_OK;
        } else {
            return NETWORK_NO_CONNECTION;
        }
    }

    public Uri getDownloadsUri() {
        return ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, mId);
    }

    public void dump(IndentingPrintWriter pw) {
        pw.println("DownloadInfo:");
        pw.increaseIndent();

        pw.printPair("mId", mId);
        pw.printPair("mLastMod", mLastMod);
        pw.println();

        pw.printPair("mUri", mUri);
        pw.println();

        pw.printPair("mMimeType", mMimeType);
        pw.println();

        pw.printPair("mFileName", mFileName);
        pw.printPair("mDestination", mDestination);
        pw.println();

        pw.printPair("mStatus", Downloads.Impl.statusToString(mStatus));
        pw.printPair("mCurrentBytes", mCurrentBytes);
        pw.printPair("mTotalBytes", mTotalBytes);
        pw.println();

        pw.printPair("mNumFailed", mNumFailed);
        pw.printPair("mETag", mETag);
        pw.println();

        pw.println();

        pw.decreaseIndent();
    }

    void startDownloadThread() {
        DownloadThread downloader = new DownloadThread(mContext, this,StorageManager.getInstance(mContext));
        downloader.start();
    }
    
    void onStart() {
        mDownloadListener.onDownloadStart(mUri);
    }
    
    void onProgress(long current,long total) {
        mDownloadListener.onDownloadSize(mUri, current, total);
    }
    
    void onError(String errMsg) {
        mDownloadListener.onError(new Throwable(errMsg));
    }
    
    void onComplete(String uri,String fileName) {
        mDownloadListener.onDownloadComplete(uri, fileName);
    }
}
