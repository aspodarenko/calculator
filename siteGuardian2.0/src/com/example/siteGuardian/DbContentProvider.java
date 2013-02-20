package com.example.siteGuardian;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/20/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbContentProvider extends ContentProvider {

    // database
    private SiteGuardSQLHelper dbHelper;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(SiteGuardianProviderContract.AUTHORITY, SiteGuardianProviderContract.BASE_PATH, SiteGuardianProviderContract.STATUSES);
        sURIMatcher.addURI(SiteGuardianProviderContract.AUTHORITY, SiteGuardianProviderContract.BASE_PATH + "/#", SiteGuardianProviderContract.STATUS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SiteGuardSQLHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        queryBuilder.setTables(SiteGuardSQLHelper.STATUS_TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SiteGuardianProviderContract.STATUSES:
                if(selection != null){
                    queryBuilder.appendWhere(selection);
                }
                break;
            case SiteGuardianProviderContract.STATUS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(SharedObjectManager.getInstance().getDb(getContext()), projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch (uriType) {
            case SiteGuardianProviderContract.STATUSES:
                id = SharedObjectManager.getInstance().getDb(getContext()).insert(SiteGuardSQLHelper.STATUS_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(SiteGuardianProviderContract.BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case SiteGuardianProviderContract.STATUSES:
                rowsDeleted = SharedObjectManager.getInstance().getDb(getContext()).delete(SiteGuardSQLHelper.STATUS_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case SiteGuardianProviderContract.STATUS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = SharedObjectManager.getInstance().getDb(getContext());
        int rowsUpdated = 0;
        switch (uriType) {
            case SiteGuardianProviderContract.STATUSES:
                rowsUpdated = db.update(SiteGuardSQLHelper.STATUS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SiteGuardianProviderContract.STATUS:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(SiteGuardSQLHelper.STATUS_TABLE_NAME,
                            values,
                            SiteGuardSQLHelper.ID_COLUMN + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(SiteGuardSQLHelper.STATUS_TABLE_NAME,
                            values,
                            SiteGuardSQLHelper.ID_COLUMN + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { SiteGuardSQLHelper.ID_COLUMN,
                SiteGuardSQLHelper.STATUS_TABLE_NAME, SiteGuardSQLHelper.RESULT_STATUS_COLUMN,
                SiteGuardSQLHelper.TIMESTAMP_COLUMN };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
