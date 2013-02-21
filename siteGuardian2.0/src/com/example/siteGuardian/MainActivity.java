package com.example.siteGuardian;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Button startStopButton;

    private Intent serviceIntent;
    private StatusListFragment statusListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CheckSiteService.isStarted = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startStopButton = (Button) findViewById(R.id.startStopButton);
        startStopButton.setOnClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        statusListFragment = (StatusListFragment)fragmentManager.findFragmentById(R.id.statusViewFragmentId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSettings();
    }

    private void updateSettings() {
        if (CheckSiteService.isStarted) {
            stopService(serviceIntent);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
            case R.id.viewAllItems:
                statusListFragment.refreshList(false);
                return true;
            case R.id.viewFailItems:
                statusListFragment.refreshList(true);
                return true;
            case R.id.diagram:
                Intent diagramIntent = new Intent(this,PieActivity.class);
                startActivity(diagramIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startStopButton) {
            serviceIntent = new Intent(this, CheckSiteService.class);
            if (CheckSiteService.isStarted) {
                stopService(serviceIntent);
                CheckSiteService.isStarted = false;
                startStopButton.setText(getString(R.string.start));
            } else {
                startService(serviceIntent);
                CheckSiteService.isStarted = true;
                startStopButton.setText(getString(R.string.stop));
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
