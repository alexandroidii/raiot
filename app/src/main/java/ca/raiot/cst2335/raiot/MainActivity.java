package ca.raiot.cst2335.raiot;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /*
        https://resocoder.com/2017/11/03/floating-action-menu-no-3rd-party-library-quick-tutorial-xamarin-android-code/

        https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu


        */

    protected static final String ACTIVITY_NAME = "1234 MainActivity";

    private static boolean isFabOpen;
    private FloatingActionButton autoAddFab;
    private FloatingActionButton manualAddFab;
    private FloatingActionButton addDeviceFab;
    private LinearLayout llManualAddFab;
    private LinearLayout llAutoAddFab;
    private LinearLayout llAddFAB;
    private HashMap<String, String> currentDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFragment(new DevicesFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectFragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_devices:
                selectFragment = new DevicesFragment();
                break;
            case R.id.navigation_schedules:
                selectFragment = new ScheduleFragment();
                break;
            case R.id.navigation_automation:
                selectFragment = new AutomationFragment();
                break;
        }

        return loadFragment(selectFragment);
    }

    /*
        source: https://www.simplifiedcoding.net/bottom-navigation-android-example/
        author: Belal Khan
        Date: 2018-01-23
    */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    public HashMap<String, String> getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(HashMap<String, String> currentDevice) {
        this.currentDevice = currentDevice;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
/*
    https://stackoverflow.com/questions/14275627/how-to-go-back-to-previous-fragment-on-pressing-manually-back-button-of-individu
    */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.helpMenuItem:
                Log.i(ACTIVITY_NAME, "Help Menu icon selected");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, new HelpFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
        return true;
    }


    /*
    * Source: https://stackoverflow.com/questions/14275627/how-to-go-back-to-previous-fragment-on-pressing-manually-back-button-of-individu
    * Author: Suhas
    * Date: 2016-06-25
    *
    * */
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }
}
