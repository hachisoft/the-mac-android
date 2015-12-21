package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interest extends FBModelObject {

    public Boolean hasReservation;
    public String name;
    public HashMap<String, Boolean> posts;
    public HashMap<String, Boolean> events;
    public HashMap<String, Boolean> reservations;
    public HashMap<String, Boolean> tidbits;
    //public HashMap<String, Boolean> faqs;
    //galleryImages: DS.hasMany('gallery-image', {async: true}),
    public HashMap<String, Boolean> reservationLocations;
    public String reservationRule;
    //rules: DS.hasMany('rule', {async: true}),
    //isAbout: Em.computed.equal('name','About')


    @JsonIgnore
    public List<FBModelObject> linkedPosts = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedEvents = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedReservations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedTidbits = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedLocations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public ReservationRule linkedReservationRule;

    public Interest() {   }

    @Override
    public void loadLinkedObjects() {

    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedPosts.clear();
        linkedEvents.clear();
        linkedReservations.clear();
        linkedTidbits.clear();
        linkedLocations.clear();

        linkedReservationRule = null;
    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }

}
