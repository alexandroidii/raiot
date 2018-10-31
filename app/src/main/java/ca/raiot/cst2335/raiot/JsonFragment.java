package ca.raiot.cst2335.raiot;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "1234 JsonFragment";

    ArrayList<HashMap<String, String>> deviceList = new ArrayList<>();
    private ListView listview;
    int progress;
    ProgressBar progressBar;

    private FragmentActivity listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    public JsonFragment(){

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


        GetDevices getDevices = new GetDevices();
        getDevices.execute();

    }

    public class GetDevices extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(listener, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://connected2.homeseer.com/JSON?request=getstatus&location1=android&user=robert@lange.ca&pass=Myeasslake$";
           String jsonStr = sh.makeServiceCall(url);
          //  String jsonStr = "{\"Name\":\"HomeSeer Devices\",\"Version\":\"1.0\",\"Devices\":[{\"ref\":990,\"name\":\"A_DiningRoom Lights\",\"location\":\"Android\",\"location2\":\"Lighting\",\"value\":0,\"status\":\"Off\",\"device_type_string\":\"\",\"last_change\":\"\\/Date(-62135578800000)\\/\",\"relationship\":0,\"hide_from_view\":false,\"associated_devices\":[],\"device_type\":{\"Device_API\":0,\"Device_API_Description\":\"No API\",\"Device_Type\":0,\"Device_Type_Description\":\"Type 0\",\"Device_SubType\":0,\"Device_SubType_Description\":\"\"},\"device_image\":\"\",\"UserNote\":\"\",\"UserAccess\":\"Any\",\"status_image\":\"/images/HomeSeer/status/off.gif\",\"voice_command\":\"\",\"misc\":4864},{\"ref\":991,\"name\":\"A_FamilyRoom Lights\",\"location\":\"Android\",\"location2\":\"Lighting\",\"value\":100,\"status\":\"On\",\"device_type_string\":\"\",\"last_change\":\"\\/Date(1540823155629)\\/\",\"relationship\":0,\"hide_from_view\":false,\"associated_devices\":[],\"device_type\":{\"Device_API\":0,\"Device_API_Description\":\"No API\",\"Device_Type\":0,\"Device_Type_Description\":\"Type 0\",\"Device_SubType\":0,\"Device_SubType_Description\":\"\"},\"device_image\":\"\",\"UserNote\":\"\",\"UserAccess\":\"Any\",\"status_image\":\"/images/HomeSeer/status/on.gif\",\"voice_command\":\"\",\"misc\":4864},{\"ref\":992,\"name\":\"A_Kitchen Lights\",\"location\":\"Android\",\"location2\":\"Lighting\",\"value\":100,\"status\":\"On\",\"device_type_string\":\"\",\"last_change\":\"\\/Date(1540940163675)\\/\",\"relationship\":0,\"hide_from_view\":false,\"associated_devices\":[],\"device_type\":{\"Device_API\":0,\"Device_API_Description\":\"No API\",\"Device_Type\":0,\"Device_Type_Description\":\"Type 0\",\"Device_SubType\":0,\"Device_SubType_Description\":\"\"},\"device_image\":\"\",\"UserNote\":\"\",\"UserAccess\":\"Any\",\"status_image\":\"/images/HomeSeer/status/on.gif\",\"voice_command\":\"\",\"misc\":4864},{\"ref\":989,\"name\":\"A_LivingRoom Lights\",\"location\":\"Android\",\"location2\":\"Lighting\",\"value\":0,\"status\":\"Off\",\"device_type_string\":\"\",\"last_change\":\"\\/Date(-62135578800000)\\/\",\"relationship\":0,\"hide_from_view\":false,\"associated_devices\":[],\"device_type\":{\"Device_API\":0,\"Device_API_Description\":\"No API\",\"Device_Type\":0,\"Device_Type_Description\":\"Type 0\",\"Device_SubType\":0,\"Device_SubType_Description\":\"\"},\"device_image\":\"\",\"UserNote\":\"\",\"UserAccess\":\"Any\",\"status_image\":\"/images/HomeSeer/status/off.gif\",\"voice_command\":\"\",\"misc\":4864}]}";
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
//
//                        // tmp hash map for single device
                        HashMap<String, String> device = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
                        device.put("name", name);
                        device.put("status", status);
                        device.put("location", location);
                        device.put("ref", ref_id);
//                        // adding device to device list
                        deviceList.add(device);
                        //   publishProgress(progress);
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
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
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

            ListAdapter adapter = new SimpleAdapter(listener, deviceList, R.layout.adapter_view_layout, new String[]{"name", "status", "location", "ref"},
                    new int[]{R.id.device, R.id.status, R.id.location, R.id.ref}); //get layout id

            listview.setAdapter(adapter);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        @Override
        public int getCount() {
            return deviceList.size();
        }



    }
}

