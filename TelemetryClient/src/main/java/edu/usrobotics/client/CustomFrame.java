package edu.usrobotics.client;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

/**
 * Created by Max on 9/11/2016.
 */
public class CustomFrame extends javax.swing.JFrame {

    private int framePosX = 0;
    private int framePosY = 0;


    public CustomFrame() {
        setResizable(true);
        setSize(new Dimension(200, 450));
        setTitle("Log");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                framePosX = e.getX();
                framePosY = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent evt) {
                setLocation(evt.getXOnScreen() - framePosX,
                        evt.getYOnScreen() - framePosY);
            }
        });

        build();

        setVisible(true);
    }

    void build () {

    }
}
