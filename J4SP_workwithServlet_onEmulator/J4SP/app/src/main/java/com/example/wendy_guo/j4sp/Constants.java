package com.example.wendy_guo.j4sp;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class Constants {
    public static final String IMAGE_TYPE = "image";
    public static final String PDF_TYPE = "pdf";
    public static final String TEXT_TYPE = "text";
    public static final String KEY_TYPE = "fileType";
    public static final String TIME_STAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String TIME_STAMP_FORMAT_DISPLAY = "yyyy-MM-dd";
    public static final String PHOTO_URI = "photo_uri";
    public static final String DELETE_FILE_OK = "true";
    public static final String VIEW_URL = "url";
    public static final String PHOTO_PATH = "photoPath";
    public static final String CLASS_MESSAGES = "Messages";
//    public static final String HOST = "http://ec2-52-0-34-233.compute-1.amazonaws.com:";
//    public static final String PORT = "8085";
    public static final String HOST = "http://10.0.2.2:";
    public static final String PORT = "8080";

    public static final String KEY_USER_NAME = "username";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NOTIFICATION = "notification";

    public static final String KEY_FILE_NAME = "filename";
    public static final String SHOW_RECORD = "show_list";
    public static final String SEARCH_RESULT = "JSON_search_result";
    public static final String SUMMARY_CODE = "summary";

    public static final String KEY_TOTAL = "cost";
    public static final String KEY_COMMENTS = "comments";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_FILE = "receipt";
    public static final String PREF_KEY_IMAGE_QUALITY = "image_quality";
    public static final String PREF_KEY_UPLOAD_WIFI = "upload_wifi";
    public static final String PREF_KEY_SAVE_DOWNLOAD = "save_download";

    public static final String PREF_KEY_REFRESH = "refresh";
    public static final String PREF_KEY_CACHE_NOT_EMPTY = "cache_status";
    public static final String TAKE_PHOTO_CODE = "take_photo";
    public static final int SHARE_BY_EMAIL = 37;
    public static final int SHARE_BY_SMS = 92;
    public static final int SHARE_BY_EMAIL_INT_EXTRA = 1;
    public static final int SHARE_BY_SMS_INT_EXTRA = 2;
    public static final String PICK_PHOTO_CODE = "pick_photo";
    public static final String OFFLINE_CACHE = "offline_cache.txt";
    public static final String CACHE_IMG_FOLDER = "cache_folder";

    public static final String ALARM_CANCEL ="com.example.wendy_guo.j4sp.CUSTOM_INTENT.CANCEL_ALARM";
    public static final String ALARM_SET ="com.example.wendy_guo.j4sp.CUSTOM_INTENT.SET_ALARM";

    private static final String DB_NAME = "J4SP.db";
    public static final String USER_ID = "UserID";
    public static final String STATUS_OK = "OK";
    public static final String PREF_KEY_CURRENCY = "currency";

    public static boolean WIFI_STATE = false;//for testing only
}
