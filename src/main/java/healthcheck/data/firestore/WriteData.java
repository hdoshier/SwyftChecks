package healthcheck.data.firestore;

import com.google.cloud.firestore.Firestore;
import healthcheck.data.Office;

import java.util.*;
import java.util.concurrent.ExecutionException;

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
        Firestore db = Database.getFirestore();
        db.collection("offices").document(office.getOfficeCode()).set(data);
    }

    public static void writeOfficeObject (Office office) {
        Firestore db = Database.getFirestore();
        db.collection("offices").document(office.getOfficeCode()).set(office);
    }

    public static void writeExcludedOffice (HashSet<String> set) {
        Firestore db = Database.getFirestore();
        //0 Office Code | 1 Office Name | 2 Owner Name | 3 Owner Email | 4 Owner Phone | 7 Agreement Exec. Date

        try {
            HashMap<String, List> map = new HashMap<>(1);
            map.put("offices", new ArrayList<String>(set));
            db.collection("settings").document("excludedOffices").set(map).get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }
}
