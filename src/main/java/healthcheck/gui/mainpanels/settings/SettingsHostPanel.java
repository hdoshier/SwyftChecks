package healthcheck.gui.mainpanels.settings;

import healthcheck.data.MySettings;
import healthcheck.gui.MainWindow;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckPeriodDetailsPanel;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckPeriodListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsHostPanel extends JPanel {
    private MainWindow parent;
    private MySettings settings;
    private JPanel contentPanel = null;
    private GridBagConstraints gbc;
    private JLabel activeLabel;

    public SettingsHostPanel (MainWindow parent) {
        this.parent = parent;
        this.settings = MySettings.getInstance();
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // create setting selection menu
        gbc.weightx = 1.0;
        this.add(createSettingsSelectionPanel(), gbc);

        // set content panel, default to templates
        gbc.weighty = 1.0;
        setContentPanel(new SettingsEmailTemplatesPanel(this, settings));
    }

    public JPanel createSettingsSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints selectionGbc = new GridBagConstraints();
        selectionGbc.weightx = 1.0;
        selectionGbc.fill = GridBagConstraints.BOTH;
        //Templates
        JLabel templateLabel = new JLabel("Email Templates");
        activeLabel = templateLabel;
        formatLabel(templateLabel);
        panel.add(templateLabel, selectionGbc);

        //Users
        selectionGbc.gridx = 1;
        JLabel usersLabel = new JLabel("Users");
        formatLabel(usersLabel);
        panel.add(usersLabel, selectionGbc);

        //Excluded offices
        selectionGbc.gridx = 2;
        JLabel excludedLabel = new JLabel("Excluded Offices");
        formatLabel(excludedLabel);
        panel.add(excludedLabel, selectionGbc);

        return panel;
    }

    private void formatLabel(JLabel label) {
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        label.setBackground(activeLabel == label ? new Color(255, 151, 25) : new Color(0, 122, 178));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (activeLabel != label) label.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                activeLabel.setBackground(new Color(0, 122, 178));
                label.setBackground(new Color(255, 151, 25));
                activeLabel = label;
                switchView(label.getText(), label);
            }
        });
    }

    public void saveSettings() {

    }

    private void setContentPanel(JPanel panel) {
        if (contentPanel != null) {
            this.remove(contentPanel);
        }
        gbc.gridy = 1;
        contentPanel = panel;
        this.add(contentPanel, gbc);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void switchView(String view, JLabel label) {
        System.out.println("Settings View Selected: " + view);
        if(view.equals("Email Templates")) {
            setContentPanel(new SettingsEmailTemplatesPanel(this, settings));
        }
        if(view.equals("Users")) {
            setContentPanel(new SettingsUsersPanel(this, settings));
        }
        if(view.equals("Excluded Offices")) {
            setContentPanel(new SettingsExcludedOfficesPanel(this, settings));
        }
    }
}
