package healthcheck.gui.mainpanels;

import healthcheck.data.HealthCheck;
import healthcheck.data.HealthCheckPeriod;
import healthcheck.data.MyGlobalVariables;
import healthcheck.data.firestore.Database;
import healthcheck.gui.MainWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

public class HomePanel extends JPanel {
    private MainWindow parent;
    private ChartPanel chartPanel;
    private JTable userTable;
    private JProgressBar progressBar;

    public HomePanel(MainWindow parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(MyGlobalVariables.SWYFTOPS_BLUE);

        // Get the current period
        HealthCheckPeriod currentPeriod = Database.getInstance().getHealthCheckPeriodList().getFirst();

        // North: Title
        JLabel titleLabel = new JLabel("SwyftChecks Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // Center: Pie Chart and Table
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setOpaque(false);

        // Pie Chart for Status
        JFreeChart pieChart = createPieChart(); // Initial empty chart
        chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(chartPanel);

        // Table for User Breakdown
        userTable = createUserTable(null); // Initial empty table
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(tableScrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // South: Progress Bar and Days Left
        JPanel southPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        southPanel.setOpaque(false);

        // Progress Bar
        progressBar = new JProgressBar(0, 150); // Will be updated by populateRealData
        progressBar.setStringPainted(true);
        progressBar.setForeground(MyGlobalVariables.SWYFTOPS_ORANGE);
        southPanel.add(progressBar);

        // Days Left
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), currentPeriod.getEndDate());
        JLabel daysLabel = new JLabel("Days Left in Period: " + daysLeft, SwingConstants.CENTER);
        daysLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        daysLabel.setForeground(Color.WHITE);
        southPanel.add(daysLabel);

        add(southPanel, BorderLayout.SOUTH);

        // Populate with real data
        populateRealData(currentPeriod);
    }

    private JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        // Initially empty; populated by populateRealData
        return ChartFactory.createPieChart(
                "Health Check Period Status",
                dataset,
                true, // legend
                true, // tooltips
                false // urls
        );
    }

    private JTable createUserTable(List<HealthCheck> healthChecks) {
        String[] columnNames = {"User", "Assigned", "Completed"};
        Object[][] data = new Object[0][3]; // Empty initially
        JTable table = new JTable(data, columnNames);

        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(MyGlobalVariables.SWYFTOPS_ORANGE);
        table.getTableHeader().setForeground(Color.WHITE);
        return table;
    }

    private void populateRealData(HealthCheckPeriod period) {
        List<HealthCheck> checks = period.getHealthCheckList();
        int[] statusCounts = new int[4]; // 0: Unassigned, 1: Assigned, 2: Reviewed, 3: Completed
        HashMap<String, int[]> userStats = new HashMap<>(); // User -> [assigned, completed]

        // Calculate stats
        for (HealthCheck check : checks) {
            statusCounts[check.getHealthCheckStatus()]++;
            String user = check.getAssignedTo();
            if (!user.equals("Unassigned")) {
                userStats.computeIfAbsent(user, k -> new int[2])[0]++; // Assigned
                if (check.getHealthCheckStatus() == 3) {
                    userStats.get(user)[1]++; // Completed
                }
            }
        }

        // Update pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Unassigned", statusCounts[0]);
        dataset.setValue("Assigned", statusCounts[1]);
        dataset.setValue("Reviewed", statusCounts[2]);
        dataset.setValue("Completed", statusCounts[3]);
        chartPanel.getChart().setTitle("Health Check Status (" + checks.size() + " Total)");
        PiePlot piePlot = (PiePlot) chartPanel.getChart().getPlot();
        piePlot.setDataset(dataset);
        piePlot.setSectionPaint("Unassigned", Color.RED);
        piePlot.setSectionPaint("Assigned", Color.YELLOW);
        piePlot.setSectionPaint("Reviewed", Color.BLUE);
        piePlot.setSectionPaint("Completed", Color.GREEN);

        // Update table
        Object[][] tableData = new Object[userStats.size()][3];
        int i = 0;
        for (String user : userStats.keySet()) {
            tableData[i] = new Object[]{user, userStats.get(user)[0], userStats.get(user)[1]};
            i++;
        }
        userTable.setModel(new javax.swing.table.DefaultTableModel(tableData, new String[]{"User", "Assigned", "Completed"}));

        // Update progress bar
        int total = checks.size();
        int completed = statusCounts[3];
        progressBar.setMaximum(total);
        progressBar.setValue(completed);
        progressBar.setString(String.format("%.1f%% Complete (%d/%d)", (completed * 100.0 / total), completed, total));
    }
}