package com.mac.themac.model.firebase;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FBModelObject {

    protected Map<String , Object> nonMappedProperties = new HashMap<String , Object>();

    @JsonAnyGetter
    public Map<String , Object> any() {
        return nonMappedProperties;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        nonMappedProperties.put(name, value);
    }

    @JsonIgnore
    public void setLinkedObjects(){

    }
}
