package healthcheck.gui.mainpanels.offices;

import com.github.lgooddatepicker.components.DatePicker;
import healthcheck.data.MyGlobalVariables;
import healthcheck.data.Office;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;

public class OfficeDetailsPanel extends JPanel implements ActionListener {
    private OfficePanel parent;

    private DatePicker execAgreementDateField;
    private JTextField officeOwnerField;
    private JTextField officeOwnerEmailField;
    private JTextField officePrimaryContactPersonField;
    private JTextField officePrimaryContactEmailField;
    private JTextField officePrimaryContactPhoneField;
    private JComboBox<String> trainingStatusComboBox;
    private JButton changeStatusButton;

    
    public OfficeDetailsPanel(OfficePanel parent, Office office) {
        this.parent = parent;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);
        int gridy = 0;
        
        this.setLayout(new GridBagLayout());
        this.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        /*
        JLabel label = new JLabel(header);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        this.add(label, gbc);
        */
        // Owner name
        gbc.gridy = gridy;
        gbc.gridx = 0;
        officeOwnerField = new JTextField(office.getOfficeOwner());
        this.add(getDefaultDataPanel(officeOwnerField, "Owner Name"), gbc);

        // Owner Email
        gbc.gridy = gridy++;
        gbc.gridx = 1;
        officeOwnerEmailField = new JTextField(office.getOfficeOwnerEmail());
        this.add(getDefaultDataPanel(officeOwnerEmailField, "Owner Email"), gbc);

        // Primary Contact
        gbc.gridy = gridy;
        gbc.gridx = 0;
        officePrimaryContactPersonField = new JTextField(office.getOfficePrimaryContactPerson());
        this.add(getDefaultDataPanel(officePrimaryContactPersonField, "Primary Contact Name"), gbc);

        // primary contact email
        gbc.gridy = gridy++;
        gbc.gridx = 1;
        officePrimaryContactEmailField = new JTextField(office.getOfficePrimaryContactEmail());
        this.add(getDefaultDataPanel(officePrimaryContactEmailField, "Primary Contact Email"), gbc);

        // primary contact phone number
        gbc.gridy = gridy;
        gbc.gridx = 0;
        officePrimaryContactPhoneField = new JTextField(office.getOfficePrimaryContactPhone());
        this.add(getDefaultDataPanel(officePrimaryContactPhoneField, "Primary Contact Phone Number"), gbc);

        // training status
        gbc.gridy = gridy++;
        gbc.gridx = 1;
        trainingStatusComboBox = new JComboBox<>(MyGlobalVariables.OFFICE_TRAINING_STATUS_ARRAY);
        trainingStatusComboBox.setSelectedIndex(office.getTrainingStatus());
        this.add(getDefaultDataPanel(trainingStatusComboBox, "Training Status"), gbc);

        // agreement date
        gbc.gridy = gridy;
        gbc.gridx = 0;
        execAgreementDateField = new DatePicker();
        LocalDate execDate = office.getExecAgreementDate();
        execAgreementDateField.setDate(execDate);
        this.add(getDefaultDataPanel(execAgreementDateField, "SwyftOps Start Date"), gbc);

        //time active
        gbc.gridy = gridy++;
        gbc.gridx = 1;
        JLabel activeTime = new JLabel(getDurationSince(execDate));
        this.add(getDefaultDataPanel(activeTime, "Time Active"), gbc);

        //change status button
        gbc.gridy = gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        changeStatusButton = new JButton(office.isActiveOffice() ? "Deactivate" : "Activate");
        changeStatusButton.addActionListener(this);
        changeStatusButton.setActionCommand("statusChange");
        this.add(changeStatusButton, gbc);
    }

    private JPanel getDefaultDataPanel(JComponent component, String label) {
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel(label));
        dataPanel.add(component);
        return dataPanel;
    }

    public void save(Office office) {
        office.setOfficeOwner(officeOwnerField.getText());
        office.setOfficeOwnerEmail(officeOwnerEmailField.getText());
        office.setOfficePrimaryContactPerson(officePrimaryContactPersonField.getText());
        office.setOfficePrimaryContactEmail(officePrimaryContactEmailField.getText());
        office.setOfficePrimaryContactPhone(officePrimaryContactPhoneField.getText());
        office.setTrainingStatus(trainingStatusComboBox.getSelectedIndex());
        LocalDate date = execAgreementDateField.getDate();
        office.setExecAgreementDate(date);
    }

    public String getDurationSince(LocalDate startDate) {
        if (startDate == null) {
            return "No Start Date";
        }

        // Get the current date
        LocalDate endDate = LocalDate.now();

        // Calculate the period between the two dates
        Period period = Period.between(startDate, endDate);

        // Extract years and months
        int years = period.getYears();
        int months = period.getMonths();

        // If less than 1 year, return only months
        if (years == 0) {
            long totalMonths = period.toTotalMonths(); // Total months including any partial years
            return totalMonths + " Month" + (totalMonths != 1 ? "s" : "");
        }

        // If 1 year or more, return years and remaining months
        String result = years + " Year" + (years != 1 ? "s" : "");
        if (months > 0) {
            result += " " + months + " Month" + (months != 1 ? "s" : "");
        }
        return result.trim();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("statusChange")) {
            parent.changeStatus();
        }
    }
}
