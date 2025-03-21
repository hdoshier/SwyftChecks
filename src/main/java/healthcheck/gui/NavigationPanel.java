package healthcheck.gui;

import healthcheck.data.MyGlobalVariables;
import healthcheck.gui.mainpanels.HomePanel;
import healthcheck.gui.mainpanels.healthchecks.HealthCheckListPanel;
import healthcheck.gui.mainpanels.offices.OfficeListPanel;
import healthcheck.gui.mainpanels.settings.SettingsHostPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *The NavigationPanel gui class.
 *
 *<p>This class creates the panel to manage the order.
 *
 *@author Hunter Doshier hunterdoshier@ksu.edu
 *
 *@version 0.1
 */
public class NavigationPanel extends JPanel {
    private MainWindow parent;
    private JLabel activeLabel;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public NavigationPanel(MainWindow parent) {
        this.parent = parent;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);

        // Create navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE); // Base color
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));

        // Add menu items
        addNavItem(navigationPanel, "Home", true);
        addNavItem(navigationPanel, "Offices", false);
        addNavItem(navigationPanel, "Health Checks", false);
        addNavItem(navigationPanel, "Reports", false);
        addNavItem(navigationPanel, "Settings", false);
    }

    private void addNavItem(JPanel panel, String name, boolean isActive) {
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setBackground(isActive ? MyGlobalVariables.SWYFTOPS_ORANGE : MyGlobalVariables.SWYFTOPS_BLUE);
        navItem.setForeground(Color.WHITE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 16));
        navItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        if (isActive) {
            activeLabel = navItem;
        }

        // Add hover effect
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.setBackground(MyGlobalVariables.SWYFTOPS_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (activeLabel != navItem) navItem.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                activeLabel.setBackground(MyGlobalVariables.SWYFTOPS_BLUE);
                activeLabel = navItem;
                activeLabel.setBackground(MyGlobalVariables.SWYFTOPS_ORANGE);
                System.out.println(name + " clicked");
                actionPerformed(name);

            }
        });

        panel.add(navItem);
        this.add(panel);
    }



    public void actionPerformed(String action) {
        if ("Home".equals(action)) {
            parent.loadPanel(new HomePanel(parent));
        }
        if ("Offices".equals(action)) {
            parent.loadPanel(new OfficeListPanel(parent));
        }
        if ("Health Checks".equals(action)) {
            parent.loadPanel(new HealthCheckListPanel(parent));
        }
        if ("Reports".equals(action)) {
            //parent.loadPanel(new MonthlyPanel(parent));
        }
        if ("Settings".equals(action)) {
            parent.loadPanel(new SettingsHostPanel(parent));
        }
    }
}