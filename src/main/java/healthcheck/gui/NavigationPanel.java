package healthcheck.gui;

import healthcheck.gui.mainpanels.healthchecks.HealthCheckHostPanel;
import healthcheck.gui.mainpanels.OfficeListPanel;
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
        this.setBackground(new Color(0, 122, 178));

        // Create navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(0, 122, 178)); // Base color
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));

        // Add menu items
        addNavItem(navigationPanel, "Home", false);
        addNavItem(navigationPanel, "Offices", true);
        addNavItem(navigationPanel, "Health Checks", false);
        addNavItem(navigationPanel, "Reports", false);
        addNavItem(navigationPanel, "Settings", false);



    }

    private void addNavItem(JPanel panel, String name, boolean isActive) {
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setBackground(isActive ? new Color(255, 151, 25) : new Color(0, 122, 178));
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
                navItem.setBackground(new Color(255, 151, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (activeLabel != navItem) navItem.setBackground(new Color(0, 122, 178));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (activeLabel != navItem) {
                    activeLabel.setBackground(new Color(0, 122, 178));
                    activeLabel = navItem;
                    activeLabel.setBackground(new Color(255, 151, 25));
                    System.out.println(name + " clicked");
                    actionPerformed(name);
                }
            }
        });

        panel.add(navItem);
        this.add(panel);
    }



    public void actionPerformed(String action) {
        if ("Home".equals(action)) {
            //parent.loadPanel(new MonthlyPanel(parent));
        }
        if ("Offices".equals(action)) {
            parent.loadPanel(new OfficeListPanel(parent));
        }
        if ("Health Checks".equals(action)) {
            parent.loadPanel(new HealthCheckHostPanel(parent));
        }
        if ("Reports".equals(action)) {
            //parent.loadPanel(new MonthlyPanel(parent));
        }
        if ("Settings".equals(action)) {
            parent.loadPanel(new SettingsHostPanel(parent));
        }
    }
}