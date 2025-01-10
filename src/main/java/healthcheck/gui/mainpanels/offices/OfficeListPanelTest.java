package healthcheck.gui.mainpanels.offices;

import healthcheck.data.Office;
import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.gui.MainWindow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
public class OfficeListPanelTest extends JPanel implements ActionListener {
    MainWindow parent;
    GridBagConstraints mainGbc;
    JPanel searchPanel;
    JPanel managePanel;
    JScrollPane officePane;
    JTable officeTable;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public OfficeListPanelTest(MainWindow parent) {
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

    private void openOfficeInfoPanel(String selectedOffice) {
        parent.loadPanel(new OfficePanel(parent, ReadData.readOffice(selectedOffice)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        System.out.println(Arrays.toString(officeTable.getSelectedRows()));
    }

    public void buildOfficeListTable() {
        // Create table with a custom model
        OfficeTableModel tableModel = new OfficeTableModel();
        officeTable = new JTable(tableModel);



        // Customize button column
        officeTable.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        officeTable.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // Wrap table in a scroll pane
        officePane = new JScrollPane(officeTable);

        // Populate table with office data
        ArrayList<Office> allOffices = Database.getInstance().getAllOfficesList();
        for (Office office : allOffices) {
            tableModel.addOffice(office);
        }

        // Add the scroll pane to the panel
        this.add(officePane, mainGbc);
        this.revalidate();
        this.repaint();
    }


    // Button Renderer Class
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "Edit");
            return this;
        }
    }

    // Button Editor Class
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String officeCode;
        private boolean isPushed;
        private OfficeListPanelTest parentPanel; // Reference to the parent panel

        public ButtonEditor(JCheckBox checkBox, OfficeListPanelTest parentPanel) {
            super(checkBox);
            this.parentPanel = parentPanel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            officeCode = table.getValueAt(row, 1).toString(); // Get officeCode from the 3rd column (index 2)
            button.setText(value != null ? value.toString() : "Edit");
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Call the method with the selected officeCode
                parentPanel.openOfficeInfoPanel(officeCode);
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
            return column == 0; // Button columns are editable
        }

        public void addOffice(Office office) {
            data.add(new Object[]{"Edit", office.getOfficeCode(), office.getOfficeName(), office.getOfficePrimaryContactPerson(), office.getOfficePrimaryContactEmail()});
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }
}
