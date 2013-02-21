package com.example.siteGuardian;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/18/13
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class StatusListFragment  extends ListFragment implements SimpleCursorAdapter.ViewBinder, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String IS_ONLY_FAILED_STATUSES = "isOnlyFailedStatuses";
    private SimpleCursorAdapter cursorAdapter;

    private String[] FROM = {
            SiteGuardSQLHelper.RESULT_STATUS_COLUMN,
            SiteGuardSQLHelper.TIMESTAMP_COLUMN
    };

    private int[] TO = {R.id.statusText,R.id.checkingTime };
    private CursorLoader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cursorAdapter = new SimpleCursorAdapter(getActivity().getApplication(), R.layout.list_item,null,FROM,TO,0);
        cursorAdapter.setViewBinder(this);
        setListAdapter(cursorAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoader();
    }

    private void initLoader() {
        Bundle bundle = new Bundle();               //Should I create new bundle or use that I receive as parameter?
        bundle.putBoolean(IS_ONLY_FAILED_STATUSES,false);
        loader = (CursorLoader) getLoaderManager().initLoader(0, bundle, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList(false);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        int id = view.getId();
        switch (id){
            case R.id.checkingTime:
                Long timestamp = Long.valueOf(cursor.getString(columnIndex));
                CharSequence relTime = DateUtils.getRelativeTimeSpanString(view.getContext(), timestamp);
                ((TextView) view).setText(relTime);
                return true;
            case R.id.statusText:
                String status = cursor.getString(columnIndex);
                ImageView image = (ImageView) ((View)view.getParent()).findViewById(R.id.imageView);
                if(status.equals(CheckSiteService.STATUS_OK)){
                    image.setImageResource(R.drawable.green);
                }   else {
                    image.setImageResource(R.drawable.red);
                }
                return false;
        }

        return false;
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Boolean isOnlyFailedStatuses = bundle.getBoolean(IS_ONLY_FAILED_STATUSES);
        String selection = makeSelections(isOnlyFailedStatuses);
        return new CursorLoader(getActivity().getApplicationContext(),SiteGuardianProviderContract.CONTENT_URI,null,selection,null,SiteGuardSQLHelper.TIMESTAMP_COLUMN+" DESC");
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
       cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        cursorAdapter.swapCursor(null);
    }

    public void refreshList(Boolean isOnlyFailStatuses) {
        if(loader.isStarted()){
            loader.setSelection(makeSelections(isOnlyFailStatuses));
            loader.forceLoad();
        }
    }

    private String makeSelections(Boolean onlyFailedStatuses) {
        String selection = null;
        if(onlyFailedStatuses != null && onlyFailedStatuses){
            selection = SiteGuardSQLHelper.RESULT_STATUS_COLUMN +"='"+ CheckSiteService.STATUS_FAIL+"'";
        }
        return selection;
    }
}
