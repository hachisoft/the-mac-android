package com.mac.themac.model;

import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.ReservationRule;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.List;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interest extends FBModelObject{

    public Boolean hasReservation;
    public String name;
    public List<Pair<String, Boolean>> posts;
    public List<Pair<String, Boolean>> events;
    public List<Pair<String, Boolean>> reservations;
    public List<Pair<String, Boolean>> tidbits;
    //public List<Pair<String, Boolean>> faqs;
    //galleryImages: DS.hasMany('gallery-image', {async: true}),
    public List<Pair<String, Boolean>> reservationLocations;
    public List<Pair<String, Boolean>> reservationRule;
    //rules: DS.hasMany('rule', {async: true}),
    //isAbout: Em.computed.equal('name','About')

    @JsonIgnore
    public String FBKey;
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
    public List<ReservationRule> linkedReservationRules;

    public Interest() {
    }

    public Boolean getHasReservation() {
        return hasReservation;
    }

    public String getName() {
        return name;
    }

    public List<Pair<String, Boolean>> getPosts() {
        return posts;
    }

    public List<Pair<String, Boolean>> getEvents() {
        return events;
    }

    public List<Pair<String, Boolean>> getReservations() {
        return reservations;
    }

    public List<Pair<String, Boolean>> getTidbits() {
        return tidbits;
    }

    public List<Pair<String, Boolean>> getReservationLocations() {
        return reservationLocations;
    }

    public List<Pair<String, Boolean>> getReservationRule() {
        return reservationRule;
    }
}
