package com.example.siteGuardian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/13/13
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiteGuardSQLHelper extends SQLiteOpenHelper {

    public static final String STATUS_TABLE_NAME = "STATUS_CHECKING_RESULTS";
    public static final String RESULT_STATUS_COLUMN = "RESULT_STATUS_COLUMN";
    public static final String TIMESTAMP_COLUMN = "TIMESTAMP_COLUMN";

    public SiteGuardSQLHelper(Context context) {
        super(context, "siteGuardDB", null, 1);
    }

    private static final String DB_CREATE = "create table " + STATUS_TABLE_NAME + "(" + BaseColumns._ID +" integer primary key, "+
            RESULT_STATUS_COLUMN +" text, "+
            TIMESTAMP_COLUMN + " integer ); ";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
           sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
