package healthcheck.gui.dialogs;

import healthcheck.data.*;
import healthcheck.data.firestore.WriteData;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckListPanel;
import healthcheck.gui.mainpanels.offices.OfficeListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckGlobalSetDialog extends JDialog implements ActionListener {
    private GridBagConstraints mainGbc;
    private HealthCheckListPanel hostPanel;
    private ArrayList<HealthCheck> checkList;
    private HealthCheckPeriod period;
    private int[] indexList;

    //global set options
    // Assigned to
    private JComboBox<String> assignedToBox;


    public HealthCheckGlobalSetDialog(JFrame parent, HealthCheckListPanel hostPanel) {
        super(parent, "Global Set", null);
        this.hostPanel = hostPanel;
        this.setSize(450, 300);
        this.setLayout(new GridBagLayout());

        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);


        mainGbc.gridy = 0;
        assignedToBox = new JComboBox<>();
        assignedToBox.addItem("Unassigned");
        for (String user : MySettings.getInstance().getUsers()) {
            assignedToBox.addItem(user);
        }
        assignedToBox.setSelectedIndex(0);
        this.add(assignedToBox, mainGbc);

        mainGbc.gridy = 2;
        JButton save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        this.add(save, mainGbc);

        mainGbc.gridx = 1;
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        this.add(cancel, mainGbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("save")) {
            for (int i : indexList) {
                HealthCheck check = checkList.get(i);
                check.setAssignedTo((String) assignedToBox.getSelectedItem());
                WriteData.updateHealthCheckInFirestore(period, check);
            }
            hostPanel.buildOfficeListTable();
        }
        this.dispose();
    }



    public void run(int[] indexList, ArrayList<HealthCheck> checkList, HealthCheckPeriod period) {
        this.indexList = indexList;
        this.checkList = checkList;
        this.period = period;
        this.setVisible(true);
    }
}
