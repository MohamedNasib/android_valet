package com.icapp.icapp.Helpers;

import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_2;

import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.Models.CarUpdate;
import com.icapp.icapp.Models.User;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealM {
    private final Realm realm;

    private RealM() {
        realm = Realm.getDefaultInstance();
    }

    private static RealM instance;

    public static synchronized RealM getInstance() {
        if (instance == null) {
            instance = new RealM();
        }
        return instance;
    }

    public void beginTransaction() {
        // Begin a transaction
        realm.beginTransaction();
    }

    public void commitTransaction() {
        // Commit the transaction
        realm.commitTransaction();
    }

    // ---------------------------------------------------------------------------------------------

    public void insertArrival(CarReport carReport) {
        // Perform the transaction to add the object to the database
        realm.beginTransaction();
        realm.copyToRealm(carReport);
        realm.commitTransaction();
    }

    public ArrayList<CarReport> getArrivalList() {
        RealmResults<CarReport> results = realm.where(CarReport.class).findAll();
        return new ArrayList<>(results);
    }

    public CarReport findArrivalByID(String id) {
        // Retrieve the object to be updated
        return realm.where(CarReport.class)
                .equalTo("id", id)
                .findFirst();
    }

    public CarReport checkCarExist(String plateNumber) {
        // Retrieve the object to be updated
        return realm.where(CarReport.class)
                .equalTo("plateNumber", plateNumber)
                .findAll().last(null);
    }

    public CarReport checkTicketExist(String ticketNumber) {
        // Retrieve the object to be updated
        return realm.where(CarReport.class)
                .equalTo("ticketNumber", ticketNumber)
                .findAll().last(null);
    }

    public ArrayList<CarReport> searchPlateOrTicket(String text) {
        // Retrieve the object to be updated
        RealmResults<CarReport> results = realm.where(CarReport.class)
                .contains("plateNumber", text, Case.INSENSITIVE)
                .or()
                .contains("ticketNumber", text, Case.INSENSITIVE)
                .findAll();
        return new ArrayList<>(results);
    }

    public void updateArrival(CarReport carReport) {
        beginTransaction();

        // Retrieve the object to be updated
        CarReport objectToUpdate = realm.where(CarReport.class)
                .equalTo("id", carReport.getId())
                .findFirst();

        // Update the fields
        if (objectToUpdate != null) {
            objectToUpdate.setStatus(carReport.getStatus());
            objectToUpdate.setCreatedAt(carReport.getCreatedAt());
            objectToUpdate.setUpdatedAt(carReport.getUpdatedAt());
            objectToUpdate.setPlateNumber(carReport.getPlateNumber());
            objectToUpdate.setTicketNumber(carReport.getTicketNumber());
            objectToUpdate.setFront_image(carReport.getFront_image());
            objectToUpdate.setFront_date(carReport.getFront_date());
            objectToUpdate.setLeft_image(carReport.getLeft_image());
            objectToUpdate.setLeft_date(carReport.getLeft_date());
            objectToUpdate.setBack_image(carReport.getBack_image());
            objectToUpdate.setBack_date(carReport.getBack_date());
            objectToUpdate.setRight_image(carReport.getRight_image());
            objectToUpdate.setRight_date(carReport.getRight_date());
            objectToUpdate.setDamages(carReport.getDamages());
            objectToUpdate.setAdditional(carReport.getAdditional());
            objectToUpdate.setSignature(carReport.getSignature());
            objectToUpdate.setUser_id(carReport.getUser_id());
            objectToUpdate.setUser_name(carReport.getUser_name());
            objectToUpdate.setUser_image(carReport.getUser_image());
            objectToUpdate.setLatitude(carReport.getLatitude());
            objectToUpdate.setLongitude(carReport.getLongitude());
            objectToUpdate.setAddress(carReport.getAddress());
            objectToUpdate.setSubmit_later(carReport.getSubmit_later());
        }
        commitTransaction();
    }

    public void deleteArrival(String id) {
        beginTransaction();

        // Retrieve the object to be deleted
        CarReport objectToDelete = realm.where(CarReport.class)
                .equalTo("id", id)
                .findFirst();

        // Delete the object
        if (objectToDelete != null) {
            objectToDelete.deleteFromRealm();
        }

        commitTransaction();
    }

    public void deleteFinalExits() {
        beginTransaction();

        // Retrieve the object to be deleted
        RealmResults<CarReport> resultsToDelete = realm.where(CarReport.class)
                .equalTo("status", ORDER_STATUS_2)
                .findAll();

        // Delete the object
        if (resultsToDelete != null && !resultsToDelete.isEmpty()) {
            for (CarReport carReport : resultsToDelete) {
                carReport.deleteFromRealm();
            }
        }

        commitTransaction();
    }

    public void dropArrival() {
        beginTransaction();
        realm.delete(CarReport.class);
        commitTransaction();
    }

    // ---------------------------------------------------------------------------------------------

    public void insertUser(User user) {
        // Perform the transaction to add the object to the database
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public ArrayList<User> getUserList() {
        RealmResults<User> results = realm.where(User.class).findAll();
        return new ArrayList<>(results);
    }

    public User getUser(String passCode) {
        // Retrieve the object to be updated
        return realm.where(User.class)
                .equalTo("passCode", passCode, Case.SENSITIVE)
                .findFirst();
    }

    public User findUserByID(String id) {
        // Retrieve the object to be updated
        return realm.where(User.class)
                .equalTo("id", id)
                .findFirst();
    }

    public void updateUser(User user) {
        beginTransaction();

        // Retrieve the object to be updated
        User objectToUpdate = realm.where(User.class)
                .equalTo("id", user.getId())
                .findFirst();

        // Update the fields
        if (objectToUpdate != null) {
            objectToUpdate.setFirstName(user.getFirstName());
            objectToUpdate.setLastName(user.getLastName());
            objectToUpdate.setCompanyId(user.getCompanyId());
            objectToUpdate.setAreaId(user.getAreaId());
            objectToUpdate.setPassCode(user.getPassCode());
            objectToUpdate.setFaceImage(user.getFaceImage());
        }

        commitTransaction();
    }

    public void deleteUser(String id) {
        beginTransaction();

        // Retrieve the object to be deleted
        User objectToDelete = realm.where(User.class)
                .equalTo("id", id)
                .findFirst();

        // Delete the object
        if (objectToDelete != null) {
            objectToDelete.deleteFromRealm();
        }

        commitTransaction();
    }

    public void dropUser() {
        beginTransaction();
        realm.delete(User.class);
        commitTransaction();
    }

    // ---------------------------------------------------------------------------------------------

    public void insertUpdate(CarUpdate carUpdate) {
        // Perform the transaction to add the object to the database
        realm.beginTransaction();
        realm.copyToRealm(carUpdate);
        realm.commitTransaction();
    }

    public ArrayList<CarUpdate> getUpdateList() {
        RealmResults<CarUpdate> results = realm.where(CarUpdate.class).findAll();
        return new ArrayList<>(results);
    }

    public CarUpdate findUpdateByID(String id) {
        // Retrieve the object to be updated
        return realm.where(CarUpdate.class)
                .equalTo("id", id)
                .findFirst();
    }

    public void deleteUpdate(String id) {
        beginTransaction();

        // Retrieve the object to be deleted
        CarUpdate objectToDelete = realm.where(CarUpdate.class)
                .equalTo("id", id)
                .findFirst();

        // Delete the object
        if (objectToDelete != null) {
            objectToDelete.deleteFromRealm();
        }

        commitTransaction();
    }

    public void dropUpdate() {
        beginTransaction();
        realm.delete(CarUpdate.class);
        commitTransaction();
    }
}