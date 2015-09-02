package com.mac.themac.model.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Date;

/**
 * Created by Samir on 7/20/2015.
 */
public class Field {

    public enum FBSupportedTypes {
        Boolean, Integer, String, Date, Weekday
    };

    private Object convertToJavaType(Object val) throws Exception{

        Object retVal = null;
        if(mType == null)
            throw new Exception("Field Type not specified for " + mName);
        switch (mType){
            case Boolean:
            case Integer:
            case String:
                //Basic types are directly convertible
                retVal = val;
                break;
            case Date:
                retVal = new Date((int)val);
                break;
            case Weekday:
                break;
        }

        return retVal;
    }



    private String mName;
    private Object mValue; //Always stored as correct Java Type
    private FBSupportedTypes mType;
    private boolean mIsEdited = true;

    public Object value() {
        return mValue;
    }

    public void setValue(Object mValue) {
        this.mValue = mValue;
        _setEdited();
    }

    public String name() {
        return mName;
    }

    public Field(String name, Object value, FBSupportedTypes fieldType) {
        mName = name;
        mValue = value;
        mType = fieldType;
        _setEdited();
    }

    public Field(String name, FBSupportedTypes fieldType) {
        mName = name;
        mValue = null;
        mType = fieldType;
        _setEdited();
    }

    private void _setEdited(){
        mIsEdited = true;
    }

    private void _setSaved(){
        mIsEdited = false;
    }

    public boolean isEdited(){
        return mIsEdited;
    }

    public void loadServerValue(DataSnapshot parentDataSnapshot) throws Exception{

        if(parentDataSnapshot.getValue() != null){//parent bucket has children

            if(parentDataSnapshot.hasChild(mName)){ //parent bucket has this field
                DataSnapshot child = parentDataSnapshot.child(mName);
                mValue = convertToJavaType( child.getValue() );
            }
        }
        _setSaved();
    }

    public void updateServerValue(Firebase parentDataRef, DataSnapshot parentDataSnapshot) {
        updateServerValue(parentDataRef, parentDataSnapshot, true);
    }

    public void updateServerValue(Firebase parentDataRef, DataSnapshot parentDataSnapshot, boolean createIfNotExist) {

        if(parentDataSnapshot.getValue() == null) {//user doesn't exist, add new
            if (createIfNotExist) {
                parentDataRef.setValue(this);
            }
        }
        else {//parent data exists, update value if any changes

            //Update any existing key-mapping
            if(parentDataSnapshot.hasChild(mName)){
                DataSnapshot child = parentDataSnapshot.child(mName);
                if(!child.getValue().equals(mValue)){
                    parentDataRef.child(mName).setValue(mValue);
                }
            }
            else {//Add new key-mapping if needed
                if(createIfNotExist)
                    parentDataRef.child(mName).setValue(mValue);
            }
        }
        _setSaved();
    }
}
