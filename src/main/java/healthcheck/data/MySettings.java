package healthcheck.data;

import java.io.Serializable;
import java.util.ArrayList;

public class MySettings implements Serializable {
    private static MySettings instance;
    private ArrayList<String> users;
    private ArrayList<String> emailTemplates;


    private MySettings() {
        MySettings.instance = this;
        users = new ArrayList<>(6);
        emailTemplates = new ArrayList<>(3);
    }

    public static MySettings getInstance() {
        if (instance == null) {
            instance = new MySettings();
        }
        return instance;
    }

    //users
    public void addUser(String name) {
        users.add(name);
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    //templates
    public void addEmailTemplate(String template) {
        emailTemplates.add(template);
    }

    public ArrayList<String> getEmailTemplates() {
        return emailTemplates;
    }
}
