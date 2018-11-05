package ca.raiot.cst2335.raiot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;


public class AddDeviceFragment extends Fragment {

    private FloatingActionButton saveDeviceFAB;
    private LinearLayout llSaveFAB;
    private FragmentActivity listener;
    private DeviceDatabaseHelper deviceDatabaseHelper;
    private EditText etDeviceName;
    private EditText etRefNumber;
    private EditText etLocation;
    private EditText etStatus;

    public AddDeviceFragment() {
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
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveDeviceFAB = (FloatingActionButton) listener.findViewById(R.id.saveDeviceFAB);
        llSaveFAB = (LinearLayout) listener.findViewById(R.id.llSaveFAB);

        etDeviceName = (EditText) listener.findViewById(R.id.etDeviceName);
        etRefNumber = (EditText) listener.findViewById(R.id.etRefNumber);
        etLocation = (EditText) listener.findViewById(R.id.etLocation);
        etStatus = (EditText) listener.findViewById(R.id.etStatus);




        saveDeviceFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> newDevice = new HashMap<>();

                newDevice.put(getString(R.string.deviceNameKey), etDeviceName.getText().toString());
                newDevice.put(getString(R.string.deviceRefKey), etRefNumber.getText().toString());
                newDevice.put(getString(R.string.deviceLocationKey), etLocation.getText().toString());
                newDevice.put(getString(R.string.deviceStatusKey), etStatus.getText().toString());
                if(deviceDatabaseHelper.addDevice(newDevice, listener)){
                    Toast.makeText(listener, "new Device Saved", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(listener, "Error saving new device", Toast.LENGTH_LONG).show();
                }


            }
        });

        Spinner deviceTypeDropDown = listener.findViewById(R.id.typeSpinner);

        String[] types = new String[]{"Choose a Type", "Light", "Fan", "Garage Door", "Camera"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listener, android.R.layout.simple_list_item_1, types);

        deviceTypeDropDown.setAdapter(adapter);

    }
}
