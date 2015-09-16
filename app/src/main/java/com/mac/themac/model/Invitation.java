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
public class Invitation extends FBModelObject{

    public String message;
    public String event;
    public String recipient;

    @JsonIgnore
    public Event linkedEvent;
    @JsonIgnore
    public MemberProfilePublic linkedRecipient;

    public Invitation() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events,
                    event, linkedEvent);
        }

        if(recipient != null && !recipient.isEmpty()) {
            loadLinkedObject(MemberProfilePublic.class,
                    FirebaseHelper.FBRootContainerNames.memberProfilePublics,
                    recipient, linkedRecipient);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, MemberProfilePublic.class)) {
            linkedRecipient = (MemberProfilePublic) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Event.class)){
            linkedEvent = (Event)modelObject;
        }

    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedEvent = null;
        linkedRecipient = null;

    }
}
