package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Michael on 10/14/2016.
 */
public class MotorTest extends RobotHardware {

    @Override public void loop () {

        if(gamepad1.x) set_drive_power(0.25, 0.25);

        if(!gamepad1.x) set_drive_power(0, 0);

    }

}