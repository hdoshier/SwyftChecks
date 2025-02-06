package healthcheck.gui.mainpanels.healthchecks;

import healthcheck.data.*;
import healthcheck.data.firestore.Database;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HealthCheckListPanel extends JPanel implements ActionListener {
    private MainWindow parent;
    private HealthCheckPeriod period;
    private ArrayList<HealthCheck> healthCheckList;
    private Database db;
    private int currentHealthCheckIndex = -1;
    GridBagConstraints mainGbc;
    JPanel searchPanel;
    JPanel managePanel;
    JScrollPane officePane;
    JTable officeTable;

    // search criteria
    private JComboBox<String> periodSearchField;
    private JTextField officeNameSearchField;
    private JComboBox<String> assignedToSearchField;
    private JComboBox<String> healthCheckStatusSearchField;
    private JCheckBox flaggedForReviewSearchField;


    public HealthCheckListPanel(MainWindow parent) {
        this.parent = parent;
        this.setLayout(new GridBagLayout());
        this.db = Database.getInstance();
        // defaults to most current period
        this.period = db.getHealthCheckPeriodList().getFirst();

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
        configureSearchCriteriaFields();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weightx = 1.0;

        JLabel searchLabel = new JLabel("Search");
        searchPanel.add(searchLabel, gbc);

        // Period combo box
        int xcord = 0;
        gbc.gridx = xcord++;
        gbc.gridy = 1;
        searchPanel.add(buildSearchFieldHostPanel("Health Check Period", periodSearchField), gbc);

        // office search
        gbc.gridx = xcord++;
        searchPanel.add(buildSearchFieldHostPanel("Office Code/Name", officeNameSearchField), gbc);

        // assigned to user combo box
        gbc.gridx = xcord++;
        searchPanel.add(buildSearchFieldHostPanel("Assigned To", assignedToSearchField), gbc);

        // HC status combo box
        gbc.gridx = xcord++;
        searchPanel.add(buildSearchFieldHostPanel("Status", healthCheckStatusSearchField), gbc);

        // flagged for review
        gbc.gridx = xcord++;
        searchPanel.add(buildSearchFieldHostPanel("Flagged for Review", flaggedForReviewSearchField), gbc);

        gbc.gridx = xcord;
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

    private JPanel buildSearchFieldHostPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(component);
        return panel;
    }

    private void configureSearchCriteriaFields() {
        officeNameSearchField = new JTextField();
        officeNameSearchField.setPreferredSize(new Dimension(150, 20));

        // assigned to user search
        assignedToSearchField = new JComboBox<>();
        assignedToSearchField.addItem("All");
        assignedToSearchField.addItem("Unassigned");
        for (String user : MySettings.getInstance().getUsers()) {
            assignedToSearchField.addItem(user);
        }
        assignedToSearchField.setSelectedIndex(0);

        // period search
        periodSearchField = new JComboBox<>();
        for (HealthCheckPeriod i : db.getHealthCheckPeriodList()) {
            periodSearchField.addItem(i.getPeriodDateRange());
        }
        periodSearchField.setSelectedIndex(0);

        // status search
        healthCheckStatusSearchField = new JComboBox<>();
        healthCheckStatusSearchField.addItem("All");
        healthCheckStatusSearchField.addItem("Incomplete");
        for (String status : MyGlobalVariables.HEALTH_CHECK_STATUS_ARRAY) {
            healthCheckStatusSearchField.addItem(status);
        }
        // set to all by default
        healthCheckStatusSearchField.setSelectedIndex(0);

        // flagged for review
        flaggedForReviewSearchField = new JCheckBox();
        flaggedForReviewSearchField.setSelected(false);
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

    public void buildOfficeListTable() {
        // Create table with a custom model
        HealthCheckListPanel.OfficeTableModel tableModel = new HealthCheckListPanel.OfficeTableModel();
        officeTable = new JTable(tableModel);
        officeTable.setRowHeight(25);


        // Customize button column
        TableColumn buttonColumn = officeTable.getColumnModel().getColumn(0);
        buttonColumn.setCellRenderer(new HealthCheckListPanel.ButtonRenderer());
        buttonColumn.setCellEditor(new HealthCheckListPanel.ButtonEditor(new JCheckBox(), this));

        // Adjust the width of the button column
        buttonColumn.setPreferredWidth(2); // Set the preferred width for column 0

        if (officePane != null) {
            this.remove(officePane);
        }

        // Wrap table in a scroll pane
        officePane = new JScrollPane(officeTable);

        // Populate table with office data
        filterHealthCheckList();
        for (HealthCheck check : healthCheckList) {
            tableModel.addHealthCheck(check);
        }

        // Add the scroll pane to the panel
        this.add(officePane, mainGbc);
        this.revalidate();
        this.repaint();
    }

    private void filterHealthCheckList() {
        String nameFilter = officeNameSearchField.getText().toUpperCase();
        String assignedTo = (String) assignedToSearchField.getSelectedItem();
        String status = (String) healthCheckStatusSearchField.getSelectedItem();
        boolean flagged = flaggedForReviewSearchField.isSelected();

        period = db.getHealthCheckPeriodList().get(periodSearchField.getSelectedIndex());
        healthCheckList = new ArrayList<>();
        for (HealthCheck i : period.getHealthCheckList()) {
            if (!satisfiesNameCheck(i, nameFilter)) {
                continue;
            }
            if (!satisfiesAssignedToCheck(i, assignedTo)) {
                continue;
            }
            if (!satisfiesStatusCheck(i, status)) {
                continue;
            }
            if (!satisfiesFlagCheck(i, flagged)) {
                continue;
            }
            healthCheckList.add(i);
        }
    }

    private boolean satisfiesNameCheck(HealthCheck check, String filter) {
        if (filter == null) {
            return true;
        }

        Office office = check.getOffice();
        if (office.getOfficeCode().contains(filter) || office.getOfficeName().toUpperCase().contains(filter)) {
            return true;
        }

        return false;
    }

    private boolean satisfiesAssignedToCheck(HealthCheck check, String assignedTo) {
        if (assignedTo.equals("All")) {
            return true;
        }
        return check.getAssignedTo().equals(assignedTo);
    }

    private boolean satisfiesStatusCheck(HealthCheck check, String status) {
        if (status.equals("All")) {
            return true;
        }
        String checkStatus = MyGlobalVariables.HEALTH_CHECK_STATUS_ARRAY[check.getHealthCheckStatus()];
        // TODO figure out how to avoid specifically calling "completed"
        if (status.equals("Incomplete") && !checkStatus.equals("Completed")) {
            return true;
        }
        return status.equals(checkStatus);
    }

    private boolean satisfiesFlagCheck(HealthCheck check, boolean flagged) {
        if (flagged == false) {
            return true;
        }
        return check.isFlagedForLeadershipReview();
    }


    private void loadHealthCheck(int selectedHealthCheck) {
        HealthCheck check = healthCheckList.get(selectedHealthCheck);
        currentHealthCheckIndex = selectedHealthCheck;
        parent.loadPanel(new HealthCheckPanel (parent, this, check));
    }

    public void loadNextHealthCheck() {
        int index = currentHealthCheckIndex + 1;
        if (index < 0 || index >= healthCheckList.size()) {
            return;
        }
        loadHealthCheck(index);
    }

    public void loadPreviousHealthCheck() {
        int index = currentHealthCheckIndex - 1;
        if (index < 0 || index >= healthCheckList.size()) {
            return;
        }
        loadHealthCheck(index);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand);

        if (actionCommand.equals("addNew")) {

        }
        if (actionCommand.equals("globalSet")) {

        }
        if (actionCommand.equals("search")) {
            buildOfficeListTable();
        }
        if (actionCommand.equals("reset")) {
            configureSearchCriteriaFields();
            buildOfficeListTable();
        }
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
        private final HealthCheckListPanel parentPanel; // Reference to the parent panel
        private int currentRow; // Store the row number

        public ButtonEditor(JCheckBox checkBox, HealthCheckListPanel parentPanel) {
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
            data.add(new Object[]{"Edit", office.getOfficeCode(), office.getOfficeName(), MyGlobalVariables.HEALTH_CHECK_STATUS_ARRAY[status], check.getAssignedTo()});
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }
}
