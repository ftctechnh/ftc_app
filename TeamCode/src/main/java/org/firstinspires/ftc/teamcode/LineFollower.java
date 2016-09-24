package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by djfigs1 on 5/27/16.
 */
public class LineFollower extends LineFollowBot {

    private boolean foundLine = false;
    private boolean stopped = false;


    private double slowSpeed = 0.07;
    private double fastSpeed = 0.25;

    @Override
    public void loop(){
        if (foundLine == false) {
            //Move forward until the robot has detected a line
            setDrivePower(slowSpeed,slowSpeed);
            if (getColorSensorsState() == ColorSensorsState.BOTH) {
                //If any of the color sensors detect a line, start following it.
                foundLine = true;
            }
        }else{
            //Attempt to follow the line
            switch (getColorSensorsState()){
                case LEFT:
                    setDrivePower(fastSpeed, 0);
                    break;

                case RIGHT:
                    setDrivePower(0, fastSpeed);
                    break;

                case BOTH:
                    setDrivePower(slowSpeed, slowSpeed);
                    break;

                case NONE:
                    //Since we've found a line, stop.
                    setDrivePower(0,0);
                    stopped = true;
                    break;
            }
        }
        if (stopped){
            //After we stopped, start spinning the handle.
            setSpinHandlePower(0.3);
        }


        telemetry.addData("LeftBlue", leftColorSensor.blue());
        telemetry.addData("RightBlue", rightColorSensor.blue());
        telemetry.addData("State", getColorSensorsState().toString());
        telemetry.addData("Found Line", foundLine);
        telemetry.addData("Stopped", stopped);
    }
}
