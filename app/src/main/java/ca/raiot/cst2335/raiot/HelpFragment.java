package ca.raiot.cst2335.raiot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String content = null;
        try {

            InputStream is = getActivity().getAssets().open("HelpText.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer,"UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i("error","error");
        }


        TextView output = getView().findViewById(R.id.helpFragment);
        output.setText(content);

    }

}

//
//    @Override
//    public void onViewCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        try {
//            InputStream is = getContext().getAssets().open("test.txt");
//            int size = is.available();
//            // Read the entire asset into a local byte buffer.
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            content = new String(buffer,"UTF-8");
//        } catch (IOException e) {
//            // Should never happen!
//            throw new RuntimeException(e);
//        }
//        TextView output= (TextView)getView().findViewById(R.id.fragment_help);
//        output.setText(content);
//    }
//}
