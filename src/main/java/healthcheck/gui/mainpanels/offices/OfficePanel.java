package healthcheck.gui.mainpanels.offices;

import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.WriteData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.github.lgooddatepicker.components.DatePicker;

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
    JPanel otherParentPanel;
    Office office;
    JPanel hostPanel;
    private DatePicker execAgreementDateField;
    private JTextField officeNameField;
    private JTextField officeOwnerField;
    private JTextField officeOwnerEmailField;
    private JTextField officePrimaryContactPersonField;
    private JTextField officePrimaryContactEmailField;
    private JTextField officePrimaryContactPhoneField;
    private JTextArea leadershipNotesField;
    private JTextArea generalNotesField;
    private JTextArea contactNotesField;
    private JComboBox<String> trainingStatusComboBox;

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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // navigation panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(buildNavPanel(), gbc);

        // office data/info panel
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        this.add(buildContentPanel(), gbc);
    }



    private JPanel buildNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(0, 122, 178)); // Base color
        navPanel.setLayout(new GridBagLayout());
        navPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints navGbc = new GridBagConstraints();
        JPanel navOptionsPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        navOptionsPanel.setBackground(new Color(0, 122, 178));

        // have next and prev buttons to the far left
        navGbc.gridx = 0;
        // switch office panel config
        JPanel buttonPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(0, 122, 178));
        JButton prevButton = new JButton("Prev");
        prevButton.setActionCommand("prev");
        prevButton.addActionListener(this);
        buttonPanel.add(prevButton);
        JButton nextButton = new JButton("Next");
        nextButton.setActionCommand("next");
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        navPanel.add(buttonPanel, navGbc);



        //add nav options
        navGbc.gridx = 1;
        navGbc.weightx = 1.0;
        addNavItem(navOptionsPanel, office.getOfficeCode(), true);
        addNavItem(navOptionsPanel, "Health Check History", false);
        navPanel.add(navOptionsPanel, navGbc);


        // have save button be on the far right
        navGbc.gridx = 2;
        navGbc.weightx = 0;
        // save panel config
        buttonPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0, 122, 178));
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.addActionListener(this);
        buttonPanel.add(backButton);
        navPanel.add(buttonPanel, navGbc);

        return navPanel;
    }

    private void addNavItem(JPanel panel, String name, boolean isActive){
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setBackground(isActive ? new Color(255, 151, 25) : new Color(0, 122, 178)); // Highlight active
        navItem.setForeground(Color.WHITE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 16));
        navItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add hover effect
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) navItem.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Perform action (switch view, print message, etc.)
                System.out.println("Health check history clicked");

            }
        });

        panel.add(navItem);
    }

    private JPanel buildContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weighty = 1.0;
        gbc.weightx = 0.3333;
        gbc.fill = GridBagConstraints.BOTH;

        // office info panel
        panel.add(createOfficeInfoPanel(), gbc);

        // contact info panel
        gbc.gridx = 1;
        panel.add(createContactInfoPanel(), gbc);

        // billable hours panel
        gbc.gridx = 2;
        gbc.weightx = 0.25;
        panel.add(createBillableHoursPanel(), gbc);


        return panel;
    }

    private JPanel createOfficeInfoPanel() {
        GridBagConstraints gbc = gbcFactory();
        JPanel panel = panelFactory("Office Info", gbc);

        // office name
        gbc.gridy = 1;
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Office Name"));
        officeNameField = new JTextField(office.getOfficeName());
        dataPanel.add(officeNameField);
        panel.add(dataPanel, gbc);

        // agreement date
        gbc.gridy = 2;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("SwyftOps Start Date"));
        execAgreementDateField = new DatePicker();
        execAgreementDateField.setDate(office.getExecAgreementDate());
        dataPanel.add(execAgreementDateField);
        panel.add(dataPanel, gbc);

        // training status
        gbc.gridy = 3;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Training Status"));
        String[] arr =
                {"Hasn't Begun Training", "Session 1 Completed", "Session 2 Completed", "Session 3 Completed", "Training Completed"};
        trainingStatusComboBox = new JComboBox<>(arr);
        trainingStatusComboBox.setSelectedIndex(office.getTrainingStatus());
        dataPanel.add(trainingStatusComboBox);
        panel.add(dataPanel, gbc);


        // leadership notes
        gbc.weighty = 1;
        gbc.gridy = 4;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Leadership Notes"));
        leadershipNotesField = new JTextArea(office.getLeadershipNotes());
        leadershipNotesField.setLineWrap(true);
        dataPanel.add(leadershipNotesField);
        panel.add(dataPanel, gbc);

        // general notes
        gbc.gridy = 5;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("General Notes"));
        generalNotesField = new JTextArea(office.getGeneralNotes());
        generalNotesField.setLineWrap(true);
        dataPanel.add(generalNotesField);
        panel.add(dataPanel, gbc);

        // deactivate button
        gbc.gridy = 6;
        gbc.weighty = 0;
        JButton statusButton = new JButton(office.isActiveOffice() ? "Deactivate" : "Activate");
        statusButton.addActionListener(this);
        statusButton.setActionCommand("statusChange");
        panel.add(statusButton, gbc);

        return panel;
    }

    private JPanel createContactInfoPanel() {
        GridBagConstraints gbc = gbcFactory();
        JPanel panel = panelFactory("Contact Info", gbc);

        // Owner name
        gbc.gridy = 1;
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Owner Name"));
        officeOwnerField = new JTextField(office.getOfficeOwner());
        dataPanel.add(officeOwnerField);
        panel.add(dataPanel, gbc);

        // Owner Email
        gbc.gridy = 2;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Owner Email"));
        officeOwnerEmailField = new JTextField(office.getOfficeOwnerEmail());
        dataPanel.add(officeOwnerEmailField);
        panel.add(dataPanel, gbc);

        // Primary Contact
        gbc.gridy = 3;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Primary Contact Name"));
        officePrimaryContactPersonField = new JTextField(office.getOfficePrimaryContactPerson());
        dataPanel.add(officePrimaryContactPersonField);
        panel.add(dataPanel, gbc);

        // primary contact email
        gbc.gridy = 4;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Primary Contact Email"));
        officePrimaryContactEmailField = new JTextField(office.getOfficePrimaryContactEmail());
        dataPanel.add(officePrimaryContactEmailField);
        panel.add(dataPanel, gbc);

        // primary contact phone number
        gbc.gridy = 5;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Primary Contact Phone Number"));
        officePrimaryContactPhoneField = new JTextField(office.getOfficePrimaryContactPhone());
        dataPanel.add(officePrimaryContactPhoneField);
        panel.add(dataPanel, gbc);


        // contact notes
        gbc.gridy = 6;
        gbc.weighty = 1;
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Contact Notes"));
        contactNotesField = new JTextArea(office.getContactNotes());
        contactNotesField.setLineWrap(true);
        dataPanel.add(contactNotesField);
        panel.add(dataPanel, gbc);

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

    private GridBagConstraints gbcFactory() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);
        return gbc;
    }

    private JPanel panelFactory (String header, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 122, 178));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label = new JLabel(header);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, gbc);

        return panel;
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
            mainWindow.loadPanel(officeListPanel);
        }
        if (actionCommand.equals("next")) {
            officeListPanel.loadNextOffice();
        }
        if (actionCommand.equals("prev")) {
            officeListPanel.loadPreviousOffice();
        }
        if (actionCommand.equals("statusChange")) {
            Database db = Database.getInstance();
            // TODO create confirmation dialog
            System.out.println(office.isActiveOffice());
            db.switchOfficeStatus(office);
            // TODO reload this panel
        }
    }

    private void updateOfficeData() {
        office.setOfficeName(officeNameField.getText());
        office.setOfficeOwner(officeOwnerField.getText());
        office.setOfficeOwnerEmail(officeOwnerEmailField.getText());
        office.setOfficePrimaryContactPerson(officePrimaryContactPersonField.getText());
        office.setOfficePrimaryContactEmail(officePrimaryContactEmailField.getText());
        office.setOfficePrimaryContactPhone(officePrimaryContactPhoneField.getText());
        office.setTrainingStatus(trainingStatusComboBox.getSelectedIndex());
        office.setLeadershipNotes(leadershipNotesField.getText());
        office.setGeneralNotes(generalNotesField.getText());
        office.setContactNotes(contactNotesField.getText());
        LocalDate date = execAgreementDateField.getDate();
        office.setExecAgreementDate(date);
    }
}


