package com.mac.themac.model.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * Created by Samir on 9/14/2015.
 */
public class FBQueryIdentifier {

    public enum OrderBy{
        Child, Key, Value, Priority
    };
    public enum Qualifier{
        NotSpecified, limitToFirst, limitToLast, startAt, endAt, equalTo
    }

    public OrderBy orderByType;
    public String orderByValue;
    public Qualifier qualifierType;
    public String qualifierValue;


    public FBQueryIdentifier(OrderBy orderByType) {
        this.orderByType = orderByType;
        this.orderByValue = "";
        this.qualifierType = Qualifier.NotSpecified;
        this.qualifierValue = null;
    }

    public FBQueryIdentifier(OrderBy orderByType, String orderByValue) {
        this.orderByType = orderByType;
        this.orderByValue = orderByValue;
        this.qualifierType = Qualifier.NotSpecified;
        this.qualifierValue = null;
    }

    public FBQueryIdentifier(OrderBy orderByType, String orderByValue, Qualifier qualifierType, String qualifierValue) {
        this.orderByType = orderByType;
        this.orderByValue = orderByValue;
        this.qualifierType = qualifierType;
        this.qualifierValue = qualifierValue;

    }

    public FBQueryIdentifier(OrderBy orderByType, String orderByValue, Qualifier qualifierType, Integer qualifierValue) {
        this.orderByType = orderByType;
        this.orderByValue = orderByValue;
        this.qualifierType = qualifierType;
        this.qualifierValue =  qualifierValue.toString();

    }

    public FBQueryIdentifier(OrderBy orderByType, String orderByValue, Qualifier qualifierType) {
        this.orderByType = orderByType;
        this.orderByValue = orderByValue;
        this.qualifierType = qualifierType;
        this.qualifierValue =  null;

    }

    public Boolean IsIndededQuery(OrderBy orderByType){
        return this.orderByType == orderByType &&
                this.orderByValue.length()==0 &&
                this.qualifierType == Qualifier.NotSpecified &&
                this.qualifierValue == null;
    }

    public Boolean IsIndededQuery(OrderBy orderByType,
                                         String orderByValue){
        return this.orderByType == orderByType && 
                this.orderByValue.compareTo(orderByValue)==0 &&
                this.qualifierType == Qualifier.NotSpecified && 
                this.qualifierValue == null;
    }

    public Boolean IsIndededQuery(OrderBy orderByType,
                                         String orderByValue, Qualifier qualifierType,
                                         String qualifierValue){
        return this.orderByType == orderByType && 
                this.orderByValue.compareTo(orderByValue)==0 &&
                this.qualifierType == qualifierType && 
                this.qualifierValue.compareTo(qualifierValue)==0;
    }

    public Boolean IsIndededQuery(OrderBy orderByType,
                                         String orderByValue, Qualifier qualifierType,
                                         Integer qualifierValue){
        return this.orderByType == orderByType && 
                this.orderByValue.compareTo(orderByValue)==0 &&
                this.qualifierType == qualifierType && 
                this.qualifierValue.compareTo(qualifierValue.toString())==0;
    }

    public Boolean IsIndededQuery(OrderBy orderByType,
                                         String orderByValue, Qualifier qualifierType){
        return this.orderByType == orderByType && 
                this.orderByValue.compareTo(orderByValue)==0 &&
                this.qualifierType == qualifierType && 
                this.qualifierValue==null;
    }

    public Query getQuery(Firebase fbRef) {
        switch (orderByType){
            case Child:
                return getQuery(fbRef.orderByChild(orderByValue));
            case Key:
                return getQuery(fbRef.orderByKey());
            case Value:
                return getQuery(fbRef.orderByValue());
            case Priority:
                return getQuery(fbRef.orderByPriority());
            default:
                return fbRef.orderByKey();
        }
    }

    private Query getQuery(Query fbQuery){
        switch (qualifierType){
            case limitToFirst:
                return fbQuery.limitToFirst(Integer.getInteger(qualifierValue));
            case limitToLast:
                return fbQuery.limitToLast(Integer.getInteger(qualifierValue));
            case startAt:
                return fbQuery.startAt(qualifierValue);
            case endAt:
                return fbQuery.endAt(qualifierValue);
            case equalTo:
                return fbQuery.equalTo(qualifierValue);
            case NotSpecified:
            default:
                return fbQuery;
        }
    }
}
