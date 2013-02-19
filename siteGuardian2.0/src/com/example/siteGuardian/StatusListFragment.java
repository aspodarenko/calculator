package com.example.siteGuardian;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
public class StatusListFragment  extends ListFragment implements SimpleCursorAdapter.ViewBinder {

    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;

    private String[] FROM = {
            SiteGuardSQLHelper.RESULT_STATUS_COLUMN,
            SiteGuardSQLHelper.TIMESTAMP_COLUMN
    };

    private int[] TO = {R.id.statusText,R.id.checkingTime };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void refreshList(Boolean isOnlyFailStatuses) {
        String sqlWhere = null;
        if(isOnlyFailStatuses){
           sqlWhere = SiteGuardSQLHelper.RESULT_STATUS_COLUMN +"='"+CheckSiteService.STATUS_FAIL+"'";
        }
        cursor = SharedObjectManager.getInstance().getDb(getActivity().getApplication()).query(SiteGuardSQLHelper.STATUS_TABLE_NAME,
                null, sqlWhere, null, null, null, SiteGuardSQLHelper.TIMESTAMP_COLUMN +" DESC ");
        cursorAdapter = new SimpleCursorAdapter(getActivity().getApplication(), R.layout.list_item,cursor,FROM,TO,0);
        cursorAdapter.setViewBinder(this);
        setListAdapter(cursorAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        cursor.deactivate();
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
}
