package com.mac.themac.model.firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 7/20/2015.
 */
abstract public class Container {

    private boolean mIsEdited = false;
    protected HashMap<String, Field> mFields = new HashMap<String, Field>();
    protected HashMap<String, Container> mDirectContainers = new HashMap<String, Container>();
    protected HashMap<String, Container> mLinkedContainers = new HashMap<String, Container>();
    protected Firebase mFirebaseRef = null;

    public Container (){
        // empty default constructor, necessary for Firebase to be able to deserialize
    }

    public Container(Firebase firebaseRef) {

        mFirebaseRef = firebaseRef;

        for(Map.Entry<String, Field.FBSupportedTypes> kvp : fieldTypeMap().entrySet()){
            Field field = new Field(kvp.getKey(), kvp.getValue());
            mFields.put(kvp.getKey(), field);
        }
    }

    protected abstract Map<String, Field.FBSupportedTypes> fieldTypeMap();
    protected abstract Field.FBSupportedTypes fieldType(String fieldName);

    protected Object fieldValue(String fieldName){

        if(mFields.containsKey(fieldName)){
            return mFields.get(fieldName).value();
        }
        else{
            return null;
        }
    }

    protected void setFieldValue(String fieldName, Object fieldValue){
        setFieldValue(fieldName, fieldValue, fieldType(fieldName));
    }

    protected void setFieldValue(String fieldName, Object fieldValue, Field.FBSupportedTypes fieldType){

        if(mFields.containsKey(fieldName)) {
            Field field = mFields.get(fieldName);
            field.setValue(fieldValue);
        }
        else{
            mFields.put(fieldName, new Field(fieldName, fieldValue, fieldType));
        }
        setEdited();
    }

    protected void setEdited(){
        mIsEdited = true;
    }

    protected void setSaved(){
        mIsEdited = false;
    }

    protected void setFirebaseRef(Firebase firebaseRef){
        mFirebaseRef = firebaseRef;
    }

    public void loadServerData(Firebase firebaseRef, DataSnapshot dataSnapshot) {

        if(dataSnapshot.getValue() == null)//Data doesn't exist, don't do anything
            return;
        else {//Data exists, load values to local model

            //load fields
            for(Field field : mFields.values()){

                try {
                    //load values from server returned snapshot
                    if(dataSnapshot.hasChild(field.name())){
                        DataSnapshot child = dataSnapshot.child(field.name());
                        setFieldValue(field.name(), child.getValue());
                    }
                }
                catch(Exception x){
                    Log.d(this.getClass().getName(), x.getMessage());
                }
            }

            //load direct containers
            for(HashMap.Entry<String, Container> kvp : mDirectContainers.entrySet()){

                try {
                    if (dataSnapshot.hasChild(kvp.getKey())) {
                        DataSnapshot child = dataSnapshot.child(kvp.getKey());
                        if (kvp.getValue() != null)
                            kvp.getValue().loadServerData(firebaseRef.child(kvp.getKey()), child);
                    }
                }
                catch(Exception x){
                    Log.d(this.getClass().getName(), x.getMessage());
                }
            }

            //load linked containers
            for(HashMap.Entry<String, Container> kvp : mLinkedContainers.entrySet()){

                try {
                    if (dataSnapshot.hasChild(kvp.getKey())) {
                        DataSnapshot child = dataSnapshot.child(kvp.getKey());
                        if (kvp.getValue() != null)
                            kvp.getValue().loadServerData(firebaseRef.child(kvp.getKey()), child);
                    }
                }
                catch(Exception x){
                    Log.d(this.getClass().getName(), x.getMessage());
                }
            }
        }
        setSaved();
    }

    private void saveFields(Firebase firebaseRef, DataSnapshot dataSnapshot){
        //save fields
        for(Field field : mFields.values()){
            try {
                //Update any existing key-mapping
                if(dataSnapshot.hasChild(field.name())){
                    DataSnapshot child = dataSnapshot.child(field.name());
                    if(!child.getValue().equals(this.fieldValue(field.name()))){
                        firebaseRef.child(field.name()).setValue(this.fieldValue(field.name()));
                    }
                }
                else {//Add new key-mapping if needed
                    firebaseRef.child(field.name()).setValue(this.fieldValue(field.name()));
                }
            }
            catch(Exception x){
                Log.d(this.getClass().getName(), x.getMessage());
            }
        }
    }

    private void saveContainers(Firebase firebaseRef, DataSnapshot dataSnapshot){
        //save containers
        for(HashMap.Entry<String, Container> kvp : mDirectContainers.entrySet()){
            try {
                //Update any existing key-mapping
                if(dataSnapshot.hasChild(kvp.getKey())){
                    DataSnapshot child = dataSnapshot.child(kvp.getKey());
                    if(kvp.getValue() != null){
                        kvp.getValue().saveServerData(firebaseRef.child(kvp.getKey()), child);
                    }
                }
                else {//Add new key-mapping if needed
                    if(kvp.getValue() != null)
                        kvp.getValue().saveServerData(firebaseRef.child(kvp.getKey()), null);
                }
            }
            catch(Exception x){
                Log.d(this.getClass().getName(), x.getMessage());
            }
        }
    }

    public void saveServerData(Firebase firebaseRef, DataSnapshot dataSnapshot) {

        if(dataSnapshot == null|| dataSnapshot.getValue() == null) {//data doesn't exist, add new
            firebaseRef.setValue(this);

            //saveContainers(firebaseRef, dataSnapshot);
        }
        else {

            saveFields(firebaseRef, dataSnapshot);
            //saveContainers(firebaseRef, dataSnapshot);

        }

        setSaved();
    }

}
