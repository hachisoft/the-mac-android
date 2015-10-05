package com.mac.themac.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

/**
 * Created by Bryan on 9/19/2015.
 */
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
    public interface TimePickerFragmentInterface{
        public void onTimeSet(int timeMode, int hourOfDay, int minute);
    }

    private TimePickerFragmentInterface mInterface;

    private static final String MINUTES = "minutes";
    private static final String TIME_MODE = "timeMode";

    public TimePickerFragment () {}

    public static TimePickerFragment newInstance(int timeMode, long minutes){
        TimePickerFragment frag = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TIME_MODE, timeMode);
        bundle.putLong(MINUTES, minutes);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (TimePickerFragmentInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long minutes = getArguments().getLong(MINUTES);
        return new TimePickerDialog(getActivity(), this, (int)minutes/60, (int)minutes%60, false);
    }

        @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mInterface.onTimeSet(getArguments().getInt(TIME_MODE), hourOfDay, minute);
    }
}