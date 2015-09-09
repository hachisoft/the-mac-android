package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import android.util.Pair;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public Date created;
    public List<Pair<String, Boolean>> dependents;
    public String firstName;
    public String middleName;
    public String lastName;
    public Boolean isAdmin;
    public Boolean isDeptHead;
    public Boolean isJunior;
    public Boolean isLeader;
    public Date lastLogin;
    public List<Pair<String, Boolean>> logins;
    public String memberNumber;
    public String memberProfile;
    public String memberProfilePublic;
    public String employeeProfile;
    public long numAdbRes;
    public long numGenRes;
    public String primaryMemberNumber;
    public List<Pair<String, Boolean>> interests;
    public List<Pair<String, Boolean>> createdReservations;


    @JsonIgnore
    public String FBKey;
    @JsonIgnore
    public List<User> linkedDependents;
    @JsonIgnore
    public List<Login> linkedLogins;
    @JsonIgnore
    public MemberProfile linkedMemberProfile;
    @JsonIgnore
    public MemberProfilePublic linkedMemberProfilePublic;
    @JsonIgnore
    public EmployeeProfile linkedEmployeeProfile;
    @JsonIgnore
    public List<Reservation> linkedReservations;

}
