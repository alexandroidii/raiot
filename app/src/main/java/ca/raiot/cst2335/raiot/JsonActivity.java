package ca.raiot.cst2335.raiot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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

public class JsonActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "1234 JsonActivity";

    ArrayList<HashMap<String, String>> deviceList = new ArrayList<>();
    private ListView listview;
    int progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        listview = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        progressBar.setVisibility(View.VISIBLE);


        GetDevices getDevices = new GetDevices();
        getDevices.execute();

    }

    public class GetDevices extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(JsonActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://www.lange.ca:8080/JSON?user=adroid&password=androiduser&request=getstatus&location1=android";
            String jsonStr = sh.makeServiceCall(url);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.i(ACTIVITY_NAME, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
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

            ListAdapter adapter = new SimpleAdapter(JsonActivity.this, deviceList, R.layout.adapter_view_layout, new String[]{"name", "status", "location", "ref"},
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

