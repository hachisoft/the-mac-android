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
import com.mac.themac.model.Event;
import com.mac.themac.model.Registration;
import com.mac.themac.model.firebase.FBDataChangeListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 12/18/2015.
 */
public class RegistrationsAdapter extends ArrayAdapter<Registration> implements Filterable {

    List<Registration> allRegistrations;
    List<Registration> filteredRegistrations;

    public RegistrationsAdapter(Context context, int resource, List<Registration> objects) {
        super(context, resource, objects);
        allRegistrations = objects;
        filteredRegistrations = objects;
    }

    public int getCount() {
        return filteredRegistrations.size();
    }

    public Registration getItem(int position) {
        return filteredRegistrations.get(position);
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

                filteredRegistrations = (List<Registration>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                final List<Registration> list = allRegistrations;

                Collections.sort(allRegistrations, new Comparator<Registration>() {
                    @Override
                    public int compare(Registration lhs, Registration rhs) {
                        if (lhs.linkedEvent != null && rhs.linkedEvent != null) {
                            return lhs.linkedEvent.getStartDate().before(rhs.linkedEvent.getStartDate()) ? -1 : 1;
                        }
                        return 0;
                    }
                });

                int count = list.size();
                final ArrayList<Registration> nlist = new ArrayList<Registration>(count);

                for (int i = 0; i < count; i++) {
                    Registration r = list.get(i);
                    if (r.linkedEvent != null &&
                            r.linkedEvent.getEndDate().after(Calendar.getInstance().getTime())) {
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

        final Registration registration = getItem(position);

        final RegistrationViewHolder holder;
        if(convertView != null){
            holder = (RegistrationViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.registration_list_row, parent, false);
            holder = new RegistrationViewHolder(convertView);
            convertView.setTag(holder);
        }

        final View listView = convertView;
        registration.setModelUpdateListener(new FBDataChangeListener() {
            @Override
            public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
                if (identifier.IsIntendedObject(model, Event.class)) {
                    Event event = (Event) model;
                    holder._registrationTitle.setText(event.getTitle());
                    holder._registrationTime.setText(
                            DateFormat.getDateInstance(DateFormat.SHORT).format(event.getStartDate()) +
                                    "  -  " +
                                    DateFormat.getDateInstance(DateFormat.SHORT).format(event.getEndDate()));

                    if (registration.linkedEvent != null) {
                        holder._registrationViewSwitcher.setDisplayedChild(
                                holder._registrationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
                        getFilter().filter("");
                    }
                }
            }
        });
        if(registration.linkedEvent != null){
            holder._registrationTitle.setText(registration.linkedEvent.getTitle());
            holder._registrationTime.setText(
                    DateFormat.getDateInstance(DateFormat.SHORT).format(registration.linkedEvent.getStartDate()) +
                            "  -  " +
                            DateFormat.getDateInstance(DateFormat.SHORT).format(registration.linkedEvent.getEndDate()));
            holder._registrationViewSwitcher.setDisplayedChild(
                    holder._registrationViewSwitcher.indexOfChild(listView.findViewById(R.id.dataRelativeLayout)));
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

        return convertView;
    }

    public class RegistrationViewHolder {
        @Bind(R.id.registrationViewSwitcher)ViewSwitcher _registrationViewSwitcher;
        @Bind(R.id.indicator)
        ImageView _statusImage;
        @Bind(R.id.tv_title)
        TextView _registrationTitle;
        @Bind(R.id.tv_time) TextView _registrationTime;

        public RegistrationViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}

