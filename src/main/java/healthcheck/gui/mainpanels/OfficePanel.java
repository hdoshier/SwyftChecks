package healthcheck.gui.mainpanels;

import healthcheck.data.Database;
import healthcheck.data.Office;
import healthcheck.gui.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *The ExpensePanel gui class.
 *
 *<p>This class creates the panel that manages Expenses.
 *
 *@author Hunter Doshier hunterdoshier@ksu.edu
 *
 *@version 0.1
 */
public class OfficePanel extends JPanel implements ActionListener {
    MainWindow parent;
    GridBagConstraints mainGbc;
    JPanel searchPanel;
    JPanel managePanel;
    JScrollPane officePane;
    JList<Office> officeList;
    DefaultListModel model;
    Database database;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public OfficePanel(MainWindow parent) {
        this.database = Database.getInstance();
        this.parent = parent;
        this.setPreferredSize(new Dimension(850, 600));
        this.setLayout(new GridBagLayout());

        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;

        // search panel
        mainGbc.gridy = 0;
        this.buildSearchPanel();

        // manage panel
        mainGbc.gridy = 1;
        this.buildManagePanel();

        // list panel
        mainGbc.gridy = 2;
        mainGbc.weighty = 1.0;
        this.buildListPanel();
    }



    private void buildSearchPanel() {
        if (this.searchPanel != null) {
            this.remove(searchPanel);
        }
        searchPanel = new JPanel();
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

        this.add(searchPanel, mainGbc);
    }

    private void buildManagePanel() {
        managePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

        this.add(managePanel, mainGbc);
    }

    private void buildListPanel() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        officePane = new JScrollPane(listPanel);

        ArrayList<Office> allOffices = database.getOfficeList();
        for (Office i : allOffices) {
            listPanel.add(createOfficeListItem(i));
        }

        this.add(officePane, mainGbc);
    }

    private JPanel createOfficeListItem(Office office) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.gridy = 0;

        // edit button
        gbc.gridx = 0;
        JButton btn = new JButton("Edit");
        btn.addActionListener(this);
        btn.setActionCommand(office.getOfficeCode());
        panel.add(btn, gbc);

        // office code
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(new JLabel(office.getOfficeCode()), gbc);

        // office name
        gbc.gridx = 2;
        panel.add(new JLabel(office.getOfficeName()), gbc);
        return panel;
    }
/*
    private void buildListPanel() {
        model = new DefaultListModel();
        ArrayList<Office> allOffices = database.getOfficeList();
        for (Office i : allOffices) {
            model.addElement(i);
        }
        officeList = new JList(model);
        officeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        officeList.addListSelectionListener(e -> {
            // Ensure the event is not triggered during list updates
            if (!e.getValueIsAdjusting()) {
                Office selectedOffice = officeList.getSelectedValue();
                if (selectedOffice != null) {
                    openOfficeInfoPanel(selectedOffice);
                }
            }
        });

        officePane = new JScrollPane(officeList);

        this.add(officePane, mainGbc);
    }

 */

    private void openOfficeInfoPanel(Office selectedOffice) {
        parent.loadPanel(new OfficeInfoPanel(parent, selectedOffice));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        openOfficeInfoPanel(Database.getInstance().getOfficeMap().get(e.getActionCommand()));
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
