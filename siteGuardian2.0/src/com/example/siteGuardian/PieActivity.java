package com.example.siteGuardian;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PieActivity extends Activity {

    final static String urlGoogleChart
            = "http://chart.apis.google.com/chart";
    final static String urlp3Api
            = "?cht=p3&chs=400x200&chl=OK|DOWN&chd=t:";


    ImageView pieChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagram);
        pieChart = (ImageView) findViewById(R.id.pie);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Integer[] siteAvailableStatistics = getSiteAvailableStatistics();
        String urlRqs3DPie = urlGoogleChart
                + urlp3Api
                + siteAvailableStatistics[0].toString() + "," + siteAvailableStatistics[1].toString();

        new PiaImageLoader().execute(urlRqs3DPie);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.diagram_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.settings:
                intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
            case R.id.mainActivity:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*
    result int[0] - status OK count
    result int[1] - status FAIL count
     */
    public Integer[] getSiteAvailableStatistics() {
        Integer[] result = new Integer[2];
        result[0] = 0;
        result[1] = 0;
        Cursor cursor = SharedObjectManager.getInstance().getDb(this).query(SiteGuardSQLHelper.STATUS_TABLE_NAME,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(SiteGuardSQLHelper.RESULT_STATUS_COLUMN)).equals(CheckSiteService.STATUS_OK)) {
                result[0]++;
            } else if (cursor.getString(cursor.getColumnIndex(SiteGuardSQLHelper.RESULT_STATUS_COLUMN)).equals(CheckSiteService.STATUS_FAIL)) {
                result[1]++;
            }
        }
        return result;
    }

    private class PiaImageLoader extends AsyncTask<String,Void,Bitmap> {

        private Bitmap loadChart(String urlRqs) {
            Bitmap bm = null;
            InputStream inputStream = null;

            try {
                inputStream = OpenHttpConnection(urlRqs);
                bm = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return bm;
        }

        private InputStream OpenHttpConnection(String strURL) throws IOException {
            InputStream is = null;
            URL url = new URL(strURL);
            URLConnection urlConnection = url.openConnection();

            try {
                HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = httpConn.getInputStream();
                }
            } catch (Exception ex) {
            }

            return is;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            if (url[0]!=null){
            return loadChart(url[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                Toast.makeText(PieActivity.this,
                        "Problem in loading 3D Pie Chart",
                        Toast.LENGTH_LONG).show();
            } else {
                pieChart.setImageBitmap(bitmap);
            }

        }



    }
}