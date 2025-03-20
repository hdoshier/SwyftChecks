package healthcheck.data;

import healthcheck.data.firestore.Database;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HealthCheckPeriod implements Serializable {
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<HealthCheck> healthCheckList;

    public HealthCheckPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.addHealthChecks();
    }

    private void addHealthChecks() {
        ArrayList<Office> officeList = Database.getInstance().getOfficeList();
        healthCheckList = new ArrayList<>(officeList.size());
        for (Office office : officeList) {
            // Only active offices are added to the health check list
            if (office.isActiveOffice()) {
                HealthCheck check = new HealthCheck(office);
                healthCheckList.add(check);
            }
        }
    }

    public String getPeriodDateRange() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        StringBuilder sb = new StringBuilder();
        sb.append(startDate.format(formatter));
        sb.append(" - ");
        sb.append(endDate.format(formatter));
        return sb.toString();
    }


    // getters and setters
    public String getIdentifier() {
        StringBuilder sb = new StringBuilder();
        sb.append(startDate.toString());
        sb.append(" - ");
        sb.append(endDate.toString());
        return sb.toString();
    }

    public ArrayList<HealthCheck> getHealthCheckList() {
        return healthCheckList;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return startDate.toString();
    }
}
