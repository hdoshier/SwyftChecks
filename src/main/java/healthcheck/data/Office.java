package healthcheck.data;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.annotation.DocumentId;
import healthcheck.Main;
import org.threeten.bp.DateTimeUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Office {

    private String officeCode;
    private String officeName = "";
    private String lastHealthCheckDate = null;
    private String execAgreementDate = null;
    private String officeOwner = "";
    private String officeOwnerEmail = "";
    private String officePrimaryContactPerson = "";
    private String officePrimaryContactEmail = "";
    private String leadershipNotes = "";
    private String generalNotes = "";
    private int trainingStatus = 0;
    private HashMap<String, Double> billableHourHistory;

    public Office(String officeCode){
        this.officeCode = officeCode;
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
            if (billableHourHistory.containsKey(month.toString())) {
                return month;
            }
            month = month.minusMonths(1);
        }
    }

    public void addBillableHourHistory(YearMonth yearMonth, Double hourCount) {
        billableHourHistory.put(yearMonth.toString(), hourCount);
    }

    // getters and setters


    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getLastHealthCheckDate() {
        return lastHealthCheckDate;
    }

    public void setLastHealthCheckDate(String lastHealthCheckDate) {
        this.lastHealthCheckDate = lastHealthCheckDate;
    }

    public String getExecAgreementDate() {
        return execAgreementDate;
    }

    public void setExecAgreementDate(String execAgreementDate) {
        this.execAgreementDate = execAgreementDate;
    }

    // LocalDate getters and setters
    public LocalDate getLastHealthCheckLocalDate() {
        if (lastHealthCheckDate == null) {
            return null;
        }
        return LocalDate.parse(lastHealthCheckDate);

    }

    public void setLastHealthCheckDate(LocalDate lastHealthCheckDate) {
        if (lastHealthCheckDate != null) {
            this.lastHealthCheckDate = lastHealthCheckDate.toString();
        }
    }

    public LocalDate getExecAgreementLocalDate() {
        if (execAgreementDate == null) {
            return null;
        }
        return LocalDate.parse(execAgreementDate);

    }

    public void setExecAgreementDate(LocalDate execAgreementDate) {
        if (execAgreementDate != null) {
            this.execAgreementDate = execAgreementDate.toString();
        }
    }

    //end local date getters and setters

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

    public void setLeadershipNotes(String leadershipNotes) {
        this.leadershipNotes = leadershipNotes;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public int getTrainingStatus() {
        return trainingStatus;
    }

    public void setTrainingStatus(int trainingStatus) {
        this.trainingStatus = trainingStatus;
    }

    public HashMap<String, Double> getBillableHourHistory() {
        return billableHourHistory;
    }

    public void setBillableHourHistory(HashMap<String, Double> billableHourHistory) {
        this.billableHourHistory = billableHourHistory;
    }

    public boolean equals(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Office office) {
            return this.officeCode.equals(office.getOfficeCode());
        } else {
            return false;
        }
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