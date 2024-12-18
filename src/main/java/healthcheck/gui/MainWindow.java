package healthcheck.gui;

import healthcheck.gui.mainpanels.OfficePanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

    /**
     *Constructs the window.
     */
    public MainWindow() {
        this.setPreferredSize(new Dimension(1100, 600));
        this.setTitle("SwyftChecks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.mainPanel = null;
        this.loadPanel(new OfficePanel(this));
        this.loadNavPanel();
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
        GridBagConstraints mainPanelgbc = new GridBagConstraints();
        mainPanelgbc.gridx = 1;
        mainPanelgbc.gridy = 0;
        mainPanelgbc.weightx = 1.0;
        mainPanelgbc.weighty = 1.0;
        mainPanelgbc.fill = GridBagConstraints.BOTH;
        mainPanelgbc.insets = new Insets(10, 10, 10, 10);
        this.add(this.mainPanel, mainPanelgbc);
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
        GridBagConstraints navGbc = new GridBagConstraints();
        navGbc.gridx = 0;
        navGbc.gridy = 0;
        navGbc.weightx = 0.1;
        navGbc.weighty = 1.0;
        navGbc.fill = GridBagConstraints.BOTH;
        navGbc.insets = new Insets(10, 10, 10, 10);
        this.add(this.nav, navGbc);
        this.pack();
        this.repaint();
    }
}
