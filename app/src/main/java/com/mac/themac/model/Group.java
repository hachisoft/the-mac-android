package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends FBModelObject{

    public String name;
    public HashMap<String, Boolean> members;
    public HashMap<String, Boolean>leaders;

    @JsonIgnore
    public List<FBModelObject> linkedMembers;
    @JsonIgnore
    public List<FBModelObject> linkedLeaders;

    public Group() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if (members == null) {
            members = new HashMap<String, Boolean>();
        }
        if (members != null) {

            loadLinkedObjects(MemberProfile.class, FirebaseHelper.FBRootContainerNames.memberProfiles,
                    members, linkedMembers);
        }

        if(leaders == null){
            leaders = new HashMap<String, Boolean>();
        }
        if (leaders != null) {
            loadLinkedObjects(MemberProfile.class, FirebaseHelper.FBRootContainerNames.memberProfiles,
                    leaders, linkedLeaders);
        }

    }
}
