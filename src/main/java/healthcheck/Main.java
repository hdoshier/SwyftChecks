package healthcheck;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import healthcheck.data.DataImport;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.net.URI;
import java.net.URLEncoder;
import java.awt.Desktop;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("SwyftChecks Started!");

        System.out.println("Hello");

        String filepath = "src/main/csvdata/Offices.csv";

        //DataImport.importOfficeData(filepath);
        //Database.getInstance().loadDatabase();
        //Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(3));
        //Database.getInstance().addNewHealthCheckPeriod(LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2));
        //Database.getInstance().addNewHealthCheckPeriod(LocalDate.now(), LocalDate.now().plusMonths(1));

        runGui();




        //Office office = new Office("abc 123");
        //office.setOfficeName("Hunters heros");
        //office.addBillableHourHistory(YearMonth.now(), 1.23);
        //WriteData.writeOffice(office);
        //WriteData.writeOfficeObject(office);
        //Office office1 = new Office("abe");

        //System.out.println(FirestoreDatabase.containsExcludedOffice("CFC TX ART"));

    }



    public String mainTest(){
        return "test";
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