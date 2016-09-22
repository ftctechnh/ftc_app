package edu.usrobotics.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Created by Max on 9/16/2016.
 */
public class TelemetryFrame extends CustomFrame {

    private JTable table;
    private String[] columnNames;
    private Object[][] data;

    @Override public void build () {
        setTitle("Telemetry");
        setResizable(true);
        setSize(new Dimension(400, 340));
        setLocation(TelemetryClient.logFrame.getLocation().x + 400, TelemetryClient.logFrame.getLocation().y);
        setType(javax.swing.JFrame.Type.UTILITY);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.darkGray);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        columnNames = new String[]{
                "Device",
                "Id",
                "Data",
        };

        data = new Object[16][3];

        table = new JTable(data, columnNames);
        table.setBackground(higherlightColor);
        table.setForeground(Color.darkGray);

        JTextField tf = new JTextField();
        tf.setEditable(false);
        DefaultCellEditor editor = new DefaultCellEditor( tf );
        table.setDefaultEditor(Object.class, editor);

        JScrollPane list = new JScrollPane(table);
        list.setAlignmentX(Component.CENTER_ALIGNMENT);
        list.setBorder(null);
        list.getViewport().setBackground(Color.lightGray);

        contentPanel.add(list);


        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        contentPane.add(new JWindowTag("Telemetry"));
        contentPane.add(contentPanel);
    }

    public void updateData (String name, int id, String dat) {
        if (name != null || table.getValueAt(id, 0) == null) {
            table.setValueAt(name != null ? name : "???", id, 0);
        }
        table.setValueAt(id, id, 1);
        table.setValueAt(dat, id, 2);
    }

}
