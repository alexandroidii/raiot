package ca.raiot.cst2335.raiot;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DevicesFragment extends Fragment {

    /*
    https://resocoder.com/2017/11/03/floating-action-menu-no-3rd-party-library-quick-tutorial-xamarin-android-code/

    https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu


    */

    private static final String ACTIVITY_NAME = "1234 DevicesFragment";

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
    private int progress;
    private ProgressBar progressBar;

    private ArrayList<HashMap<String, String>> jsonDeviceList;
    private ArrayList<HashMap<String, String>> deviceList;


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

        GetDevices getDevices = new GetDevices("Updating Device Statuses... Please wait!");
        getDevices.execute("https://connected2.homeseer.com/JSON?request=getstatus&location1=android&user=robert@lange.ca&pass=Myeasslake$");

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


        public DatabaseDeviceAdapter(Context ctx) {
            super(ctx, 0);

            deviceList = new ArrayList<>();
            cursor = deviceDatabaseHelper.getAllSavedDevices();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                HashMap<String, String> currentDevice = new HashMap<String, String>();

                int columnIndex = 0;
                currentDevice.put("ref", cursor.getString(columnIndex++));
                currentDevice.put("name", cursor.getString(columnIndex++));
                currentDevice.put("location", cursor.getString(columnIndex++));

                for (int i = 0; i < jsonDeviceList.size(); i++) {
                    if (currentDevice.get("ref").equals(jsonDeviceList.get(i).get("ref"))) {
                        currentDevice.put("status", jsonDeviceList.get(i).get("status"));
                    }
                }

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


    public class GetDevices extends AsyncTask<String, Integer, String> {

        private String preJsonToast;
        LinearLayout llSpinnerProgress = (LinearLayout)listener.findViewById(R.id.llSpinnerProgress);

        public void setPreJsonToast(String toastMessage) {
            this.preJsonToast = preJsonToast;
        }

        public GetDevices() {

        }

        public GetDevices(String preJsonToast) {
            this.preJsonToast = preJsonToast;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonDeviceList = new ArrayList<>();
            llSpinnerProgress.setVisibility(View.VISIBLE);
            publishProgress(10); //give some indication of progress
            if (preJsonToast != null) {
                Toast.makeText(listener, preJsonToast, Toast.LENGTH_LONG).show();
            }
        }

        //Source: https://www.tutorialspoint.com/android/android_json_parser.htm

        @Override
        protected String doInBackground(String... urls) {

            HttpHandler httpHandler = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(urls[0]);

            Log.i(ACTIVITY_NAME, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray hsDevices = jsonObj.getJSONArray("Devices");

                    // looping through All hsDevices
                    for (int i = 0; i < hsDevices.length(); i++) {
                        progress = progress + 25;
                        JSONObject hsd = hsDevices.getJSONObject(i);
                        String name = hsd.getString("name");
                        String status = hsd.getString("status");
                        String location = hsd.getString("location");
                        String ref_id = hsd.getString("ref");

//                        // tmp hash map for single device
                        HashMap<String, String> device = new HashMap<>();

//                        // adding each child node to HashMap key => value
                        device.put("name", name);
                        device.put("status", status);
                        device.put("location", location);
                        device.put("ref", ref_id);
//                        // adding device to device list
                        jsonDeviceList.add(device);
                        publishProgress(progress);
                        jsonProgress(progress);
                    }
                } catch (final JSONException e) {
                    Log.i(ACTIVITY_NAME, "Json parsing error: " + e.getMessage());
                    listener.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(listener.getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.i(ACTIVITY_NAME, "Couldn't get json from server.");
                listener.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(listener.getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Integer... progress) {
            Log.i(ACTIVITY_NAME, "" + progress[0]);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress[0]);
            }
        }


        private void jsonProgress(int pcnt) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(pcnt);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            listView = (ListView) listener.findViewById(R.id.deviceListView);
            View emptyDbListMessage = listener.findViewById(R.id.emptyDbList);
            listView.setEmptyView(emptyDbListMessage);

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


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final long deviceId = id;

                    AlertDialog.Builder builder = new AlertDialog.Builder(listener);
                    builder.setMessage("Do you want to delete this device?")
                            .setTitle("Delete Device?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {

                                    deviceDatabaseHelper.deleteDevice(String.valueOf(deviceId), listener);
                                    listener.getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.frame_layout, new DevicesFragment())
                                            .addToBackStack(null)
                                            .commit();

                                    Toast.makeText(listener, "Device Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {

                                }
                            })
                            .show();


                    return true;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, String> currentDevice = new HashMap<>();

                    TextView deviceName = (TextView) view.findViewById(R.id.deviceName);
                    TextView status = (TextView) view.findViewById(R.id.status);
                    TextView location = (TextView) view.findViewById(R.id.location);
                    TextView ref = (TextView) view.findViewById(R.id.reference);

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

            llSpinnerProgress.setVisibility(View.GONE);

        }
    }
}
