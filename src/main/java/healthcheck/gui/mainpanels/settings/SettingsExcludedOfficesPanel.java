package healthcheck.gui.mainpanels.settings;

import healthcheck.data.MySettings;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class SettingsExcludedOfficesPanel extends JPanel {
    private SettingsHostPanel parent;
    private MySettings settings;

    public SettingsExcludedOfficesPanel(SettingsHostPanel parent, MySettings settings) {
        this.parent = parent;
        this.settings = settings;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel("Excluded Offices"));
    }
}
