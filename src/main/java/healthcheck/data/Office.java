package healthcheck.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

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
    private String leaderhsipNotes = "";
    private String generalNotes = "";
    private int trainingStatus = 0;
    private ArrayList<BillableHoursTuple> billableHourHistoryList;



    public Office(String officeCode){
        this.officeCode = officeCode;
        this.healthCheckList = new ArrayList<>(1);
        billableHourHistoryList = new ArrayList<>(5);
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public String getOfficeName() {
        return officeName;
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

    public String getLeaderhsipNotes() {
        return leaderhsipNotes;
    }

    public void setLeaderhsipNotes(String leaderhsipNotes) {
        this.leaderhsipNotes = leaderhsipNotes;
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