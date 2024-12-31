package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.HealthCheckPeriod;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class HealthCheckPeriodDetailsPanel extends JPanel {
    private MainWindow parent;
    private HealthCheckPeriod period;

    public HealthCheckPeriodDetailsPanel(MainWindow parent, HealthCheckPeriod period) {
        this.parent = parent;
        this.period = period;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel("Details Panel" + period.toString()));
    }
}
