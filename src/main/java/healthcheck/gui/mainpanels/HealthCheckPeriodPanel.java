package healthcheck.gui.mainpanels;

import healthcheck.data.Database;
import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;
import healthcheck.gui.dialogs.NewHealthCheckPeriodDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckPeriodPanel extends JPanel implements ActionListener {
    private HealthCheckPeriod period;
    private HealthCheckPeriodHostPanel parent;

    public HealthCheckPeriodPanel(HealthCheckPeriodHostPanel parent, HealthCheckPeriod period) {
        this.period = period;
        this.parent = parent;
        this.setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;

        // manage panel
        this.add(createManagePanel(), mainGbc);

        // show healthcheck list
        mainGbc.gridy = 1;
        mainGbc.weighty = 1.0;
        this.add(healthCheckListPanel(), mainGbc);

        //health check info panel
    }



    private JScrollPane healthCheckListPanel() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane officePane = new JScrollPane(listPanel);



        for (HealthCheck i : period.getHealthCheckList()) {
            listPanel.add(createOfficeListItem(i));
        }

        return officePane;
    }

    private JPanel createOfficeListItem(HealthCheck check) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.gridy = 0;
        Office office = check.getOffice();

        // edit button
        gbc.gridx = 0;
        JButton btn = new JButton("Edit");
        btn.addActionListener(this);
        btn.setActionCommand(office.getOfficeCode());
        panel.add(btn, gbc);

        // office code
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(new JLabel(office.getOfficeCode()), gbc);

        // office name
        gbc.gridx = 2;
        panel.add(new JLabel(office.getOfficeName()), gbc);
        return panel;
    }

    private JPanel createManagePanel() {
        JPanel managePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        managePanel.setBackground(new Color(248, 248, 248));
        managePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton addNewButton = new JButton("Add New");
        addNewButton.setActionCommand("addNew");
        addNewButton.addActionListener(this);
        managePanel.add(addNewButton);

        JButton globalSetButton = new JButton("Global Set");
        globalSetButton.setActionCommand("globalSet");
        globalSetButton.addActionListener(this);
        managePanel.add(globalSetButton);
        return managePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand + "Period");
        if (actionCommand.equals("addNew")) {
        } else if (actionCommand.equals("globalSet")) {

        }

        this.revalidate();
        this.repaint();
    }
}
