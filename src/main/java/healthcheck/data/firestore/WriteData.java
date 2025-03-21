package healthcheck.data.firestore;

import com.google.cloud.firestore.Firestore;
import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;

import java.util.HashMap;

public class WriteData {

    public static void createNewHealthCheckPeriod(HealthCheckPeriod period) {
        Firestore db = Database.getFirestore();
        //add period to db
        db.collection("healthCheckPeriods").document(period.getPeriodRange()).set(period.packagePeriod());
    }

    public static void updateHealthCheckInFirestore(HealthCheckPeriod period, HealthCheck check) {
        Firestore firestore = Database.getFirestore();
        HashMap<String, Object> checkData = check.packageHealthCheck();

        firestore.collection("healthCheckPeriods")
                .document(period.getPeriodRange())
                .update("healthChecks." + check.getOfficeCode(), checkData);
    }

    public static void saveOffice(Office office) {
        // pushes the data to the database
        Firestore db = Database.getFirestore();
        // ensures the cached database has the most recent changes made by the user.
        Database.getInstance().replaceOffice(office);
        if (office.isActiveOffice()) {
            db.collection("offices").document(office.getOfficeCode()).set(office.packageOffice());
        } else {
            db.collection("inactiveOffices").document(office.getOfficeCode()).set(office.packageOffice());
        }
    }

    /**
     * Switches which collection the given office is a part of. If the office is being activated
     * it will be moved from the "inactiveOffices" collection to the "offices" collection.
     * Vise versa when deactivating an office.
     *
     * @param office
     */
    public static void switchOfficeCollection(Office office) {
        Firestore db = Database.getFirestore();
        if (office.isActiveOffice()) {
            // Move office from "inactiveOffices" to "offices"
            db.collection("inactiveOffices").document(office.getOfficeCode()).delete();
            db.collection("offices").document(office.getOfficeCode()).set(office.packageOffice());
        } else {
            // Move office from "offices" to "inactiveOffices"
            db.collection("offices").document(office.getOfficeCode()).delete();
            db.collection("inactiveOffices").document(office.getOfficeCode()).set(office.packageOffice());
        }
    }

    /**
     * This method is to be explicitly run if I need to merge a new data field to the Database.
     *
     * Build it to push new data to the DB.
     */
    public static void mergeNewDataToDatabase() {

    }
}