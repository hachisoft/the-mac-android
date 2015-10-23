package com.mac.themac.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.model.ParkingProjection;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 10/23/2015.
 */
public class ParkingWidgetDay extends LinearLayout {
    @Bind(R.id.month)
    TextView month;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.early)
    TextView early;
    @Bind(R.id.midday)
    TextView midDay;
    @Bind(R.id.late)
    TextView late;


    public ParkingWidgetDay(Context context) {
        super(context);
        init();
    }

    public ParkingWidgetDay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParkingWidgetDay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParkingWidgetDay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.parking_widget_day, this);
        ButterKnife.bind(this);
    }

    public void setParkingProjection(ParkingProjection pp) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEE");
        month.setText(monthFormat.format(pp.getDate()));
        date.setText(dateFormat.format(pp.getDate()));
        midDay.setText(weekdayFormat.format(pp.getDate()));
        setStatusColor(early, pp.getStatusEarly());
        setStatusColor(midDay, pp.getStatusMidDay());
        setStatusColor(late, pp.getStatusLate());
    }

    private void setStatusColor(TextView tv, String status){
        if(status.equals("Low"))
            tv.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(status.equals("Medium"))
            tv.setBackgroundColor(getResources().getColor(R.color.green));
        else if(status.equals("High"))
            tv.setBackgroundColor(getResources().getColor(R.color.red));
    }

}
