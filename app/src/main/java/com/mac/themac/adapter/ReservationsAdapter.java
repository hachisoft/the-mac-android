package com.mac.themac.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Location;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.model.firebase.ModelListener;
import com.mac.themac.utility.FirebaseHelper;

import java.text.DateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 12/17/2015.
 */
public class ReservationsAdapter extends ArrayAdapter<Reservation> {
    public ReservationsAdapter(Context context, int resource, List<Reservation> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Reservation reservation = getItem(position);

        final ReservationViewHolder holder;
        if(convertView != null){
            holder = (ReservationViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_list_row, parent, false);
            holder = new ReservationViewHolder(convertView);
            convertView.setTag(holder);
        }

        final View listView = convertView;
        reservation.setModelUpdateListener(new ModelListener() {
            @Override
            public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
                if(identifier.IsIntendedObject(model, Location.class)){
                    Location location = (Location)model;
                    holder._reservationTitle.setText(location.name);
                    holder._reservationTime.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(reservation.dateReserved));
                    if(reservation.status.equals("Reserved"))
                        holder._statusImage.setImageResource(R.drawable.status_reserved);
                    else if(reservation.status.equals("SoldOut"))
                        holder._statusImage.setImageResource(R.drawable.status_sold_out);
                    else if(reservation.status.equals("Unavailable"))
                        holder._statusImage.setImageResource(R.drawable.status_unavailable);
                    else if(reservation.status.equals("Waitlist"))
                        holder._statusImage.setImageResource(R.drawable.status_waitlist);
                    else if(reservation.status.equals("NoRegistrationRequired"))
                        holder._statusImage.setImageResource(R.drawable.status_no_registration);

                    holder._reservationViewSwitcher.setDisplayedChild(
                            holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
                }
            }
        });
        reservation.loadLinkedObjects();
        return convertView;
    }

    public class ReservationViewHolder {
        @Bind(R.id.reservationViewSwitcher)ViewSwitcher _reservationViewSwitcher;
        @Bind(R.id.indicator) ImageView _statusImage;
        @Bind(R.id.tv_title) TextView _reservationTitle;
        @Bind(R.id.tv_time) TextView _reservationTime;

        public ReservationViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}
