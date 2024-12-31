package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class Database {
    private static Database instance = null;
    private Firestore firestore;
    private ArrayList<Office> officeList;
    private HashSet<String> excludedOffices;
    private ArrayList<HealthCheckPeriod> healthCheckPeriodList;

    private Database() {
        instance = this;
        firestore = initializeFirebase();
        officeList = ReadData.getOfficeList(firestore);
        excludedOffices = ReadData.getExcludedOfficeSet(firestore);
        healthCheckPeriodList = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Checks to see if the office is supposed to be excluded from the database.
     * @param officeCode
     * @return
     */
    public boolean isExcludedOffice(String officeCode) {
        return excludedOffices.contains(officeCode);
    }

    public void addExcludedOffice(String officeCode) {
        excludedOffices.add(officeCode);
        WriteData.writeExcludedOffice(excludedOffices);
    }

    /**
     * Checks to see if the database contains the specified office.
     * @param office
     * @return true if the office exist in the database, false if not.
     */
    public boolean containsOffice(Office office) {
        return containsOffice(office.getOfficeCode());
    }

    public boolean containsOffice(String officeCode) {
        if (officeList == null) {
            getAllOfficesList();
        }
        for (Office office : officeList) {
            if (office.getOfficeCode().equals(officeCode)) {
                return true;
            }
        }
        return false;
    }

    public HealthCheckPeriod addNewHealthCheckPeriod(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = new HealthCheckPeriod(start, end);
        healthCheckPeriodList.add(period);
        return period;
    }


    // getters and setters


    public ArrayList<HealthCheckPeriod> getHealthCheckPeriodList() {
        return healthCheckPeriodList;
    }

    public void setHealthCheckPeriodList(ArrayList<HealthCheckPeriod> healthCheckPeriodList) {
        this.healthCheckPeriodList = healthCheckPeriodList;
    }

    public static Firestore getFirestore() {
        return instance.firestore;
    }

    public HashSet<String> getExcludedOffices() {
        return excludedOffices;
    }

    public ArrayList<Office> getAllOfficesList(){
        return officeList;
    }

    public Firestore initializeFirebase() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./firebase-key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://swyftchecks-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);

            return  FirestoreClient.getFirestore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
