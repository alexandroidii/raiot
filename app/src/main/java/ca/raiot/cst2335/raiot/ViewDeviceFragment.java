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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class ViewDeviceFragment extends Fragment {
    private FloatingActionButton updateDeviceFAB;
    private FragmentActivity listener;
    private String ref;
    private String status;
    private DeviceDatabaseHelper deviceDatabaseHelper;
    private EditText etViewDeviceName;
    private TextView tvRefNumber;
    private EditText etViewLocation;
    private Switch statusSwitch;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
            deviceDatabaseHelper = new DeviceDatabaseHelper(listener);
        }
    }


    public ViewDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_device, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        etViewDeviceName = (EditText) listener.findViewById(R.id.etViewDeviceName);
        tvRefNumber = (TextView) listener.findViewById(R.id.tvRefNumber);
        etViewLocation = (EditText) listener.findViewById(R.id.etViewLocation);
        statusSwitch = (Switch) listener.findViewById(R.id.statusSwitch);

        if (bundle != null) {
            ref = bundle.getString("ref");
            status = bundle.getString("status");
            etViewDeviceName.setText(bundle.getString("name"));
            etViewLocation.setText(bundle.getString("location"));
            tvRefNumber.setText(ref);
        }

        statusSwitch.setChecked(status.equals("On"));

        checkSwitch();

        updateDeviceFAB = (FloatingActionButton) listener.findViewById(R.id.updateDeviceFAB);

        updateDeviceFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // implement code to save fields to the database.
                HashMap<String, String> updatedDevice = new HashMap<>();

                Toast.makeText(listener, "Updating Device", Toast.LENGTH_SHORT).show();

                updatedDevice.put("name", etViewDeviceName.getText().toString());
                updatedDevice.put("ref", tvRefNumber.getText().toString());
                updatedDevice.put("location", etViewLocation.getText().toString());
                updatedDevice.put("status", status);
                if (deviceDatabaseHelper.updateDevice(updatedDevice, listener)) {
                    Toast.makeText(listener, "Failed to update device", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(listener, "Device successfully updated", Toast.LENGTH_SHORT).show();
                }


                //Robert, If there is a string we can send to the server to update the devices name and location we could use that here.

            }
        });


    }

    private void checkSwitch() {
        /*  source: https://android--code.blogspot.com/2015/08/android-switch-button-listener.html
            Author: Unknown
            Date: 2015-08-22
        */
        statusSwitch = (Switch) listener.findViewById(R.id.statusSwitch);

        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        JsonFragment jsonFragment = new JsonFragment(listener);
                                                        String state;
                                                        if (isChecked) {

                                                            state = "On";
                                                            devHome();
                                                        } else {
                                                            state = "Off";
                                                            devHome();

                                                        }
                                                        JsonFragment.GetDevices jsonRequest = jsonFragment.new GetDevices("Changing status of light id " + ref + " to " + state + "... Please wait!");
                                                        jsonRequest.execute("https://connected2.homeseer.com/JSON?request=controldevicebylabel&ref=" + ref + "&label=" + state + "&user=robert@lange.ca&pass=Myeasslake$");

                                                    }
                                                }
        );
    }

    // handle to direct back to device list after selection change
    private void devHome() {
        listener.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new DevicesFragment())
                .addToBackStack(null)
                .commit();
    }
}
