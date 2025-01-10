package healthcheck;

import healthcheck.data.DataImport;
import healthcheck.data.Office;
import healthcheck.data.customlists.OfficeList;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.data.firestore.WriteData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.net.URI;
import java.net.URLEncoder;
import java.awt.Desktop;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("SwyftChecks Started!");
/*
        // sets the data for the instance.
        Database db = Database.getInstance();

        String filepath = "src/main/csvdata/Offices.csv";
        //DataImport.importOfficeData(filepath);

        Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(2));
        Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1));
        //current period
        Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(1));

        runGui();

 */

        OfficeList list = new OfficeList();
        list.add(new Office("1"));
        list.add(new Office("2"));
        list.add(new Office("3"));
        list.add(new Office("4"));

        for (Office i : list) {
            System.out.println(i.getOfficeCode());
        }
    }

    public static void runGui() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    public static void createEmail() {
        String emailTemplate = "Hello Hunter,\nHow are you doing today?\n\n[INSERT_SIGNATURE_HERE]";

        Desktop desktop = Desktop.getDesktop();
        try{
            String encodedBody = URLEncoder.encode(emailTemplate, "UTF-8").replace("+", "%20");
            // pipe | = %7C
            // space = %20
            // newline = %0A
            URI uri = new URI("mailto:hunter.doshier@swyftops.com?cc=teams@swyftops.com&subject=SwyftOps%20%7C%20DEV%20KS%20WTC%20%7C%20Healthcheck&body="+encodedBody);
            desktop.mail(uri);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
}