package org.firstinspires.ftc.teamcode;

/**
 * Created by Sahiti and Raghav on 10/4/2017.
 */

public interface AttatchmentsInterface
{
    void moveLift(double x, double y, boolean leftTrigger, boolean leftBumper);
    /*Joystick=fine tuning position of forklift (left,right,slight up and down)
    Bumper=Position increases one position upwards
    Trigger=Position increases one position downwards
     */
    void moveWings(boolean up);
    boolean isSeeingRed();
    void releaseGrabber(boolean buttonPressed);
    String readDiagram();
    double[] getAccelerometerValues();
}
