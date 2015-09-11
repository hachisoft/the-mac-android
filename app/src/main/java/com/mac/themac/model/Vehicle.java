package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle extends FBModelObject {

    public String make;
    public String model;
    public String licensePlate;
    public String state;
    public String color;
    public String permitNumber;
    public Date permitIssued;
    public String memberProfile;

    @JsonIgnore
    public MemberProfile linkedMemberProfile;

    public Vehicle() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(memberProfile != null && !memberProfile.isEmpty()) {
            loadLinkedObject(MemberProfile.class, FirebaseHelper.FBRootContainerNames.memberProfiles, memberProfile);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                   FBModelObject modelObject, int secondaryIdentifier) {

        if(targetObjectType.equals(MemberProfile.class) &&
                modelObject instanceof MemberProfile) {
            linkedMemberProfile = (MemberProfile) modelObject;
        }
    }
}
