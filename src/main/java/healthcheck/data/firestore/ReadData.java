package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReadData {
    // SETTINGS
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
                list.add(createHealthCheckFromData(document.getId(), document));
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

            return createHealthCheckFromData(officeCode, documentSnapshot);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Office createHealthCheckFromData(String officeCode, DocumentSnapshot document) {
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

    // HEALTH CHECK PERIODS
    public static ArrayList<HealthCheckPeriod> getHealthCheckPeriods() {
        Firestore firestore = Database.getFirestore();
        // Get collection from db
        ApiFuture<QuerySnapshot> query = firestore.collection("healthCheckPeriods").get();
        try {
            QuerySnapshot querySnapshot = query.get();
            ArrayList<HealthCheckPeriod> list = new ArrayList<>(querySnapshot.size());

            // Iterate through each document in the healthCheckPeriods collection
            for (QueryDocumentSnapshot document : querySnapshot) {
                // Extract period fields
                String startDateStr = document.getString("startDate");
                String endDateStr = document.getString("endDate");
                Long uniqueIdLong = document.getLong("uniqueId");
                if (startDateStr == null || endDateStr == null || uniqueIdLong == null) {
                    System.err.println("Skipping document with missing fields: " + document.getId());
                    continue;
                }

                // Parse dates and uniqueId
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);
                int uniqueId = uniqueIdLong.intValue();

                // Create HealthCheckPeriod object (without health checks for now)
                HealthCheckPeriod period = new HealthCheckPeriod(startDate, endDate, uniqueId);

                // Extract healthChecks map
                @SuppressWarnings("unchecked")
                HashMap<String, HashMap<String, Object>> healthChecksMap = (HashMap<String, HashMap<String, Object>>) document.get("healthChecks");
                if (healthChecksMap != null) {
                    ArrayList<HealthCheck> healthCheckList = new ArrayList<>(healthChecksMap.size());
                    for (Map.Entry<String, HashMap<String, Object>> entry : healthChecksMap.entrySet()) {
                        String officeCode = entry.getKey();
                        HashMap<String, Object> checkData = entry.getValue();

                        // Create HealthCheck object
                        healthCheckList.add(createHealthCheckFromData(checkData, officeCode));
                    }
                    healthCheckList.sort((hc1, hc2) -> hc1.getOfficeCode().compareTo(hc2.getOfficeCode()));
                    // Replace the default healthCheckList with the one from Firestore
                    period.getHealthCheckList().clear();
                    period.getHealthCheckList().addAll(healthCheckList);

                }

                list.add(period);
            }

            // Sort by uniqueId (descending order: higher uniqueId = newer)
            list.sort((p1, p2) -> Integer.compare(p2.getUniqueId(), p1.getUniqueId()));

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(1);
        }
    }

    public static HealthCheck getHealthCheckFromPeriod(String periodRange, String officeCode) {
        Firestore firestore = Database.getFirestore();
        try{
            // Get the specific period document
            DocumentReference docRef = firestore.collection("healthCheckPeriods").document(periodRange);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (!document.exists()) {
                System.err.println("Period not found: " + periodRange);
                return null;
            }

            // Extract the healthChecks map
            @SuppressWarnings("unchecked")
            Map<String, HashMap<String, Object>> healthChecksMap = (HashMap<String, HashMap<String, Object>>) document.get("healthChecks");
            if (healthChecksMap == null || !healthChecksMap.containsKey(officeCode)) {
                System.err.println("HealthCheck not found for officeCode: " + officeCode + " in period: " + periodRange);
                return null;
            }
            return createHealthCheckFromData(healthChecksMap.get(officeCode), officeCode);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HealthCheck createHealthCheckFromData(HashMap<String, Object> checkData, String code) {
        HealthCheck check = new HealthCheck(code);

        // Populate HealthCheck fields
        check.setActiveClients(((Long) checkData.get("activeClients")).intValue());
        check.setActiveCaregivers(((Long) checkData.get("activeCaregivers")).intValue());
        check.setContactReason((String) checkData.get("contactReason"));
        String lastLoginStr = (String) checkData.get("lastLogin");
        check.setLastLogin(lastLoginStr != null ? LocalDate.parse(lastLoginStr) : null);
        String oldestTaskDateStr = (String) checkData.get("oldestTaskDate");
        check.setOldestTaskDate(oldestTaskDateStr != null ? LocalDate.parse(oldestTaskDateStr) : null);
        check.setExpiredLicenseCount(((Long) checkData.get("expiredLicenseCount")).intValue());
        String lastScheduleDateStr = (String) checkData.get("lastScheduleDate");
        check.setLastScheduleDate(lastScheduleDateStr != null ? LocalDate.parse(lastScheduleDateStr) : null);
        check.setScheduleGenerationMethod(((Long) checkData.get("scheduleGenerationMethod")).intValue());
        check.setShiftsInDifferentStatuses((Boolean) checkData.get("shiftsInDifferentStatuses"));
        check.setCaregiversUsingTheApp((Boolean) checkData.get("caregiversUsingTheApp"));
        check.setClientGeneralSchedulesConfigured(((Long) checkData.get("clientGeneralSchedulesConfigured")).intValue());
        String lastBillingProcessDateStr = (String) checkData.get("lastBillingProcessDate");
        check.setLastBillingProcessDate(lastBillingProcessDateStr != null ? LocalDate.parse(lastBillingProcessDateStr) : null);
        String lastPayrollProcessDateStr = (String) checkData.get("lastPayrollProcessDate");
        check.setLastPayrollProcessDate(lastPayrollProcessDateStr != null ? LocalDate.parse(lastPayrollProcessDateStr) : null);
        String checkCompletionDateStr = (String) checkData.get("checkCompletionDate");
        check.setCheckCompletionDate(checkCompletionDateStr != null ? LocalDate.parse(checkCompletionDateStr) : null);
        check.setRepeatingBillingAdjustments((Boolean) checkData.get("repeatingBillingAdjustments"));
        check.setRepeatingPayrollAdjustments((Boolean) checkData.get("repeatingPayrollAdjustments"));
        check.setGeneralNotes((String) checkData.get("generalNotes"));
        check.setAssignedTo((String) checkData.get("assignedTo"));
        check.setHealthCheckStatus(((Long) checkData.get("healthCheckStatus")).intValue());
        check.setFlagedForLeadershipReview((Boolean) checkData.get("flagedForLeadershipReview"));
        check.setGrowthNotes((String) checkData.get("growthNotes"));
        check.setPriorMonthShiftCount(((Long) checkData.get("priorMonthShiftCount")).intValue());

        return check;
    }
}