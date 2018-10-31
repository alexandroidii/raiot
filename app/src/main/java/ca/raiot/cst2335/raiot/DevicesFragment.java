package ca.raiot.cst2335.raiot;

import android.animation.Animator;
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


public class DevicesFragment extends Fragment {

    /*
    https://resocoder.com/2017/11/03/floating-action-menu-no-3rd-party-library-quick-tutorial-xamarin-android-code/

    https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu


    */
    private static boolean isFabOpen;
    private FloatingActionButton autoAddFab;
    private FloatingActionButton manualAddFab;
    private FloatingActionButton addDeviceFab;
    private LinearLayout llManualAddFab;
    private LinearLayout llAutoAddFab;
    private LinearLayout llAddFAB;
    private FragmentActivity listener;

    public DevicesFragment() {
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
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addDeviceFab = (FloatingActionButton) listener.findViewById(R.id.addDeviceFAB);
        autoAddFab = (FloatingActionButton) listener.findViewById(R.id.autoAddDeviceFAB);
        manualAddFab = (FloatingActionButton) listener.findViewById(R.id.manualAddDeviceFAB);
        llManualAddFab = (LinearLayout) listener.findViewById(R.id.llManualAddDeviceFAB);
        llAutoAddFab = (LinearLayout) listener.findViewById(R.id.llAutoAddDeviceFAB);
        llAddFAB = (LinearLayout) listener.findViewById(R.id.llAddFAB);

        addDeviceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    hideFABMenu();
                } else {
                    showFabMenu();
                }

            }
        });

        autoAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Robert, you can call your new activity that will
                 automatically add the devices here.  Copy what I did in the
                 manualAddFab.setOnClickListener.*/

                //loadFragment(new <YOUR CLASS HERE>())
                listener.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new JsonFragment())
                        .commit();

                hideFABMenu();

            }
        });

        manualAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new AddDeviceFragment())
                        .commit();
                hideFABMenu();
            }
        });
    }

    private void showFabMenu() {
        isFabOpen = true;
        /*
        https://github.com/ajaydewari/FloatingActionButtonMenu/blob/master/app/src/main/java/com/ajaysinghdewari/floatingactionbuttonmenu/activities/MainActivity.java
        */
        llAutoAddFab.setVisibility(View.VISIBLE);
        llManualAddFab.setVisibility(View.VISIBLE);

        addDeviceFab.animate().rotation(360f);
        llAutoAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_105))
                .rotation(0f);
        llManualAddFab.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_55))
                .rotation(0f);

    }

    private void hideFABMenu() {
        isFabOpen = false;

        addDeviceFab.animate().rotation(-360f);
        llAutoAddFab.animate()
                .translationY(0);
        llManualAddFab.animate()
                .translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isFabOpen) {
                    llAutoAddFab.setVisibility(View.GONE);
                    llManualAddFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
