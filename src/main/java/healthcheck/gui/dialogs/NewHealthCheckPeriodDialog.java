package healthcheck.gui.dialogs;

import com.github.lgooddatepicker.components.DatePicker;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckHostPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewHealthCheckPeriodDialog extends JDialog implements ActionListener {
    private GridBagConstraints mainGbc;
    private DatePicker startdate;
    private DatePicker enddate;
    private HealthCheckPeriod period = null;
    private HealthCheckHostPanel hostPanel;

    public NewHealthCheckPeriodDialog(JFrame parent, HealthCheckHostPanel hostPanel) {
        super(parent, "Add New", null);
        this.hostPanel = hostPanel;
        this.setSize(450, 300);
        this.setLayout(new GridBagLayout());

        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);

        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        JLabel label = new JLabel("Select the start and end date for the new health check period.");
        this.add(label, mainGbc);

        mainGbc.gridy = 1;
        startdate = new DatePicker();
        this.add(startdate, mainGbc);

        mainGbc.gridy = 2;
        enddate = new DatePicker();
        this.add(enddate, mainGbc);

        mainGbc.gridy = 3;
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
            //hostPanel.addTab(startdate.getDate(), enddate.getDate());
        }
        this.dispose();
    }

    /**
     *Displays the window.
     *
     *@return the newly created health check period
     */
    public HealthCheckPeriod run() {
        this.setVisible(true);
        return this.period;
    }
}
