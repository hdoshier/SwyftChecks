package healthcheck.gui.mainpanels.offices;

import healthcheck.data.Email;
import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
        searchButton.setToolTipText("(Enter)");
        searchButton.setActionCommand("search");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);

        // Add ENTER key binding
        searchPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "searchAction"
        );
        searchPanel.getActionMap().put("searchAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButton.doClick();
            }
        });

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

        JButton globalSetButton = new JButton("Global Set");
        globalSetButton.setActionCommand("globalSet");
        globalSetButton.addActionListener(this);
        managePanel.add(globalSetButton);

        JButton emailButton = new JButton("Email");
        emailButton.setActionCommand("email");
        emailButton.addActionListener(this);
        managePanel.add(emailButton);

        JButton callButton = new JButton("Call");
        callButton.setActionCommand("call");
        callButton.addActionListener(this);
        managePanel.add(callButton);

        this.add(managePanel, mainGbc);
    }

    private void filterOfficeList() {
        String nameFilter = nameSearchField.getText().toUpperCase();
        int activeStatus =  statusSearch.getSelectedIndex();


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
            JOptionPane.showMessageDialog(this, "No next office available.", "End of List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        loadOffice(index);
    }

    public void loadPreviousOffice() {
        int index = officeIndex - 1;
        if (index < 0 || index >= officeList.size()) {
            JOptionPane.showMessageDialog(this, "No previous office available.", "Start of List", JOptionPane.INFORMATION_MESSAGE);
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
        if (actionCommand.equals("call")) {
            int row = officeTable.getSelectedRow();
            if (row < 0) {
                return;
            }
            Office office = officeList.get(row);
            Email.prepCall(office.getOfficePrimaryContactPhone());
        }
        if (actionCommand.equals("email")) {
            int row = officeTable.getSelectedRow();
            if (row < 0) {
                return;
            }
            Office office = officeList.get(row);
            String subject = "SwyftOps | " + office.getOfficeCode() + " | ";
            Email.prepEmail(office.getOfficePrimaryContactPerson(), office.getOfficePrimaryContactEmail(), subject, "");
        }
        System.out.println(Arrays.toString(officeTable.getSelectedRows()));
    }

    public void buildOfficeListTable() {
        if (officeTable == null) {
            OfficeTableModel tableModel = new OfficeTableModel();
            officeTable = new JTable(tableModel);
            officeTable.setRowHeight(25);

            TableColumn buttonColumn = officeTable.getColumnModel().getColumn(0);
            buttonColumn.setCellRenderer(new ButtonRenderer());
            buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), this));
            buttonColumn.setPreferredWidth(50);

            officePane = new JScrollPane(officeTable);
            add(officePane, mainGbc);
        }

        OfficeTableModel tableModel = (OfficeTableModel) officeTable.getModel();
        tableModel.clear();
        filterOfficeList();
        for (Office office : officeList) {
            tableModel.addOffice(office);
        }
        officeTable.revalidate();
        officeTable.repaint();
    }

    private Office getFirstSelectedOffice() {
        int selectedRow = officeTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < officeList.size()) {
            return officeList.get(selectedRow);
        }
        return null;
    }

    private Office getOfficeAt(int row) {
        if (row >= 0 && row < officeList.size()) {
            return officeList.get(row);
        }
        return null;
    }

    // Utility Methods
    private JPanel createLabeledField(String labelText, JComponent component, int alignment) {
        JPanel panel = new JPanel(new FlowLayout(alignment));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    private JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        return button;
    }

    // Inner Classes for Table Configuration
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "Edit");
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

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private boolean isPushed;
        private final OfficeListPanel parentPanel;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, OfficeListPanel parentPanel) {
            super(checkBox);
            this.parentPanel = parentPanel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            button.setText(value != null ? value.toString() : "Edit");
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                parentPanel.loadOffice(currentRow);
            }
            isPushed = false;
            return button.getText();
        }
    }

    static class OfficeTableModel extends AbstractTableModel {
        private static final String[] COLUMN_NAMES = {"", "Office Code", "Office Name", "Contact Name", "Contact Email"};
        private final ArrayList<Office> offices = new ArrayList<>();

        @Override
        public int getRowCount() {
            return offices.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Office office = offices.get(row);
            return switch (column) {
                case 0 -> "Edit";
                case 1 -> office.getOfficeCode();
                case 2 -> office.getOfficeName();
                case 3 -> office.getOfficePrimaryContactPerson();
                case 4 -> office.getOfficePrimaryContactEmail();
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0;
        }

        public void addOffice(Office office) {
            offices.add(office);
            fireTableRowsInserted(offices.size() - 1, offices.size() - 1);
        }

        public void clear() {
            int size = offices.size();
            offices.clear();
            if (size > 0) {
                fireTableRowsDeleted(0, size - 1);
            }
        }

        public Office getOfficeAt(int row) {
            return offices.get(row);
        }
    }
}
