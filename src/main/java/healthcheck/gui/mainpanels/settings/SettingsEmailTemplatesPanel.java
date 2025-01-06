package healthcheck.gui.mainpanels.settings;

import healthcheck.data.MySettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SettingsEmailTemplatesPanel extends JPanel implements ActionListener {
    private SettingsHostPanel parent;
    private MySettings settings;
    private JTextField templateName;
    private JTextArea templateContent;
    private JComboBox<String> templateSelector;

    public SettingsEmailTemplatesPanel(SettingsHostPanel parent, MySettings settings) {
        this.parent = parent;
        this.settings = settings;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(createManagePanel(), gbc);
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        this.add(createContentPanel(), gbc);

    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        templateContent = new JTextArea();
        templateContent.setLineWrap(true);
        panel.add(templateContent, gbc);

        return panel;
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.insets = new Insets(2, 2, 2, 2);
        JButton addNewButton = new JButton("Add New");
        addNewButton.setActionCommand("new");
        addNewButton.addActionListener(this);
        panel.add(addNewButton, gbc);

        gbc.gridx = 1;
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);
        panel.add(deleteButton, gbc);

        templateName = new JTextField();
        gbc.gridx = 2;
        templateSelector = new JComboBox<>(settings.getEmailTemplateNames().toArray(new String[0]));
        templateSelector.setSelectedIndex(-1);
        templateSelector.addActionListener(e -> {
            String template = (String) templateSelector.getSelectedItem();
            if (templateName != null && template != null) {
                templateName.setText(template);
                templateContent.setText(settings.getTemplate(template));
                this.revalidate();
                this.repaint();
            }
        });
        panel.add(templateSelector, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0; // Stretch horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch only in X direction

        panel.add(templateName, gbc);

        // Add "Save" button
        gbc.gridx = 4;
        gbc.weightx = 0; // Do not stretch horizontally
        gbc.fill = GridBagConstraints.NONE; // Normal size
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        panel.add(saveButton, gbc);

        return panel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("save")) {
            String name = templateName.getText();
            if (name.equals("")|| name == null) {
                return;
            }
            settings.putEmailTemplate(name, templateContent.getText());
            templateSelector.addItem(name);
            templateSelector.setSelectedItem(name);
        }
        if (actionCommand.equals("delete")) {
            settings.removeEmailTemplate(templateName.getText());
            int index = templateSelector.getSelectedIndex();
            templateSelector.setSelectedIndex(-1);
            templateSelector.remove(index);
            templateName.setText("");
            templateContent.setText("");
        }
        if (actionCommand.equals("new")) {
            templateSelector.setSelectedIndex(-1);
            templateName.setText("New Email Template Name");
            templateContent.setText("");
        }
        this.revalidate();
        this.repaint();
    }
}
