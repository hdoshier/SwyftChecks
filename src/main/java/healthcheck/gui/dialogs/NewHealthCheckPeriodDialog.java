package healthcheck.gui.dialogs;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.firestore.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class NewHealthCheckPeriodDialog extends JDialog implements ActionListener {
    private GridBagConstraints mainGbc;
    private DatePicker startdate;
    private DatePicker enddate;
    private HealthCheckPeriod period = null;

    public NewHealthCheckPeriodDialog(JFrame parent) {
        super(parent, "Add New", null);
        //this.hostPanel = hostPanel;
        this.setSize(450, 300);
        this.setLayout(new GridBagLayout());

        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);

        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.gridwidth = 2;
        JLabel label = new JLabel("Select the start and end date for the new health check period.");
        this.add(label, mainGbc);

        LocalDate lastPeriodEndDate = Database.getInstance().getHealthCheckPeriodList().getFirst().getEndDate();

        LocalDate firstDate = lastPeriodEndDate.plusDays(1);

        mainGbc.gridy = 1;
        DatePickerSettings setting = new DatePickerSettings();
        startdate = new DatePicker(setting);
        startdate.setDate(firstDate);
        this.add(startdate, mainGbc);
        setting.setDateRangeLimits(firstDate, null);

        mainGbc.gridy = 2;
        setting = new DatePickerSettings();
        enddate = new DatePicker(setting);
        enddate.setDate(firstDate.plusMonths(3));
        this.add(enddate, mainGbc);
        setting.setDateRangeLimits(firstDate, null);

        mainGbc.gridy = 3;
        mainGbc.gridwidth = 1;
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
            Database.getInstance().addNewHealthCheckPeriod(startdate.getDate(), enddate.getDate());
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
