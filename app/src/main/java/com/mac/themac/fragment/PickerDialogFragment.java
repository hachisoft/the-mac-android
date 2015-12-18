package com.mac.themac.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mac.themac.R;

import java.util.ArrayList;

/**
 * Created by Bryan on 12/13/2015.
 */
public class PickerDialogFragment extends DialogFragment {
    public static final int SINGLE_SELECT = 1;
    public static final int MULTI_SELECT = 2;

    static final String ARG_MODE = "mode";
    static final String ARG_TITLE = "title";
    static final String ARG_OPTIONS = "options";
    static final String ARG_SELECTED = "selected";

    public interface PickerDialogFragmentListener {
        public void onSingleChoiceItemSelected(int choice);
        public void onMultiChoiceOkPressed(boolean[] selected);
    }

    private PickerDialogFragmentListener listener;

    public static PickerDialogFragment SingleSelectInstance(String title, ArrayList<String> options){
        PickerDialogFragment frag = new PickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, SINGLE_SELECT);
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_OPTIONS, options);
        frag.setArguments(args);
        return frag;
    }

    public static PickerDialogFragment multiSelectInstance(String title, ArrayList<String> options, boolean[] selected){
        PickerDialogFragment frag = new PickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, MULTI_SELECT);
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_OPTIONS, options);
        args.putBooleanArray(ARG_SELECTED, selected);
        frag.setArguments(args);
        return frag;
    }

    public void setListener(PickerDialogFragmentListener listener){
        this.listener = listener;
    }

    private boolean[] multiselectedChoices;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString(ARG_TITLE));
        if(getArguments().getInt(ARG_MODE) == SINGLE_SELECT){
            builder.setItems(getArguments().getStringArrayList(ARG_OPTIONS).toArray(new CharSequence[getArguments().getStringArrayList(ARG_OPTIONS).size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(listener!=null)
                        listener.onSingleChoiceItemSelected(which);
                }
            });
        } else if(getArguments().getInt(ARG_MODE) == MULTI_SELECT){
            multiselectedChoices = getArguments().getBooleanArray(ARG_SELECTED);
            builder.setMultiChoiceItems(getArguments().getStringArrayList(ARG_OPTIONS).toArray(new CharSequence[getArguments().getStringArrayList(ARG_OPTIONS).size()]), multiselectedChoices, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    multiselectedChoices[which] = isChecked;
                }
            });
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(listener != null)
                        listener.onMultiChoiceOkPressed(multiselectedChoices);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        return builder.create();
    }
}
