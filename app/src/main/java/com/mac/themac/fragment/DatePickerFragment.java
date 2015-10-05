package com.mac.themac.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Samir on 9/15/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public interface DatePickerFragmentInterface{
        public void onDateSet(int dateType, int year, int month, int day);
    }

    private DatePickerFragmentInterface mListener;

    private final static String ARG_YEAR = "year";
    private final static String ARG_MONTH = "month";
    private final static String ARG_DAY = "day";
    private final static String ARG_DATE_TYPE = "dateType";

    public static DatePickerFragment newInstance(int dateType, int year, int month, int day){
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DATE_TYPE, dateType);
        frag.setArguments(args);
        return frag;
    }


    public DatePickerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DatePickerFragmentInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = getArguments().getInt(ARG_YEAR);
        int month = getArguments().getInt(ARG_MONTH);
        int day = getArguments().getInt(ARG_DAY);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        mListener.onDateSet(getArguments().getInt(ARG_DATE_TYPE), year, month, day);
    }
}
