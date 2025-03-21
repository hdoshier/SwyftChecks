package healthcheck.data;

import healthcheck.data.firestore.Database;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class HealthCheckPeriod implements Serializable {
    private LocalDate startDate;
    private LocalDate endDate;
    private int uniqueId;
    private ArrayList<HealthCheck> healthCheckList;

    public HealthCheckPeriod(LocalDate startDate, LocalDate endDate, int id) {
        this.startDate = startDate;
        this.uniqueId = id;
        this.endDate = endDate;
        this.addHealthChecks();
    }

    public boolean isPeriodComplete() {
        for (HealthCheck i : healthCheckList) {
            if (i.getHealthCheckStatus() != 3) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Object> packagePeriod() {
        HashMap<String, Object> periodData = new HashMap<>();
        periodData.put("startDate", startDate.toString());
        periodData.put("endDate", endDate.toString());
        periodData.put("uniqueId", uniqueId);
        HashMap<String, HashMap<String, Object>> healthChecksMap = new HashMap<>();
        for (HealthCheck i : healthCheckList) {
            healthChecksMap.put(i.getOfficeCode(), i.packageHealthCheck());
        }
        periodData.put("healthChecks", healthChecksMap);
        return periodData;
    }

    private void addHealthChecks() {
        ArrayList<Office> officeList = Database.getInstance().getOfficeList();
        healthCheckList = new ArrayList<>(officeList.size());
        for (Office office : officeList) {
            // Only active offices are added to the health check list
            if (office.isActiveOffice()) {
                HealthCheck check = new HealthCheck(office.getOfficeCode());
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
    public String getPeriodRange() {
        StringBuilder sb = new StringBuilder();
        sb.append(startDate.toString());
        sb.append(" - ");
        sb.append(endDate.toString());
        return sb.toString();
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
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
