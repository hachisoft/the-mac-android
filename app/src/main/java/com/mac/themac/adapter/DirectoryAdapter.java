package com.mac.themac.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.model.Department;
import com.mac.themac.model.DirectoryData;
import com.mac.themac.model.EmployeeProfile;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 10/14/2015.
 */
public class DirectoryAdapter extends ArrayAdapter<DirectoryData>{
    class ViewHolder{
        @Bind(R.id.department_label) TextView departmentLabel;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.title) TextView title;
        @Bind(R.id.phone) TextView phone;
        @Bind(R.id.email) TextView email;
        @Bind(R.id.profile) ImageView profile;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
    private ArrayList<DirectoryData> directoryDatas = new ArrayList<>();
    public DirectoryAdapter(Context context) {
        super(context, -1);
    }

    public void addObject(Department dept){
        directoryDatas.add(new DirectoryData(dept));
        sortData();
    }

    public void addObject(EmployeeProfile prof){
        directoryDatas.add(new DirectoryData(prof));
        sortData();
    }

    public void sortData(){
        Collections.sort(directoryDatas, new Comparator<DirectoryData>() {
            @Override
            public int compare(DirectoryData lhs, DirectoryData rhs) {
                return new CompareToBuilder().append(lhs.department, rhs.department).append(!lhs.isDepartment(), !rhs.isDepartment())
                        .append(lhs.last, rhs.last).append(lhs.first, rhs.first).toComparison();
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return directoryDatas.size();
    }

    @Override
    public DirectoryData getItem(int position) {
        return directoryDatas.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(getContext(), R.layout.directory_row, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(getItem(position).isDepartment()){
            holder.departmentLabel.setVisibility(View.VISIBLE);
            holder.departmentLabel.setText(getItem(position).getName());
        } else
            holder.departmentLabel.setVisibility(View.GONE);
        holder.name.setText(getItem(position).getName());
        holder.title.setText(getItem(position).getTitle());
        holder.email.setText(getItem(position).getEmail());
        holder.phone.setText(getItem(position).getPhone());
        Picasso.with(getContext()).load(getItem(position).getImg()).into(holder.profile);
        return convertView;
    }
}
