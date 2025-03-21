package healthcheck;

import com.google.cloud.firestore.Firestore;
import com.google.gson.Gson;
import healthcheck.data.DataImport;
import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.gui.MainWindow;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println("SwyftChecks Started!");

        // sets the data for the instance.
        Database db = Database.getInstance();

        String filepath = "src/main/csvdata/Offices.csv";
        //DataImport.importOfficeData(filepath);

        //current period
        //Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().minusMonths(1), LocalDate.now().minusDays(1));
        //Database.getInstance().addNewHealthCheckPeriod(LocalDate.now(), LocalDate.now().plusMonths(1));

        runGui();
        //callTest();
    }

    public static void runGui() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    public static void callTest() throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        URI uri = new URI("tel:" + "3162104232");
        desktop.browse(uri);
    }
}