package edu.usrobotics.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 * Created by Max on 9/16/2016.
 */
public class LogFrame extends CustomFrame {

    private String logText = "";
    private JTextPane log;

    @Override public void build () {

        JButton close = new JButton("X");
        close.setAlignmentX(Component.RIGHT_ALIGNMENT);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setBorder(null);
        close.setMaximumSize(new Dimension(25, 25));
        close.setForeground(Color.lightGray);
        close.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                evt.getComponent().setForeground(Color.red);
            }

            public void mouseExited(MouseEvent evt) {
                evt.getComponent().setForeground(Color.lightGray);
            }

            public void mousePressed(MouseEvent evt) {
            }

            public void mouseReleased(MouseEvent evt) {
            }
        });
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        final ImageIcon inactiveOptions = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/settings_inactive.png")));
        final ImageIcon activeOptions = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/settings.png")));

        JButton options = new JButton();
        options.setIcon(inactiveOptions);
        options.setAlignmentX(Component.LEFT_ALIGNMENT);
        options.setContentAreaFilled(false);
        options.setBorderPainted(false);
        options.setFocusPainted(false);
        options.setBorder(null);
        options.setMaximumSize(new Dimension(25, 25));
        options.setForeground(Color.darkGray);
        options.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                ((AbstractButton) evt.getComponent()).setIcon(activeOptions);
            }

            public void mouseExited(MouseEvent evt) {
                ((AbstractButton) evt.getComponent()).setIcon(inactiveOptions);
            }

            public void mousePressed(MouseEvent evt) {
            }

            public void mouseReleased(MouseEvent evt) {
            }
        });
        options.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //System.exit(0);
            }
        });

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.LINE_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        menuPanel.add(options);
        menuPanel.add(Box.createRigidArea(new Dimension(160, 0)));
        menuPanel.add(close);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel topContentPanel = new JPanel();

        JLabel jobs = new JLabel("Log", SwingConstants.CENTER);
        jobs.setForeground(Color.lightGray);
        jobs.setFont(jobs.getFont().deriveFont(14.0f).deriveFont(1));
        jobs.setAlignmentX(Component.CENTER_ALIGNMENT);

        log = new JTextPane();
        log.setPreferredSize(new Dimension(200, 340));
        log.setAlignmentX(Component.CENTER_ALIGNMENT);
        log.setBackground(Color.lightGray);
        log.setFont(log.getFont().deriveFont(12.0f).deriveFont(1));
        log.setEditable(false);

        JScrollPane list = new JScrollPane(log);
        list.setPreferredSize(new Dimension(200, 340));
        list.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.setBorder(null);

        topContentPanel.add(jobs);
        contentPanel.add(list);

        Container contentPane = getContentPane();
        contentPane.add(menuPanel, BorderLayout.PAGE_START);
        contentPane.add(topContentPanel, BorderLayout.CENTER);
        contentPane.add(contentPanel, BorderLayout.PAGE_END);
    }

    public void log (String msg) {
        logText+=msg+"\n";
        log.setText(logText);
    }
}
