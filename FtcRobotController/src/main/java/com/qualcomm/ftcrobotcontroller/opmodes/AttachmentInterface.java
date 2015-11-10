package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Dublin FTC on 10/4/2015.
 */
public interface AttachmentInterface
{
    void moveGrabberUp();
    void moveGrabberDown();
    void releaseClimbers();
    void grabClimbers();
    void senseColor(/*param for value of blue*/);
    void primeObjectsInRobot();
    void loadGrabber();
    void collectObjects();
    void pushButton(boolean isButtonLeft);
}
