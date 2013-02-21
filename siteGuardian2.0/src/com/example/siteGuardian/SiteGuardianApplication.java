package com.example.siteGuardian;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/13/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiteGuardianApplication extends Application{

    private SiteGuardSQLHelper siteGuardSQLHelper;

    public SiteGuardSQLHelper getSiteGuardSQLHelper() {
        if(siteGuardSQLHelper == null){
            siteGuardSQLHelper = new SiteGuardSQLHelper(this);
        }
        return siteGuardSQLHelper;
    }

    public SQLiteDatabase getDb(){
        return getSiteGuardSQLHelper().getWritableDatabase();
    }


}
