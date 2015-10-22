package com.mac.themac.model;

import com.mac.themac.fragment.Directory;

/**
 * Created by Bryan on 10/14/2015.
 */
public class DirectoryData implements IDirectoryData {
    String name;
    String title;
    String phone;
    String email;
    String img;
    boolean isDepartment;
    public String first;
    public String last;
    public String department;

    public DirectoryData(Department dept){
        name = dept.getName();
        title = dept.getTitle();
        phone = dept.getPhone();
        email = dept.getEmail();
        img = dept.getImg();
        isDepartment = dept.isDepartment();
        first = "";
        last = "";
        department = dept.getName();
    }

    public DirectoryData(EmployeeProfile prof){
        name = prof.getName();
        title = prof.getTitle();
        phone = prof.getPhone();
        email = prof.getEmail();
        img = prof.getImg();
        isDepartment = prof.isDepartment();
        first = prof.firstName;
        last = prof.lastName;
        department = prof.department;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public boolean isDepartment() {
        return isDepartment;
    }
}
