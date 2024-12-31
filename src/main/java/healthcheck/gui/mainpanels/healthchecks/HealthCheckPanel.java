package healthcheck.gui.mainpanels.healthchecks;

import com.github.lgooddatepicker.components.DatePicker;
import healthcheck.data.HealthCheck;
import healthcheck.data.MySettings;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckPanel extends JPanel implements ActionListener {
    private MainWindow parent;
    private HealthCheck check;

    public HealthCheckPanel(MainWindow parent, HealthCheck check) {
        this.parent = parent;
        this.check = check;
        this.setLayout(new GridBagLayout());

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;

        // manage data
        this.add(createManagePanel(), mainGbc);

        // add health check data
        mainGbc.gridy = 1;
        mainGbc.weighty = 1.0;
        this.add(createDataPanel(), mainGbc);

    }

    public JPanel createDataPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));


        JSpinner activeClientsField = new JSpinner();
        activeClientsField.setValue(check.getActiveClients());
        panel.add(dataCollectionPanel("Number of Active Clients", activeClientsField));

        JSpinner activeCaregiversField = new JSpinner();
        activeCaregiversField.setValue(check.getActiveCaregivers());
        panel.add(dataCollectionPanel("Number of Active Caregivers", activeCaregiversField));

        JSpinner expiredLicenseCountField = new JSpinner();
        expiredLicenseCountField.setValue(check.getExpiredLicenseCount());
        panel.add(dataCollectionPanel("Expired License Count", expiredLicenseCountField));

        JSpinner clientGeneralSchedulesConfiguredField = new JSpinner();
        clientGeneralSchedulesConfiguredField.setValue(check.getClientGeneralSchedulesConfigured());
        panel.add(dataCollectionPanel("Clients Without a General Schedule", clientGeneralSchedulesConfiguredField));


        DatePicker lastLoginField = new DatePicker();
        lastLoginField.setDate(check.getLastLogin());
        panel.add(dataCollectionPanel("Most Recent Login Date", lastLoginField));

        DatePicker oldestTaskDateField = new DatePicker();
        oldestTaskDateField.setDate(check.getOldestTaskDate());
        panel.add(dataCollectionPanel("Oldest Task Date", oldestTaskDateField));

        DatePicker lastBillingProcessDateField = new DatePicker();
        lastBillingProcessDateField.setDate(check.getLastBillingProcessDate());
        panel.add(dataCollectionPanel("Last Billing Process Date", lastBillingProcessDateField));

        DatePicker lastPayrollProcessDateField = new DatePicker();
        lastPayrollProcessDateField.setDate(check.getLastPayrollProcessDate());
        panel.add(dataCollectionPanel("Last Payroll Process Date", lastPayrollProcessDateField));

        DatePicker lastScheduleDateField = new DatePicker();
        lastScheduleDateField.setDate(check.getLastScheduleDate());
        panel.add(dataCollectionPanel("Last Schedule Generation Date", lastScheduleDateField));


        JComboBox<Integer> scheduleGenerationMethodComboBox;

        JCheckBox shiftsInDifferentStatusesCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Shifts in Different Statuses", shiftsInDifferentStatusesCheckBox));
        JCheckBox caregiversUsingTheAppCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Caregivers Actively Using the App", caregiversUsingTheAppCheckBox));
        JCheckBox repeatAdjustmentsCheckBox = new JCheckBox();
        panel.add(dataCollectionPanel("Repeat Adjustments being Made", repeatAdjustmentsCheckBox));

        JTextArea generalNotesArea = new JTextArea(check.getGeneralNotes());
        generalNotesArea.setLineWrap(true);
        panel.add(dataCollectionPanel("General Notes", generalNotesArea));

        JTextArea contactReasonField = new JTextArea(check.getContactReason());
        contactReasonField.setLineWrap(true);
        panel.add(dataCollectionPanel("Contact Reason", contactReasonField));


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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(248, 248, 248));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JTextArea previousContactReasonField = new JTextArea();;
        previousContactReasonField.setLineWrap(true);
        panel.add(dataCollectionPanel("Previous Contact Reason", previousContactReasonField));

        ArrayList<String> userList = new ArrayList<>();
        userList.add("Unassigned");
        userList.addAll(MySettings.getInstance().getUsers());
        JComboBox<String>assignedToField = new JComboBox<>(userList.toArray(new String[0]));
        assignedToField.setSelectedIndex(userList.indexOf(check.getAssignedTo()));
        panel.add(dataCollectionPanel("Assigned To", assignedToField));

        JTextField reviewPerformedByField = new JTextField(check.getReviewPerformedBy());;
        panel.add(dataCollectionPanel("Review Performed By", reviewPerformedByField));

        JTextField followUpPerformedByField = new JTextField(check.getFollowUpPerformedBy());;
        panel.add(dataCollectionPanel("Follow Up Performed By", followUpPerformedByField));

        String[] statusList = new String[] {"Pending", "Reviewed", "Completed"};
        JComboBox<String> healthCheckStatusComboBox = new JComboBox<>(statusList);
        healthCheckStatusComboBox.setSelectedIndex(check.getHealthCheckStatus());
        panel.add(dataCollectionPanel("Health Check Status", healthCheckStatusComboBox));

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
