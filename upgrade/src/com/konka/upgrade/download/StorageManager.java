package com.konka.upgrade.download;

import android.content.Context;
import android.util.Log;

import com.konka.upgrade.utils.StorageUtils;

import java.io.File;

//import libcore.io.ErrnoException;
//import libcore.io.Libcore;
//import libcore.io.StructStat;

class StorageManager {

    private final File mDownloadDataDir;

    private static StorageManager sSingleton = null;

    /** how often do we need to perform checks on space to make sure space is available */
    private static final int FREQUENCY_OF_CHECKS_ON_SPACE_AVAILABILITY = 1024 * 1024 * 10; // 10MB
    private int mBytesDownloadedSinceLastCheckOnSpace = 0;

    /**
     * maintains Singleton instance of this class
     */
    synchronized static StorageManager getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new StorageManager(context);
        }
        return sSingleton;
    }

    private StorageManager(Context context) { // constructor is private
        mDownloadDataDir = new File(StorageUtils.FILE_ROOT);
    }

    void verifySpaceBeforeWritingToFile(int destination, String path, long length)
            throws StopRequestException {
        // do this check only once for every 1MB of downloaded data
        if (incrementBytesDownloadedSinceLastCheckOnSpace(length) <
                FREQUENCY_OF_CHECKS_ON_SPACE_AVAILABILITY) {
            return;
        }
        verifySpace(destination, path, length);
    }

    void verifySpace(int destination, String path, long length) throws StopRequestException {
        resetBytesDownloadedSinceLastCheckOnSpace();
        File dir = null;
        if (Constants.LOGV) {
            Log.i(Constants.TAG, "in verifySpace, destination: " + destination +
                    ", path: " + path + ", length: " + length);
        }
        if (path == null) {
            throw new IllegalArgumentException("path can't be null");
        }
        switch (destination) {
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION:
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION_NOROAMING:
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION_PURGEABLE:
//                dir = mDownloadDataDir;
//                break;
//            case Downloads.Impl.DESTINATION_EXTERNAL:
//                dir = mExternalStorageDir;
//                break;
//            case Downloads.Impl.DESTINATION_SYSTEMCACHE_PARTITION:
//                dir = mSystemCacheDir;
//                break;
//            case Downloads.Impl.DESTINATION_FILE_URI:
//                if (path.startsWith(mExternalStorageDir.getPath())) {
//                    dir = mExternalStorageDir;
//                } else if (path.startsWith(mDownloadDataDir.getPath())) {
//                    dir = mDownloadDataDir;
//                } else if (path.startsWith(mSystemCacheDir.getPath())) {
//                    dir = mSystemCacheDir;
//                }
//                break;
            default:
                dir = mDownloadDataDir;
         }
        if (dir == null) {
            throw new IllegalStateException("invalid combination of destination: " + destination +
                    ", path: " + path);
        }
        findSpace(dir, length, destination);
    }

    /**
     * finds space in the given filesystem (input param: root) to accommodate # of bytes
     * specified by the input param(targetBytes).
     * returns true if found. false otherwise.
     */
    private synchronized void findSpace(File root, long targetBytes, int destination)
            throws StopRequestException {
        if (targetBytes == 0) {
            return;
        }
        if (root.equals(mDownloadDataDir)) {
            long bytesAvailable = getAvailableBytesInDownloadsDataDir(mDownloadDataDir);
            if (bytesAvailable < targetBytes) {
                throw new StopRequestException(Downloads.Impl.STATUS_INSUFFICIENT_SPACE_ERROR,
                        "not enough free space in the filesystem rooted at: " + root);
            }
        }
    }

    /**
     * returns the number of bytes available in the downloads data dir
     * TODO this implementation is too slow. optimize it.
     */
    private long getAvailableBytesInDownloadsDataDir(File root) {
        long space = StorageUtils.getAvailableStorage();
        
        if (Constants.LOGV) {
            Log.i(Constants.TAG, "available space (in bytes) in downloads data dir: " + space);
        }
        return space;
    }

    File locateDestinationDirectory(String mimeType, int destination, long contentLength)
            throws StopRequestException {
        switch (destination) {
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION:
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION_PURGEABLE:
//            case Downloads.Impl.DESTINATION_CACHE_PARTITION_NOROAMING:
//                return mDownloadDataDir;
//            case Downloads.Impl.DESTINATION_SYSTEMCACHE_PARTITION:
//                return mSystemCacheDir;
//            case Downloads.Impl.DESTINATION_EXTERNAL:
//                File base = new File(mExternalStorageDir.getPath() + Constants.DEFAULT_DL_SUBDIR);
//                if (!base.isDirectory() && !base.mkdir()) {
//                    // Can't create download directory, e.g. because a file called "download"
//                    // already exists at the root level, or the SD card filesystem is read-only.
//                    throw new StopRequestException(Downloads.Impl.STATUS_FILE_ERROR,
//                            "unable to create external downloads directory " + base.getPath());
//                }
//                return base;
//            default:
//                throw new IllegalStateException("unexpected value for destination: " + destination);
            default:
                return mDownloadDataDir;
        }
    }

    File getDownloadDataDirectory() {
        return mDownloadDataDir;
    }

    private synchronized int incrementBytesDownloadedSinceLastCheckOnSpace(long val) {
        mBytesDownloadedSinceLastCheckOnSpace += val;
        return mBytesDownloadedSinceLastCheckOnSpace;
    }

    private synchronized void resetBytesDownloadedSinceLastCheckOnSpace() {
        mBytesDownloadedSinceLastCheckOnSpace = 0;
    }
}
