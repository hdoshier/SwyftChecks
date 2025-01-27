package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.Office;
import healthcheck.data.firestore.Database;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckPeriodListPanelNew extends JPanel implements ActionListener {
    private HealthCheckPeriodPanel parent;
    private HealthCheckPeriod period;
    GridBagConstraints mainGbc;
    JPanel searchPanel;
    JPanel managePanel;
    JScrollPane officePane;
    JTable officeTable;
    JTextField nameSearchField;
    JComboBox<String> statusSearch;
    ArrayList<HealthCheck> healthCheckList;
    int officeIndex = -1;


    public HealthCheckPeriodListPanelNew(HealthCheckPeriodPanel parent, HealthCheckPeriod period) {
        this.parent = parent;
        this.period = period;
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

    private void filterOfficeList() {
        String nameFilter = nameSearchField.getText().toUpperCase();
        int activeStatus =  statusSearch.getSelectedIndex();

        // Loads inactive offices from the firestore DB.
        if (activeStatus != 0) {
            Database.getInstance().loadInactiveOffices();
        }

        //healthCheckList = new ArrayList<>();
        healthCheckList = period.getHealthCheckList();
        /*
        for (HealthCheck i : period.getHealthCheckList()) {
            // TODO filter based on search criteria
        }

         */
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

    private void buildOfficeListTable() {
        // Create table with a custom model
        HealthCheckPeriodListPanelNew.OfficeTableModel tableModel = new HealthCheckPeriodListPanelNew.OfficeTableModel();
        officeTable = new JTable(tableModel);
        officeTable.setRowHeight(25);


        // Customize button column
        TableColumn buttonColumn = officeTable.getColumnModel().getColumn(0);
        buttonColumn.setCellRenderer(new HealthCheckPeriodListPanelNew.ButtonRenderer());
        buttonColumn.setCellEditor(new HealthCheckPeriodListPanelNew.ButtonEditor(new JCheckBox(), this));

        // Adjust the width of the button column
        buttonColumn.setPreferredWidth(2); // Set the preferred width for column 0

        if (officePane != null) {
            this.remove(officePane);
        }

        // Wrap table in a scroll pane
        officePane = new JScrollPane(officeTable);

        // Populate table with office data
        filterOfficeList();
        for (HealthCheck check : healthCheckList) {
            tableModel.addHealthCheck(check);
        }

        // Add the scroll pane to the panel
        this.add(officePane, mainGbc);
        this.revalidate();
        this.repaint();
    }

    private void loadHealthCheck(int selectedOffice) {
        HealthCheck check = healthCheckList.get(selectedOffice);
        parent.setContentPanel(new HealthCheckPanel (this, check));
    }

    public void loadNextHealthCheck() {
        int index = officeIndex + 1;
        if (index < 0 || index >= healthCheckList.size()) {
            return;
        }
        loadHealthCheck(index);
    }

    public void loadPreviousHealthCheck() {
        int index = officeIndex - 1;
        if (index < 0 || index >= healthCheckList.size()) {
            return;
        }
        loadHealthCheck(index);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);
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
        private final HealthCheckPeriodListPanelNew parentPanel; // Reference to the parent panel
        private int currentRow; // Store the row number

        public ButtonEditor(JCheckBox checkBox, HealthCheckPeriodListPanelNew parentPanel) {
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
                parentPanel.loadHealthCheck(currentRow);
            }
            isPushed = false;
            return button.getText();
        }
    }

    // Custom TableModel
    static class OfficeTableModel extends AbstractTableModel {
        private final String[] columnNames = {"", "Office Code", "Office Name", "Status", "Assigned To"};
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

        public void addHealthCheck(HealthCheck check) {
            Office office = check.getOffice();
            int status = check.getHealthCheckStatus();
            String statusString;
            // {"Unassigned", "Pending", "Reviewed", "Completed"}
            if (status == 0) {
                statusString = "Unassigned";
            } else if (status == 1) {
                statusString = "Pending";
            } else if (status == 2) {
                statusString = "Reviewed";
            } else  {
                statusString = "Completed";
            }
            data.add(new Object[]{"Edit", office.getOfficeCode(), office.getOfficeName(), statusString, check.getAssignedTo()});
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }
}
