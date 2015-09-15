package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberProfilePublic extends FBModelObject{

    public String prefix;
    public String firstName;
    public String middleName;
    public String lastName;
    public String suffix;
    public String email;
    public String phone;
    public String photoId;
    public String user;
    public HashMap<String, Boolean> receivedInvitations;

    @JsonIgnore
    public User linkedUser;
    public List<FBModelObject> linkedReceivedInvitations;

    public MemberProfilePublic() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

        if(receivedInvitations == null){
            receivedInvitations = new HashMap<String, Boolean>();
        }
        if (receivedInvitations != null) {
            loadLinkedObjects(Invitation.class, FirebaseHelper.FBRootContainerNames.invitations,
                    receivedInvitations, linkedReceivedInvitations);
        }

    }

    @Override
    @JsonIgnore
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
        linkedReceivedInvitations.clear();

    }
}
