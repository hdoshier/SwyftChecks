package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import healthcheck.Main;
import healthcheck.data.Office;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class WriteData {

    public static void writeOffice(Office office) {
        HashMap<String, Object> data = new HashMap<>();

        //all office data to be written
        data.put("officeCode", office.getOfficeCode());
        data.put("officeName", office.getOfficeName());
        data.put("lastHealthCheckDate", office.getLastHealthCheckDate());
        data.put("execAgreementDate", office.getExecAgreementDate());
        data.put("officeOwner", office.getOfficeOwner());
        data.put("officeOwnerEmail", office.getOfficeOwnerEmail());
        data.put("officePrimaryContactPerson", office.getOfficePrimaryContactPerson());
        data.put("officePrimaryContactEmail", office.getOfficePrimaryContactEmail());
        data.put("leadershipNotes", office.getLeadershipNotes());
        data.put("generalNotes", office.getGeneralNotes());
        data.put("trainingStatus", office.getTrainingStatus());
        data.put("billableHourHistory", office.getBillableHourHistory());

        // pushes the data to the database
        Firestore db = FirestoreDatabase.getFirestore();
        db.collection("offices").document(office.getOfficeCode()).set(data);
    }

    public static void writeOfficeObject (Office office) {
        Firestore db = FirestoreDatabase.getFirestore();
        db.collection("offices").document(office.getOfficeCode()).set(office);
    }

    public static void writeExcludedOffice (String[] officeData) {
        Firestore db = FirestoreDatabase.getFirestore();
        //0 Office Code | 1 Office Name | 2 Owner Name | 3 Owner Email | 4 Owner Phone | 7 Agreement Exec. Date
        HashMap<String, Object> data = new HashMap<>();
        data.put("officeCode", officeData[0]);
        data.put("officeName", officeData[1]);
        data.put("ownerName", officeData[2]);
        data.put("ownerEmail", officeData[3]);
        data.put("ownerPhone", officeData[4]);
        db.collection("excludedOffices").document(officeData[0]).set(data);

    }
}
