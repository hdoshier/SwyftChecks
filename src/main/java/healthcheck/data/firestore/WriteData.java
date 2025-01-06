package healthcheck.data.firestore;

import com.google.cloud.firestore.Firestore;
import healthcheck.data.HealthCheck;
import healthcheck.data.Office;

import java.util.*;
import java.util.concurrent.ExecutionException;

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

    public static void writeOffice(Office office) {
        HashMap<String, Object> data = new HashMap<>(12);

        //all office data to be written
        data.put("officeCode", office.getOfficeCode());
        data.put("officeName", office.getOfficeName());
        data.put("lastHealthCheckDate", office.getLastHealthCheckDate().toString());
        data.put("execAgreementDate", office.getExecAgreementDate().toString());
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
