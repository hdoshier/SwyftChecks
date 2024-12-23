package healthcheck.gui.mainpanels;

import healthcheck.data.HealthCheck;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public class HealthCheckPanel extends JPanel implements ActionListener {
    private MainWindow parent;
    private HealthCheck check;

    public HealthCheckPanel(MainWindow parent, HealthCheck check) {
        this.parent = parent;
        this.check = check;
    }
}
