package com.mac.themac.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 12/17/2015.
 */
public class ReservationsAdapter extends ArrayAdapter<Reservation> implements Filterable{

    List<Reservation> allReservations;
    List<Reservation> filteredReservations;

    public ReservationsAdapter(Context context, int resource, List<Reservation> objects) {
        super(context, resource, objects);
        allReservations = objects;
        filteredReservations = objects;
    }

    public int getCount() {
        return filteredReservations.size();
    }

    public Reservation getItem(int position) {
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

                filteredReservations = (List<Reservation>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                final List<Reservation> list = allReservations;

                int count = list.size();
                final ArrayList<Reservation> nlist = new ArrayList<Reservation>(count);

                for (int i = 0; i < count; i++) {
                    Reservation r = list.get(i);
                    if (r.linkedSession != null &&
                            r.linkedSession.endDate.after(Calendar.getInstance().getTime())) {
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
                        getFilter().filter("");
                    }
                }
            }
        });
        if(reservation.linkedLocation == null ||
                reservation.linkedSession == null) {
            reservation.loadLinkedObjects();
        }
        else {
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
            getFilter().filter("");
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

        return convertView;
    }

    private void updateLocationUI(Location location, ReservationViewHolder holder){



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
