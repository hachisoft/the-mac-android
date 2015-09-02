package com.mac.themac.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.themac.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 8/31/2015.
 */
public class CourtButton extends RelativeLayout {
    @Bind(R.id.square) SquaredView squaredView;
    @Bind(R.id.label) TextView label;
    @Bind(R.id.ball) ImageView ball;

    public enum CourtState {
        available, reserved, waitlist, mac_reserved;
    }

    public CourtButton(Context context) {
        super(context);
        init();
    }

    public CourtButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        applyAttrs(attrs);
    }

    public CourtButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        applyAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CourtButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        applyAttrs(attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.court_widget_court_button, this);
        ButterKnife.bind(this);
    }

    private void applyAttrs(AttributeSet attrs){
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CourtButton, 0, 0);
        try{
            String courtNumber = a.getString(R.styleable.CourtButton_courtNumber);
            label.setText(courtNumber!=null?courtNumber:"1");
            int courtcolor = a.getInt(R.styleable.CourtButton_courtColor, -1);
            if(courtcolor!= -1)
                squaredView.setBackgroundColor(courtcolor);
            else
                squaredView.setBackgroundColor(getResources().getColor(R.color.green));
            setGreyed(a.getBoolean(R.styleable.CourtButton_isGreyed, false));
        } catch (Exception e){}
    }

    public void setCourtState(CourtState state){
        switch(state){
            case available:
                squaredView.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case reserved:
                squaredView.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case waitlist:
                squaredView.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case mac_reserved:
                squaredView.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    public void setHasBallMachine(boolean hasBallMachine){
        if(hasBallMachine)
            ball.setVisibility(View.VISIBLE);
        else
            ball.setVisibility(View.INVISIBLE);
    }

    public void setGreyed(boolean isGreyed){
        if(isGreyed){
            this.setAlpha(.1f);
        } else {
            this.setAlpha(1);
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((float) width) / 2);
        RelativeLayout.LayoutParams lp = (LayoutParams) ball.getLayoutParams();
        lp.width = (int) (4f/8f*width);
        lp.height = (int) (4f/8f*width);
        lp.setMargins(0, (int) (-2f/8f*width), 0, 0);
        ball.setLayoutParams(lp);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
