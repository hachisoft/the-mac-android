package com.mac.themac.model;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryan on 9/1/2015.
 */
public class ReservationRules {
    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    long advancedWindowLength, generalWindowLength, reservationChangeDeadline, saturdayPlayBegins,
    saturdayPlayEnds, sessionLength, sundayPlayBegins, sundayPlayEnds, timeRegistrationOpens, weekdayPlayBegins, weekdayPlayEnds;
    String key, interest;
    boolean allowBackToBack;

    public ReservationRules(){

    }

    public long getAdvancedWindowLength() {
        return advancedWindowLength;
    }

    public long getGeneralWindowLength() {
        return generalWindowLength;
    }

    public long getReservationChangeDeadline() {
        return reservationChangeDeadline;
    }

    public long getSaturdayPlayBegins() {
        return saturdayPlayBegins;
    }

    public long getSaturdayPlayEnds() {
        return saturdayPlayEnds;
    }

    public long getSessionLength() {
        return sessionLength;
    }

    public long getSundayPlayBegins() {
        return sundayPlayBegins;
    }

    public long getSundayPlayEnds() {
        return sundayPlayEnds;
    }

    public long getTimeRegistrationOpens() {
        return timeRegistrationOpens;
    }

    public long getWeekdayPlayBegins() {
        return weekdayPlayBegins;
    }

    public long getWeekdayPlayEnds() {
        return weekdayPlayEnds;
    }

    public String getKey() {
        return key;
    }

    public String getInterest() {
        return interest;
    }

    public boolean isAllowBackToBack() {
        return allowBackToBack;
    }

    //    public enum FirebaseFieldName {
//        advancedWindowLength, allowBackToBack, generalWindowLength, interest,
//        reservationChangeDeadline, saturdayPlayBegins, saturdayPlayEnds, sessionLength,
//        sundayPlayBegins, sundayPlayEnds, timeRegistrationOpens, weekdayPlayBegins, weekdayPlayEnds
//    };

//    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
//    static {
//        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();
//
//        //Add all firebase field Type mappings here (no object/container, only fields)
//        aMap.put(FirebaseFieldName.advancedWindowLength.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.allowBackToBack.name(), Field.FirebaseSupportedTypes.Boolean);
//        aMap.put(FirebaseFieldName.generalWindowLength.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.interest.name(), Field.FirebaseSupportedTypes.String);
//        aMap.put(FirebaseFieldName.reservationChangeDeadline.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.saturdayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.saturdayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.sessionLength.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.sundayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.sundayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.timeRegistrationOpens.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.weekdayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
//        aMap.put(FirebaseFieldName.weekdayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
//        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
//    };
//
//    //Add all firebase object(containers of other fields) keys here
//    public enum FirebaseContainerName {
//    };
//
//    String key;
//
//    public ReservationRules(DataSnapshot dataSnapshot){
//        super();
////        key = (String) dataSnapshot.getValue();
//    }
//
//    @Override
//    protected Map<String, Field.FirebaseSupportedTypes> fieldTypeMap() {
//        return FirebaseFieldTypeMap;
//    }
//
//    @Override
//    protected Field.FirebaseSupportedTypes fieldType(String fieldName) {
//        return FirebaseFieldTypeMap.get(fieldName);
//    }
//
//    public long getAdvancedWindowLength() {
//        return (long)fieldValue(FirebaseFieldName.advancedWindowLength.name());
//    }
//
//    public Boolean getAllowBackToBack() {
//        return (Boolean)fieldValue(FirebaseFieldName.allowBackToBack.name());
//    }
//
//    public long getGeneralWindowLength() {
//        return (long)fieldValue(FirebaseFieldName.generalWindowLength.name());
//    }
//
//    public long getReservationChangeDeadline(){
//        return (long)fieldValue(FirebaseFieldName.reservationChangeDeadline.name());
//    }
//
//    public long getWeekdayPlayBegins(){
//        return (long)fieldValue(FirebaseFieldName.weekdayPlayBegins.name());
//    }
//
//    public long getWeekdayPlayEnds(){
//        return (long)fieldValue(FirebaseFieldName.weekdayPlayEnds.name());
//    }
//
//    public long getSaturdayPlayBegins(){
//        return (long)fieldValue(FirebaseFieldName.saturdayPlayBegins.name());
//    }
//
//    public long getSaturdayPlayEnds(){
//        return (long)fieldValue(FirebaseFieldName.saturdayPlayEnds.name());
//    }
//
//    public long getSessionLength(){
//        return (long)fieldValue(FirebaseFieldName.sessionLength.name());
//    }
//
//    public long getSundayPlayBegins(){
//        return (long)fieldValue(FirebaseFieldName.sundayPlayBegins.name());
//    }
//
//    public long getSundayPlayEnds(){
//        return (long)fieldValue(FirebaseFieldName.sundayPlayEnds.name());
//    }
//
//    public long getTimeRegistrationOpens(){
//        return (long)fieldValue(FirebaseFieldName.timeRegistrationOpens.name());
//    }
}
