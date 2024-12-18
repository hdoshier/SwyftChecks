package healthcheck.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    /**
     *Constructs the panel.
     *
     *@param parent is the parent window.
     *
     */
    public NavigationPanel(MainWindow parent) {
        this.parent = parent;
        this.setPreferredSize(new Dimension(250, 600));
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(Color.black));


        GridBagConstraints mainGbc = new GridBagConstraints();

        mainGbc.weightx = 1.0;
        mainGbc.insets = new Insets(2, 2, 2, 2);

        //nav lavel
        JLabel navLabel = new JLabel("Navigation Menu");
        mainGbc.anchor = GridBagConstraints.PAGE_START;
        this.add(navLabel, mainGbc);

        //monthly button
        mainGbc.gridy = 1;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.anchor = GridBagConstraints.CENTER;
        this.monthlyButton = new JButton("Home");
        monthlyButton.setActionCommand("home");
        monthlyButton.addActionListener(this);
        this.add(monthlyButton, mainGbc);

        //weekly button
        mainGbc.gridy = 2;
        this.weeklyButton = new JButton("Offices");
        weeklyButton.setActionCommand("offices");
        weeklyButton.addActionListener(this);
        this.add(weeklyButton, mainGbc);

        //expense button
        mainGbc.gridy = 3;
        this.expenseButton = new JButton("Health Checks");
        expenseButton.setActionCommand("hc");
        expenseButton.addActionListener(this);
        this.add(expenseButton, mainGbc);

        //income button
        mainGbc.gridy = 4;
        this.incomeButton = new JButton("Reports");
        incomeButton.setActionCommand("reports");
        incomeButton.addActionListener(this);
        this.add(incomeButton, mainGbc);
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
    }
}