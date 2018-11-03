package ca.raiot.cst2335.raiot;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class DevicesFragment extends Fragment {

    /*
    https://resocoder.com/2017/11/03/floating-action-menu-no-3rd-party-library-quick-tutorial-xamarin-android-code/

    https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu


    */
    private ListView listView;
    private static boolean isFabOpen;
    private FloatingActionButton autoAddFab;
    private FloatingActionButton manualAddFab;
    private FloatingActionButton addDeviceFab;
    private LinearLayout llManualAddFab;
    private LinearLayout llAutoAddFab;
    private LinearLayout llAddFAB;
    private FragmentActivity listener;
    private DeviceDatabaseHelper deviceDatabaseHelper;
    private Cursor cursor;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
            deviceDatabaseHelper = new DeviceDatabaseHelper(listener);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView)listener.findViewById(R.id.deviceListView);
        addDeviceFab = (FloatingActionButton) listener.findViewById(R.id.addDeviceFAB);
        autoAddFab = (FloatingActionButton) listener.findViewById(R.id.autoAddDeviceFAB);
        manualAddFab = (FloatingActionButton) listener.findViewById(R.id.manualAddDeviceFAB);
        llManualAddFab = (LinearLayout) listener.findViewById(R.id.llManualAddDeviceFAB);
        llAutoAddFab = (LinearLayout) listener.findViewById(R.id.llAutoAddDeviceFAB);
        llAddFAB = (LinearLayout) listener.findViewById(R.id.llAddFAB);

        // create custom adapter and when the view is set, set the id to "checkbox" + ref #
        ListAdapter adapter = new DevicesFragment.DatabaseDeviceAdapter(listener); //get layout id

        /*
        * Source: https://stackoverflow.com/questions/8846707/how-to-implement-a-long-click-listener-on-a-listview
        * Author: dinesh Sharma
        * Date: 2012-01-13
        * Comment: This breaks the code but this is a long press of the device pops up an alert
        *           confirming a delete of the device in the database.
        * */
       /*

       listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(listener);
                builder.setMessage("Do you want to delete this device?")
                        .setTitle("Delete Device?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int id){
                                deviceDatabaseHelper.deleteDevice(String.valueOf(id), listener);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int id){

                            }
                        })
                        .show();


                return true;
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   /*
                    https://stackoverflow.com/questions/40295528/get-id-of-checkbox-inside-of-listview-item-in-onitemclick-listener
                    */



                HashMap<String, String> currentDevice = new HashMap<>();

                TextView deviceName = (TextView) view.findViewById(R.id.deviceName);
                TextView status = (TextView) view.findViewById(R.id.status);
                TextView location = (TextView) view.findViewById(R.id.location);
                TextView ref = (TextView) view.findViewById(R.id.reference);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

                Bundle bundle = new Bundle();
                bundle.putString("name", deviceName.getText().toString());
                bundle.putString("status", status.getText().toString());
                bundle.putString("location", location.getText().toString());
                bundle.putString("ref", ref.getText().toString());

                ViewDeviceFragment viewDeviceFragment = new ViewDeviceFragment();

                viewDeviceFragment.setArguments(bundle);

                listener.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, viewDeviceFragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

        listView.setAdapter(adapter);



        addDeviceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    hideFABMenu();
                } else {
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
                listener.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new JsonFragment())
                        .addToBackStack(null)
                        .commit();

                hideFABMenu();

            }
        });

        manualAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new AddDeviceFragment())
                        .addToBackStack(null)
                        .commit();
                hideFABMenu();
            }
        });
    }

    private void showFabMenu() {
        isFabOpen = true;
        /*
        https://github.com/ajaydewari/FloatingActionButtonMenu/blob/master/app/src/main/java/com/ajaysinghdewari/floatingactionbuttonmenu/activities/MainActivity.java
        */
        llAutoAddFab.setVisibility(View.VISIBLE);
        llManualAddFab.setVisibility(View.VISIBLE);

        addDeviceFab.animate().rotation(360f);
        llAutoAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_105))
                .rotation(0f);
        llManualAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_55))
                .rotation(0f);

    }

    private void hideFABMenu() {
        isFabOpen = false;

        addDeviceFab.animate().rotation(-360f);
        llAutoAddFab.animate()
                .translationY(0);
        llManualAddFab.animate()
                .translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isFabOpen) {
                    llAutoAddFab.setVisibility(View.GONE);
                    llManualAddFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private class DatabaseDeviceAdapter extends ArrayAdapter<HashMap<String, String>> {

        private ArrayList<HashMap<String, String>> deviceList = new ArrayList<>();

        public DatabaseDeviceAdapter(Context ctx) {
            super(ctx, 0);
            cursor = deviceDatabaseHelper.getAllSavedDevices();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                HashMap<String, String> currentDevice = new HashMap<String, String>();

                int columnIndex = 0;
                currentDevice.put("ref", cursor.getString(columnIndex++));
                currentDevice.put("name", cursor.getString(columnIndex++));
                currentDevice.put("location", cursor.getString(columnIndex++));
                currentDevice.put("status", cursor.getString(columnIndex++));
                deviceList.add(currentDevice);
                cursor.moveToNext();
            }
        }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {


        long id = Long.parseLong(deviceList.get(position).get("ref"));

        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = listener.getLayoutInflater();

        View adapterViewLayout = inflater.inflate(R.layout.adapter_view_devices_layout, null);

        HashMap<String, String> currentDevice = deviceList.get(position);

        TextView deviceName = (TextView) adapterViewLayout.findViewById(R.id.deviceName);
        TextView status = (TextView) adapterViewLayout.findViewById(R.id.status);
        TextView location = (TextView) adapterViewLayout.findViewById(R.id.location);
        TextView reference = (TextView) adapterViewLayout.findViewById(R.id.reference);

        deviceName.setText(currentDevice.get("name"));
        status.setText(currentDevice.get("status"));
        location.setText(currentDevice.get("location"));
        reference.setText(currentDevice.get("ref"));

        return adapterViewLayout;

    }


}

}
