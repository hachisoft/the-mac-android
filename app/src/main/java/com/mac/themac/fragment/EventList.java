package com.mac.themac.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.activity.FindEvents;
import com.mac.themac.model.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 9/15/2015.
 */
public class EventList extends FragmentWithTopActionBar {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentDay";
    private static final String ARG_LASTDATE = "lastDay";

    private Calendar currentDay;
    private Calendar firstDay;
    private Calendar lastDay;
    private EventAdapter mAdapter;

    @Bind(R.id.lv_event) ListView listView;
    @Bind(R.id.date_label) TextView dateLabel;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Profile.
     */
    public static EventList newInstance(Date param1, Date lastDate) {
        EventList fragment = new EventList();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1.getTime());
        args.putLong(ARG_LASTDATE, lastDate.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    public EventList() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_event_list;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.event_results;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentDay = Calendar.getInstance();
            currentDay.setTime(new Date(getArguments().getLong(ARG_PARAM1)));
            firstDay = Calendar.getInstance();
            firstDay.setTime(new Date(getArguments().getLong(ARG_PARAM1)));
            lastDay = Calendar.getInstance();
            lastDay.setTime(new Date(getArguments().getLong(ARG_LASTDATE)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        updateDateLabel();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mAdapter = new EventAdapter(getActivity(), -1);
        listView.setAdapter(mAdapter);
        getSessionsForDay();
    }

    private void updateDateLabel(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
        dateLabel.setText(format.format(currentDay.getTime()));
    }

    @OnClick(R.id.prev)
    public void onPrev(){
        currentDay.add(Calendar.DAY_OF_YEAR, -1);
        if(currentDay.before(firstDay)){
            currentDay.add(Calendar.DAY_OF_YEAR, 1);
        } else
            getSessionsForDay();
    }

    @OnClick(R.id.next)
    public void onNext(){
        currentDay.add(Calendar.DAY_OF_YEAR, 1);
        if(currentDay.after(lastDay)){
            currentDay.add(Calendar.DAY_OF_YEAR, -1);
        } else
            getSessionsForDay();
    }

    private void getSessionsForDay(){
        mAdapter.clear();
        ((FindEvents)getActivity()).loadSessionsForDate(currentDay);
        updateDateLabel();
    }

    public void addSession(Session session){
        mAdapter.add(session);
        mAdapter.notifyDataSetChanged();
    }

    public class EventAdapter extends ArrayAdapter<Session>{

        public EventAdapter(Context context, int resource) {
            super(context, resource);
        }

        public View getView(int position, View convertView, ViewGroup container){
            View view = convertView;
            if(view == null){
                view = View.inflate(getContext(), R.layout.event_list_row, null);
            }
            ImageView status = (ImageView) view.findViewById(R.id.indicator);
            if(getItem(position).linkedEvent.getStatus().equals("Reserved"))
                status.setImageResource(R.drawable.status_reserved);
            else if(getItem(position).linkedEvent.getStatus().equals("SoldOut"))
                status.setImageResource(R.drawable.status_sold_out);
            else if(getItem(position).linkedEvent.getStatus().equals("Unavailable"))
                status.setImageResource(R.drawable.status_unavailable);
            else if(getItem(position).linkedEvent.getStatus().equals("Waitlist"))
                status.setImageResource(R.drawable.status_waitlist);
            else if(getItem(position).linkedEvent.getStatus().equals("NoRegistrationRequired"))
                status.setImageResource(R.drawable.status_no_registration);
            TextView title = (TextView) view.findViewById(R.id.tv_title);
            TextView time = (TextView) view.findViewById(R.id.tv_time);
            title.setText(getItem(position).linkedEvent.getTitle());
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(getItem(position).date);
            end.setTime(getItem(position).date);
            end.add(Calendar.MINUTE, getItem(position).duration.intValue());
            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            time.setText(format.format(start.getTime()) + " - " + format.format(end.getTime()));
            view.setTag(getItem(position).event);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FindEvents)getActivity()).showEventDetails((String)v.getTag());
                }
            });
            return view;
        }

    }

}
