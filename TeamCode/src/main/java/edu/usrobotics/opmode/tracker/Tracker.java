package edu.usrobotics.opmode.tracker;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

/**
 * Created by Max on 9/14/2016.
 */
public interface Tracker {

    float getReliability(); // float [0, 1], 1 = reliable, 0 = unreliable

    boolean track (); // Return true if successfully tracked

    OpenGLMatrix getRobotPosition (); // Return robot position transform in millimeters

    OpenGLMatrix getRobotOrientation (); // Return robot rotation transform in degrees

    void setRobotPosition ();

    void setRobotOrientation ();
}
