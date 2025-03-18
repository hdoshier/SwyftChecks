package healthcheck.gui.mainpanels.healthchecks;

import com.github.lgooddatepicker.components.DatePicker;
import healthcheck.data.*;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.gui.MainWindow;
import healthcheck.gui.mainpanels.offices.OfficePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class HealthCheckPanel extends JPanel implements ActionListener {
    private HealthCheck check;
    private Office office;
    private MainWindow mainWindow;
    private JPanel contentPanel = null;
    private GridBagConstraints mainGbc;
    private JLabel activeLabel;

    // contact panel
    private JTextField subjectTextField;
    private JTextArea bodyArea;
    private JTextField contactEmail;
    private JTextField contactName;
    private HealthCheckListPanel parent;
    private JComboBox<String> emailTemplateBox;

    // manage fields
    private JComboBox<String> assignedToField;
    private JComboBox<String> checkStatusField;
    private DatePicker checkCompletionDateField;
    private JCheckBox flagForLeadershipCheckBox;

    // health check data fields
    private JSpinner activeClientsField;
    private JSpinner activeCaregiversField;
    private JSpinner expiredLicenseCountField;
    private DatePicker lastPayrollProcessDateField;
    private JSpinner clientGeneralSchedulesConfiguredField;
    private DatePicker lastLoginField;
    private DatePicker oldestTaskDateField;
    private DatePicker lastScheduleDateField;
    private DatePicker lastBillingProcessDateField;
    private JCheckBox shiftsInDifferentStatusesCheckBox;
    private JCheckBox caregiversUsingTheAppCheckBox;
    private JCheckBox repeatAdjustmentsCheckBox;
    private JTextArea generalNotesArea;
    private JTextArea contactReasonField;


    public HealthCheckPanel(MainWindow mainWindow, HealthCheckListPanel parent, HealthCheck check) {
        this.parent = parent;
        this.check = check;
        this.office = check.getOffice();
        this.mainWindow = mainWindow;
        this.setLayout(new GridBagLayout());
        this.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weightx = 1.0;
        this.add(createNavPanel(), mainGbc);
        setContentPanel(checkDataPanel());
    }

    private void setContentPanel(JPanel panel) {
        if (contentPanel != null) {
            this.remove(contentPanel);
        }

        this.contentPanel = panel;
        mainGbc.gridy = 1;
        mainGbc.weighty = 1.0;
        this.add(contentPanel, mainGbc);
        this.revalidate();
        this.repaint();
    }

    private JPanel checkDataPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // manage data
        gbc.gridwidth = 1;
        gbc.weightx = 0.75;
        panel.add(createManagePanel(), gbc);

        // Office Data
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        panel.add(createOfficePanel(), gbc);

        // email info
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 0.25;
        panel.add(contactOfficePanel(), gbc);

        // add health check data
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.75;
        gbc.gridheight = 1;
        panel.add(createDataPanel(), gbc);

        // add billable hour history
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weighty = 0;
        gbc.gridheight = 1;
        panel.add(createBillableHoursPanel(), gbc);

        return panel;
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE); // Base color
        navPanel.setLayout(new GridBagLayout());

        GridBagConstraints navGbc = new GridBagConstraints();
        JPanel navOptionsPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        navOptionsPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);

        // have next and prev buttons to the far left
        navGbc.gridx = 0;
        // switch office panel config
        JPanel buttonPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
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
        addNavItem(navOptionsPanel, office.getOfficeCode(), false);
        addNavItem(navOptionsPanel, "Health Check", true);
        navPanel.add(navOptionsPanel, navGbc);


        // have save button be on the far right
        navGbc.gridx = 2;
        navGbc.weightx = 0;
        // save panel config
        buttonPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
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
        navItem.setBackground(isActive ? MyGlobalVariables.SWYFTOPS_ORANGE : MyGlobalVariables.SWYFTOPS_BLUE); // Highlight active
        navItem.setForeground(Color.WHITE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 16));
        navItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add hover effect
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.setBackground(MyGlobalVariables.SWYFTOPS_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) navItem.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Perform action (switch view, print message, etc.)
                System.out.println(name + " clicked");
                /*
                TODO implement switching between office and HC
                String officeCode = office.getOfficeCode();
                if (name.equals(officeCode)) {
                    setContentPanel(new OfficePanel(HealthCheckPanel.this, ReadData.readIndividualOffice(office)));
                } else {
                    setContentPanel(checkDataPanel());
                }
                 */
            }
        });

        panel.add(navItem);
    }

    private JScrollPane createBillableHoursPanel() {
        JPanel scrollPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelFormatter(scrollPanel);

        YearMonth month = office.getMostRecentBillableHistory();
        HashMap<String, Double> billableMap = office.getBillableHourHistory();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        while (billableMap.containsKey(month.toString())) {
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            JLabel monthYear = new JLabel(month.format(formatter));
            detailsPanel.add(monthYear);
            JLabel billableHours = new JLabel(String.valueOf(billableMap.get(month.toString())));
            detailsPanel.add(billableHours);
            scrollPanel.add(detailsPanel);
            month = month.minusMonths(1);
        }
        return scrollPane;
    }

    private JPanel contactOfficePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panelFormatter(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        int ycord = 0;


        //office primary contact name - textfield
        gbc.gridx = 0;
        gbc.gridy = ycord;
        contactName = new JTextField(office.getOfficePrimaryContactPerson());
        panel.add(dataCollectionPanel("Primary Contact Person", contactName), gbc);

        //office owner - Label
        gbc.gridx = 1;
        gbc.gridy = ycord++;
        JLabel officeOwner = new JLabel(office.getOfficeOwner());
        panel.add(dataCollectionPanel("Office Owner", officeOwner), gbc);

        //office primary contact email - textfield
        gbc.gridx = 0;
        gbc.gridy = ycord;
        contactEmail = new JTextField(office.getOfficePrimaryContactEmail());
        panel.add(dataCollectionPanel("Primary Contact Email", contactEmail), gbc);

        //office primary contact Phone - Label
        gbc.gridx = 1;
        gbc.gridy = ycord++;
        JLabel phoneLabel = new JLabel(office.getOfficePrimaryContactPhone());
        phoneLabel.setOpaque(true);
        phoneLabel.setForeground(MyGlobalVariables.SWYFTOPS_BLUE);
        phoneLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                phoneLabel.setBackground(MyGlobalVariables.SWYFTOPS_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                phoneLabel.setBackground(officeOwner.getBackground());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try{
                    String phoneNumber = office.getOfficePrimaryContactPhone().replaceAll("[^0-9]", "");
                    URI uri = new URI("tel:" + phoneNumber);
                    desktop.browse(uri);
                } catch (Exception ex) {
                    // do nothing
                }

            }
        });
        panel.add(dataCollectionPanel("Primary Contact Phone", phoneLabel), gbc);

        // contact notes
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = ycord++;
        JLabel contactNotesLabel = new JLabel(office.getContactNotes());
        panel.add(dataCollectionPanel("Contact Notes", contactNotesLabel), gbc);


        gbc.anchor = GridBagConstraints.WEST;
        // template combo box
        JPanel templatePanel = new JPanel();
        templatePanel.setLayout(new BoxLayout(templatePanel, BoxLayout.X_AXIS));
        templatePanel.add(new JLabel("Email Template"));
        emailTemplateBox = new JComboBox<>(MySettings.getInstance().getEmailTemplateNames().toArray(new String[0]));;
        emailTemplateBox.setSelectedIndex(-1);
        emailTemplateBox.addActionListener(e -> {
            String template = (String) emailTemplateBox.getSelectedItem();
            if (bodyArea != null && template != null) {
                bodyArea.setText(MySettings.getInstance().getTemplate(template));
            }
        });
        templatePanel.add(emailTemplateBox);
        gbc.gridy = ycord++;
        panel.add(templatePanel, gbc);

        // subject line
        StringBuilder sb = new StringBuilder("SwyftOps | ");
        sb.append(office.getOfficeCode());
        sb.append(" | Health Check");

        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.add(new JLabel("Subject Line"));
        subjectTextField = new JTextField(sb.toString());
        subjectPanel.add(subjectTextField);
        gbc.gridy = ycord++;
        panel.add(subjectPanel, gbc);

        // template area
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.add(new JLabel("Email Body"));
        bodyArea = new JTextArea("Select template or write custom email.");
        bodyArea.setLineWrap(true);
        bodyArea.setRows(15);
        bodyArea.setColumns(20);
        bodyPanel.add(bodyArea);
        gbc.gridy = ycord++;
        panel.add(bodyPanel, gbc);

        // button
        JButton sendEmailBtn = new JButton("Prep Email");
        sendEmailBtn.addActionListener(this);
        sendEmailBtn.setActionCommand("email");
        gbc.gridy = ycord++;
        panel.add(sendEmailBtn, gbc);

        return panel;
    }

    private JPanel createOfficePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panelFormatter(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        //office code label
        JPanel codePanel = new JPanel();
        codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.Y_AXIS));
        codePanel.add(new JLabel("Office Code"));
        JLabel codeLabel = new JLabel(office.getOfficeCode());
        codePanel.add(codeLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(codePanel, gbc);


        //office name label
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.add(new JLabel("Office Name"));
        namePanel.add(new JLabel(office.getOfficeName()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(namePanel, gbc);



        // general notes
        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
        generalPanel.add(new JLabel("General Notes"));
        generalPanel.add(new JLabel(office.getGeneralNotes()));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(generalPanel, gbc);

        // leadership notes
        JPanel leadershipPanel = new JPanel();
        leadershipPanel.setLayout(new BoxLayout(leadershipPanel, BoxLayout.Y_AXIS));
        leadershipPanel.add(new JLabel("Leadership Notes"));
        leadershipPanel.add(new JLabel(office.getLeadershipNotes()));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(leadershipPanel, gbc);

        return panel;
    }

    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panelFormatter(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridy = 0;
        activeClientsField = new JSpinner();
        activeClientsField.setValue(check.getActiveClients());
        panel.add(dataCollectionPanel("Number of Active Clients", activeClientsField), gbc);

        gbc.gridy = 1;
        activeCaregiversField = new JSpinner();
        activeCaregiversField.setValue(check.getActiveCaregivers());
        panel.add(dataCollectionPanel("Number of Active Caregivers", activeCaregiversField), gbc);

        gbc.gridy = 2;
        expiredLicenseCountField = new JSpinner();
        expiredLicenseCountField.setValue(check.getExpiredLicenseCount());
        panel.add(dataCollectionPanel("Expired License Count", expiredLicenseCountField), gbc);

        gbc.gridy = 3;
        clientGeneralSchedulesConfiguredField = new JSpinner();
        clientGeneralSchedulesConfiguredField.setValue(check.getClientGeneralSchedulesConfigured());
        panel.add(dataCollectionPanel("Clients Without a General Schedule", clientGeneralSchedulesConfiguredField), gbc);

        gbc.gridy = 4;
        JComboBox<Integer> scheduleGenerationMethodComboBox;

        gbc.gridx = 1;
        gbc.gridy = 0;
        lastLoginField = new DatePicker();
        lastLoginField.setDate(check.getLastLogin());
        panel.add(dataCollectionPanel("Most Recent Login Date", lastLoginField), gbc);

        gbc.gridy = 1;
        oldestTaskDateField = new DatePicker();
        oldestTaskDateField.setDate(check.getOldestTaskDate());
        panel.add(dataCollectionPanel("Oldest Task Date", oldestTaskDateField), gbc);

        gbc.gridy = 2;
        lastBillingProcessDateField = new DatePicker();
        lastBillingProcessDateField.setDate(check.getLastBillingProcessDate());
        panel.add(dataCollectionPanel("Last Billing Process Date", lastBillingProcessDateField), gbc);

        gbc.gridy = 3;
        lastPayrollProcessDateField = new DatePicker();
        lastPayrollProcessDateField.setDate(check.getLastPayrollProcessDate());
        panel.add(dataCollectionPanel("Last Payroll Process Date", lastPayrollProcessDateField), gbc);

        gbc.gridy = 4;
        lastScheduleDateField = new DatePicker();
        lastScheduleDateField.setDate(check.getLastScheduleDate());
        panel.add(dataCollectionPanel("Last Schedule Generation Date", lastScheduleDateField), gbc);



        gbc.gridx = 2;
        gbc.gridy = 0;
        shiftsInDifferentStatusesCheckBox = new JCheckBox();
        shiftsInDifferentStatusesCheckBox.setSelected(check.isShiftsInDifferentStatuses());
        panel.add(dataCollectionPanel("Shifts in Different Statuses", shiftsInDifferentStatusesCheckBox), gbc);
        gbc.gridy = 1;
        caregiversUsingTheAppCheckBox = new JCheckBox();
        caregiversUsingTheAppCheckBox.setSelected(check.isCaregiversUsingTheApp());
        panel.add(dataCollectionPanel("Caregivers Actively Using the App", caregiversUsingTheAppCheckBox), gbc);
        gbc.gridy = 2;
        repeatAdjustmentsCheckBox = new JCheckBox();
        repeatAdjustmentsCheckBox.setSelected(check.isRepeatAdjustments());
        panel.add(dataCollectionPanel("Repeat Adjustments being Made", repeatAdjustmentsCheckBox), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        generalNotesArea = new JTextArea(check.getGeneralNotes());
        generalNotesArea.setLineWrap(true);
        panel.add(dataCollectionPanel("General Notes", generalNotesArea), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        contactReasonField = new JTextArea(check.getContactReason());
        contactReasonField.setLineWrap(true);
        panel.add(dataCollectionPanel("Contact Reason", contactReasonField), gbc);


        return panel;
    }

    private JPanel dataCollectionPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(component);

        return panel;
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panelFormatter(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        //assigned to
        gbc.gridx = 0;
        ArrayList<String> userList = new ArrayList<>(MySettings.getInstance().getUsers());
        userList.addFirst("Unassigned");
        assignedToField = new JComboBox<>(userList.toArray(new String[0]));
        assignedToField.setSelectedIndex(userList.indexOf(check.getAssignedTo()));
        assignedToField.addActionListener(e -> {
            if (assignedToField.getSelectedIndex() > 0) {
                checkStatusField.setSelectedIndex(1);
                checkCompletionDateField.setEnabled(true);
                checkStatusField.setEnabled(true);
            } else {
                checkStatusField.setSelectedIndex(0);
                checkCompletionDateField.setEnabled(false);
                checkStatusField.setEnabled(false);
            }
        });
        panel.add(dataCollectionPanel("Assigned To", assignedToField), gbc);

        // status
        gbc.gridx = 1;
        checkStatusField = new JComboBox<>(MyGlobalVariables.HEALTH_CHECK_STATUS_ARRAY);
        checkStatusField.setSelectedIndex(check.getHealthCheckStatus());
        checkStatusField.addActionListener(e -> {
            if (checkStatusField.getSelectedItem().equals("Completed")) {
                checkCompletionDateField.setDate(LocalDate.now());
            } else {
                checkCompletionDateField.setDate(null);
            }
        });
        panel.add(dataCollectionPanel("Status", checkStatusField), gbc);

        // completion date
        gbc.gridx = 2;
        checkCompletionDateField = new DatePicker();
        checkCompletionDateField.setDate(check.getCheckCompletionDate());
        checkCompletionDateField.addDateChangeListener(e -> {
            if (checkCompletionDateField.getDate() != null) {
                checkStatusField.setSelectedItem("Completed");
            }
        });
        panel.add(dataCollectionPanel("Completed On", checkCompletionDateField), gbc);

        //flag for review
        gbc.gridx = 3;
        flagForLeadershipCheckBox = new JCheckBox();
        flagForLeadershipCheckBox.setSelected(check.isFlagedForLeadershipReview());
        panel.add(dataCollectionPanel("Flag for Leadership Review", flagForLeadershipCheckBox), gbc);

        if (assignedToField.getSelectedIndex() == 0) {
            checkCompletionDateField.setEnabled(false);
            checkStatusField.setEnabled(false);
        }
        return panel;
    }

    private void saveHealthCheck() {
        check.setActiveClients((int) activeClientsField.getValue());
        check.setActiveCaregivers((int) activeCaregiversField.getValue());
        check.setExpiredLicenseCount((int) expiredLicenseCountField.getValue());
        check.setClientGeneralSchedulesConfigured((int) clientGeneralSchedulesConfiguredField.getValue());
        check.setShiftsInDifferentStatuses(shiftsInDifferentStatusesCheckBox.isSelected());
        check.setCaregiversUsingTheApp(caregiversUsingTheAppCheckBox.isSelected());
        check.setRepeatAdjustments(repeatAdjustmentsCheckBox.isSelected());
        check.setGeneralNotes(generalNotesArea.getText());
        check.setContactReason(contactReasonField.getText());
        check.setFlagedForLeadershipReview(flagForLeadershipCheckBox.isSelected());
        check.setAssignedTo((String) assignedToField.getSelectedItem());
        check.setHealthCheckStatus(checkStatusField.getSelectedIndex());

        LocalDate date = lastLoginField.getDate();
        if (date != null) {
            check.setLastLogin(date);
        }

        date = checkCompletionDateField.getDate();
        if (date != null) {
            check.setCheckCompletionDate(date);
        }

        date = oldestTaskDateField.getDate();
        if (date != null) {
            check.setOldestTaskDate(date);
        }

        date = lastBillingProcessDateField.getDate();
        if (date != null) {
            check.setLastBillingProcessDate(date);
        }

        date = lastPayrollProcessDateField.getDate();
        if (date != null) {
            check.setLastPayrollProcessDate(date);
        }

        date = lastScheduleDateField.getDate();
        if (date != null) {
            check.setLastScheduleDate(date);
        }
        // TODO write to Database
    }

    private void panelFormatter(JPanel panel) {
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);
        if (actionCommand.equals("email")) {
            // TODO add save validation

            LocalDate date = lastLoginField.getDate();
            String body =  bodyArea.getText();

            // replaces the <lastLoginDate> with the date of the last login.
            if (date != null && body.contains("<lastLoginDate>")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                String dateString = date.format(formatter);
                body =  body.replaceAll("<lastLoginDate>", dateString);
            }
            check.setEmailTemplateSent((String) emailTemplateBox.getSelectedItem());
            saveHealthCheck();
            Email.prepEmail(contactName.getText(), contactEmail.getText(), subjectTextField.getText(), body);
        }
        if (actionCommand.equals("save")) {
            System.out.println(actionCommand);
            saveHealthCheck();
        }
        if (actionCommand.equals("next")) {
            parent.loadNextHealthCheck();
        }
        if (actionCommand.equals("prev")) {
            parent.loadPreviousHealthCheck();
        }
        if (actionCommand.equals("back")) {
            parent.buildOfficeListTable();
            mainWindow.loadPanel(parent);
        }
    }
}
