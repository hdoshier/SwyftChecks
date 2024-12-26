package healthcheck.data.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import healthcheck.data.Office;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FirestoreDatabase {
    private static Firestore instance = null;

    public static Firestore getFirestore() {
        if (instance == null) {
            instance = initializeFirebase();
        }
        return instance;
    }

    public static ArrayList<Office> getAllOfficesList(){
        if(instance == null) {getFirestore();}
        // get collection from db
        ApiFuture<QuerySnapshot> query = instance.collection("offices").get();

        try {
            QuerySnapshot querySnapshot = query.get();
            ArrayList<Office> list = new ArrayList<>(querySnapshot.size());
            for (QueryDocumentSnapshot document : querySnapshot) {
                list.add(ReadData.readOffice(document));
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(1);
        }

    }

    /**
     * Checks to see if the office is supposed to be excluded from the database.
     * @param officeCode
     * @return
     */
    public static boolean isExcludedOffice(String officeCode) {
        if(instance == null) {getFirestore();}
        try {
            // Get the document reference
            DocumentReference documentRef = instance.collection("excludedOffices").document(officeCode);

            // Fetch the document snapshot
            ApiFuture<DocumentSnapshot> future = documentRef.get();
            DocumentSnapshot document = future.get();
            //return documentRef

            // Return true if the document exists
            return document.exists();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks to see if the database contains the specified office.
     * @param office
     * @return true if the office exist in the database, false if not.
     */
    public static boolean containsOffice(Office office) {
        return containsOffice(office.getOfficeCode());
    }

    public static boolean containsOffice(String officeCode) {
        if(instance == null) {getFirestore();}
        try {
            // Get the document reference
            DocumentReference documentRef = instance.collection("offices").document(officeCode);

            // Fetch the document snapshot
            ApiFuture<DocumentSnapshot> future = documentRef.get();
            DocumentSnapshot document = future.get();

            // Return true if the document exists
            return document.exists();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        }
    }

    private static Firestore initializeFirebase() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./firebase-key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://swyftchecks-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);

            return FirestoreClient.getFirestore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
