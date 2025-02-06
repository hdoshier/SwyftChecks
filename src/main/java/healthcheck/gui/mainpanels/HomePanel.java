package healthcheck.gui.mainpanels;

import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private MainWindow parent;

    public HomePanel (MainWindow parent) {
        this.parent = parent;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        // number of pending health checks
        // number of reviewed health checks
        // number of completed health checks
        // completed percentage
        // days left in health check period
    }
}
