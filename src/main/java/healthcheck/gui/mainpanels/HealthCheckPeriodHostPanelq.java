package healthcheck.gui.mainpanels;

import healthcheck.data.Database;
import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.gui.MainWindow;
import healthcheck.gui.dialogs.NewHealthCheckPeriodDialog;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckPanelq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class HealthCheckPeriodHostPanelq extends JPanel implements ActionListener {
    private MainWindow parent;
    private JTabbedPane tabs;
    private GridBagConstraints mainGbc;

    public HealthCheckPeriodHostPanelq(MainWindow parent) {
        this.parent = parent;
        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setPreferredSize(new Dimension(850, 600));
        this.setLayout(new GridBagLayout());
        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;

        //add tabs
        mainGbc.weighty = 1.0;
        this.add(tabs, mainGbc);
        populateTabs();

        //add manage
        mainGbc.gridy = 1;
        //this.add(createManagePanel(), mainGbc);

        //add health check period panel
        mainGbc.gridy = 2;
        //
        // sets it to the most recent period
        //TODO test to see if it's the most recently added period
        //this.add(setPeriodPanel(Database.getInstance().getHealthCheckPeriodList().getFirst()), mainGbc);

    }

    public HealthCheckPeriodPanelq setPeriodPanel(HealthCheckPeriod period) {
        return new HealthCheckPeriodPanelq(this, period);
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

    public void addTab(HealthCheckPeriod period) {
        HealthCheckPeriodPanelq panel = new HealthCheckPeriodPanelq(this, period);
        tabs.add(period.toString(), panel);
        Database.getInstance().saveDatabase();
    }

    public void addTab(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = Database.getInstance().addNewHealthCheckPeriod(start, end);
        HealthCheckPeriodPanelq panel = new HealthCheckPeriodPanelq(this, period);
        tabs.add(period.toString(), panel);
        Database.getInstance().saveDatabase();
    }

    private void populateTabs() {
        Database db = Database.getInstance();

        ArrayList<HealthCheckPeriod> list = db.getHealthCheckPeriodList();
        if (list.isEmpty()) {
            return;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            addTab(list.get(i));
        }
    }

    public void loadPanel(HealthCheck check) {
        parent.loadPanel(new HealthCheckPanelq(parent, check));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand + " Host");
        if (actionCommand.equals("addNew")) {
            NewHealthCheckPeriodDialog diag = new NewHealthCheckPeriodDialog(parent, this);
            diag.run();
        } else if (actionCommand.equals("globalSet")) {

        }

        this.revalidate();
        this.repaint();
    }
}
