package healthcheck.gui.mainpanels.reports;

import healthcheck.data.firestore.Database;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class BillableHourReportPanel extends JPanel {
    Database db;
    MainWindow parent;
    public BillableHourReportPanel(MainWindow parent) {
        this.db = Database.getInstance();
        this.parent = parent;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


    }
}
