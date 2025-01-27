package healthcheck.data;

import com.google.cloud.firestore.DocumentSnapshot;
import healthcheck.data.firestore.ReadData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MySettings {
    private static MySettings instance;
    private ArrayList<String> users;
    private HashMap<String, String> emailTemplates;

    public static MySettings getInstance() {
        if (instance == null) {
            instance = new MySettings();
        }
        return instance;
    }

    private MySettings() {
        MySettings.instance = this;
        DocumentSnapshot document = ReadData.getSettingsDocument();
        configureUsers(document);
        configureEmailSettings(document);
    }

    // email template settings

    /**
     * Sets the email template map with data retrieved from the database.
     * @param document
     */
    private void configureEmailSettings(DocumentSnapshot document) {
        emailTemplates = ReadData.getEmailTemplates(document);
    }

    /**
     * Gets the email body template based on the name of template.
     * Replaces all the HTML < br >'s with new lines.
     * @param template
     * @return
     */
    public String getTemplate(String template) {
        return emailTemplates.get(template).replaceAll("<br>", "\n");
    }

    /**
     * Gets a list of the template names.
     * @return
     */
    public Set<String> getEmailTemplateNames() {
        return emailTemplates.keySet();
    }

    /**
     * Adds or replaces a template to the template map.
     * Replaces all new lines with an HTML < br > for storage in the database.
     * @param name
     * @param template
     */
    public void putEmailTemplate(String name, String template) {
        emailTemplates.put(name, template.replaceAll("\n", "<br>"));
    }

    /**
     * Deletes the template from the map and database.
     * @param name
     */
    public void removeEmailTemplate(String name) {
        emailTemplates.remove(name);
    }
    //end email template settings


    private void configureUsers(DocumentSnapshot document) {
        users = ReadData.getUsers(document);
    }

    public ArrayList<String> getUsers() {
        return users;
    }
}
