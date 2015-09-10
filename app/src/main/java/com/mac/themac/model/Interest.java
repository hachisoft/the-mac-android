package com.mac.themac.model;

import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.ReservationRule;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interest extends FBModelObject{

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
    public List<Post> linkedPosts;
    @JsonIgnore
    public List<Event> linkedEvents;
    @JsonIgnore
    public List<Reservation> linkedReservations;
    @JsonIgnore
    public List<Tidbit> linkedTidbits;
    @JsonIgnore
    public List<Location> linkedLocations;
    @JsonIgnore
    public ReservationRule linkedReservationRule;

    public Interest() {   }

}
