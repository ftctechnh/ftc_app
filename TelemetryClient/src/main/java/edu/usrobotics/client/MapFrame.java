package edu.usrobotics.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Created by Max on 9/16/2016.
 */
public class MapFrame extends CustomFrame {

    public ImageComponent component;

    @Override
    public void build() {
        setSize(new Dimension(256 + 60, 256 + 60));
        setLocation(TelemetryClient.logFrame.getLocation().x - 400, TelemetryClient.logFrame.getLocation().y);
        setType(javax.swing.JFrame.Type.UTILITY);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));

        component = new ImageComponent("/field.png", TelemetryClient.FTCFieldWidth_mm);
        add(component);
    }

    public void updateTrackable(int id, float x, float z, float r) {
        component.updateTrackable(id, x, z, r);
    }
}

class ImageComponent extends JComponent {
    private static final long serialVersionUID = 1L;
    private Image image;
    private Trackable[] trackables = new Trackable[16];
    private float fieldSize_mm;
    private String[] idLocalizations = new String[]{"R", "T1", "T2"};
    private BufferedImage trailImage;
    private Graphics2D trailGraphics;


    public ImageComponent(String img, float fieldSize_mm) {
        image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(img))).getImage();
        this.fieldSize_mm = fieldSize_mm;

        trailImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        trailGraphics = trailImage.createGraphics();
        trailGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        trailGraphics.setStroke(new BasicStroke(1));
    }

    public void updateTrackable(int id, float x, float z, float r) {
        if (trackables[id] != null) {
            Trackable t = trackables[id];
            t.lastTransform.set(t.transform.x, 0, t.transform.z);
            t.lastTransform.ry = t.transform.y;
            t.transform.set(x, 0, z);
            t.transform.ry = r;

        } else {
            trackables[id] = new Trackable();

            Trackable t = trackables[id];
            t.transform = new Transform(x, 0, z);
            t.transform.ry = r;
            t.lastTransform = new Transform(t.transform.x, 0, t.transform.z);
            t.lastTransform.ry = t.transform.y;

        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        if (image == null) return;

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(image, 0, 0, 256, 256, this);

        g2.drawImage(trailImage, 0, 0, 256, 256, this);

        g2.setStroke(new BasicStroke(4));

        // Blue Station
        g2.setColor(Color.BLUE);
        g2.drawRect(256 + 8, 8, 30, 256 - 16);

        // Red Station
        g2.setColor(Color.RED);
        g2.drawRect(8, 256 + 8, 256 - 16, 30);

        for (int i = 0; i < trackables.length; i++) {
            if (trackables[i] != null) {
                Transform v = trackables[i].transform;
                Transform lastV = trackables[i].lastTransform;

                String name = i < idLocalizations.length ? idLocalizations[i] : Integer.toString(i);

                int posX = (int) (v.x / fieldSize_mm * 256f);
                int posY = (int) ((1 - v.z / fieldSize_mm) * 256f);

                int lastPosX = (int) (lastV.x / fieldSize_mm * 256f);
                int lastPosY = (int) ((1 - lastV.z / fieldSize_mm) * 256f);

                g2.setFont(new Font("Garamond", Font.BOLD, 12));
                FontMetrics metrics = g2.getFontMetrics();

                int labelSizeX = metrics.stringWidth(name) + 6;
                int labelSizeY = metrics.getHeight();

                // Trail
                trailGraphics.setColor(Color.YELLOW);
                trailGraphics.drawLine(lastPosX, lastPosY, posX, posY);

                // Rotation Dot
                int xOffset = (int) (Math.cos(Math.toRadians(v.ry)) * (float) (labelSizeX));
                int yOffset = (int) (Math.sin(Math.toRadians(v.ry)) * (float) (labelSizeY));

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(posX, posY, posX + xOffset, posY + yOffset);

                // ID Label
                g2.setColor(Color.BLACK);
                g2.fillRect(posX - labelSizeX / 2, posY - labelSizeY / 2, labelSizeX, labelSizeY);
                g2.setColor(Color.WHITE);
                g2.drawString(name, posX - metrics.stringWidth(name)/2, posY + metrics.getHeight()/2 - 3);

                // Coordinate Label
                posX += 12 + name.length() * 6 + 6;
                String coords = "(" + (int) v.x + "mm, " + (int) v.z + "mm)";
                g2.setFont(new Font("Garamond", Font.PLAIN, 10));
                g2.setColor(new Color(1f, 1f, 1f, 0.5f));
                g2.fillRect(posX - 5, posY - 6 - 6, 5 + coords.length() * 6 + 5, 12 + 4);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(coords, posX, posY);
            }
        }

        g2.dispose();
    }

}
