package com.mac.themac.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.ActivityWithBottomActionBar;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 7/8/2015.
 */
public abstract class FragmentWithTopActionBar extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected FirebaseHelper mFBHelper;
    protected abstract int getFragmentLayoutId();
    protected abstract int getTitleResourceId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFBHelper = TheMACApplication.theApp.getFirebaseHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getFragmentLayoutId(), container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityWithBottomActionBar activity = (ActivityWithBottomActionBar) mListener;
        if(activity != null){
            if(getTitleResourceId()>0)
                activity.setTitle(getTitleResourceId());
            else
                activity.setTitle("");
            activity.showTopActionBar();
        }
    }

    @Override
    public void onPause() {
        ActivityWithBottomActionBar activity = (ActivityWithBottomActionBar) mListener;
        if(activity != null){
//            activity.hideTopActionBar();
        }
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
