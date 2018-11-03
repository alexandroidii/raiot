package ca.raiot.cst2335.raiot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class HelpFragment extends Fragment {

    private TextView textView;
    private StringBuilder text = new StringBuilder();

    public HelpFragment() {
        // Required empty public constructor
    }
    //https://stackoverflow.com/questions/27092827/how-to-read-text-file-from-assets-in-fragment

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String content = null;
        try {

            InputStream file = getActivity().getAssets().open("helptext.txt");
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            content = new String(buffer,"UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i("error","error");
        }


        TextView output = getView().findViewById(R.id.helpHeader);
        output.setText(content);

    }

}