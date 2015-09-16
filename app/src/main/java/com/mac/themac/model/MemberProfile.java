package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberProfile extends FBModelObject{
    public String number;
    public String primaryMemberNumber;
    public String prefix;
    public String firstName;
    public String lastName;
    public String middleName;
    public String suffix;
    public String gender;
    public Date dob;
    public String businessPhone;
    public String businessExt;
    public String homePhone;
    public String cellPhone;
    public String fax;
    public String email;
    public String maritalStatus;
    public String title;
    public String classification;
    public String confidential;
    public Date memberSince;
    public String type;
    public String status;
    public String photoId;
    public String thumbId;
    public String occupation;
    public String user;
    public HashMap<String, Boolean> addresses;
    public HashMap<String, Boolean>  vehicles;
    public HashMap<String, Boolean> emergencyContacts;

    public MemberProfile() {
    }

    @JsonIgnore
    public User linkedUser;
    @JsonIgnore
    public List<FBModelObject> linkedAddresses;
    @JsonIgnore
    public List<FBModelObject> linkedVehicles;
    @JsonIgnore
    public List<FBModelObject> linkedEmergencyContacts;

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

        if (addresses == null) {
            addresses = new HashMap<String, Boolean>();
        }
        if (addresses != null) {

            loadLinkedObjects(Address.class, FirebaseHelper.FBRootContainerNames.addresses,
                    addresses, linkedAddresses);
        }

        if(vehicles == null){
            vehicles = new HashMap<String, Boolean>();
        }
        if (vehicles != null) {
            loadLinkedObjects(Vehicle.class, FirebaseHelper.FBRootContainerNames.vehicles,
                    vehicles, linkedVehicles);
        }

        if(emergencyContacts == null){
            emergencyContacts = new HashMap<String, Boolean>();
        }
        if (emergencyContacts != null) {
            loadLinkedObjects(EmergencyContact.class, FirebaseHelper.FBRootContainerNames.emergencyContacts,
                    emergencyContacts, linkedEmergencyContacts);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedUser = (User) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedUser = null;
        linkedAddresses.clear();
    }

}
