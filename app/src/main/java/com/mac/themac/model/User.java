package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.util.Pair;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends FBModelObject{

    public Date created;
    public String firstName;
    public String middleName;
    public String lastName;
    public Boolean isAdmin;
    public Boolean isDeptHead;
    public Boolean isJunior;
    public Boolean isLeader;
    public Date lastLogin;
    public String memberNumber;
    public String memberProfile;
    public String memberProfilePublic;
    public String employeeProfile;
    public long numAdvRes;
    public long numGenRes;
    public String primaryMemberNumber;
    public HashMap<String, Boolean> logins;
    public HashMap<String, Boolean> dependents;
    public HashMap<String, Boolean> interests;
    public HashMap<String, Boolean> createdReservations;
    public HashMap<String, Boolean> registrations;
    public HashMap<String, Boolean> reservations;


    @JsonIgnore
    public MemberProfile linkedMemberProfile;
    @JsonIgnore
    public MemberProfilePublic linkedMemberProfilePublic;
    @JsonIgnore
    public EmployeeProfile linkedEmployeeProfile;
    @JsonIgnore
    public List<FBModelObject> linkedDependents = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedLogins = new ArrayList<FBModelObject>() ;
    @JsonIgnore
    public List<FBModelObject> linkedCreatedReservations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedInterests = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedRegistrations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedReservations = new ArrayList<FBModelObject>();

    public User() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(memberProfile != null && !memberProfile.isEmpty()) {
            loadLinkedObject(MemberProfile.class, FirebaseHelper.FBRootContainerNames.memberProfiles, memberProfile);
        }

        if(memberProfilePublic != null && !memberProfilePublic.isEmpty()) {
            loadLinkedObject(MemberProfilePublic.class, FirebaseHelper.FBRootContainerNames.memberProfilePublics, memberProfilePublic);
        }

        if(employeeProfile != null && !employeeProfile.isEmpty()) {
            loadLinkedObject(EmployeeProfile.class, FirebaseHelper.FBRootContainerNames.employeeProfiles, employeeProfile);
        }

        if (logins == null) {
            logins = new HashMap<String, Boolean>();
        }
        if (logins != null) {

            loadLinkedObjects(Login.class, FirebaseHelper.FBRootContainerNames.logins,
                                    logins, linkedLogins);
        }

        if(interests == null){
            interests = new HashMap<String, Boolean>();
        }
        if (interests != null) {
            loadLinkedObjects(Interest.class, FirebaseHelper.FBRootContainerNames.interests,
                                    interests, linkedInterests);
        }

        if(dependents == null){
            dependents = new HashMap<String, Boolean>();
        }
        if (dependents != null) {
            loadLinkedObjects(User.class, FirebaseHelper.FBRootContainerNames.users,
                    dependents, linkedDependents);
        }

        if(registrations == null){
            registrations = new HashMap<String, Boolean>();
        }
        if(registrations != null){
            loadLinkedObjects(Registration.class, FirebaseHelper.FBRootContainerNames.registrations,
                    registrations, linkedRegistrations);
        }

        if(reservations == null){
            reservations = new HashMap<String, Boolean>();
        }
        if(reservations != null){
            loadLinkedObjects(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    reservations, linkedReservations);
        }

        if(createdReservations == null){
            createdReservations = new HashMap<String, Boolean>();
        }
        if(createdReservations != null){
            loadLinkedObjects(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    createdReservations, linkedCreatedReservations);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType, FBModelObject modelObject) {

        if(targetObjectType.equals(MemberProfile.class) &&
                modelObject instanceof MemberProfile) {
            linkedMemberProfile = (MemberProfile) modelObject;
        }
        else if(targetObjectType.equals(EmployeeProfile.class) &&
                modelObject instanceof EmployeeProfile) {
            linkedEmployeeProfile = (EmployeeProfile) modelObject;
        }
        else if(targetObjectType.equals(MemberProfilePublic.class) &&
                modelObject instanceof MemberProfilePublic) {
            linkedMemberProfilePublic = (MemberProfilePublic) modelObject;
        }
    }
}
