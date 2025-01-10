package healthcheck.data;

import java.awt.*;
import java.net.URI;
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
            body = "Hello " + contactName + ",\n\n" + body;
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
}
