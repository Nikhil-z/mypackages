package com.konka.upgrade.download;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class Constants {

    /** Tag used for debugging/logging */
    public static final String TAG = "DownloadManager";

    /** The column that used to be used for the HTTP method of the request */
    public static final String RETRY_AFTER_X_REDIRECT_COUNT = "method";

    /** The column that is used for the downloads's ETag */
    public static final String ETAG = "etag";

    /** The column that is used to count retries */
    public static final String FAILED_CONNECTIONS = "numfailed";

    /** The default base name for downloaded files if we can't get one at the HTTP level */
    public static final String DEFAULT_DL_FILENAME = "downloadfile";

    /** The default extension for html files if we can't get one at the HTTP level */
    public static final String DEFAULT_DL_HTML_EXTENSION = ".html";

    /** The default extension for text files if we can't get one at the HTTP level */
    public static final String DEFAULT_DL_TEXT_EXTENSION = ".txt";

    /** The default extension for binary files if we can't get one at the HTTP level */
    public static final String DEFAULT_DL_BINARY_EXTENSION = ".bin";

    /**
     * When a number has to be appended to the filename, this string is used to separate the
     * base filename from the sequence number
     */
    public static final String FILENAME_SEQUENCE_SEPARATOR = "-";

    /** The default user agent used for downloads */
    public static final String DEFAULT_USER_AGENT;

    static {
        final StringBuilder builder = new StringBuilder();

        final boolean validRelease = !TextUtils.isEmpty(Build.VERSION.RELEASE);
        final boolean validId = !TextUtils.isEmpty(Build.ID);
        final boolean includeModel = "REL".equals(Build.VERSION.CODENAME)
                && !TextUtils.isEmpty(Build.MODEL);

        builder.append("AndroidDownloadManager");
        if (validRelease) {
            builder.append("/").append(Build.VERSION.RELEASE);
        }
        builder.append(" (Linux; U; Android");
        if (validRelease) {
            builder.append(" ").append(Build.VERSION.RELEASE);
        }
        if (includeModel || validId) {
            builder.append(";");
            if (includeModel) {
                builder.append(" ").append(Build.MODEL);
            }
            if (validId) {
                builder.append(" Build/").append(Build.ID);
            }
        }
        builder.append(")");

        DEFAULT_USER_AGENT = builder.toString();
    }
	
    /** The buffer size used to stream the data */
    public static final int SyncBUFFER_SIZE = 0x100000;//0x20000;
    /** The buffer size used to stream the data */
    public static final int BUFFER_SIZE = 4096;

    /** The minimum amount of progress that has to be done before the progress bar gets updated */
    public static final int MIN_PROGRESS_STEP = 4096;

    /** The minimum amount of time that has to elapse before the progress bar gets updated, in ms */
    public static final long MIN_PROGRESS_TIME = 1500;

    /** The maximum number of rows in the database (FIFO) */
    public static final int MAX_DOWNLOADS = 1000;

    /**
     * The number of times that the download manager will retry its network
     * operations when no progress is happening before it gives up.
     */
    public static final int MAX_RETRIES = 5;

    /**
     * The minimum amount of time that the download manager accepts for
     * a Retry-After response header with a parameter in delta-seconds.
     */
    public static final int MIN_RETRY_AFTER = 30; // 30s

    /**
     * The maximum number of redirects.
     */
    public static final int MAX_REDIRECTS = 5; // can't be more than 7.

    /** Enable separate connectivity logging */
    static final boolean LOGX = false;

    /** Enable verbose logging - use with "setprop log.tag.DownloadManager VERBOSE" */
    private static final boolean LOCAL_LOGV = true;
    public static final boolean LOGV = LOCAL_LOGV && Log.isLoggable(TAG, Log.WARN);

    /** Enable super-verbose logging */
    private static final boolean LOCAL_LOGVV = true;
    public static final boolean LOGVV = LOCAL_LOGVV && LOGV;
}
