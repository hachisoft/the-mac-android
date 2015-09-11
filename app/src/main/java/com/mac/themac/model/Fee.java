package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fee extends FBModelObject {
    public String type;
    public Double amount;
    public Double cancellationAmount;
    public Double noShowAmount;
    public String description;
    public String event;

    @JsonIgnore
    public Event linkedEvent;

    public Fee() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {
        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events, event);
        }
    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                   FBModelObject modelObject, int secondaryIdentifier) {

        if(targetObjectType.equals(Event.class) &&
                modelObject instanceof Event) {
            linkedEvent = (Event) modelObject;
        }
    }
}
