package com.mac.themac.model.firebase;

import com.mac.themac.model.firebase.FBModelObject;

/**
 * Created by Samir on 9/11/2015.
 */
public class FBModelIdentifier {
    public Class<? extends FBModelObject> getIntendedClass() {
        return intendedClass;
    }

    public int getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    private Class<? extends FBModelObject> intendedClass;
    private int secondaryIdentifier;

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = 0;
    }

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass, int secondaryIdentifier) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = secondaryIdentifier;

    }

    public boolean IsIntendedObject(FBModelObject modelObject, Class<? extends FBModelObject> classType, int secondaryIdentifier){
        return this.intendedClass.equals(classType) &&
                this.secondaryIdentifier == secondaryIdentifier &&
                classType.equals(modelObject.getClass());
    }

    public boolean IsIntendedObject(FBModelObject modelObject, Class<? extends FBModelObject> classType){
        return this.intendedClass.equals(modelObject.getClass()) &&
                this.secondaryIdentifier == 0 &&
                classType.equals(modelObject.getClass());
    }
}
