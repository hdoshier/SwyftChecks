package healthcheck.data;

import java.io.Serializable;
import java.time.LocalDate;

public class HealthCheck implements Serializable {

    private final HealthCheckPeriod healthCheckPeriod;
    private int activeClients = -1;
    private int activeCaregivers = -1;
    private String contactReason = "";
    private String previousContactReason = "";
    private LocalDate lastLogin = null;
    private LocalDate oldestTaskDate = null;
    private int expiredLicenseCount = -1;
    private LocalDate lastScheduleDate = null;
    private int scheduleGenerationMethod = -1;
    private boolean shiftsInDifferentStatuses = false;
    private boolean caregiversUsingTheApp = false;
    private int clientGeneralSchedulesConfigured = -1;
    private LocalDate lastBillingProcessDate = null;
    private LocalDate lastPayrollProcessDate = null;
    private boolean repeatAdjustments = false;
    private String generalNotes = "";
    private int reviewPerformedBy = -1;
    private int followUpPerformedBy = -1;
    private double currentMonthBillableHours = -1;
    private int healthCheckStatus = 1;

    public HealthCheck(HealthCheckPeriod healthCheckPeriod) {
        this.healthCheckPeriod = healthCheckPeriod;
        //TODO automatomate previousContactReason
    }

    public HealthCheckPeriod getHealthCheckPeriod() {
        return healthCheckPeriod;
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

    public String getPreviousContactReason() {
        return previousContactReason;
    }

    public void setPreviousContactReason(String previousContactReason) {
        this.previousContactReason = previousContactReason;
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

    public boolean isRepeatAdjustments() {
        return repeatAdjustments;
    }

    public void setRepeatAdjustments(boolean repeatAdjustments) {
        this.repeatAdjustments = repeatAdjustments;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public int getReviewPerformedBy() {
        return reviewPerformedBy;
    }

    public void setReviewPerformedBy(int reviewPerformedBy) {
        this.reviewPerformedBy = reviewPerformedBy;
    }

    public int getFollowUpPerformedBy() {
        return followUpPerformedBy;
    }

    public void setFollowUpPerformedBy(int followUpPerformedBy) {
        this.followUpPerformedBy = followUpPerformedBy;
    }

    public double getCurrentMonthBillableHours() {
        return currentMonthBillableHours;
    }

    public void setCurrentMonthBillableHours(double currentMonthBillableHours) {
        this.currentMonthBillableHours = currentMonthBillableHours;
    }
}
