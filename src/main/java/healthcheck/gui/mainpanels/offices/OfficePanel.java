package healthcheck.gui.mainpanels.offices;

import healthcheck.data.MyGlobalVariables;
import healthcheck.data.Office;
import healthcheck.data.firestore.WriteData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *The OfficePanel gui class.
 *
 *<p>This class creates the panel that manages Office specific data.
 *
 *@author Hunter Doshier
 *
 *@version 0.1
 */
public class OfficePanel extends JPanel implements ActionListener {
    MainWindow mainWindow;
    OfficeListPanel officeListPanel;
    Office office;
    JPanel hostPanel;
    private JTextArea leadershipNotesField;
    private JTextArea generalNotesField;
    private JTextArea contactNotesField;
    private JButton changeStatusButton;
    private OfficeDetailsPanel officeDetailsPanel;

    public OfficePanel(MainWindow mainWindow, OfficeListPanel officeListPanel, Office office) {
        this.officeListPanel = officeListPanel;
        this.mainWindow = mainWindow;
        buildOfficePanel(office);
    }

    public OfficePanel(JPanel parent, Office office) {
        this.hostPanel = parent;
        buildOfficePanel(office);
    }

    private void buildOfficePanel(Office office) {
        this.office = office;
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // navigation panel
        this.add(buildNavPanel(), gbc);

        // office data/info panel
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        gbc.weightx = 0.3333;

        // office info panel
        gbc.gridheight = 2;
        officeDetailsPanel = new OfficeDetailsPanel(this, office);
        this.add(officeDetailsPanel, gbc);

        // contact info panel
        gbc.gridheight = 1;
        gbc.gridx = 1;
        this.add(createNotesPanel(), gbc);

        gbc.gridy = 2;
        this.add(new HealthCheckReviewPanel(this, office), gbc);

        // billable hours panel
        gbc.gridx = 2;
        gbc.weightx = 0.25;
        gbc.gridheight = 2;
        gbc.gridy = 1;
        this.add(createBillableHoursPanel(), gbc);

    }



    private JPanel buildNavPanel() {
        JPanel navPanel = new JPanel(new GridBagLayout());
        navPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        navPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weighty = 0.05; // Further reduce vertical weight to minimize height

        // Left buttons (Prev/Next)
        gbc.gridx = 0;
        gbc.weightx = 0;
        navPanel.add(createButtonPanel(
                new String[]{"Prev", "Next"},
                new String[]{"prev", "next"},
                FlowLayout.LEFT
        ), gbc);

        // Center office code and name
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel navOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 17, 0));
        navOptionsPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        addNavItem(navOptionsPanel, office.getOfficeCode());
        addNavItem(navOptionsPanel, office.getOfficeName());
        navPanel.add(navOptionsPanel, gbc);

        // Right buttons (Save/Back)
        gbc.gridx = 2;
        gbc.weightx = 0;
        navPanel.add(createButtonPanel(
                new String[]{"Save", "Back"},
                new String[]{"save", "back"},
                FlowLayout.RIGHT
        ), gbc);

        // Enforce a compact preferred height
        navPanel.setPreferredSize(new Dimension(navPanel.getPreferredSize().width, 40)); // Reduced to 40 pixels
        navPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Cap maximum height

        return navPanel;
    }

    // Helper method to create button panels
    private JPanel createButtonPanel(String[] labels, String[] commands, int alignment) {
        JPanel panel = new JPanel(new FlowLayout(alignment, 5, 0)); // Added small horizontal gap (5)
        panel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(labels[i]);
            button.setActionCommand(commands[i]);
            button.addActionListener(this);
            button.setMargin(new Insets(2, 5, 2, 5)); // Reduce internal padding of buttons
            panel.add(button);
        }
        return panel;
    }

    // Modified addNavItem to control label height and background
    private void addNavItem(JPanel panel, String name) {
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setForeground(Color.WHITE);
        navItem.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 30)); // Slightly smaller font to reduce height
        navItem.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10)); // Minimal vertical padding (2, 2)
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Limit height to 30 pixels
        panel.add(navItem);
    }

    private JPanel createNotesPanel() {
        GridBagConstraints gbc = gbcFactory();
        gbc.insets = new Insets(2, 10, 15, 10);
        gbc.gridwidth = 3;
        JPanel panel = panelFactory("Notes", gbc);

        // leadership notes
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        leadershipNotesField = new JTextArea(office.getLeadershipNotes());
        leadershipNotesField.setLineWrap(true);
        panel.add(getDefaultDataPanel(leadershipNotesField, "Leadership Notes"), gbc);

        // general notes
        gbc.gridx = 1;
        generalNotesField = new JTextArea(office.getGeneralNotes());
        generalNotesField.setLineWrap(true);
        panel.add(getDefaultDataPanel(generalNotesField, "General Notes"), gbc);

        // contact notes
        gbc.gridx = 2;
        contactNotesField = new JTextArea(office.getContactNotes());
        contactNotesField.setLineWrap(true);
        panel.add(getDefaultDataPanel(contactNotesField, "Contact Notes"), gbc);

        return panel;
    }

    private JPanel createBillableHoursPanel() {
        GridBagConstraints gbc = gbcFactory();
        JPanel panel = panelFactory("Billable Hour History", gbc);
        JScrollPane scrollPane = new JScrollPane();
        JPanel hourHistoryHostPanel = new JPanel();
        hourHistoryHostPanel.setLayout(new BoxLayout(hourHistoryHostPanel, BoxLayout.Y_AXIS));

        HashMap<String, Double> map = office.getBillableHourHistory();

        for (String month : office.getSortedBillableHourHistoryList()) {
            Double hourCount = map.get(month);
            JLabel label = new JLabel(month + " : " + hourCount);
            hourHistoryHostPanel.add(label);
        }

        scrollPane.setViewportView(hourHistoryHostPanel);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);
        return panel;
    }

    private JPanel getDefaultDataPanel(JComponent component, String label) {
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel(label));
        dataPanel.add(component);
        return dataPanel;
    }

    private GridBagConstraints gbcFactory() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);
        return gbc;
    }

    private JPanel panelFactory (String header, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label = new JLabel(header);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, gbc);

        return panel;
    }

    public void changeStatus() {
        System.out.println("Changing status for: " + office.getOfficeName());
        //confirmStatusChange();
        //TODO manage status change
        //updateOfficeData();
        //Database db = Database.getInstance();
        //db.switchOfficeStatus(office);
        //changeStatusButton.setText(activeOffice ? "Deactivate" : "Activate");
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);
        if (actionCommand.equals("save")) {
            updateOfficeData();
            WriteData.saveOffice(office);
        }
        if (actionCommand.equals("back")) {
            officeListPanel.buildOfficeListTable();
            mainWindow.loadPanel(officeListPanel);
        }
        if (actionCommand.equals("next")) {
            officeListPanel.loadNextOffice();
        }
        if (actionCommand.equals("prev")) {
            officeListPanel.loadPreviousOffice();
        }
    }

    private boolean confirmStatusChange(boolean activeOffice) {
        String[] options = {"Yes", "No", "Cancel"};
        String statusOption = activeOffice ? "deactivate" : "activate";
        String text = "Are you sure you want to " + statusOption + " this office?";
        int n = JOptionPane.showOptionDialog(this, text, "Confirm",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);
        // n is == 0 if yes is clicked.
        return n == 0;
    }

    private void updateOfficeData() {
        officeDetailsPanel.save(office);
        office.setLeadershipNotes(leadershipNotesField.getText());
        office.setGeneralNotes(generalNotesField.getText());
        office.setContactNotes(contactNotesField.getText());
    }
}


