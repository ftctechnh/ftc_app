package edu.usrobotics.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Created by Max on 9/20/2016.
 */
public class JWindowTag extends JComponent {

    String title;
    int height = 80;

    public JWindowTag (String title) {
        this.title = title;

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setMinimumSize(new Dimension(0, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(new Font("Garamond", Font.BOLD, 14));
        FontMetrics fontMetrics = g.getFontMetrics();

        g.setColor(CustomFrame.highlightColor);
        g.fillRect(0, 0, fontMetrics.stringWidth(title) + 10, g.getClipBounds().height);
        g.fillRect(0, g.getClipBounds().height - 5, g.getClipBounds().width, 5);
        g.setColor(new Color(0.9f, 0.9f, 0.9f));
        g.drawString(title, 5, 18);

        g.dispose();
    }
}
