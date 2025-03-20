package healthcheck.gui;

import healthcheck.gui.mainpanels.HomePanel;
import healthcheck.gui.mainpanels.offices.OfficeListPanel;

import java.awt.*;
import javax.swing.*;

/**
 *The PrimaryPanel gui class.
 *
 *<p>This class creates the budget window.
 *
 *@author Hunter Doshier hunterdoshier@ksu.edu
 *
 *@version 0.1
 */
public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private NavigationPanel nav;
    private GridBagConstraints gbc;

    /**
     *Constructs the window.
     */
    public MainWindow() {
        //this.setPreferredSize(new Dimension(1100, 600));
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setPreferredSize(new Dimension(xSize, ySize-50));
        this.setTitle("SwyftChecks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.mainPanel = null;
        gbc = new GridBagConstraints();

        gbc.weighty = 1.0;
        gbc.weightx = 0.15;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        this.loadNavPanel();

        gbc.weightx = 0.85;
        gbc.gridx = 1;
        this.loadPanel(new HomePanel(this));

    }

    /**
     *Loads the panel into the window.
     *
     *@param panel is the panel needing to be loaded.
     *
     */
    public void loadPanel(JPanel panel) {
        if (this.mainPanel != null) {
            this.remove(this.mainPanel);
        }
        this.mainPanel = panel;
        //mainPanel.setPreferredSize(new Dimension(850, 600));
        this.add(this.mainPanel, gbc);
        this.pack();
        this.repaint();
    }

    /**
     *Loads the left hand navigation panel.
     */
    public void loadNavPanel() {
        if (this.nav != null) {
            this.remove(this.nav);
        }
        this.nav = new NavigationPanel(this);
        //nav.setPreferredSize(new Dimension(250, 600));
        this.add(this.nav, gbc);
        this.pack();
        this.repaint();
    }
}
