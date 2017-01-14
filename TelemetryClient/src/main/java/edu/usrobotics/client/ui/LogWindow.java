package edu.usrobotics.client.ui;

import edu.usrobotics.client.CustomFrame;
import edu.usrobotics.client.JWindowTag;
import edu.usrobotics.client.TelemetryClient;
import edu.usrobotics.util.NumberFixedLengthFifoQueue;

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
import java.util.Queue;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;

/**
 * Created by Max on 9/16/2016.
 */
public class LogWindow extends CustomFrame {

    public static Style LOG_STYLE;
    public static Style WARN_STYLE;
    public static Style ERROR_STYLE;

    private StyledDocument logDocument;
    private JTextPane log;

    @Override public void build () {
        setTitle("Logging");
        setResizable(true);
        setSize(new Dimension(400, 340));
        setLocation(TelemetryClient.startupWindow.getLocation().x - 400, TelemetryClient.startupWindow.getLocation().y);
        setType(javax.swing.JFrame.Type.UTILITY);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.darkGray);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        log = new JTextPane();
        log.setEditable(false);
        log.setBackground(Color.BLACK);

        JScrollPane list = new JScrollPane(log);
        list.setAlignmentX(Component.CENTER_ALIGNMENT);
        list.setBorder(null);
        list.getViewport().setBackground(Color.lightGray);
        list.setMaximumSize(new Dimension(400, 340));

        contentPanel.add(list);


        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        contentPane.add(new JWindowTag("Logging", 32));
        contentPane.add(contentPanel);

        logDocument = log.getStyledDocument();
        Style defaultStyle = logDocument.getStyle(StyleContext.DEFAULT_STYLE);

        LOG_STYLE = logDocument.addStyle("log", defaultStyle);
        StyleConstants.setLeftIndent(LOG_STYLE, 16);
        StyleConstants.setRightIndent(LOG_STYLE, 16);
        StyleConstants.setFirstLineIndent(LOG_STYLE, 16);
        StyleConstants.setFontFamily(LOG_STYLE, "serif");
        StyleConstants.setFontSize(LOG_STYLE, 14);
        StyleConstants.setForeground(LOG_STYLE, Color.WHITE);

        WARN_STYLE = logDocument.addStyle("warn", LOG_STYLE);
        StyleConstants.setBold(WARN_STYLE, true);
        StyleConstants.setForeground(WARN_STYLE, Color.YELLOW);

        ERROR_STYLE = logDocument.addStyle("error", WARN_STYLE);
        StyleConstants.setForeground(WARN_STYLE, Color.RED);
    }

    public void log (String msg, Style style) {
        try {
            logDocument.insertString(logDocument.getLength(), msg + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void log (String msg) {
        log (msg, LOG_STYLE);
    }
}
