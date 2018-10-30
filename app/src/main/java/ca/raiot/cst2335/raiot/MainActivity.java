package ca.raiot.cst2335.raiot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /*
        https://resocoder.com/2017/11/03/floating-action-menu-no-3rd-party-library-quick-tutorial-xamarin-android-code/

        https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu


        */
    private static boolean isFabOpen;
    private FloatingActionButton autoAddFab;
    private FloatingActionButton manualAddFab;
    private FloatingActionButton addDeviceFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFragment(new DevicesFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        addDeviceFab = (FloatingActionButton) findViewById(R.id.addDeviceFAB);
        autoAddFab = (FloatingActionButton) findViewById(R.id.autoAddDeviceFAB);
        manualAddFab = (FloatingActionButton) findViewById(R.id.manualAddDeviceFAB);

        addDeviceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFabOpen){
                    hideFABMenu();
                }else{
                    showFabMenu();
                }

            }
        });

        autoAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Robert, you can call your new activity that will
                 automatically add the devices here.  Copy what I did in the
                 manualAddFab.setOnClickListener.*/

                //loadFragment(new <YOUR CLASS HERE>())


                hideFABMenu();

            }
        });

        manualAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddDeviceFragment());
                hideFABMenu();
            }
        });
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
                    .commit();
            return true;
        }
        return false;
    }

    private void showFabMenu() {
        isFabOpen = true;

        addDeviceFab.animate().rotation(360f);
        autoAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_105))
                .rotation(0f);
        manualAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_55))
                .rotation(0f);

    }

    private void hideFABMenu() {
        isFabOpen = false;

        addDeviceFab.animate().rotation(-360f);
        autoAddFab.animate()
                .translationY(0);
        manualAddFab.animate()
                .translationY(0);

    }


}
