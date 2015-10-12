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
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends FBModelObject{

    public Date created;
    public String firstName;
    public String middleName;
    public String lastName;
    public Boolean isAdmin;
    public Boolean isDeptHead;
    public Boolean isJunior;
    public Boolean isLeader;
    public Date lastLogin;
    public String memberNumber;
    public String memberProfile;
    public String memberProfilePublic;
    public String employeeProfile;
    public long numAdvRes;
    public long numGenRes;
    public String primaryMemberNumber;
    public String prefix;
    public String suffix;
    public String gender;
    public Date dob;
    public String businessPhone;
    public String businessExt;
    public String homePhone;
    public String cellPhone;
    public String fax;
    public String email;
    public String maritalStatus;
    public String title;
    public long classification;
    public String confidential;
    public Date memberSince;
    public String type;
    public String status;
    public String photoId;
    public String thumbId;
    public String occupation;
    public String interests;//comma seperated array string
    public HashMap<String, Boolean> logins;
    public HashMap<String, Boolean> dependents;
    public HashMap<String, Boolean> transactions;
    public HashMap<String, Boolean> statements;
    public HashMap<String, Boolean> createdReservations;
    public HashMap<String, Boolean> createdRegistrations;
    public HashMap<String, Boolean> registrations;
    public HashMap<String, Boolean> reservations;
    public HashMap<String, Boolean> addresses;
    public HashMap<String, Boolean> vehicles;
    public HashMap<String, Boolean> emergencyContacts;

    @JsonIgnore
    public MemberProfilePublic linkedMemberProfilePublic;
    @JsonIgnore
    public EmployeeProfile linkedEmployeeProfile;
    @JsonIgnore
    public List<FBModelObject> linkedDependents = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedLogins = new ArrayList<FBModelObject>() ;
    @JsonIgnore
    public List<FBModelObject> linkedCreatedReservations = new ArrayList<FBModelObject>();
    /*@JsonIgnore
    public List<FBModelObject> linkedInterests = new ArrayList<FBModelObject>();
    */
    @JsonIgnore
    public List<FBModelObject> linkedRegistrations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedReservations = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedAddresses = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedVehicles = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<FBModelObject> linkedEmergencyContacts = new ArrayList<FBModelObject>();
    @JsonIgnore
    public List<Transaction> linkedTransactions = new ArrayList<Transaction>();
    @JsonIgnore
    public List<Statement> linkedStatements = new ArrayList<Statement>();

    public User() {
    }

    @JsonIgnore
    public void loadMemberProfileInterestsRegistrations(){

        /*if(interests == null){
            interests = new HashMap<String, Boolean>();
        }
        if (interests != null) {
            loadLinkedObjects(Interest.class, FirebaseHelper.FBRootContainerNames.interests,
                    interests, linkedInterests);
        }*/
        if(registrations == null){
            registrations = new HashMap<String, Boolean>();
        }
        if(registrations != null){
            loadLinkedObjects(Registration.class, FirebaseHelper.FBRootContainerNames.registrations,
                    registrations, linkedRegistrations);
        }
        if(transactions == null){
            transactions = new HashMap<String, Boolean>();
        }
        if(transactions != null){
            loadLinkedObjects(Transaction.class, FirebaseHelper.FBRootContainerNames.transactions,
                    transactions, linkedTransactions);
        }
        if(statements == null){
            statements = new HashMap<String, Boolean>();
        }
        if(statements != null){
            loadLinkedObjects(Statement.class, FirebaseHelper.FBRootContainerNames.statements,
                    statements, linkedStatements);
        }
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(memberProfilePublic != null && !memberProfilePublic.isEmpty()) {
            loadLinkedObject(MemberProfilePublic.class,
                    FirebaseHelper.FBRootContainerNames.memberProfilePublics,
                    memberProfilePublic, linkedMemberProfilePublic);
        }

        if(employeeProfile != null && !employeeProfile.isEmpty()) {
            loadLinkedObject(EmployeeProfile.class,
                    FirebaseHelper.FBRootContainerNames.employeeProfiles,
                    employeeProfile, linkedEmployeeProfile);
        }

        if (logins == null) {
            logins = new HashMap<String, Boolean>();
        }
        if (logins != null) {

            loadLinkedObjects(Login.class, FirebaseHelper.FBRootContainerNames.logins,
                    logins, linkedLogins);
        }

        /*if(interests == null){
            interests = new HashMap<String, Boolean>();
        }
        if (interests != null) {
            loadLinkedObjects(Interest.class, FirebaseHelper.FBRootContainerNames.interests,
                    interests, linkedInterests);
        }*/

        if(dependents == null){
            dependents = new HashMap<String, Boolean>();
        }
        if (dependents != null) {
            loadLinkedObjects(User.class, FirebaseHelper.FBRootContainerNames.users,
                    dependents, linkedDependents);
        }

        if(registrations == null){
            registrations = new HashMap<String, Boolean>();
        }
        if(registrations != null){
            loadLinkedObjects(Registration.class, FirebaseHelper.FBRootContainerNames.registrations,
                    registrations, linkedRegistrations);
        }

        if(reservations == null){
            reservations = new HashMap<String, Boolean>();
        }
        if(reservations != null){
            loadLinkedObjects(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    reservations, linkedReservations);
        }

        if(createdReservations == null){
            createdReservations = new HashMap<String, Boolean>();
        }
        if(createdReservations != null){
            loadLinkedObjects(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    createdReservations, linkedCreatedReservations);
        }

        if (addresses == null) {
            addresses = new HashMap<String, Boolean>();
        }
        if (addresses != null) {

            loadLinkedObjects(Address.class, FirebaseHelper.FBRootContainerNames.addresses,
                    addresses, linkedAddresses);
        }

        if(vehicles == null){
            vehicles = new HashMap<String, Boolean>();
        }
        if (vehicles != null) {
            loadLinkedObjects(Vehicle.class, FirebaseHelper.FBRootContainerNames.vehicles,
                    vehicles, linkedVehicles);
        }

        if(emergencyContacts == null){
            emergencyContacts = new HashMap<String, Boolean>();
        }
        if (emergencyContacts != null) {
            loadLinkedObjects(EmergencyContact.class, FirebaseHelper.FBRootContainerNames.emergencyContacts,
                    emergencyContacts, linkedEmergencyContacts);
        }

        if(transactions == null){
            transactions = new HashMap<String, Boolean>();
        }
        if (transactions != null) {
            loadLinkedObjects(Transaction.class, FirebaseHelper.FBRootContainerNames.transactions,
                    transactions, linkedTransactions);
        }

        if(statements == null){
            statements = new HashMap<String, Boolean>();
        }
        if(statements != null){
            loadLinkedObjects(Statement.class, FirebaseHelper.FBRootContainerNames.statements,
                    statements, linkedStatements);
        }
    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, EmployeeProfile.class)) {
            linkedEmployeeProfile = (EmployeeProfile) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, MemberProfilePublic.class)) {
            linkedMemberProfilePublic = (MemberProfilePublic) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedMemberProfilePublic = null;
        linkedEmployeeProfile = null;
        linkedLogins.clear();
        //linkedInterests.clear();
        linkedDependents.clear();
        linkedCreatedReservations.clear();
        linkedRegistrations.clear();
        linkedReservations.clear();
        linkedAddresses.clear();
        linkedVehicles.clear();
        linkedEmergencyContacts.clear();
        linkedTransactions.clear();
        linkedStatements.clear();
    }
}
