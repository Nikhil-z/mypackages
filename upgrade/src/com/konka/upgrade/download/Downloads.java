package com.konka.upgrade.download;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Downloads {
    
    public static final String AUTHORITY = "com.konka.upgrade.download.DownloadProvider";
    
    private Downloads() {}

    public static final class Impl implements BaseColumns{
        private Impl() {}

        public static final Uri CONTENT_URI =
                Uri.parse("content://"+AUTHORITY+"/downloads");

        public static final String COLUMN_URI = "uri";

        public static final String COLUMN_FILE_NAME_HINT = "hint";
        
        public static final String _DATA = "_data";
        
        public static final String COLUMN_MIME_TYPE = "mimetype";

        public static final String COLUMN_STATUS = "status";
        
        public static final String COLUMN_ERROR_MSG = "errorMsg";

        public static final String COLUMN_LAST_MODIFICATION = "lastmod";

        public static final String COLUMN_TOTAL_BYTES = "total_bytes";

        public static final String COLUMN_CURRENT_BYTES = "current_bytes";
        
        /**
         * This download is allowed to run.
         */
        public static final int CONTROL_RUN = 0;

        /**
         * This download must pause at the first opportunity.
         */
        public static final int CONTROL_PAUSED = 1;
        
        
        public static Uri UPGRADE_CONTENT_URI =
                Uri.parse("content://"+AUTHORITY+"/upgrade");
        
        public static final String COLUMN_MANUFACTURE = "manufacture";
        
        public static final String COLUMN_MODEL = "model";
        
        public static final String COLUMN_HW_VERSION = "hw_version";
        
        public static final String COLUMN_SW_VERSION = "sw_version";
        
        public static final String COLUMN_UPGRADE_URI = "upgrade_uri";
        
        public static final String COLUMN_UPGRADE_FILE = "upgrade_file";
        
        public static final String COLUMN_UPGRADE_STATUS = "upgrade_status";

        public static boolean isStatusInformational(int status) {
            return (status >= 100 && status < 200);
        }

        public static boolean isStatusSuccess(int status) {
            return (status >= 200 && status < 300);
        }

        public static boolean isStatusError(int status) {
            return (status >= 400 && status < 600);
        }

        public static boolean isStatusClientError(int status) {
            return (status >= 400 && status < 500);
        }

        public static boolean isStatusServerError(int status) {
            return (status >= 500 && status < 600);
        }

        public static boolean isStatusCompleted(int status) {
            return (status >= 200 && status < 300) || (status >= 400 && status < 600);
        }

        public static final int STATUS_PENDING = 190;

        public static final int STATUS_RUNNING = 192;

        public static final int STATUS_PAUSED_BY_APP = 193;

        public static final int STATUS_WAITING_TO_RETRY = 194;

        public static final int STATUS_WAITING_FOR_NETWORK = 195;

        public static final int STATUS_QUEUED_FOR_WIFI = 196;

        public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 198;

        public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 199;

        public static final int STATUS_SUCCESS = 200;

        public static final int STATUS_BAD_REQUEST = 400;

        public static final int STATUS_NOT_ACCEPTABLE = 406;

        public static final int STATUS_LENGTH_REQUIRED = 411;

        public static final int STATUS_PRECONDITION_FAILED = 412;

        public static final int MIN_ARTIFICIAL_ERROR_STATUS = 488;

        public static final int STATUS_FILE_ALREADY_EXISTS_ERROR = 488;

        public static final int STATUS_CANNOT_RESUME = 399;

        public static final int STATUS_CANCELED = 490;

        public static final int STATUS_UNKNOWN_ERROR = 491;

        public static final int STATUS_FILE_ERROR = 492;

        public static final int STATUS_UNHANDLED_REDIRECT = 493;

        public static final int STATUS_UNHANDLED_HTTP_CODE = 494;

        public static final int STATUS_HTTP_DATA_ERROR = 495;

        public static final int STATUS_HTTP_EXCEPTION = 496;

        public static final int STATUS_TOO_MANY_REDIRECTS = 497;

        /**
         * This download has failed because requesting application has been
         * blocked by {@link NetworkPolicyManager}.
         *
         * @hide
         * @deprecated since behavior now uses
         *             {@link #STATUS_WAITING_FOR_NETWORK}
         */
        @Deprecated
        public static final int STATUS_BLOCKED = 498;

        /** {@hide} */
        public static String statusToString(int status) {
            switch (status) {
                case STATUS_PENDING: return "PENDING";
                case STATUS_RUNNING: return "RUNNING";
                case STATUS_PAUSED_BY_APP: return "PAUSED_BY_APP";
                case STATUS_WAITING_TO_RETRY: return "WAITING_TO_RETRY";
                case STATUS_WAITING_FOR_NETWORK: return "WAITING_FOR_NETWORK";
                case STATUS_QUEUED_FOR_WIFI: return "QUEUED_FOR_WIFI";
                case STATUS_INSUFFICIENT_SPACE_ERROR: return "INSUFFICIENT_SPACE_ERROR";
                case STATUS_DEVICE_NOT_FOUND_ERROR: return "DEVICE_NOT_FOUND_ERROR";
                case STATUS_SUCCESS: return "SUCCESS";
                case STATUS_BAD_REQUEST: return "BAD_REQUEST";
                case STATUS_NOT_ACCEPTABLE: return "NOT_ACCEPTABLE";
                case STATUS_LENGTH_REQUIRED: return "LENGTH_REQUIRED";
                case STATUS_PRECONDITION_FAILED: return "PRECONDITION_FAILED";
                case STATUS_FILE_ALREADY_EXISTS_ERROR: return "FILE_ALREADY_EXISTS_ERROR";
                case STATUS_CANNOT_RESUME: return "CANNOT_RESUME";
                case STATUS_CANCELED: return "CANCELED";
                case STATUS_UNKNOWN_ERROR: return "UNKNOWN_ERROR";
                case STATUS_FILE_ERROR: return "FILE_ERROR";
                case STATUS_UNHANDLED_REDIRECT: return "UNHANDLED_REDIRECT";
                case STATUS_UNHANDLED_HTTP_CODE: return "UNHANDLED_HTTP_CODE";
                case STATUS_HTTP_DATA_ERROR: return "HTTP_DATA_ERROR";
                case STATUS_HTTP_EXCEPTION: return "HTTP_EXCEPTION";
                case STATUS_TOO_MANY_REDIRECTS: return "TOO_MANY_REDIRECTS";
                case STATUS_BLOCKED: return "BLOCKED";
                default: return Integer.toString(status);
            }
        }

        /**
         * Constants related to HTTP request headers associated with each download.
         */
        public static class RequestHeaders {
            public static final String HEADERS_DB_TABLE = "request_headers";
            public static final String COLUMN_DOWNLOAD_ID = "download_id";
            public static final String COLUMN_HEADER = "header";
            public static final String COLUMN_VALUE = "value";

            /**
             * Path segment to add to a download URI to retrieve request headers
             */
            public static final String URI_SEGMENT = "headers";

            /**
             * Prefix for ContentValues keys that contain HTTP header lines, to be passed to
             * DownloadProvider.insert().
             */
            public static final String INSERT_KEY_PREFIX = "http_header_";
        }
    }
}
