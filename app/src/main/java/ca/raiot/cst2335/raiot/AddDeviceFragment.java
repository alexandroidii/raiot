package ca.raiot.cst2335.raiot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
    private String snackBarSaveManual = "Your manual device addition was saved was successful";
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

                String deviceName = etDeviceName.getText().toString();
                String refnumber = etRefNumber.getText().toString();
                String location = etLocation.getText().toString();
                String status = etStatus.getText().toString();


                if (TextUtils.isEmpty(deviceName) ||
                        TextUtils.isEmpty(refnumber) ||
                        TextUtils.isEmpty(location) ||
                        TextUtils.isEmpty(status)) {
                    Toast.makeText(listener, "Please fill in the fields", Toast.LENGTH_LONG).show();
                } else {
                    newDevice.put(getString(R.string.deviceNameKey), deviceName);
                    newDevice.put(getString(R.string.deviceRefKey), refnumber);
                    newDevice.put(getString(R.string.deviceLocationKey), location);
                    newDevice.put(getString(R.string.deviceStatusKey), status);

                    if (deviceDatabaseHelper.addDevice(newDevice, listener)) {
                        Toast.makeText(listener, "Error saving new device", Toast.LENGTH_LONG).show();
                    } else {
                       // Toast.makeText(listener, "new Device Saved", Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar.make(listener.findViewById(R.id.manualAddDeviceFAB),  snackBarSaveManual, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        // take us back to devhome
                        listener.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new DevicesFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }


            }
        });

        Spinner deviceTypeDropDown = listener.findViewById(R.id.typeSpinner);

        String[] types = new String[]{"Choose a Type", "Light", "Fan", "Garage Door", "Camera"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listener, android.R.layout.simple_list_item_1, types);

        deviceTypeDropDown.setAdapter(adapter);

    }
}
