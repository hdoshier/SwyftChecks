package healthcheck.gui.mainpanels.offices;

import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.gui.MainWindow;
import healthcheck.gui.dialogs.OfficeGlobalSetDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

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
    JTable officeTable;
    JTextField nameSearchField;
    JComboBox<String> statusSearch;
    ArrayList<Office> officeList;
    int officeIndex = -1;

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
        this.buildOfficeListTable();
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

        JLabel searchLabel = new JLabel("Search");
        searchPanel.add(searchLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Office Code/Name");
        nameSearchField = new JTextField();
        nameSearchField.setPreferredSize(new Dimension(150, 20));
        panel.add(nameLabel);
        panel.add(nameSearchField);

        searchPanel.add(panel, gbc);


        gbc.gridx = 1;
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameLabel = new JLabel("Status");
        String[] arr = {"Active", "Inactive", "All"};
        statusSearch = new JComboBox<>(arr);
        statusSearch.setSelectedIndex(0);
        panel.add(nameLabel);
        panel.add(statusSearch);
        searchPanel.add(panel, gbc);

        gbc.gridx = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton searchButton = new JButton("Search");
        searchButton.setActionCommand("search");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);
        JButton resetButton = new JButton("Reset Filters");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);


        searchPanel.add(buttonPanel, gbc);

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

    private void filterOfficeList() {
        String nameFilter = nameSearchField.getText().toUpperCase();
        int activeStatus =  statusSearch.getSelectedIndex();

        // Loads inactive offices from the firestore DB.
        if (activeStatus != 0) {
            Database.getInstance().loadInactiveOffices();
        }

        officeList = new ArrayList<>();
        for (Office i : Database.getInstance().getOfficeList()) {
            // checks status
            if (!satisfiesStatusCheck(i.isActiveOffice(), activeStatus)) {
                continue;
            }
            // name/code match
            if (i.getOfficeCode().contains(nameFilter)) {
                officeList.add(i);
                continue;
            }
            if (i.getOfficeName().toUpperCase().contains(nameFilter)) {
                officeList.add(i);
            }
        }
    }

    private boolean satisfiesStatusCheck (boolean isOfficeActive, int statusSelected) {
        if (statusSelected == 2) {
            return true;
        }
        if (isOfficeActive && statusSelected == 0) {
            return true;
        }
        if (!isOfficeActive && statusSelected == 1) {
            return true;
        }

        return false;
    }

    private void loadOffice(int selectedOffice) {
        Office office = officeList.get(selectedOffice);
        officeIndex = selectedOffice;
        System.out.println("Opening Office: " + office.getOfficeCode());
        parent.loadPanel(new OfficePanel
                (parent,this, ReadData.readIndividualOffice(office)));
    }

    public void loadNextOffice() {
        int index = officeIndex + 1;
        if (index < 0 || index >= officeList.size()) {
            System.out.println("Invalid Index: " + index);
            return;
        }
        loadOffice(index);
    }

    public void loadPreviousOffice() {
        int index = officeIndex - 1;
        if (index < 0 || index >= officeList.size()) {
            System.out.println("Invalid Index: " + index);
            return;
        }
        loadOffice(index);
    }

    // ACTION PERFORMED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);
        if (actionCommand.equals("search")) {
            buildOfficeListTable();
        }
        if (actionCommand.equals("reset")) {
            nameSearchField.setText("");
            statusSearch.setSelectedIndex(0);
            buildOfficeListTable();
        }
        if (actionCommand.equals("globalSet")) {
            // TODO Pausing Development until I have a better idea of what to add.
            //OfficeGlobalSetDialog diag = new OfficeGlobalSetDialog(parent, this);
            //diag.run();
        }
        System.out.println(Arrays.toString(officeTable.getSelectedRows()));
    }

    public void buildOfficeListTable() {
        // Create table with a custom model
        OfficeTableModel tableModel = new OfficeTableModel();
        officeTable = new JTable(tableModel);
        officeTable.setRowHeight(25);


        // Customize button column
        TableColumn buttonColumn = officeTable.getColumnModel().getColumn(0);
        buttonColumn.setCellRenderer(new ButtonRenderer());
        buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // Adjust the width of the button column
        buttonColumn.setPreferredWidth(2); // Set the preferred width for column 0

        if (officePane != null) {
            this.remove(officePane);
        }

        // Wrap table in a scroll pane
        officePane = new JScrollPane(officeTable);

        // Populate table with office data
        filterOfficeList();
        for (Office office : officeList) {
            tableModel.addOffice(office);
        }

        // Add the scroll pane to the panel
        this.add(officePane, mainGbc);
        this.revalidate();
        this.repaint();
    }

    // TABLE CONFIGURATION CLASSES !!

    // Button Renderer Class
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "Edit");
            setPreferredSize(new Dimension(getPreferredSize().width + 10, getPreferredSize().height)); // Adjust button width
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    // Button Editor Class
    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private boolean isPushed;
        private final OfficeListPanel parentPanel; // Reference to the parent panel
        private int currentRow; // Store the row number

        public ButtonEditor(JCheckBox checkBox, OfficeListPanel parentPanel) {
            super(checkBox);
            this.parentPanel = parentPanel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row; // Store the current row
            button.setText(value != null ? value.toString() : "Edit");
            button.setPreferredSize(new Dimension(button.getPreferredSize().width + 10, button.getPreferredSize().height)); // Adjust button width
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Use the currentRow to load the office
                parentPanel.loadOffice(currentRow);
            }
            isPushed = false;
            return button.getText();
        }
    }

    // Custom TableModel
    static class OfficeTableModel extends AbstractTableModel {
        private final String[] columnNames = {"", "Office Code", "Office Name", "Contact Name", "Contact Email"};
        private final ArrayList<Object[]> data = new ArrayList<>();

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            return data.get(row)[column];
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            data.get(row)[column] = value;
            fireTableCellUpdated(row, column);
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // allows the button to be clicked
        }

        public void addOffice(Office office) {
            data.add(new Object[]{"Edit", office.getOfficeCode(), office.getOfficeName(), office.getOfficePrimaryContactPerson(), office.getOfficePrimaryContactEmail()});
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }
}
