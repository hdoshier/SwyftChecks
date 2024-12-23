package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.Database;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.gui.MainWindow;
import healthcheck.gui.mainpanels.HealthCheckPeriodPanelq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class HealthCheckHostPanel extends JPanel {
    private MainWindow parent;
    private JPanel periodListPanel;

    public HealthCheckHostPanel (MainWindow parent) {
        this.parent = parent;
        this.setPreferredSize(new Dimension(850, 600));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // create periodsPanel
        gbc.weightx = 1.0;
        this.add(createPeriodsPanel(), gbc);

        //create viewTypePanel
        gbc.weighty = 1.0;
        gbc.gridy = 1;
        this.add(new JPanel(), gbc);

    }

    private JPanel createPeriodsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JButton addNewPeriodBtn = new JButton("+");
        //addNewPeriodBtn.addActionListener(this);
        addNewPeriodBtn.setActionCommand("addNew");
        panel.add(addNewPeriodBtn, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(createPeriodListPane(), gbc);
        return panel;
    }

    private JScrollPane createPeriodListPane() {
        periodListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addExistingPeriods();
        JScrollPane scrollPane = new JScrollPane(periodListPanel);
        return scrollPane;
    }

    private void addExistingPeriods() {
        Database db = Database.getInstance();

        ArrayList<HealthCheckPeriod> list = db.getHealthCheckPeriodList();
        if (list.isEmpty()) {
            return;
        }

        for (HealthCheckPeriod i : list) {
            JLabel label = new JLabel(i.toString());
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Change the active item

                    label.setBackground(new Color(255, 151, 25)); // Highlight active item

                    // Perform action (switch view, print message, etc.)
                    System.out.println(i.toString() + " clicked");
                    //actionPerformed(name);
                }
            });
            periodListPanel.add(label);
        }
    }
    /*
    private HealthCheckPeriod createNewPeriod(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = Database.getInstance().addNewHealthCheckPeriod(start, end);
        HealthCheckPeriodPanelq panel = new HealthCheckPeriodPanelq(this, period);
        tabs.add(period.toString(), panel);
        Database.getInstance().saveDatabase();
    }*/

    // select period details panel

    // select period list panel
}
