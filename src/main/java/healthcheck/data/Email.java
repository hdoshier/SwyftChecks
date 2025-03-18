package healthcheck.data;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class Email {
    public static void prepEmail(String contactName, String contactEmail, String subjectLine, String body) {
        Desktop desktop = Desktop.getDesktop();
        try{
            StringBuilder sb = new StringBuilder("mailto:");
            sb.append(contactEmail);
            sb.append("?cc=teams@swyftops.com&subject=");
            String subject = URLEncoder.encode(subjectLine, "UTF-8").replace("+", "%20");
            sb.append(subject);
            sb.append("&body=");
            if (!body.equals("")) {
                body = "Hello " + getContactFirstName(contactName) + ",\n\n" + body;
            }
            String encodedBody = URLEncoder.encode(body, "UTF-8").replace("+", "%20");
            sb.append(encodedBody);
            // pipe | = %7C
            // space = %20
            // newline = %0A
            URI uri = new URI(sb.toString());
            desktop.mail(uri);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    public static void prepCall(String phoneNumber) {
        Desktop desktop = Desktop.getDesktop();
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        try {
            URI uri = new URI("tel:" + phoneNumber);
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }



    private static String getContactFirstName(String name) {
        String[] splitName = name.split(" ");
        return splitName[0];
    }
}
