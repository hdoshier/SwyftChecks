package healthcheck.gui.mainpanels.healthchecks;

import com.github.lgooddatepicker.components.DatePicker;
import healthcheck.data.Email;
import healthcheck.data.HealthCheck;
import healthcheck.data.MySettings;
import healthcheck.data.Office;
import healthcheck.data.firestore.ReadData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class HealthCheckPanel extends JPanel implements ActionListener {
    private HealthCheck check;
    private JTextField subjectTextField;
    private JTextArea bodyArea;
    private JTextField contactEmail;
    private JTextField contactName;
    private HealthCheckPeriodListPanel parent;
    private JLabel healthCheckStatusLabel = null;

    public HealthCheckPanel(HealthCheckPeriodListPanel parent, HealthCheck check) {
        this.parent = parent;
        this.check = check;
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(0, 122, 178));
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.fill = GridBagConstraints.BOTH;

        // manage data
        mainGbc.weightx = 0.75;
        this.add(createManagePanel(), mainGbc);

        // Office Data
        mainGbc.gridx = 1;
        mainGbc.weightx = 0.25;
        this.add(createOfficePanel(), mainGbc);

        // email info
        mainGbc.gridy = 1;
        mainGbc.gridx = 1;
        mainGbc.weighty = 1.0;
        mainGbc.weightx = 0.25;
        this.add(createEmailPanel(), mainGbc);

        // add health check data
        mainGbc.gridy = 1;
        mainGbc.gridx = 0;
        mainGbc.weightx = 0.75;
        this.add(createDataPanel(), mainGbc);

    }

    private JPanel createEmailPanel() {
        Office office = check.getOffice();
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // template combo box
        JPanel templatePanel = new JPanel();
        templatePanel.setLayout(new BoxLayout(templatePanel, BoxLayout.X_AXIS));
        templatePanel.add(new JLabel("Email Template"));
        JComboBox<String> emailTemplateBox = new JComboBox<>(MySettings.getInstance().getEmailTemplateNames().toArray(new String[0]));;
        emailTemplateBox.setSelectedIndex(-1);
        emailTemplateBox.addActionListener(e -> {
            String template = (String) emailTemplateBox.getSelectedItem();
            if (bodyArea != null && template != null) {
                bodyArea.setText(MySettings.getInstance().getTemplate(template));
            }
        });
        templatePanel.add(emailTemplateBox);
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(bodyPanel, gbc);

        // button
        JButton sendEmailBtn = new JButton("Prep Email");
        sendEmailBtn.addActionListener(this);
        sendEmailBtn.setActionCommand("email");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(sendEmailBtn, gbc);

        return panel;
    }

    private String getContactFirstName() {
        String[] name = contactName.getText().split(" ");
        return name[0];
    }

    private JPanel createOfficePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        Office office = check.getOffice();
        //office code label
        JPanel codePanel = new JPanel();
        codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.Y_AXIS));
        codePanel.add(new JLabel("Office Code"));
        JLabel codeLabel = new JLabel(office.getOfficeCode());
        codeLabel.setOpaque(true);
        codeLabel.setForeground(new Color(0, 102, 204));
        codeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                codeLabel.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                codeLabel.setBackground(codePanel.getBackground());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                parent.openOfficePanel(ReadData.readOffice(office.getOfficeCode()));
            }
        });
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

        //office primary contact name - textfield
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.add(new JLabel("Primary Contact Name"));
        contactName = new JTextField(office.getOfficePrimaryContactPerson());
        contactPanel.add(contactName);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(contactPanel, gbc);

        //office primary contact email - textfield
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.add(new JLabel("Primary Contact Email"));
        contactEmail = new JTextField(office.getOfficePrimaryContactEmail());
        emailPanel.add(contactEmail);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailPanel, gbc);

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
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridy = 0;
        JSpinner activeClientsField = new JSpinner();
        activeClientsField.setValue(check.getActiveClients());
        panel.add(dataCollectionPanel("Number of Active Clients", activeClientsField), gbc);

        gbc.gridy = 1;
        JSpinner activeCaregiversField = new JSpinner();
        activeCaregiversField.setValue(check.getActiveCaregivers());
        panel.add(dataCollectionPanel("Number of Active Caregivers", activeCaregiversField), gbc);

        gbc.gridy = 2;
        JSpinner expiredLicenseCountField = new JSpinner();
        expiredLicenseCountField.setValue(check.getExpiredLicenseCount());
        panel.add(dataCollectionPanel("Expired License Count", expiredLicenseCountField), gbc);

        gbc.gridy = 3;
        JSpinner clientGeneralSchedulesConfiguredField = new JSpinner();
        clientGeneralSchedulesConfiguredField.setValue(check.getClientGeneralSchedulesConfigured());
        panel.add(dataCollectionPanel("Clients Without a General Schedule", clientGeneralSchedulesConfiguredField), gbc);

        gbc.gridy = 4;
        JComboBox<Integer> scheduleGenerationMethodComboBox;

        gbc.gridx = 1;
        gbc.gridy = 0;
        DatePicker lastLoginField = new DatePicker();
        lastLoginField.setDate(check.getLastLogin());
        panel.add(dataCollectionPanel("Most Recent Login Date", lastLoginField), gbc);

        gbc.gridy = 1;
        DatePicker oldestTaskDateField = new DatePicker();
        oldestTaskDateField.setDate(check.getOldestTaskDate());
        panel.add(dataCollectionPanel("Oldest Task Date", oldestTaskDateField), gbc);

        gbc.gridy = 2;
        DatePicker lastBillingProcessDateField = new DatePicker();
        lastBillingProcessDateField.setDate(check.getLastBillingProcessDate());
        panel.add(dataCollectionPanel("Last Billing Process Date", lastBillingProcessDateField), gbc);

        gbc.gridy = 3;
        DatePicker lastPayrollProcessDateField = new DatePicker();
        lastPayrollProcessDateField.setDate(check.getLastPayrollProcessDate());
        panel.add(dataCollectionPanel("Last Payroll Process Date", lastPayrollProcessDateField), gbc);

        gbc.gridy = 4;
        DatePicker lastScheduleDateField = new DatePicker();
        lastScheduleDateField.setDate(check.getLastScheduleDate());
        panel.add(dataCollectionPanel("Last Schedule Generation Date", lastScheduleDateField), gbc);



        gbc.gridx = 2;
        gbc.gridy = 0;
        JCheckBox shiftsInDifferentStatusesCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Shifts in Different Statuses", shiftsInDifferentStatusesCheckBox), gbc);
        gbc.gridy = 1;
        JCheckBox caregiversUsingTheAppCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Caregivers Actively Using the App", caregiversUsingTheAppCheckBox), gbc);
        gbc.gridy = 2;
        JCheckBox repeatAdjustmentsCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Repeat Adjustments being Made", repeatAdjustmentsCheckBox), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JTextArea generalNotesArea = new JTextArea(check.getGeneralNotes());
        generalNotesArea.setLineWrap(true);
        panel.add(dataCollectionPanel("General Notes", generalNotesArea), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        JTextArea contactReasonField = new JTextArea(check.getContactReason());
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
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        //row 1
        gbc.gridx = 0;
        ArrayList<String> userList = new ArrayList<>();
        userList.add("Unassigned");
        userList.addAll(MySettings.getInstance().getUsers());
        JComboBox<String> assignedToField = new JComboBox<>(userList.toArray(new String[0]));
        assignedToField.setSelectedIndex(userList.indexOf(check.getAssignedTo()));
        assignedToField.addActionListener(e -> {
            int selectedIndex = assignedToField.getSelectedIndex();
            if (healthCheckStatusLabel != null && selectedIndex > 1) {
                healthCheckStatusLabel.setText("Assigned");
                check.setAssignedTo(assignedToField.getItemAt(selectedIndex));

            }
        });
        panel.add(dataCollectionPanel("Assigned To", assignedToField), gbc);

        userList = new ArrayList<>(MySettings.getInstance().getUsers());
        gbc.gridx = 1;
        JComboBox<String> reviewPerformedByField = new JComboBox<>(userList.toArray(new String[0]));;
        reviewPerformedByField.setSelectedIndex(userList.indexOf(check.getReviewPerformedBy()));
        panel.add(dataCollectionPanel("Review Performed By", reviewPerformedByField), gbc);
        gbc.gridx = 2;
        JComboBox<String> completedByField = new JComboBox<>(userList.toArray(new String[0]));;
        completedByField.setSelectedIndex(userList.indexOf(check.getHealthCheckCompletedBy()));
        panel.add(dataCollectionPanel("Health Check Completed By", completedByField), gbc);

        //row 2
        gbc.gridy = 1;
        gbc.gridx = 0;
        JPanel prevContactReasonPanel = new JPanel();
        prevContactReasonPanel.setLayout(new BoxLayout(prevContactReasonPanel, BoxLayout.Y_AXIS));
        prevContactReasonPanel.add(new JLabel("Previous Contact Reason"));
        prevContactReasonPanel.add(new JLabel(check.getPreviousContactReason()));
        panel.add(prevContactReasonPanel, gbc);
        gbc.gridx = 1;
        String[] statusList = new String[] {"Unassigned", "Pending", "Reviewed", "Completed"};
        healthCheckStatusLabel = new JLabel(statusList[check.getHealthCheckStatus()]);
        panel.add(dataCollectionPanel("Health Check Status", healthCheckStatusLabel), gbc);
        gbc.gridx = 2;
        JCheckBox flagForLeadershipCheckBox = new JCheckBox();
        flagForLeadershipCheckBox.setSelected(check.isFlagedForLeadershipReview());
        panel.add(dataCollectionPanel("Flag for Leadership Review", flagForLeadershipCheckBox), gbc);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("email")) {
            Email.prepEmail(getContactFirstName(), contactEmail.getText(), subjectTextField.getText(), bodyArea.getText());
        }
    }
}
