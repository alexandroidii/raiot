package ca.raiot.cst2335.raiot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class AddDeviceFragment extends Fragment {

    FragmentActivity listener;

    public AddDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
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

        Spinner deviceTypeDropDown = listener.findViewById(R.id.spinner);

        String[] types = new String[]{"Light", "Fan", "Garage Door", "Camera"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listener, android.R.layout.simple_list_item_1, types);

        deviceTypeDropDown.setAdapter(adapter);

    }
}
