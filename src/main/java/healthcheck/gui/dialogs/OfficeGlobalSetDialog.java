package healthcheck.gui.dialogs;

import healthcheck.data.Office;
import healthcheck.gui.mainpanels.offices.OfficeListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OfficeGlobalSetDialog  extends JDialog implements ActionListener {
    private GridBagConstraints mainGbc;
    private OfficeListPanel hostPanel;
    private JCheckBox excludeOffice;
    private JCheckBox eligibleForChecks;
    private int[] indexes;
    private ArrayList<Office> officeList;

    public OfficeGlobalSetDialog(JFrame parent, OfficeListPanel hostPanel) {
        super(parent, "Global Set", null);
        this.hostPanel = hostPanel;
        this.setSize(450, 300);
        this.setLayout(new GridBagLayout());

        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);


        mainGbc.gridy = 0;
        eligibleForChecks = new JCheckBox( "Receives Health Checks");
        this.add(eligibleForChecks, mainGbc);

        mainGbc.gridy = 1;
        excludeOffice = new JCheckBox( "");
        this.add(excludeOffice, mainGbc);

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
            if (eligibleForChecks.isSelected()) {
                for (int i : indexes) {
                    Office office = officeList.get(i);
                    office.setActiveOffice(false);
                }
            }
        }
        this.dispose();
    }



    public void run(int[] indexes, ArrayList<Office> officeList) {
        this.indexes = indexes;
        this.officeList = officeList;
        this.setVisible(true);
    }
}
