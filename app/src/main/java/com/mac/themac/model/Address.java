package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.HashMap;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends FBModelObject {
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;
    public String city;
    public String state;
    public String country;
    public String zip;
    public String company;
    public String memberProfile;

    @JsonIgnore
    public MemberProfile linkedMemberProfile;

    public Address() {
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
