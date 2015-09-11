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
public class Directory extends FBModelObject {

    public HashMap<String, Boolean> employees;
    public HashMap<String, Boolean> members;
    public HashMap<String, Boolean> groups;

    @JsonIgnore
    public List<FBModelObject> linkedEmployees;
    @JsonIgnore
    public List<FBModelObject> linkedMembers;
    @JsonIgnore
    public List<FBModelObject> linkedGroups;

    public Directory() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if (employees == null) {
            employees = new HashMap<String, Boolean>();
        }
        if (employees != null) {

            loadLinkedObjects(EmployeeProfile.class, FirebaseHelper.FBRootContainerNames.employeeProfiles,
                    employees, linkedEmployees);
        }

        if(members == null){
            members = new HashMap<String, Boolean>();
        }
        if (members != null) {
            loadLinkedObjects(MemberProfilePublic.class, FirebaseHelper.FBRootContainerNames.memberProfilePublics,
                    members, linkedMembers);
        }

        if(groups == null){
            groups = new HashMap<String, Boolean>();
        }
        if (groups != null) {
            loadLinkedObjects(Group.class, FirebaseHelper.FBRootContainerNames.groups,
                    groups, linkedGroups);
        }

    }
}
