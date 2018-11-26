package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 11/25/2018.
 */
public class RobotLocation  {

    private double xCoord;
    private double yCoord;
    private double heading;

    void setPositionValues(double x, double y, double head){
        xCoord = x;
        yCoord = y;
        heading = head;

    }

    double getX(){
        return xCoord;
    }

    double getY(){
        return yCoord;
    }

    double getHeading(){
        return heading;
    }
}