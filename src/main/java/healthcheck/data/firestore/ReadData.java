package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import healthcheck.data.Office;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadData {



    public static DocumentSnapshot getSettingsDocument() {
        Firestore firestore = Database.getFirestore();
        try {
            // Get the document reference
            DocumentReference documentRef = firestore.collection("settings").document("MySettings");

            // Fetch the document snapshot
            ApiFuture<DocumentSnapshot> future = documentRef.get();
            return future.get();
        }  catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getUsers(DocumentSnapshot document) {
        Object dbList = document.get("users");

        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>) dbList;
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public static HashMap<String, String> getEmailTemplates(DocumentSnapshot document) {
        Object dbMap = document.get("emailTemplates");

        @SuppressWarnings("unchecked")
        HashMap<String, String> map = (HashMap<String, String>) dbMap;
        if (map == null) {
            return new HashMap<String, String>();
        }
        return map;
    }

    // OFFICES IMPORT

    public static ArrayList<Office> getActiveOfficeList() {
        Firestore firestore = Database.getFirestore();
        // get collection from db
        ApiFuture<QuerySnapshot> query = firestore.collection("offices").get();

        try {
            QuerySnapshot querySnapshot = query.get();
            ArrayList<Office> list = new ArrayList<>(querySnapshot.size());
            for (QueryDocumentSnapshot document : querySnapshot) {
                list.add(createOfficeFromData(document.getId(), document));
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(1);
        }
    }


    public static Office readIndividualOffice(Office office) {
        String officeCode = office.getOfficeCode();
        DocumentReference document;
        Firestore db = Database.getFirestore();
        document = db.collection("offices").document(officeCode);

        try {
            // Get the document snapshot
            ApiFuture<DocumentSnapshot> future = document.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (!documentSnapshot.exists()) {
                return null;
            }

            return createOfficeFromData(officeCode, documentSnapshot);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Office createOfficeFromData(String officeCode, DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }

        Office office = new Office(officeCode);
        // Map data to the Office object

        office.setOfficeName(document.getString("officeName"));
        office.setExecAgreementDate(document.getString("execAgreementDate"));
        office.setOfficeOwner(document.getString("officeOwner"));
        office.setOfficeOwnerEmail(document.getString("officeOwnerEmail"));
        office.setOfficePrimaryContactPerson(document.getString("officePrimaryContactPerson"));
        office.setOfficePrimaryContactEmail(document.getString("officePrimaryContactEmail"));
        office.setOfficePrimaryContactPhone(document.getString("officePrimaryContactPhone"));
        office.setLeadershipNotes(document.getString("leadershipNotes"));
        office.setGeneralNotes(document.getString("generalNotes"));
        office.setContactNotes(document.getString("contactNotes"));
        office.setActiveOffice(document.getBoolean("activeOffice"));
        Long status = document.getLong("trainingStatus");
        if (status != null) {
            office.setTrainingStatus(Math.toIntExact(status));
        }

        Object docMap = document.get("billableHourHistory");
        if (docMap != null) {
            @SuppressWarnings("unchecked")
            HashMap<String, Double> hourMap = (HashMap<String, Double>) docMap;
            office.setBillableHourHistory(hourMap);
        }

        return office;
    }


}