package com.konka.upgrade.download;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DownloadProvider extends ContentProvider {
    
    private static String DB_NAME = "download.db";
    private static int DB_VERSION = 5;
    private static String DB_DOWNLOAD_TABLE = "downloads";
    private static String DB_UPGRADE_TABLE = "upgrade";
    
    /** MIME type for an individual download */
    private static final String DOWNLOAD_TYPE = "vnd.android.cursor.item/download";
    private static final String UPGRADE_TYPE = "vnd.com.konka.upgrade.item/upgrade";
    
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int DOWNLOADS = 1;
    private static final int DOWNLOADS_ID =2;
    private static final int UPGRADE = 3;
    private static final int UPGRADE_ID = 4;
    
    static {
        sURIMatcher.addURI(Downloads.AUTHORITY, "downloads",DOWNLOADS);
        sURIMatcher.addURI(Downloads.AUTHORITY, "downloads/#",DOWNLOADS_ID);
        sURIMatcher.addURI(Downloads.AUTHORITY, "upgrade",UPGRADE);
        sURIMatcher.addURI(Downloads.AUTHORITY, "upgrade/#",UPGRADE_ID);
    }
    
    /** Different base URIs that could be used to access an individual download */
    private static final Uri[] BASE_URIS = new Uri[] {
            Downloads.Impl.CONTENT_URI,
            Downloads.Impl.UPGRADE_CONTENT_URI,
    };
    
    private SQLiteOpenHelper mOpenHelper = null;
    
    private String getDownloadIdFromUri(final Uri uri) {
        return uri.getPathSegments().get(1);
    }
    
    private SqlSelection getWhereClause(final Uri uri, final String where, final String[] whereArgs,
            int uriMatch) {
        SqlSelection selection = new SqlSelection();
        selection.appendClause(where, whereArgs);
        if (uriMatch == DOWNLOADS_ID) {
            selection.appendClause(Downloads.Impl._ID + " = ?", getDownloadIdFromUri(uri));
        }
        return selection;
    }
    
    private void notifyContentChanged(final Uri uri, int uriMatch) {
        Long downloadId = null;
        if (uriMatch == DOWNLOADS_ID || uriMatch == DOWNLOADS_ID) {
            downloadId = Long.parseLong(getDownloadIdFromUri(uri));
        }
        for (Uri uriToNotify : BASE_URIS) {
            if (downloadId != null) {
                uriToNotify = ContentUris.withAppendedId(uriToNotify, downloadId);
            }
            getContext().getContentResolver().notifyChange(uriToNotify, null);
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        SqlSelection selection;
        int match = sURIMatcher.match(uri);
        switch (match) {
            case DOWNLOADS:
            case DOWNLOADS_ID:
                selection = getWhereClause(uri, where, whereArgs, match);
                count = db.delete(DB_DOWNLOAD_TABLE, selection.getSelection(), selection.getParameters());
                break;
            case UPGRADE:
            case UPGRADE_ID:
                selection = getWhereClause(uri, where, whereArgs, match);
                count = db.delete(DB_UPGRADE_TABLE, selection.getSelection(), selection.getParameters());
                break;
            default:
                Log.d(Constants.TAG, "deleting unknown/invalid URI: " + uri);
                throw new UnsupportedOperationException("Cannot delete URI: " + uri);
        }
        notifyContentChanged(uri, match);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        int match = sURIMatcher.match(uri);
        switch (match) {
        case DOWNLOADS:
        case DOWNLOADS_ID:
            final String id = getDownloadIdFromUri(uri);
            final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            final String mimeType = DatabaseUtils.stringForQuery(db,
                    "SELECT " + Downloads.Impl.COLUMN_MIME_TYPE + " FROM " + DB_DOWNLOAD_TABLE +
                    " WHERE " + Downloads.Impl._ID + " = ?",
                    new String[]{id});
            if (TextUtils.isEmpty(mimeType)) {
                return DOWNLOAD_TYPE;
            } else {
                return mimeType;
            }
        case UPGRADE:
        case UPGRADE_ID:
            return UPGRADE_TYPE;
        default:
            if (Constants.LOGV) {
                Log.v(Constants.TAG, "calling getType on an unknown URI: " + uri);
            }
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues filteredValues;
        long rowID;
        
        int match = sURIMatcher.match(uri);
        switch (match) {
        case DOWNLOADS:
        case DOWNLOADS_ID:
            filteredValues = new ContentValues();
            copyString(Downloads.Impl.COLUMN_URI, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_MIME_TYPE, values, filteredValues);
    
            filteredValues.put(Downloads.Impl.COLUMN_TOTAL_BYTES, -1);
            filteredValues.put(Downloads.Impl.COLUMN_CURRENT_BYTES, 0);
            
            long lastMod = System.currentTimeMillis();
            filteredValues.put(Downloads.Impl.COLUMN_LAST_MODIFICATION, lastMod);
            
            rowID = db.insert(DB_DOWNLOAD_TABLE, null, filteredValues);
            if (rowID == -1) {
                Log.d(Constants.TAG, "couldn't insert into downloads database");
                return null;
            }
            notifyContentChanged(uri, match);
            return ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, rowID);
        case UPGRADE:
        case UPGRADE_ID:
            filteredValues = new ContentValues();
            copyString(Downloads.Impl.COLUMN_MANUFACTURE, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_MODEL, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_HW_VERSION, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_SW_VERSION, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_UPGRADE_URI, values, filteredValues);
            copyString(Downloads.Impl.COLUMN_UPGRADE_FILE, values, filteredValues);
            copyBoolean(Downloads.Impl.COLUMN_UPGRADE_STATUS, values, filteredValues);
            rowID = db.insert(DB_UPGRADE_TABLE, null, filteredValues);
            if (rowID == -1) {
                Log.d(Constants.TAG, "couldn't insert into downloads database");
                return null;
            }
            notifyContentChanged(uri, match);
            return ContentUris.withAppendedId(Downloads.Impl.UPGRADE_CONTENT_URI, rowID);
        default:
            if (Constants.LOGV) {
                Log.v(Constants.TAG, "calling getType on an unknown URI: " + uri);
            }
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        
        int match = sURIMatcher.match(uri);
        if ((match == -1) && 
                ((match != DOWNLOADS) || (match != DOWNLOADS_ID) &&
                ((match != UPGRADE) || (match != UPGRADE_ID)))) {
            if (Constants.LOGV) {
                Log.v(Constants.TAG, "querying unknown URI: " + uri);
            }
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SqlSelection fullSelection = getWhereClause(uri, selection,selectionArgs,match);

        if (true) {
            logVerboseQueryInfo(projection, selection, selectionArgs,
                    sortOrder, db);
        }
        
        if((match == DOWNLOADS) || (match == DOWNLOADS_ID)) {
            Cursor ret = db.query(DB_DOWNLOAD_TABLE, projection,
                    fullSelection.getSelection(), fullSelection.getParameters(),
                    null, null, sortOrder);
            return ret;
        } else {
            Cursor ret = db.query(DB_UPGRADE_TABLE, projection,
                    fullSelection.getSelection(), fullSelection.getParameters(),
                    null, null, sortOrder);
            return ret;
        }
    }
    
    private void logVerboseQueryInfo(String[] projection, final String selection,
            final String[] selectionArgs, final String sort, SQLiteDatabase db) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("starting query, database is ");
        if (db != null) {
            sb.append("not ");
        }
        sb.append("null; ");
        if (projection == null) {
            sb.append("projection is null; ");
        } else if (projection.length == 0) {
            sb.append("projection is empty; ");
        } else {
            for (int i = 0; i < projection.length; ++i) {
                sb.append("projection[");
                sb.append(i);
                sb.append("] is ");
                sb.append(projection[i]);
                sb.append("; ");
            }
        }
        sb.append("selection is ");
        sb.append(selection);
        sb.append("; ");
        if (selectionArgs == null) {
            sb.append("selectionArgs is null; ");
        } else if (selectionArgs.length == 0) {
            sb.append("selectionArgs is empty; ");
        } else {
            for (int i = 0; i < selectionArgs.length; ++i) {
                sb.append("selectionArgs[");
                sb.append(i);
                sb.append("] is ");
                sb.append(selectionArgs[i]);
                sb.append("; ");
            }
        }
        sb.append("sort is ");
        sb.append(sort);
        sb.append(".");
        Log.v(Constants.TAG, sb.toString());
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
            String[] whereArgs) {
        // TODO Auto-generated method stub
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        int count;
        SqlSelection selection;
        
        int match = sURIMatcher.match(uri);
        switch (match) {
            case DOWNLOADS:
            case DOWNLOADS_ID:
                selection = getWhereClause(uri, where, whereArgs, match);
                if (values.size() > 0) {
                    count = db.update(DB_DOWNLOAD_TABLE, values, selection.getSelection(),
                            selection.getParameters());
                } else {
                    count = 0;
                }
                break;
            case UPGRADE:
            case UPGRADE_ID:
                selection = getWhereClause(uri, where, whereArgs, match);
                if (values.size() > 0) {
                    count = db.update(DB_UPGRADE_TABLE, values, selection.getSelection(),
                            selection.getParameters());
                } else {
                    count = 0;
                }
                break;
            default:
                Log.d(Constants.TAG, "updating unknown/invalid URI: " + uri);
                throw new UnsupportedOperationException("Cannot update URI: " + uri);
        }
        notifyContentChanged(uri, match);
        return count;
    }
    
    @SuppressWarnings("unused")
    private static final void copyInteger(String key, ContentValues from, ContentValues to) {
        Integer i = from.getAsInteger(key);
        if (i != null) {
            to.put(key, i);
        }
    }

    private static final void copyBoolean(String key, ContentValues from, ContentValues to) {
        Boolean b = from.getAsBoolean(key);
        if (b != null) {
            to.put(key, b);
        }
    }

    private static final void copyString(String key, ContentValues from, ContentValues to) {
        String s = from.getAsString(key);
        if (s != null) {
            to.put(key, s);
        }
    }

    @SuppressWarnings("unused")
    private static final void copyStringWithDefault(String key, ContentValues from,
            ContentValues to, String defaultValue) {
        copyString(key, from, to);
        if (!to.containsKey(key)) {
            to.put(key, defaultValue);
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        
        public DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            onUpgrade(db,0,DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            createDownloadsTable(db);
            createUpgradeTable(db);
        }
        
        private void createUpgradeTable(SQLiteDatabase db) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + DB_UPGRADE_TABLE);
                db.execSQL("CREATE TABLE " + DB_UPGRADE_TABLE + "(" +
                        Downloads.Impl._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Downloads.Impl.COLUMN_MANUFACTURE + " TEXT, " +
                        Downloads.Impl.COLUMN_MODEL + " TEXT, " +
                        Downloads.Impl.COLUMN_HW_VERSION + " TEXT, " +
                        Downloads.Impl.COLUMN_SW_VERSION + " TEXT, " +
                        Downloads.Impl.COLUMN_UPGRADE_URI + " TEXT, " +
                        Downloads.Impl.COLUMN_UPGRADE_FILE + " TEXT, " +
                        Downloads.Impl.COLUMN_UPGRADE_STATUS + " BOOLEAN);");
            } catch (SQLException ex) {
                Log.e(Constants.TAG, "couldn't create table in downloads database");
                throw ex;
            }
        }
        
        private void createDownloadsTable(SQLiteDatabase db) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + DB_DOWNLOAD_TABLE);
                db.execSQL("CREATE TABLE " + DB_DOWNLOAD_TABLE + "(" +
                        Downloads.Impl._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Downloads.Impl.COLUMN_URI + " TEXT, " +
                        Downloads.Impl.COLUMN_FILE_NAME_HINT + " TEXT, " +
                        Downloads.Impl._DATA + " TEXT, " +
                        Downloads.Impl.COLUMN_MIME_TYPE + " TEXT, " +
                        Downloads.Impl.COLUMN_STATUS + " INTEGER, " +
                        Constants.FAILED_CONNECTIONS + " INTEGER, " +
                        Downloads.Impl.COLUMN_LAST_MODIFICATION + " BIGINT, " +
                        Downloads.Impl.COLUMN_TOTAL_BYTES + " INTEGER, " +
                        Downloads.Impl.COLUMN_CURRENT_BYTES + " INTEGER, " +
                        Constants.ETAG + " TEXT, " +
                        Downloads.Impl.COLUMN_ERROR_MSG + " TEXT);");
            } catch (SQLException ex) {
                Log.e(Constants.TAG, "couldn't create table in downloads database");
                throw ex;
            }
        }
    }
    
    private static class SqlSelection {
        public StringBuilder mWhereClause = new StringBuilder();
        public List<String> mParameters = new ArrayList<String>();

        public <T> void appendClause(String newClause, final T... parameters) {
            if (newClause == null || newClause.isEmpty()) {
                return;
            }
            if (mWhereClause.length() != 0) {
                mWhereClause.append(" AND ");
            }
            mWhereClause.append("(");
            mWhereClause.append(newClause);
            mWhereClause.append(")");
            if (parameters != null) {
                for (Object parameter : parameters) {
                    mParameters.add(parameter.toString());
                }
            }
        }

        public String getSelection() {
            return mWhereClause.toString();
        }

        public String[] getParameters() {
            String[] array = new String[mParameters.size()];
            return mParameters.toArray(array);
        }
    }
}
