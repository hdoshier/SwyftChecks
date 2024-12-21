package healthcheck.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

public class Office implements Serializable {

    private final String officeCode;
    private String officeName = "";
    private LocalDate lastHealthCheckDate = null;
    private LocalDate execAgreementDate = null;
    private ArrayList<HealthCheck> healthCheckList;
    private String officeOwner = "";
    private String officeOwnerEmail = "";
    private String officePrimaryContactPerson = "";
    private String officePrimaryContactEmail = "";
    private String leadershipNotes = "";
    private String generalNotes = "";
    private int trainingStatus = 0;
    private HashMap<YearMonth, Double> billableHourHistory;



    public Office(String officeCode){
        this.officeCode = officeCode;
        this.healthCheckList = new ArrayList<>(1);
        billableHourHistory = new HashMap<>(4);
    }

    /**
     * <p>Finds and returns the most recent month that has billable hour history.
     *
     * For example: If it's December 2024, but no hour history has been recorded since
     * September 2024, this function will return YearMonth of 2024 - 09.</p>
     *
     * @return YearMonth
     */
    public YearMonth getMostRecentBillableHistory() {
        if (billableHourHistory.isEmpty()) {
            return null;
        }

        YearMonth month = YearMonth.now();
        while (true) {
            if (billableHourHistory.containsKey(month)) {
                return month;
            }
            month = month.minusMonths(1);
        }
    }

    public HashMap<YearMonth, Double> getBillableHourHistory() {
        return billableHourHistory;
    }

    public void addBillableHourHistory(YearMonth yearMonth, Double hourCount) {
        billableHourHistory.put(yearMonth, hourCount);
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public LocalDate getExecAgreementDate() {
        return execAgreementDate;
    }

    public void setExecAgreementDate(LocalDate execAgreementDate) {
        this.execAgreementDate = execAgreementDate;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public LocalDate getLastHealthCheckDate() {
        return lastHealthCheckDate;
    }

    public void setLastHealthCheckDate(LocalDate lastHealthCheckDate) {
        this.lastHealthCheckDate = lastHealthCheckDate;
    }

    public ArrayList<HealthCheck> getHealthCheckList() {
        return healthCheckList;
    }

    public void setHealthCheckList(ArrayList<HealthCheck> healthCheckList) {
        this.healthCheckList = healthCheckList;
    }

    public String getOfficeOwner() {
        return officeOwner;
    }

    public void setOfficeOwner(String officeOwner) {
        this.officeOwner = officeOwner;
    }

    public String getOfficeOwnerEmail() {
        return officeOwnerEmail;
    }

    public void setOfficeOwnerEmail(String officeOwnerEmail) {
        this.officeOwnerEmail = officeOwnerEmail;
    }

    public String getOfficePrimaryContactPerson() {
        return officePrimaryContactPerson;
    }

    public void setOfficePrimaryContactPerson(String officePrimaryContactPerson) {
        this.officePrimaryContactPerson = officePrimaryContactPerson;
    }

    public String getOfficePrimaryContactEmail() {
        return officePrimaryContactEmail;
    }

    public void setOfficePrimaryContactEmail(String officePrimaryContactEmail) {
        this.officePrimaryContactEmail = officePrimaryContactEmail;
    }

    public String getLeadershipNotes() {
        return leadershipNotes;
    }

    public void setLeadershipNotes(String leaderhsipNotes) {
        this.leadershipNotes = leaderhsipNotes;
    }

    public int getTrainingStatus() {
        return trainingStatus;
    }

    public void setTrainingStatus(int trainingStatus) {
        this.trainingStatus = trainingStatus;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.officeCode);
        sb.append(" | ");

        sb.append(this.officeName);
        sb.append(" | ");

        sb.append(this.officeOwner);
        sb.append(" | ");

        sb.append(this.officePrimaryContactEmail);
        return sb.toString();
    }

}