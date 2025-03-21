package healthcheck.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class HealthCheck implements Serializable {
    private String officeCode;
    private int activeClients = 0;
    private int activeCaregivers = 0;
    private String contactReason = "";
    private LocalDate lastLogin = null;
    private LocalDate oldestTaskDate = null;
    private int expiredLicenseCount = 0;
    private LocalDate lastScheduleDate = null;
    private int scheduleGenerationMethod = 0;
    private boolean shiftsInDifferentStatuses = false;
    private boolean caregiversUsingTheApp = false;
    private int clientGeneralSchedulesConfigured = 0;
    private LocalDate lastBillingProcessDate = null;
    private LocalDate lastPayrollProcessDate = null;
    private LocalDate checkCompletionDate = null;
    private boolean repeatingBillingAdjustments = false;
    private boolean repeatingPayrollAdjustments = false;
    private String generalNotes = "";
    private String assignedTo = "Unassigned";
    private int healthCheckStatus = 0;
    private boolean flagedForLeadershipReview = false;
    private String growthNotes = "";
    private int priorMonthShiftCount = 0;

    public HealthCheck(String officeCode) {
        this.officeCode = officeCode;
        //TODO automate previousContactReason
    }

    public HashMap<String, Object> packageHealthCheck() {
        HashMap<String, Object> checkData = new HashMap<>();
        checkData.put("officeCode", officeCode);
        checkData.put("activeClients", activeClients);
        checkData.put("activeCaregivers", activeCaregivers);
        checkData.put("contactReason", contactReason);
        checkData.put("lastLogin", lastLogin!= null ? lastLogin.toString() : null);
        checkData.put("oldestTaskDate", oldestTaskDate != null ? oldestTaskDate.toString() : null);
        checkData.put("expiredLicenseCount", expiredLicenseCount);
        checkData.put("lastScheduleDate", lastScheduleDate != null ? lastScheduleDate.toString() : null);
        checkData.put("scheduleGenerationMethod", scheduleGenerationMethod);
        checkData.put("shiftsInDifferentStatuses", shiftsInDifferentStatuses);
        checkData.put("caregiversUsingTheApp", caregiversUsingTheApp);
        checkData.put("clientGeneralSchedulesConfigured", clientGeneralSchedulesConfigured);
        checkData.put("checkCompletionDate", checkCompletionDate != null ? checkCompletionDate.toString() : null);
        checkData.put("lastBillingProcessDate", lastBillingProcessDate != null ? lastBillingProcessDate.toString() : null);
        checkData.put("lastPayrollProcessDate", lastPayrollProcessDate != null ? lastPayrollProcessDate.toString() : null);
        checkData.put("repeatingBillingAdjustments", repeatingBillingAdjustments);
        checkData.put("repeatingPayrollAdjustments", repeatingPayrollAdjustments);
        checkData.put("generalNotes", generalNotes);
        checkData.put("assignedTo", assignedTo);
        checkData.put("healthCheckStatus", healthCheckStatus);
        checkData.put("flagedForLeadershipReview", flagedForLeadershipReview);
        checkData.put("growthNotes", generalNotes);
        checkData.put("priorMonthShiftCount", priorMonthShiftCount);
        return checkData;
    }

    // getters and setters


    public LocalDate getCheckCompletionDate() {
        return checkCompletionDate;
    }

    public void setCheckCompletionDate(LocalDate checkCompletionDate) {
        this.checkCompletionDate = checkCompletionDate;
    }

    public int getPriorMonthShiftCount() {
        return priorMonthShiftCount;
    }

    public void setPriorMonthShiftCount(int priorMonthShiftCount) {
        this.priorMonthShiftCount = priorMonthShiftCount;
    }

    public String getGrowthNotes() {
        return growthNotes;
    }

    public void setGrowthNotes(String growthNotes) {
        this.growthNotes = growthNotes;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public boolean isFlagedForLeadershipReview() {
        return flagedForLeadershipReview;
    }

    public void setFlagedForLeadershipReview(boolean flagedForLeadershipReview) {
        this.flagedForLeadershipReview = flagedForLeadershipReview;
    }

    public int getHealthCheckStatus() {
        return healthCheckStatus;
    }

    public void setHealthCheckStatus(int healthCheckStatus) {
        this.healthCheckStatus = healthCheckStatus;
    }

    public int getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(int activeClients) {
        this.activeClients = activeClients;
    }

    public int getActiveCaregivers() {
        return activeCaregivers;
    }

    public void setActiveCaregivers(int activeCaregivers) {
        this.activeCaregivers = activeCaregivers;
    }

    public String getContactReason() {
        return contactReason;
    }

    public void setContactReason(String contactReason) {
        this.contactReason = contactReason;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDate getOldestTaskDate() {
        return oldestTaskDate;
    }

    public void setOldestTaskDate(LocalDate oldestTaskDate) {
        this.oldestTaskDate = oldestTaskDate;
    }

    public int getExpiredLicenseCount() {
        return expiredLicenseCount;
    }

    public void setExpiredLicenseCount(int expiredLicenseCount) {
        this.expiredLicenseCount = expiredLicenseCount;
    }

    public LocalDate getLastScheduleDate() {
        return lastScheduleDate;
    }

    public void setLastScheduleDate(LocalDate lastScheduleDate) {
        this.lastScheduleDate = lastScheduleDate;
    }

    public int getScheduleGenerationMethod() {
        return scheduleGenerationMethod;
    }

    public void setScheduleGenerationMethod(int scheduleGenerationMethod) {
        this.scheduleGenerationMethod = scheduleGenerationMethod;
    }

    public boolean isShiftsInDifferentStatuses() {
        return shiftsInDifferentStatuses;
    }

    public void setShiftsInDifferentStatuses(boolean shiftsInDifferentStatuses) {
        this.shiftsInDifferentStatuses = shiftsInDifferentStatuses;
    }

    public boolean isCaregiversUsingTheApp() {
        return caregiversUsingTheApp;
    }

    public void setCaregiversUsingTheApp(boolean caregiversUsingTheApp) {
        this.caregiversUsingTheApp = caregiversUsingTheApp;
    }

    public int getClientGeneralSchedulesConfigured() {
        return clientGeneralSchedulesConfigured;
    }

    public void setClientGeneralSchedulesConfigured(int clientGeneralSchedulesConfigured) {
        this.clientGeneralSchedulesConfigured = clientGeneralSchedulesConfigured;
    }

    public LocalDate getLastBillingProcessDate() {
        return lastBillingProcessDate;
    }

    public void setLastBillingProcessDate(LocalDate lastBillingProcessDate) {
        this.lastBillingProcessDate = lastBillingProcessDate;
    }

    public LocalDate getLastPayrollProcessDate() {
        return lastPayrollProcessDate;
    }

    public void setLastPayrollProcessDate(LocalDate lastPayrollProcessDate) {
        this.lastPayrollProcessDate = lastPayrollProcessDate;
    }

    public boolean isRepeatingBillingAdjustments() {
        return repeatingBillingAdjustments;
    }

    public void setRepeatingBillingAdjustments(boolean repeatAdjustments) {
        this.repeatingBillingAdjustments = repeatAdjustments;
    }

    public boolean isRepeatingPayrollAdjustments() {
        return repeatingPayrollAdjustments;
    }

    public void setRepeatingPayrollAdjustments(boolean repeatAdjustments) {
        this.repeatingPayrollAdjustments = repeatAdjustments;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
        if (assignedTo.equals("Unassigned")) {
            healthCheckStatus = 0;
        } else {
            healthCheckStatus = 1;
        }
    }
}
