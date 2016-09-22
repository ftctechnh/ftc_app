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
    public final int mapSize = 256;

    @Override
    public void build() {
        setSize(new Dimension(mapSize + 60, mapSize + 60));
        setLocation(TelemetryClient.logFrame.getLocation().x - 400, TelemetryClient.logFrame.getLocation().y);
        setType(javax.swing.JFrame.Type.UTILITY);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));

        component = new ImageComponent("/field.png", mapSize, TelemetryClient.FTCFieldWidth_mm);
        add(component);
    }

    public void updateTrackable(int id, float x, float y, float z, float rx, float ry, float rz) {
        component.updateTrackable(id, x, y, z, rx, ry, rz);
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
    private int mapSize;


    public ImageComponent(String img, int mapSize, float fieldSize_mm) {
        image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(img))).getImage();
        this.mapSize = mapSize;
        this.fieldSize_mm = fieldSize_mm;

        trailImage = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
        trailGraphics = trailImage.createGraphics();
        trailGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        trailGraphics.setStroke(new BasicStroke(1));
    }

    public void updateTrackable(int id, float x, float y, float z, float rx, float ry, float rz) {
        if (trackables[id] != null) {
            Trackable t = trackables[id];
            t.lastTransform.set(t.transform.x, t.transform.y, t.transform.z, t.transform.rx, t.transform.ry, t.transform.rz);
            t.transform.set(x, y, z, rx, ry, rz);

        } else {
            Trackable t = new Trackable();

            t.trailColor = Color.YELLOW;
            t.transform = new Transform(x, y, z, rx, ry, rz);
            t.lastTransform = new Transform(t.transform.x, t.transform.y, t.transform.z, t.transform.rx, t.transform.ry, t.transform.rz);

            trackables[id] = t;
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        if (image == null) return;

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(image, 0, 0, mapSize, mapSize, this);

        g2.drawImage(trailImage, 0, 0, mapSize, mapSize, this);

        g2.setStroke(new BasicStroke(4));

        // Blue Station
        g2.setColor(new Color(0, 0, 1, 0.5f));
        g2.drawRect(mapSize + 8, 8, 30, mapSize - 16);

        // Red Station
        g2.setColor(new Color(1, 0, 0, 0.5f));
        g2.drawRect(8, mapSize + 8, mapSize - 16, 30);

        for (int i = 0; i < trackables.length; i++) {
            if (trackables[i] != null) {
                Transform v = trackables[i].transform;
                Transform lastV = trackables[i].lastTransform;

                String name = i < idLocalizations.length ? idLocalizations[i] : Integer.toString(i);

                int posX = (int) (v.x / fieldSize_mm * (float)mapSize);
                int posY = (int) ((1 - v.z / fieldSize_mm) * (float)mapSize);

                int lastPosX = (int) (lastV.x / fieldSize_mm * (float)mapSize);
                int lastPosY = (int) ((1 - lastV.z / fieldSize_mm) * (float)mapSize);

                g2.setFont(new Font("Garamond", Font.BOLD, 12));
                FontMetrics metrics = g2.getFontMetrics();

                int labelSizeX = metrics.stringWidth(name) + 6;
                int labelSizeY = metrics.getHeight();

                // Trail
                trackables[i].trailColor = trackables[i].trailColor.equals(Color.YELLOW) ? Color.BLACK : Color.YELLOW;
                trailGraphics.setColor(trackables[i].trailColor);
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
