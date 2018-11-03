package ca.raiot.cst2335.raiot;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
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

public class JsonFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "1234 JsonFragment";

    ArrayList<HashMap<String, String>> deviceList = new ArrayList<>();
    ArrayList<HashMap<String, String>> saveDeviceList = new ArrayList<>();

    private FloatingActionButton saveDevicesFAB;
    private LinearLayout llSaveDevicesFAB;

    private ListView listview;
    int progress;
    ProgressBar progressBar;

    private FragmentActivity listener;

    private DeviceDatabaseHelper deviceDatabaseHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
            deviceDatabaseHelper = new DeviceDatabaseHelper(listener);
        }
    }

    public JsonFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_json, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        listview = (ListView) listener.findViewById(R.id.listView);

        progressBar = (ProgressBar) listener.findViewById(R.id.ProgressBar);
        progressBar.setVisibility(View.VISIBLE);


        saveDevicesFAB = (FloatingActionButton) listener.findViewById(R.id.saveDevicesFAB);
        llSaveDevicesFAB = (LinearLayout) listener.findViewById(R.id.llSaveDevicesFAB);

        saveDevicesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < saveDeviceList.size(); i++) {
                    deviceDatabaseHelper.addDevice(saveDeviceList.get(i), listener);
                }
                Toast.makeText(listener, "Saving new devices to database", Toast.LENGTH_LONG).show();

            }
        });

        GetDevices getDevices = new GetDevices("Json Data is downloading... Please wait!");
        getDevices.execute("https://connected2.homeseer.com/JSON?request=getstatus&location1=android&user=robert@lange.ca&pass=Myeasslake$");
    }

    public class GetDevices extends AsyncTask<String, Integer, String> {

        private String preJsonToast;

        public void setPreJsonToast(String toastMessage){
            this.preJsonToast = preJsonToast;
        }

        public GetDevices(){

        }
        public GetDevices(String preJsonToast){
            this.preJsonToast = preJsonToast;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            publishProgress(10); //give some indication of progress
            Toast.makeText(listener, preJsonToast, Toast.LENGTH_LONG).show();
        }

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
                        deviceList.add(device);
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


            // create custom adapter and when the view is set, set the id to "checkbox" + ref #
            ListAdapter adapter = new JsonDeviceAdapter(listener); //get layout id

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                    currentDevice.put("name", deviceName.getText().toString());
                    currentDevice.put("status", status.getText().toString());
                    currentDevice.put("location", location.getText().toString());
                    currentDevice.put("ref", ref.getText().toString());

                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        saveDeviceList.remove(currentDevice);
                    } else {
                        checkBox.setChecked(true);
                        saveDeviceList.add(currentDevice);
                    }
                }
            });

            listview.setAdapter(adapter);

            if (progressBar != null) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class JsonDeviceAdapter extends ArrayAdapter<HashMap<String, String>> {


        public JsonDeviceAdapter(Context ctx) {
            super(ctx, 0);
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

            View adapterViewLayout = inflater.inflate(R.layout.adapter_view_json_ayout, null);

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

