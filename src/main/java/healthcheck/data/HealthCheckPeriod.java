package healthcheck.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class HealthCheckPeriod implements Serializable {
    private LocalDate startDate;
    private LocalDate endDate;
    private HashMap<String, HealthCheck> healthCheckMap;

    public HealthCheckPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void buildHealthCheckMap() {
        HashMap<String, Office> officeMap = Database.getInstance().getOfficeMap();
        healthCheckMap = new HashMap<>(officeMap.size());
        for (String officeCode : officeMap.keySet()) {
            healthCheckMap.put(officeCode, new HealthCheck(this));
        }
    }

    public HashMap<String, HealthCheck> getHealthCheckMap() {
        return healthCheckMap;
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
}
