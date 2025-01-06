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
    private HealthCheckPeriodPanel periodPanel = null;
    private GridBagConstraints gbc;
    private JPanel periodManagePanel;
    private JComboBox<String> periodBox;

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
        HealthCheckPeriod period =
                db.getHealthCheckPeriodList().isEmpty() ? null : db.getHealthCheckPeriodList().getFirst();
        setPeriodContentPanel(period);

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

        periodManagePanel.add(populateSpinner());

        return periodManagePanel;
    }

    private JComboBox<String> populateSpinner() {
        periodBox = new JComboBox<>();

        for (HealthCheckPeriod i : db.getHealthCheckPeriodList()) {
            periodBox.addItem(i.getPeriodDateRange());
        }

        // Add action listener to handle selection changes
        periodBox.addActionListener(e -> {
            int selectedIndex = periodBox.getSelectedIndex();
            setPeriodContentPanel(db.getHealthCheckPeriodList().get(selectedIndex));
        });
        return periodBox;
    }

    public void createNewPeriod(LocalDate start, LocalDate end) {
        HealthCheckPeriod period = db.addNewHealthCheckPeriod(start, end);
        setPeriodContentPanel(period);

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) periodBox.getModel();
        model.insertElementAt(period.getPeriodDateRange(), 0);

        //set the dropdown to show the first index as selected
        periodBox.setSelectedIndex(0);

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
