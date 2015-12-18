package com.mac.themac.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.firebase.client.FirebaseError;
import com.hachisoft.macapi.ThemacClient;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.ActivityWithBottomActionBar;
import com.mac.themac.model.Event;
import com.mac.themac.model.Survey;
import com.mac.themac.model.SurveyItem;
import com.mac.themac.model.SurveyResponse;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 12/10/2015.
 */
public class EventSurvey extends FragmentWithTopActionBar implements FBModelListener{
    private static final String ARG_SURVEY_KEY = "survey_key";
    private Survey survey;
    private String surveyKey;
    private SurveyResponse surveyResponse = new SurveyResponse();
    @Bind(R.id.list) ListView list;
    //TODO: need to add code to parse boolean array from multichoice response into string

    @OnClick(R.id.btnSave)
    public void onSaveClicked(){
        if(surveyResponse.multiChoiceResponses.size()>0){
            for(Long l: surveyResponse.multiChoiceResponses.keySet()){
                //TODO need to grab boolean array and generate JSON string
                String jsonString = "{";
                int responses = 0;
                for(int i = 0; i < surveyResponse.multiChoiceResponses.get(l).length; i++){
                    SurveyItem si = null;
                    for(SurveyItem s: survey.linkedSurveyItems){
                        if(s.index == l){
                            si = s;
                            break;
                        }
                    }
                    if(surveyResponse.multiChoiceResponses.get(l)[i]){
                        if(responses >= 1)
                            jsonString +=",";
                        jsonString += Integer.toString(i) + ": '";
                        if(si!=null)
                            jsonString += si.options.get(i) + "'";
                        responses ++;
                    }
                }
                if(responses > 0) {
                    jsonString += "}";
                    surveyResponse.getResponses().put(l, jsonString);
                }
            }
        }
        //TODO: need to save surveyResponse to server
    }

    public static EventSurvey newInstance(String surveyKey){
        EventSurvey frag = new EventSurvey();
        Bundle args = new Bundle();
        args.putString(ARG_SURVEY_KEY, surveyKey);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            surveyKey = getArguments().getString(ARG_SURVEY_KEY);
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_event_survey;
    }

    @Override
    protected int getTitleResourceId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
        fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(Survey.class), surveyKey);
//        if(mAdapter == null) {
//            mAdapter = new SurveyItemsAdapter(getActivity(), -1);
//            list.setAdapter(mAdapter);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityWithBottomActionBar activity = (ActivityWithBottomActionBar) mListener;
        if(activity != null){
            activity.showTopActionBar();
        }
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, Survey.class)){
            survey = (Survey) model;
            survey.loadLinkedObjects();
            if(mAdapter==null){
                mAdapter = new SurveyItemsAdapter(getActivity(), -1, survey.linkedSurveyItems);
                list.setAdapter(mAdapter);
                survey.setSurveyItemsAdapter(mAdapter);
            }
            ((ActivityWithBottomActionBar) mListener).setTitle(survey.getName());
        }
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(FBModelIdentifier identifier, FirebaseError error) {
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNullData(FBModelIdentifier identifier, String key) {
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onException(Exception x) {
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    private SurveyItemsAdapter mAdapter;

    public class SurveyItemsAdapter extends ArrayAdapter<SurveyItem> {

        class SurveyItemRowHolder implements PickerDialogFragment.PickerDialogFragmentListener{
            @Bind(R.id.tv_question) TextView tvQuestion;
            @Bind(R.id.et_answer) EditText etAnswer;
            @Bind(R.id.cb_checkbox) CheckBox cbCheckBox;
            SurveyItem item;

            SurveyItemRowHolder(View view, SurveyItem item){
                ButterKnife.bind(this, view);
                this.item = item;
                toView();
            }

            public void setItem(SurveyItem item){
                this.item = item;
                toView();
            }

            private void toView(){
                tvQuestion.setText(item.question);
                if(item.type.equals("text")){
                    cbCheckBox.setVisibility(View.GONE);
                    etAnswer.setVisibility(View.VISIBLE);
                    setEditTextAnswer();
                    etAnswer.setOnClickListener(null);
                } else if(item.type.equals("checkbox")){
                    cbCheckBox.setVisibility(View.VISIBLE);
                    etAnswer.setVisibility(View.GONE);
                } else if(item.type.equals("choose-one")){
                    cbCheckBox.setVisibility(View.GONE);
                    etAnswer.setVisibility(View.VISIBLE);
                    setEditTextAnswer();
                    etAnswer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSingleChoicePicker();
                        }
                    });
                } else if(item.type.equals("choose-many")){
                    cbCheckBox.setVisibility(View.GONE);
                    etAnswer.setVisibility(View.VISIBLE);
                    setEditTextAnswer();
                    etAnswer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMultiChoicePicker();
                        }
                    });
                }
            }

            private void showSingleChoicePicker(){
                PickerDialogFragment fragment = PickerDialogFragment.SingleSelectInstance(item.question, new ArrayList<String>(item.options.values()));
                fragment.setListener(this);
                fragment.show(getActivity().getSupportFragmentManager(), "select");
            }

            private void showMultiChoicePicker(){
                boolean[] choices;
                if(surveyResponse.multiChoiceResponses.containsKey(item.index)){
                    choices = surveyResponse.multiChoiceResponses.get(item.index);
                } else {
                    choices = new boolean[item.options.size()];
                }
                PickerDialogFragment frag = PickerDialogFragment.multiSelectInstance(item.question, new ArrayList<String>(item.options.values()), choices);
                frag.setListener(this);
                frag.show(getActivity().getSupportFragmentManager(), "select");
            }

            private void setEditTextAnswer(){
                if(item.type.equals("choose-many")){
                    if(surveyResponse.multiChoiceResponses.containsKey(item.index)){
                        String text = new String();
                        int addCount = 0;
                        for(int i = 0; i < surveyResponse.multiChoiceResponses.get(item.index).length; i++){
                            if(surveyResponse.multiChoiceResponses.get(item.index)[i]){
                                if(addCount > 0)
                                    text += ", ";
                                text += item.options.get(i);
                                addCount ++;
                            }
                        }
                        etAnswer.setText(text);
                    } else {
                        etAnswer.setText("");
                    }
                }else if(surveyResponse.getResponses().containsKey(item.index)){
                    etAnswer.setText(surveyResponse.getResponses().get(item.index));
                } else {
                    etAnswer.setText("");
                }
                etAnswer.setEnabled(item.type.equals("text"));
            }

            @Override
            public void onSingleChoiceItemSelected(int choice) {
                surveyResponse.getResponses().put(item.index, item.options.get(choice));
            }

            @Override
            public void onMultiChoiceOkPressed(boolean[] selected) {
                surveyResponse.multiChoiceResponses.put(item.index, selected);
            }
        }

        public SurveyItemsAdapter(Context context, int resource, List<SurveyItem> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SurveyItemRowHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.survey_row, null);
                holder = new SurveyItemRowHolder(convertView, getItem(position));
                convertView.setTag(holder);
            } else {
                holder = (SurveyItemRowHolder) convertView.getTag();
            }
            holder.setItem(getItem(position));
            return convertView;
        }
    }
}
