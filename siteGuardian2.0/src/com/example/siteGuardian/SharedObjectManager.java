package com.example.siteGuardian;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/13/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SharedObjectManager {



    private SiteGuardSQLHelper siteGuardSQLHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static volatile SharedObjectManager sharedObjectManager;

    public SiteGuardSQLHelper getSiteGuardSQLHelper(Context context) {
        if(siteGuardSQLHelper == null){
            siteGuardSQLHelper = new SiteGuardSQLHelper(context);
        }
        return siteGuardSQLHelper;
    }

    public static SharedObjectManager getInstance(){
        if(sharedObjectManager == null){
            synchronized (SharedObjectManager.class){
                if(sharedObjectManager ==null ){
                    sharedObjectManager = new SharedObjectManager();
                }
            }
        }
        return sharedObjectManager;
    }

    public SQLiteDatabase getDb(Context context){
        return getSiteGuardSQLHelper(context).getWritableDatabase();
    }


}
