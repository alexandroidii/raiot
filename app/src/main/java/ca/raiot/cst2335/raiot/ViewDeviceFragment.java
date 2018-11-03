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
import android.widget.LinearLayout;


public class ViewDeviceFragment extends Fragment {
    private FloatingActionButton toggleDevicestatusFAB;
    private LinearLayout llToggleDevicestatusFAB;
    private FragmentActivity listener;

    private String ref;
    private String name;
    private String location;
    private String status;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
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


        if (bundle != null) {
            ref = bundle.getString("ref");
            name = bundle.getString("name");
            location = bundle.getString("location");
            status = bundle.getString("status");
        }


        toggleDevicestatusFAB = (FloatingActionButton) listener.findViewById(R.id.toggleDeviceStatusFAB);

        toggleDevicestatusFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // implement code to save fields to the database.
                JsonFragment jsonFragment = new JsonFragment();

                String state;

                if (status.equals("on")){
                    state = "off";
                }else{
                    state = "on";
                }

                    JsonFragment.GetDevices jsonRequest = jsonFragment.new GetDevices();
                jsonRequest.execute(new String[]{"https://connected2.homeseer.com/JSON?request=controldevicebylabelandref=" + ref + "&label=" + state
                        + "&user=robert@lange.ca&pass=Myeasslake$",
                        "Changing status of light id " + ref + " to " + state + "... Please wait!"});
            }
        });


    }

}
