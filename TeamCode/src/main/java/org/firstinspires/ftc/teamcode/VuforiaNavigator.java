package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VuforiaNavigator extends VuforiaFunctions
{
    private CompRobot compRobot;

    public VuforiaNavigator(OpMode opMode_In, HardwareMap hardwareMap, CompRobot compRobot_In)
    {
        super(opMode_In, hardwareMap);
        compRobot = compRobot_In;
    }

    public void goToPosition(float x, float y, float pow)
    {

    }
}
