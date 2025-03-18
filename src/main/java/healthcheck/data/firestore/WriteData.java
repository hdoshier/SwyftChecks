package healthcheck.data.firestore;

import com.google.cloud.firestore.Firestore;
import healthcheck.data.HealthCheck;
import healthcheck.data.Office;

import java.time.LocalDate;
import java.util.*;

public class WriteData {

    public static void writeHealthCheck(HealthCheck check) {
        HashMap<String, Object> data = new HashMap<>(22);

        //enter data into map
        data.put("office", check.getOffice());
        data.put("activeClients", check.getActiveClients());
        data.put("activeCaregivers", check.getActiveCaregivers());
        data.put("contactReason", check.getContactReason());
        data.put("previousContactReason", check.getPreviousContactReason());
        data.put("lastLogin", check.getLastLogin().toString());
        data.put("oldestTaskDate", check.getOldestTaskDate().toString());
        data.put("expiredLicenseCount", check.getExpiredLicenseCount());
        data.put("lastScheduleDate", check.getLastScheduleDate().toString());
        data.put("scheduleGenerationMethod", check.getScheduleGenerationMethod());
        data.put("shiftsInDifferentStatuses", check.isShiftsInDifferentStatuses());
        data.put("caregiversUsingTheApp", check.isCaregiversUsingTheApp());
        data.put("clientGeneralSchedulesConfigured", check.getClientGeneralSchedulesConfigured());
        data.put("lastBillingProcessDate", check.getLastBillingProcessDate().toString());
        data.put("lastPayrollProcessDate", check.getLastPayrollProcessDate().toString());
        data.put("repeatAdjustments", check.isRepeatAdjustments());
        data.put("generalNotes", check.getGeneralNotes());
        data.put("assignedTo", check.getAssignedTo());
        data.put("reviewPerformedBy", check.getReviewPerformedBy());
        data.put("followUpPerformedBy", check.getHealthCheckCompletedBy());
        data.put("healthCheckStatus", check.getHealthCheckStatus());



    }

    public static void saveOffice(Office office) {
        // pushes the data to the database
        Firestore db = Database.getFirestore();
        Database.getInstance().replaceOffice(office);
        if (office.isActiveOffice()) {
            db.collection("offices").document(office.getOfficeCode()).set(packageOffice(office));
        } else {
            db.collection("inactiveOffices").document(office.getOfficeCode()).set(packageOffice(office));
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
            db.collection("offices").document(office.getOfficeCode()).set(packageOffice(office));
        } else {
            // Move office from "offices" to "inactiveOffices"
            db.collection("offices").document(office.getOfficeCode()).delete();
            db.collection("inactiveOffices").document(office.getOfficeCode()).set(packageOffice(office));
        }
    }

    private static HashMap<String, Object> packageOffice (Office office) {
        HashMap<String, Object> data = new HashMap<>(12);

        //all office data to be written
        data.put("officeCode", office.getOfficeCode());
        data.put("officeName", office.getOfficeName());

        LocalDate date = office.getExecAgreementDate();
        if (date != null) {
            data.put("execAgreementDate", date.toString());
        }

        data.put("officeOwner", office.getOfficeOwner());
        data.put("officeOwnerEmail", office.getOfficeOwnerEmail());
        data.put("officePrimaryContactPerson", office.getOfficePrimaryContactPerson());
        data.put("officePrimaryContactEmail", office.getOfficePrimaryContactEmail());
        data.put("officePrimaryContactPhone", office.getOfficePrimaryContactPhone());
        data.put("leadershipNotes", office.getLeadershipNotes());
        data.put("generalNotes", office.getGeneralNotes());
        data.put("contactlNotes", office.getContactNotes());
        data.put("trainingStatus", office.getTrainingStatus());
        data.put("billableHourHistory", office.getBillableHourHistory());
        data.put("activeOffice", office.isActiveOffice());

        return data;
    }

    /**
     * This method is to be explicitly run if I need to merge a new data field to the Database.
     *
     * Build it to push new data to the DB.
     */
    public static void mergeNewDataToDatabase() {

    }
}