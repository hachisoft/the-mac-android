package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login {

    public Date created;
    public Boolean isNotProvisioned = true;
    public String provider;
    public String user;

    @JsonIgnore
    public String FBKey;
    @JsonIgnore
    public User linkedUser;

    public Login(){

    }

    public Login(@JsonProperty("created") Date createdOn,
                 @JsonProperty("isNotProvisioned") Boolean _notProvisioned,
                 @JsonProperty("provider") String providerName,
                 @JsonProperty("user") String keyForLinkedUser){

        created = createdOn;
        isNotProvisioned = _notProvisioned;
        provider = providerName;
        user = keyForLinkedUser;
    }


    public Date getCreated() {
        return created;
    }

    public Boolean getIsNotProvisioned() {
        return isNotProvisioned;
    }

    public String getProvider() {
        return provider;
    }

    public String getUser() {
        return user;
    }
}
