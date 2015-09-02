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
public class ReservationRules extends Container {
    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)

    public enum FirebaseFieldName {
        advancedWindowLength, allowBackToBack, generalWindowLength, interest,
        reservationChangeDeadline, saturdayPlayBegins, saturdayPlayEnds, sessionLength,
        sundayPlayBegins, sundayPlayEnds, timeRegistrationOpens, weekdayPlayBegins, weekdayPlayEnds
    };

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.advancedWindowLength.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.allowBackToBack.name(), Field.FirebaseSupportedTypes.Boolean);
        aMap.put(FirebaseFieldName.generalWindowLength.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.interest.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.reservationChangeDeadline.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.saturdayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.saturdayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.sessionLength.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.sundayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.sundayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.timeRegistrationOpens.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.weekdayPlayBegins.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.weekdayPlayEnds.name(), Field.FirebaseSupportedTypes.Long);
        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FirebaseContainerName {
    };

    String key;

    public ReservationRules(DataSnapshot dataSnapshot){
        super();
        key = dataSnapshot.getKey();
    }

    @Override
    protected Map<String, Field.FirebaseSupportedTypes> fieldTypeMap() {
        return FirebaseFieldTypeMap;
    }

    @Override
    protected Field.FirebaseSupportedTypes fieldType(String fieldName) {
        return FirebaseFieldTypeMap.get(fieldName);
    }
}
