package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends FBModelObject {
    public Address() {
    }
}
