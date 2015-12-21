package com.mac.themac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mac.themac.R;
import com.mac.themac.activity.ActivityWithBottomActionBar;
import com.mac.themac.activity.MyAccount;
import com.mac.themac.fragment.AccountStatement;
import com.mac.themac.fragment.CourtReservation;
import com.mac.themac.fragment.EventDetails;
import com.mac.themac.fragment.ReservationDetails;
import com.mac.themac.fragment.Reservations;
import com.mac.themac.model.Event;
import com.mac.themac.model.Location;
import com.mac.themac.model.Registration;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.ModelListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 12/18/2015.
 */
public class MyReservationsAdapter extends ArrayAdapter<FBModelObject> implements Filterable {

    List<FBModelObject> allReservations;
    List<FBModelObject> filteredReservations;

    public MyReservationsAdapter(Context context, int resource, List<FBModelObject> objects) {
        super(context, resource, objects);
        allReservations = objects;
        filteredReservations = objects;
    }

    public int getCount() {
        return filteredReservations.size();
    }

    public FBModelObject getItem(int position) {
        return filteredReservations.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredReservations = (List<FBModelObject>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                final List<FBModelObject> list = allReservations;

                Collections.sort(allReservations, new Comparator<FBModelObject>() {
                    @Override
                    public int compare(FBModelObject lhs, FBModelObject rhs) {
                        Date lhsDate, rhsDate;
                        if(lhs instanceof Reservation && ((Reservation) lhs).linkedSession != null){
                            lhsDate = ((Reservation)lhs).linkedSession.date;
                        }
                        else if(lhs instanceof Registration && ((Registration)lhs).linkedEvent != null){
                            lhsDate = ((Registration)lhs).linkedEvent.getStartDate();
                        }
                        else {
                            lhsDate = null;
                        }

                        if(rhs instanceof Reservation && ((Reservation) rhs).linkedSession != null){
                            rhsDate = ((Reservation)rhs).linkedSession.date;
                        }
                        else if(rhs instanceof Registration && ((Registration)rhs).linkedEvent != null){
                            rhsDate = ((Registration)rhs).linkedEvent.getStartDate();
                        }
                        else {
                            rhsDate = null;
                        }
                        
                        if (lhsDate != null && rhsDate != null) {
                            return lhsDate.before(rhsDate) ? -1 : 1;
                        }
                        else {
                            return 0;
                        }
                    }
                });

                int count = list.size();
                final ArrayList<FBModelObject> nlist = new ArrayList<FBModelObject>(count);

                Date now = Calendar.getInstance().getTime();
                for (int i = 0; i < count; i++) {
                    FBModelObject r = list.get(i);
                    if(r instanceof Reservation &&
                            ((Reservation) r).linkedSession != null &&
                            ((Reservation) r).linkedSession.endDate.after(now)){
                        nlist.add(r);
                    }
                    else if(r instanceof Registration &&
                            ((Registration)r).linkedEvent != null &&
                            ((Registration)r).linkedEvent.getEndDate().after(now)){
                        nlist.add(r);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }
        };

        return filter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final FBModelObject r = getItem(position);

        if(r instanceof Reservation){
            return getReservationView(position, convertView, parent, (Reservation)r);
        }
        else if(r instanceof Registration){
            return getRegistrationView(position, convertView, parent, (Registration)r);
        }
        else{
            return convertView;
        }

    }

    private View getRegistrationView(int position, View convertView, ViewGroup parent, final Registration registration) {

        final MyReservationViewHolder holder;
        if(convertView != null){
            holder = (MyReservationViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myreservation_list_row, parent, false);
            holder = new MyReservationViewHolder(convertView);
            convertView.setTag(holder);
        }

        final View listView = convertView;
        registration.setModelUpdateListener(new ModelListener() {
            @Override
            public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
                if (identifier.IsIntendedObject(model, Event.class)) {
                    Event event = (Event) model;
                    holder._reservationTitle.setText(event.getTitle());
                    holder._reservationTime.setText(
                            DateFormat.getDateInstance(DateFormat.SHORT).format(event.getStartDate()) +
                                    "  -  " +
                                    DateFormat.getDateInstance(DateFormat.SHORT).format(event.getEndDate()));

                    if (registration.linkedEvent != null) {
                        holder._reservationViewSwitcher.setDisplayedChild(
                                holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
                        getFilter().filter("");
                    }
                }
            }
        });
        if(registration.linkedEvent != null){
            holder._reservationTitle.setText(registration.linkedEvent.getTitle());
            holder._reservationTime.setText(
                    DateFormat.getDateInstance(DateFormat.SHORT).format(registration.linkedEvent.getStartDate()) +
                            "  -  " +
                            DateFormat.getDateInstance(DateFormat.SHORT).format(registration.linkedEvent.getEndDate()));
            holder._reservationViewSwitcher.setDisplayedChild(
                    holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
        }


        if (registration.status.equals("Reserved"))
            holder._statusImage.setImageResource(R.drawable.status_reserved);
        else if (registration.status.equals("SoldOut"))
            holder._statusImage.setImageResource(R.drawable.status_sold_out);
        else if (registration.status.equals("Unavailable"))
            holder._statusImage.setImageResource(R.drawable.status_unavailable);
        else if (registration.status.equals("Waitlist"))
            holder._statusImage.setImageResource(R.drawable.status_waitlist);
        else if (registration.status.equals("NoRegistrationRequired"))
            holder._statusImage.setImageResource(R.drawable.status_no_registration);

        try {
            final MyAccount activity = (MyAccount) getContext();
            if(activity != null){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventDetails frag = EventDetails.newInstance(registration.event);
                        activity.showFragment(frag, R.id.myAccountContainer,
                                ActivityWithBottomActionBar.TransitionEffect.Slide_from_right);
                    }
                });
            }
        }
        catch(Exception x){

        }

        return convertView;
    }

    private View getReservationView(final int position, View convertView, ViewGroup parent, final Reservation reservation) {

        final MyReservationViewHolder holder;
        if(convertView != null){
            holder = (MyReservationViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myreservation_list_row, parent, false);
            holder = new MyReservationViewHolder(convertView);
            convertView.setTag(holder);
        }

        final View listView = convertView;
        reservation.setModelUpdateListener(new ModelListener() {
            @Override
            public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
                if (identifier.IsIntendedObject(model, Location.class)) {
                    Location location = (Location) model;
                    holder._reservationTitle.setText(location.name);

                    if (reservation.linkedSession != null && reservation.linkedLocation != null) {
                        holder._reservationViewSwitcher.setDisplayedChild(
                                holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
                    }
                } else if (identifier.IsIntendedObject(model, Session.class)) {
                    Session session = (Session) model;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    holder._reservationTime.setText(
                            DateFormat.getDateInstance(DateFormat.SHORT).format(session.date) +
                                    " " + timeFormat.format(session.date) + " - " +
                                    " " + timeFormat.format(session.endDate));

                    if (reservation.linkedSession != null && reservation.linkedLocation != null) {
                        holder._reservationViewSwitcher.setDisplayedChild(
                                holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
                    }
                }
            }
        });
        if(reservation.linkedLocation != null &&
                reservation.linkedSession != null){

            if(reservation.linkedLocation != null){
                holder._reservationTitle.setText(reservation.linkedLocation.name);
            }
            if(reservation.linkedSession != null){
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                holder._reservationTime.setText(
                        DateFormat.getDateInstance(DateFormat.SHORT).format(reservation.linkedSession.date) +
                                " " + timeFormat.format(reservation.linkedSession.date) + " - " +
                                " " + timeFormat.format(reservation.linkedSession.endDate));
            }
            holder._reservationViewSwitcher.setDisplayedChild(
                    holder._reservationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
        }


        if (reservation.status.equals("Reserved"))
            holder._statusImage.setImageResource(R.drawable.status_reserved);
        else if (reservation.status.equals("SoldOut"))
            holder._statusImage.setImageResource(R.drawable.status_sold_out);
        else if (reservation.status.equals("Unavailable"))
            holder._statusImage.setImageResource(R.drawable.status_unavailable);
        else if (reservation.status.equals("Waitlist"))
            holder._statusImage.setImageResource(R.drawable.status_waitlist);
        else if (reservation.status.equals("NoRegistrationRequired"))
            holder._statusImage.setImageResource(R.drawable.status_no_registration);

        try {
            final MyAccount activity = (MyAccount) getContext();
            if(activity != null){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReservationDetails frag = ReservationDetails.newInstance(reservation.FBKey);
                        activity.showFragment(frag, R.id.myAccountContainer,
                                ActivityWithBottomActionBar.TransitionEffect.Slide_from_right);
                    }
                });
            }
        }
        catch(Exception x){

        }

        return convertView;
    }

    public class MyReservationViewHolder {
        @Bind(R.id.reservationViewSwitcher)ViewSwitcher _reservationViewSwitcher;
        @Bind(R.id.indicator)
        ImageView _statusImage;
        @Bind(R.id.tv_title)
        TextView _reservationTitle;
        @Bind(R.id.tv_time) TextView _reservationTime;

        public MyReservationViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}