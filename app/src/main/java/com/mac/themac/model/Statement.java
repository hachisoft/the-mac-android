package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 10/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statement extends FBModelObject {
    public Statement() {
    }

    public String memberNumber;
    public String user;
    public Date date;
    public Date dueDate;
    public double balanceForward;
    public double currentBalance;
    public double over30;
    public double over60;
    public double over90;
    public double over120;
    public HashMap<String, Boolean> transactions;
    //card: DS.belongsTo('card', {async: true}),
    //url: DS.attr('string')


    @JsonIgnore
    public User linkedUser;
    @JsonIgnore
    public List<Transaction> linkedTransactions = new ArrayList<Transaction>();

    @JsonIgnore
    @Override
    public void loadLinkedObjects() {

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class,
                    FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

        if (transactions == null) {
            transactions = new HashMap<String, Boolean>();
        }
        if (transactions != null) {

            loadLinkedObjects(Transaction.class, FirebaseHelper.FBRootContainerNames.transactions,
                    transactions, linkedTransactions);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedUser = (User) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {
        linkedUser = null;
        linkedTransactions.clear();
    }
}
