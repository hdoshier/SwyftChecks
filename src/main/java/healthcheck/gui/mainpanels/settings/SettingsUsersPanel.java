package healthcheck.gui.mainpanels.settings;

import healthcheck.data.MySettings;

import javax.swing.*;
import java.awt.*;

public class SettingsUsersPanel  extends JPanel {
    private SettingsHostPanel parent;
    private MySettings settings;

    public SettingsUsersPanel(SettingsHostPanel parent, MySettings settings) {
        this.parent = parent;
        this.settings = settings;

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel("Users"));
    }
}
