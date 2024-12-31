package healthcheck.data;

import healthcheck.data.firestore.Database;

import java.io.Serializable;
import java.time.LocalDate;
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

    public HealthCheck getHealthCheckByOffice(Office office) {
        return getHealthCheckByOffice(office.getOfficeCode());
    }
    public HealthCheck getHealthCheckByOffice(String officeCode) {
        //TODO retrieve from database
        for (HealthCheck check : healthCheckList) {
            if (check.getOffice().getOfficeCode().equals(officeCode)) {
                return check;
            }
        }
        return null;
    }

    private void addHealthChecks() {
        ArrayList<Office> officeList = Database.getInstance().getAllOfficesList();
        healthCheckList = new ArrayList<>(officeList.size());
        for (Office office : officeList) {
            HealthCheck check = new HealthCheck(office);
            healthCheckList.add(check);
        }
    }

    public ArrayList<HealthCheck> getListByAssignedTo (String assignedTo) {
        ArrayList<HealthCheck> filteredList = new ArrayList<>();
        for (HealthCheck check : healthCheckList) {
            if (check.getAssignedTo().equals(assignedTo)) {
                filteredList.add(check);
            }
        }
        return filteredList;
    }

    public ArrayList<HealthCheck> getListByStatus (int status) {
        ArrayList<HealthCheck> filteredList = new ArrayList<>();
        for (HealthCheck check : healthCheckList) {
            if (check.getHealthCheckStatus() == status){
                filteredList.add(check);
            }
        }
        return filteredList;
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
