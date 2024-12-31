package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.firestore.Database;
import healthcheck.gui.MainWindow;
import healthcheck.gui.dialogs.NewHealthCheckPeriodDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class HealthCheckHostPanel extends JPanel implements ActionListener {
    private MainWindow parent;
    private Database db;
    private JPanel periodListPanel;
    private HealthCheckPeriodPanel periodPanel = null;
    private GridBagConstraints gbc;
    private HealthCheckPeriod activePeriod;
    private JLabel activePeriodLabel = null;
    private JPanel periodManagePanel;

    public HealthCheckHostPanel (MainWindow parent) {
        db = Database.getInstance();
        this.parent = parent;
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // create period list
        gbc.weightx = 1.0;
        this.add(createPeriodListPanel(), gbc);

        //create period view panel
        gbc.weighty = 1.0;
        gbc.gridy = 1;
        activePeriod =
                db.getHealthCheckPeriodList().isEmpty() ? null : db.getHealthCheckPeriodList().getLast();
        setPeriodContentPanel(activePeriod);

    }

    private void setPeriodContentPanel(HealthCheckPeriod period) {
        if (periodPanel != null) {
            this.remove(periodPanel);
        }
        periodPanel = new HealthCheckPeriodPanel(parent, period);
        this.add(periodPanel, gbc);
        periodPanel.revalidate();
        periodPanel.repaint();
    }

    private JPanel createPeriodListPanel() {
        periodManagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        periodManagePanel.setBackground(new Color(0, 122, 178));
        JButton addNewPeriodBtn = new JButton("+Add New");
        addNewPeriodBtn.addActionListener(this);
        addNewPeriodBtn.setActionCommand("addNew");
        periodManagePanel.add(addNewPeriodBtn);

        periodManagePanel.add(createPeriodListPane());
        return periodManagePanel;
    }

    private JScrollPane createPeriodListPane() {
        periodListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        periodListPanel.setBackground(new Color(0, 122, 178));

        //adds existing periods to the list.
        ArrayList<HealthCheckPeriod> list = db.getHealthCheckPeriodList();
        if (!list.isEmpty()) {
            for (HealthCheckPeriod i : list) {
                addPeriodToPeriodListPanel(i);
            }
        }
        return new JScrollPane(periodListPanel);
    }

    private void addPeriodToPeriodListPanel(HealthCheckPeriod period) {
        JLabel label = new JLabel(period.toString());
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        label.setBackground(new Color(255, 151, 25));


        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label != activePeriodLabel) {
                    label.setBackground(new Color(0, 122, 178));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                activePeriodLabel.setBackground(new Color(0, 122, 178));
                label.setBackground(new Color(255, 151, 25)); // Highlight active item
                activePeriodLabel = label;
                // Perform action (switch view, print message, etc.)
                System.out.println(period.toString() + " clicked");

                setPeriodContentPanel(period);
            }
        });
        if (activePeriodLabel != null) {
            activePeriodLabel.setBackground(new Color(0, 122, 178));
        }
        activePeriodLabel = label;
        periodListPanel.add(label, 0);
    }

    public void createNewPeriod(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = db.addNewHealthCheckPeriod(start, end);
        addPeriodToPeriodListPanel(period);
        setPeriodContentPanel(period);
        periodPanel.revalidate();
        periodPanel.repaint();
        periodManagePanel.revalidate();
        periodManagePanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);
        if (actionCommand.equals("addNew")) {
            NewHealthCheckPeriodDialog diag = new NewHealthCheckPeriodDialog(parent, this);
            // TODO add validation
            diag.run();
        }
    }
}
