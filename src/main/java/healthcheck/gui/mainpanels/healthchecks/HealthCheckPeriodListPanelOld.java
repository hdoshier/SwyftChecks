package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;
import healthcheck.gui.mainpanels.offices.OfficePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckPeriodListPanelOld extends JPanel implements ActionListener {
    private HealthCheckPeriodPanel parent;
    private HealthCheckPeriod period;

    public HealthCheckPeriodListPanelOld(HealthCheckPeriodPanel parent, HealthCheckPeriod period) {
        this.parent = parent;
        this.period = period;
        this.setLayout(new GridBagLayout());

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;

        // search panel
        mainGbc.gridy = 0;
        this.add(buildSearchPanel(), mainGbc);

        // manage panel
        mainGbc.gridy = 1;
        this.add(buildManagePanel(), mainGbc);

        // list panel
        mainGbc.gridy = 2;
        mainGbc.weighty = 1.0;
        this.add(buildListPanel(), mainGbc);
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        searchPanel.setBackground(new Color(255, 242, 204));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel searchLabel = new JLabel("Search");
        searchPanel.add(searchLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JTextField nameSearch = new JTextField();
        nameSearch.setPreferredSize(new Dimension(150, 20));
        searchPanel.add(nameSearch, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField statusSearch = new JTextField();
        statusSearch.setPreferredSize(new Dimension(150, 20));
        searchPanel.add(statusSearch, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        JButton searchButton = new JButton("Search");
        searchButton.setActionCommand("search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton, gbc);

        return searchPanel;
    }

    private JPanel buildManagePanel() {
        JPanel managePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        managePanel.setBackground(new Color(248, 248, 248));
        managePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JCheckBox globalBox = new JCheckBox();
        managePanel.add(globalBox);


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

    private JScrollPane buildListPanel() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane officePane = new JScrollPane(listPanel);

        ArrayList<HealthCheck> checks = period.getHealthCheckList();
        for (HealthCheck i : checks) {
            listPanel.add(createOfficeListItem(i));
        }

        return officePane;
    }

    private JPanel createOfficeListItem(HealthCheck check) {
        Office office = check.getOffice();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;

        // checkbox
        gbc.gridx = 0;
        JCheckBox globalBox = new JCheckBox();
        panel.add(globalBox, gbc);

        // edit button
        gbc.gridx = 1;
        JButton btn = new JButton("Edit");
        btn.addActionListener(this);
        btn.setActionCommand(office.getOfficeCode());
        panel.add(btn, gbc);

        // office code
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        panel.add(new JLabel(office.getOfficeCode()), gbc);

        // office name
        gbc.gridx = 3;
        panel.add(new JLabel(office.getOfficeName()), gbc);
        return panel;
    }

    private void openHealthCheckPanel(HealthCheck check) {
        //parent.setContentPanel(new HealthCheckPanel(this, check));
    }

    public void openOfficePanel(Office office) {
        parent.setContentPanel(new OfficePanel(parent, office));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        openHealthCheckPanel(period.getHealthCheckByOffice(e.getActionCommand()));
    }
}
