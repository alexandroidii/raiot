package ca.raiot.cst2335.raiot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView deviceListView;
    private ArrayList<String> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_devices:
                        selectFragment = DevicesFragment.newInstance(1);
                        break;
                    case R.id.navigation_settings:
                        mTextMessage.setText(R.string.title_settings);
                        break;
                    case R.id.navigation_help:
                        mTextMessage.setText(R.string.title_help);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectFragment);
                transaction.commit();

                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, DevicesFragment.newInstance(1));
        transaction.commit();

        deviceListView = (ListView)findViewById(R.id.deviceList);
        deviceList = new ArrayList<String>();
        deviceList.add("Light 1");
        deviceList.add("Light 2");
        deviceList.add("Light 3");

        DeviceAdapter deviceAdapter = new DeviceAdapter(this);
        deviceListView.setAdapter(deviceAdapter);
        }


public class DeviceAdapter extends ArrayAdapter<String> {
    public DeviceAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public String getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        View result = null;
            result = inflater.inflate(R.layout.device_item_layout, null);


        Switch deviceSwitch = (Switch) result.findViewById(R.id.deviceSwitch);
        deviceSwitch.setText(getItem(position));

        return result;

    }


}

}
