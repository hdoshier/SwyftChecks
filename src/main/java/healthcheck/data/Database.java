package healthcheck.data;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Database implements Serializable {
    private static Database instance = null;
    private HashMap<String, Office> officeMap;
    private ArrayList<Office> officeList;
    private ArrayList<HealthCheckPeriod> healthCheckPeriodList;
    private HashSet<String> excludedOffices;
    private MySettings settings;

    private Database(){
        Database.instance = this;
        officeMap = new HashMap<>();
        excludedOffices = new HashSet<>(50);
        officeList = new ArrayList<>();
        settings = MySettings.getInstance();
        healthCheckPeriodList = new ArrayList<>();
    }

    public static Database getInstance() {
        if(Database.instance == null) {
            new Database();
        }
        return Database.instance;
    }

    public HealthCheckPeriod addNewHealthCheckPeriod(LocalDate startDate, LocalDate endDate) {
        HealthCheckPeriod period = new HealthCheckPeriod(startDate, endDate);
        healthCheckPeriodList.addFirst(period);
        return period;
    }

    public ArrayList<Office> getOfficeList() {return officeList;}

    public HashMap<String, Office> getOfficeMap() {
        return officeMap;
    }

    public HashSet<String> getExcludedOffices() {
        return excludedOffices;
    }

    public ArrayList<HealthCheckPeriod> getHealthCheckPeriodList() {
        return healthCheckPeriodList;
    }

    public Office createOffice(String officeCode) {
        if (officeMap.containsKey(officeCode)) {
            return null;
        }
        Office office = new Office(officeCode);
        officeMap.put(officeCode, office);
        return office;
    }

    public void sortOfficeList() {
        Collections.sort(officeList, Comparator.comparing(Office::getOfficeCode));
    }

    public void saveDatabase() {
        try {

            FileOutputStream file = new FileOutputStream("database.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            file.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDatabase() {
        try {
            FileInputStream file = new FileInputStream("database.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            Database db = (Database) in.readObject();
            file.close();
            in.close();
            Database.setDatabase(db);
            //MySettings.loadSettings(db.getSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDatabase(Database db) {
        instance = db;
    }
}