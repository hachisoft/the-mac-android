package com.mac.themac.model.firebase;

/**
 * Created by Samir on 9/11/2015.
 */
public class FBModelIdentifier{

    private Class<? extends FBModelObject> intendedClass;
    private int secondaryIdentifier;
    private Object payload;

    public Class<? extends FBModelObject> getIntendedClass() {
        return intendedClass;
    }

    public int getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    public Object getPayload() {
        return payload;
    }

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = 0;
    }

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass, int secondaryIdentifier) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = secondaryIdentifier;

    }

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass, int secondaryIdentifier, Object payload) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = secondaryIdentifier;
        this.payload = payload;
    }

    public FBModelIdentifier(Class<? extends FBModelObject> intendedClass, Object payload) {
        this.intendedClass = intendedClass;
        this.secondaryIdentifier = 0;
        this.payload = payload;
    }

    static public FBModelIdentifier getIdentfier(Class<? extends FBModelObject> intendedClass){
        return new FBModelIdentifier(intendedClass);
    }

    static public FBModelIdentifier getIdentfier(Class<? extends FBModelObject> intendedClass, int secondaryIdentifier){
        return new FBModelIdentifier(intendedClass, secondaryIdentifier);
    }

    static public FBModelIdentifier getIdentfier(Class<? extends FBModelObject> intendedClass, int secondaryIdentifier, Object payload){
        return new FBModelIdentifier(intendedClass, secondaryIdentifier, payload);
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
