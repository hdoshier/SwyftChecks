package healthcheck.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class NavigationPanel extends JPanel implements ActionListener {
    private MainWindow parent;
    JButton monthlyButton;
    JButton weeklyButton;
    JButton expenseButton;
    JButton incomeButton;
    JPanel activePanel;

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public NavigationPanel(MainWindow parent) {
        this.parent = parent;
        this.setPreferredSize(new Dimension(250, 600));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(new Color(0, 120, 150));

        // Create navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(0, 120, 150)); // Base color
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));

        // Add menu items
        addNavItem(navigationPanel, "Home", false);
        addNavItem(navigationPanel, "Offices", true);
        addNavItem(navigationPanel, "Health Checks", false);
        addNavItem(navigationPanel, "Reports", false);
        addNavItem(navigationPanel, "Settings", false);



    }

    private void addNavItem(JPanel panel, String name, boolean isActive){
        JLabel navItem = new JLabel(name);
        navItem.setOpaque(true);
        navItem.setBackground(isActive ? new Color(255, 140, 0) : new Color(0, 120, 150)); // Highlight active
        navItem.setForeground(Color.WHITE);
        navItem.setFont(new Font("Arial", Font.PLAIN, 16));
        navItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // Padding
        //navItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        navItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add hover effect
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.setBackground(new Color(255, 140, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) navItem.setBackground(new Color(0, 120, 150));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Change the active item
                if (activePanel != null) {
                    activePanel.setBackground(new Color(0, 120, 150)); // Reset previous active
                }
                activePanel = panel; // Update active item
                navItem.setBackground(new Color(255, 140, 0)); // Highlight active item

                // Perform action (switch view, print message, etc.)
                System.out.println(name + " clicked");
            }
        });

        panel.add(navItem);
        this.add(panel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand().toString());
        if ("home".equals(e.getActionCommand())) {
            //parent.loadPanel(new MonthlyPanel(parent));
        }
        if ("offices".equals(e.getActionCommand())) {
            //parent.loadPanel(new OfficePanel(parent));
        }
        if ("hc".equals(e.getActionCommand())) {
            //parent.loadPanel(new ExpensePanel(parent));
        }
        if ("reports".equals(e.getActionCommand())) {
            //parent.loadPanel(new IncomePanel(parent));
        }
        if ("settings".equals(e.getActionCommand())) {
            //parent.loadPanel(new SettingsPanel(parent));
        }
    }
}