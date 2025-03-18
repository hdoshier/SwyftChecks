package healthcheck.gui.mainpanels.offices;

import healthcheck.data.Office;

import javax.swing.*;
import java.awt.*;

public class HealthCheckReviewPanel extends JPanel {

    public HealthCheckReviewPanel(OfficePanel parent, Office office) {
        this.setLayout(new GridLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.add(new JLabel("Health Check Review | WIP"), gbc);
    }
}
