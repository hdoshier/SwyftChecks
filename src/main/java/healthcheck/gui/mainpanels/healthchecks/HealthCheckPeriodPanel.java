package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.HealthCheckPeriod;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HealthCheckPeriodPanel extends JPanel {
    private HealthCheckPeriod period;
    private MainWindow parent;
    private JLabel detailsLabel;
    private JLabel healthCheckLabel;
    private boolean detailsLabelActive;
    private JPanel contentPanel = null;
    private GridBagConstraints mainGbc;

    public HealthCheckPeriodPanel (MainWindow parent, HealthCheckPeriod period) {
        this.period = period;
        this.parent = parent;
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(new Color(0, 122, 178));
        if (period == null) {
            setThisPanelAsEmptyPeriod();
            return;
        }
        this.setLayout(new GridBagLayout());
        mainGbc = new GridBagConstraints();
        // add selection panel
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;
        this.add(createSelectionPanel(), mainGbc);

        // set selection to details
        mainGbc.weighty = 1.0;
        mainGbc.gridy = 1;
        setContentPanel(new HealthCheckPeriodDetailsPanel(parent, period));
    }

    private void setContentPanel(JPanel panel) {
        if (contentPanel != null) {
            this.remove(contentPanel);
        }
        contentPanel = panel;
        this.add(contentPanel, mainGbc);
        this.repaint();
        this.revalidate();
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(0, 122, 178));
        gbc.weightx = 1.0;

        detailsLabel = new JLabel("Details");
        formatLabel(detailsLabel, true);
        detailsLabelActive = true;
        detailsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                detailsLabel.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!detailsLabelActive) detailsLabel.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switchView("Details");
            }
        });

        panel.add(detailsLabel, gbc);


        healthCheckLabel = new JLabel("Health Checks");
        formatLabel(healthCheckLabel, false);
        healthCheckLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                healthCheckLabel.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (detailsLabelActive) healthCheckLabel.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switchView("Health Checks");
            }
        });
        gbc.gridx = 1;
        panel.add(healthCheckLabel, gbc);
        return panel;
    }

    private void formatLabel(JLabel label, boolean isActive) {
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        if (isActive) {
            label.setBackground(new Color(255, 151, 25));
        } else {
            label.setBackground(new Color(0, 122, 178));
        }
    }

    private void setThisPanelAsEmptyPeriod() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel("No health check periods created."));
    }

    private void switchView(String view) {

        if(view.equals("Health Checks")) {
            System.out.println(view);
            detailsLabelActive = false;
            detailsLabel.setBackground(new Color(0, 122, 178));
            healthCheckLabel.setBackground(new Color(255, 151, 25));
            setContentPanel(new HealthCheckPeriodListPanel(parent, period));
        }
        if(view.equals("Details")) {
            System.out.println(view);
            detailsLabelActive = true;
            healthCheckLabel.setBackground(new Color(0, 122, 178));
            detailsLabel.setBackground(new Color(255, 151, 25));
            setContentPanel(new HealthCheckPeriodDetailsPanel(parent, period));
        }
    }
}
