package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule extends FBModelObject {

    public long nsRuleId;
    public String name;
    public String frequency;
    public String ruleAppliesTo;
    public String weekdays;
    public String memberOutcome;
    public String departmentOutcome;
    public long startTime;
    public long endTime;
    public long allowed;
    public long guestAllowed;
    public String impactIncludes;
    public String impactExcludes;
    public String requiredPartnerClassificationsInclude;
    public String requiredPartnerClassificationsExclude;
    public String type;
    public String warningMessage;
    public String restrictionMessage;
    public String interest;
    public HashMap<String, Boolean> locations;

    @JsonIgnore
    public Interest linkedInterest;
    @JsonIgnore
    public List<FBModelObject> linkedLocations = new ArrayList<FBModelObject>();

    public Rule() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(interest != null && !interest.isEmpty()) {
            loadLinkedObject(Interest.class, FirebaseHelper.FBRootContainerNames.interests,
                    interest, linkedInterest);
        }

        if(locations == null){
            locations = new HashMap<String, Boolean>();
        }
        if (locations != null) {
            loadLinkedObjects(Location.class, FirebaseHelper.FBRootContainerNames.locations,
                    locations, linkedLocations);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, Interest.class)) {
            linkedInterest = (Interest) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedInterest = null;
        linkedLocations.clear();

    }
}
