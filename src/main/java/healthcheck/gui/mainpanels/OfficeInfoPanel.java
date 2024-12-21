package healthcheck.gui.mainpanels;

import healthcheck.data.Office;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import com.github.lgooddatepicker.components.DatePicker;

/**
 *The ExpensePanel gui class.
 *
 *<p>This class creates the panel that manages Expenses.
 *
 *@author Hunter Doshier hunterdoshier@ksu.edu
 *
 *@version 0.1
 */
public class OfficeInfoPanel extends JPanel implements ActionListener {
    MainWindow parent;
    Office office;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public OfficeInfoPanel(MainWindow parent, Office office) {
        this.office = office;

        this.parent = parent;
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(850, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // navigation panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(buildNavPanel(), gbc);

        // office data/info panel
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        this.add(buildInfoPanel(), gbc);

        // office billable hour history panel
        gbc.gridy = 2;
        gbc.weighty = 0.09;
        this.add(buildBillableHoursPanel(), gbc);

    }



    private JPanel buildNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(0, 122, 178)); // Base color
        navPanel.setLayout(new GridBagLayout());

        GridBagConstraints navGbc = new GridBagConstraints();
        navGbc.gridx = 0;
        navGbc.gridy = 0;
        navGbc.weightx = 1.0;

        // nav options
        //configure nav options panel
        JPanel navOptionsPanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        navOptionsPanel.setBackground(new Color(0, 122, 178));
        //add nav options
        addNavItem(navOptionsPanel, office.getOfficeCode(), true);
        addNavItem(navOptionsPanel, "Health Check History", false);
        navPanel.add(navOptionsPanel, navGbc);


        // have save button be on the far right
        navGbc.gridx = 1;
        navGbc.weightx = 0;
        // save panel config
        JPanel navSavePanel = new JPanel();
        navOptionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        navSavePanel.setBackground(new Color(0, 122, 178));
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        navSavePanel.add(saveButton);
        navPanel.add(navSavePanel, navGbc);

        return navPanel;
    }

    private void addNavItem(JPanel panel, String name, boolean isActive){
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setBackground(isActive ? new Color(255, 151, 25) : new Color(0, 122, 178)); // Highlight active
        navItem.setForeground(Color.WHITE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 16));
        navItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add hover effect
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) navItem.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Perform action (switch view, print message, etc.)
                System.out.println(name + " clicked");
            }
        });

        panel.add(navItem);
    }

    private JPanel buildInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(248, 248, 248));
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        addJTextDetailsPanel(infoPanel, "Office Name", new JTextField(office.getOfficeName()));
        addJTextDetailsPanel(infoPanel, "Owner", new JTextField(office.getOfficeOwner()));
        addJTextDetailsPanel(infoPanel, "Owner Email", new JTextField(office.getOfficeOwnerEmail()));
        addJTextDetailsPanel(infoPanel, "Primary Contact", new JTextField(office.getOfficePrimaryContactPerson()));
        addJTextDetailsPanel(infoPanel, "Primary Contact Email", new JTextField(office.getOfficePrimaryContactEmail()));

        addDateFieldDetailsPanel(infoPanel, "Last Health Check Date", office.getLastHealthCheckDate());
        addDateFieldDetailsPanel(infoPanel, "Agreement Date", office.getExecAgreementDate());

        //addJTextDetailsPanel(infoPanel, "Training Status", office.getTrainingStatus(), officeOwnerEmail);
        addJTextDetailsPanel(infoPanel, "General Notes", new JTextArea(office.getGeneralNotes(), 7, 7));
        addJTextDetailsPanel(infoPanel, "Leadership Notes", new JTextArea(office.getLeadershipNotes(), 7, 7));



        return infoPanel;
    }

    private void addJTextDetailsPanel (JPanel infoPanel, String labelText, JTextComponent textField) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        textPanel.add(label);
        textPanel.add(textField);

        infoPanel.add(textPanel);

        if (textField instanceof JTextArea textArea) {
            textArea.setLineWrap(true);
        }
    }

    private void addDateFieldDetailsPanel (JPanel infoPanel, String labelText, LocalDate date) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        detailsPanel.add(label);
        DatePicker dateField = new DatePicker();
        dateField.setDate(date);
        detailsPanel.add(dateField);
        infoPanel.add(detailsPanel);
    }

    private JScrollPane buildBillableHoursPanel() {
        JPanel scrollPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        YearMonth month = office.getMostRecentBillableHistory();
        HashMap<YearMonth, Double> billableMap = office.getBillableHourHistory();
        while (billableMap.containsKey(month)) {
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            JLabel monthYear = new JLabel(month.toString());
            detailsPanel.add(monthYear);
            JLabel billableHours = new JLabel(String.valueOf(billableMap.get(month)));
            detailsPanel.add(billableHours);
            scrollPanel.add(detailsPanel);
            month = month.minusMonths(1);
        }
        return scrollPane;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        /*
        if ("create".equals(e.getActionCommand())) {
            String[] options = {"Expense", "Debt", "Cancel"};
            int n = JOptionPane.showOptionDialog(this.parent,
                    "Will this be a normal expense, or a new debt?", "Add New",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[2]);
            if (n == 0) {
                //expense
                ExpenseDialog diag = new ExpenseDialog(this.parent);
                Expense expense = diag.run();

                //it would be null if the user clicks cancel
                if (expense == null) {
                    return;
                }
                model.addElement(expense);
            } else if (n == 1) {
                //debt
                DebtDialog diag = new DebtDialog(this.parent);
                Debt debt = diag.run();

                //it would be null if the user clicks cancel
                if (debt == null) {
                    return;
                }
                model.addElement(debt);

            }
        }
        if ("edit".equals(e.getActionCommand())) {
            Expense i = (Expense) expenseList.getSelectedValue();
            //no value selected
            if (i == null) {
                return;
            }

            //if debt else expense
            if (i.getBillType() == BillType.DEBT) {
                DebtDialog diag = new DebtDialog(this.parent, (Debt) i);
                Debt expense = diag.run();
                //cancel button selected
                if (expense == null) {
                    return;
                }
                Database database = Database.getDatabase();
                model.removeElement(i);
                database.removeExpense(i);
                model.addElement(expense);
            } else {
                ExpenseDialog diag = new ExpenseDialog(this.parent, i);
                Expense expense = diag.run();
                //cancel button selected
                if (expense == null) {
                    return;
                }
                Database database = Database.getDatabase();
                model.removeElement(i);
                database.removeExpense(i);
                model.addElement(expense);
            }
        }
        if ("delete".equals(e.getActionCommand())) {
            Expense i = (Expense) expenseList.getSelectedValue();
            //no value selected
            if (i == null) {
                return;
            }
            Database database = Database.getDatabase();
            model.removeElement(i);
            database.removeExpense(i);
            if (i.getBillType() == BillType.DEBT) {
                this.buildDebtPanel();
                debtPanel.revalidate();
                debtPanel.repaint();
            }
        }
        Database database = Database.getDatabase();
        System.out.println(database.getDebtList().size());
        this.buildDebtPanel();
        debtPanel.revalidate();
        debtPanel.repaint();
        this.buildmanagePanel();
        managePanel.revalidate();
        managePanel.repaint();*/
    }
}
