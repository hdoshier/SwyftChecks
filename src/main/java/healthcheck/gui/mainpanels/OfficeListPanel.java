package healthcheck.gui.mainpanels;

import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
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
public class OfficeListPanel extends JPanel implements ActionListener {
    MainWindow parent;
    GridBagConstraints mainGbc;
    JPanel searchPanel;
    JPanel managePanel;
    JScrollPane officePane;
    JList<Office> officeList;
    DefaultListModel model;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public OfficeListPanel(MainWindow parent) {
        this.parent = parent;
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

        ArrayList<Office> allOffices = Database.getInstance().getAllOfficesList();
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

    private void openOfficeInfoPanel(Office selectedOffice) {
        parent.loadPanel(new OfficePanel(parent, selectedOffice));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        openOfficeInfoPanel(ReadData.readOffice(e.getActionCommand()));
    }
}
