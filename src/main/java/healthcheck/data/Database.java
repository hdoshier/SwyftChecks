package healthcheck.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Database implements Serializable {
    private static Database instance = null;
    private HashMap<String, Office> officeMap;
    private ArrayList<Office> officeList;
    private ArrayList<HealthCheckPeriod> healthCheckPeriodList;
    private HashSet<String> excludedOffices;

    private Database(){
        Database.instance = this;
        officeMap = new HashMap<>();
        excludedOffices = new HashSet<>(50);
        officeList = new ArrayList<>();
    }

    public static Database getInstance() {
        if(Database.instance == null) {
            new Database();
        }
        return Database.instance;
    }

    public ArrayList<Office> getOfficeList() {return officeList;}

    public HashMap<String, Office> getOfficeMap() {
        return officeMap;
    }

    public HashSet<String> getExcludedOffices() {
        return excludedOffices;
    }

    public Office createOffice(String officeCode) {
        if (officeMap.containsKey(officeCode)) {
            return null;
        }
        Office office = new Office(officeCode);
        officeMap.put(officeCode, office);
        return office;
    }
}