package healthcheck.data.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.MySettings;
import healthcheck.data.Office;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    private static Database instance = null;
    private Firestore firestore;
    private ArrayList<Office> officeList;
    private ArrayList<HealthCheckPeriod> healthCheckPeriodList;
    private MySettings settings;
    private boolean inactiveOfficesLoaded;
    private LocalDate databaseCreationDate = LocalDate.now();

    private Database() {
        instance = this;
        firestore = initializeFirebase();
        officeList = ReadData.getActiveOfficeList();
        healthCheckPeriodList = new ArrayList<>();
        settings = MySettings.getInstance();
        inactiveOfficesLoaded = false;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public HealthCheckPeriod addNewHealthCheckPeriod(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = new HealthCheckPeriod(start, end);
        healthCheckPeriodList.addFirst(period);
        return period;
    }

    public void switchOfficeStatus(Office office) {
        System.out.println(office.isActiveOffice());
        office.setActiveOffice(!office.isActiveOffice());
        System.out.println(office.isActiveOffice());
        WriteData.switchOfficeCollection(office);
    }

    public void loadInactiveOffices() {
        // skip if the inactive offices have already been added to the officeList
        if (inactiveOfficesLoaded) {
            return;
        }

        inactiveOfficesLoaded = true;
        officeList.addAll(ReadData.getInactiveOfficeList());
    }

    public Office getOffice(String officeCode) {
        for (Office i : officeList) {
            if (i.getOfficeCode().equals(officeCode)) {
                return i;
            }
        }
        return null;
    }

    public void replaceOffice(Office office) {
        for (int i = 0; i < officeList.size(); i++) {
            if (officeList.get(i).equals(office)) {
                officeList.set(i, office);
                System.out.println("Office Replaced");
                return;
            }
        }
    }


    // getters and setters
    public ArrayList<HealthCheckPeriod> getHealthCheckPeriodList() {
        return healthCheckPeriodList;
    }

    public ArrayList<Office> getOfficeList(){
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

    public void clearDatabase() {
        instance = null;
    }

    public static Firestore getFirestore() {
        return instance.firestore;
    }
}
