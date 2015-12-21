package com.mac.themac.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.ActivityWithBottomActionBar;
import com.mac.themac.activity.FindEvents;
import com.mac.themac.model.Survey;
import com.mac.themac.model.SurveyItem;
import com.mac.themac.model.SurveyResponse;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.ModelCollectionListener;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 12/10/2015.
 */
public class EventSurvey extends FragmentWithTopActionBar implements FBModelListener{
    private static final String ARG_SURVEY_KEY = "survey_key";
    private static final String ARG_IS_MEMBER= "is_member";
    private static final String ARG_POS = "pos";
    private Survey survey;
    private String surveyKey;
    private SurveyResponse surveyResponse = new SurveyResponse();
    @Bind(R.id.list) ListView list;

    public static EventSurvey newInstance(String surveyKey, boolean isMember, int pos){
        EventSurvey frag = new EventSurvey();
        Bundle args = new Bundle();
        args.putString(ARG_SURVEY_KEY, surveyKey);
        args.putBoolean(ARG_IS_MEMBER, isMember);
        args.putInt(ARG_POS, pos);
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
        setHasOptionsMenu(true);
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
            survey.setCollectionUpdateListner(survey.linkedSurveyItems, new ModelCollectionListener() {
                @Override
                public void onCollectionUpdated(List<? extends FBModelObject> linkedCollection, FBModelObject fbObject, boolean isAdded) {
                    mAdapter.notifyDataSetChanged();
                }
            });
            survey.loadLinkedObjects();
            ((FindEvents)getActivity()).setCurrentSurvey(survey);
            if(mAdapter==null){
                mAdapter = new SurveyItemsAdapter(getActivity(), -1, survey.linkedSurveyItems);
                list.setAdapter(mAdapter);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_survey_fragment_continue, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_continue_survey){
            saveAndClose();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndClose(){
        surveyResponse.survey = surveyKey;
        if(surveyResponse.multiChoiceResponses.size()>0){
            for(Long l: surveyResponse.multiChoiceResponses.keySet()){
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
        for(int i = 0; i < mAdapter.getCount(); i++){
            if(mAdapter.getItem(i).type.equals("text")){
                surveyResponse.responses.put(mAdapter.getItem(i).index, ((EditText) list.getChildAt(i).findViewById(R.id.et_answer)).getText().toString());
            } else if(mAdapter.getItem(i).type.equals("checkbox")){
                surveyResponse.responses.put(mAdapter.getItem(i).index, ((CheckBox)list.getChildAt(i).findViewById(R.id.cb_checkbox)).isChecked()?"true":"false");
            }
        }
        ((FindEvents)getActivity()).saveSurveyResponse(surveyResponse, getArguments().getBoolean(ARG_IS_MEMBER), getArguments().getInt(ARG_POS));
        getActivity().getSupportFragmentManager().popBackStack();
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
