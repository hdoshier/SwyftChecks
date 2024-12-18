package healthcheck;

import healthcheck.data.DataImport;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.net.URI;
import java.net.URLEncoder;
import java.awt.Desktop;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello");

        String filepath = "src/main/csvdata/Offices.csv";

        DataImport.importOfficeData(filepath);

        runGui();
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