package com.example.siteGuardian;

import android.content.ContentResolver;
import android.net.Uri;

public class SiteGuardianProviderContract {

    static final int STATUSES = 10;
    static final int STATUS = 20;
    static final String AUTHORITY = "com.example.siteGuardian.contentprovider";

    static final String BASE_PATH = "statuses";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/statuses";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/status";
}