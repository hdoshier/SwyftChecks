package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import healthcheck.data.Office;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadData {

    public static Office readOffice(QueryDocumentSnapshot document) {
        return officeDataToRead(document.getId(), document);
    }


    public static Office readOffice(String officeCode) {
        Firestore db = FirestoreDatabase.getFirestore();
        DocumentReference document = db.collection("offices").document(officeCode);

        try {
            // Get the document snapshot
            ApiFuture<DocumentSnapshot> future = document.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (!documentSnapshot.exists()) {
                return null;
            }

            return officeDataToRead(officeCode, documentSnapshot);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Office officeDataToRead(String officeCode, DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }

        Office office = new Office(officeCode);
        // Map data to the Office object

        office.setOfficeName(document.getString("officeName"));
        office.setLastHealthCheckDate(document.getString("lastHealthCheckDate"));
        office.setExecAgreementDate(document.getString("execAgreementDate"));
        office.setOfficeOwner(document.getString("officeOwner"));
        office.setOfficeOwnerEmail(document.getString("officeOwnerEmail"));
        office.setOfficePrimaryContactPerson(document.getString("officePrimaryContactPerson"));
        office.setOfficePrimaryContactEmail(document.getString("officePrimaryContactEmail"));
        office.setLeadershipNotes(document.getString("leadershipNotes"));
        office.setGeneralNotes(document.getString("generalNotes"));
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
