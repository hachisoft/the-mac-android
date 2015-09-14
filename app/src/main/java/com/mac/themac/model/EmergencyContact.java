package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmergencyContact extends FBModelObject {
    public String firstName;
    public String lastName;
    public String phone;
    public String email;
    public String memberProfile;

    @JsonIgnore
    public MemberProfile linkedMemberProfile;

    public EmergencyContact() {
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
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, MemberProfile.class)) {
            linkedMemberProfile = (MemberProfile) modelObject;
        }
    }
}
