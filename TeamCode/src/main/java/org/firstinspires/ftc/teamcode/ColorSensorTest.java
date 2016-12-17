package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;

/**
 * Created by Noah Pietrafesa on 12/17/16.
 */
@Autonomous

public class ColorSensorTest extends OpMode {

    DcMotor left1;
    DcMotor left2;
    DcMotor right1;
    DcMotor right2;
    ModernRoboticsI2cColorSensor red;

    @Override
    public void init() {

        left1 = hardwareMap.dcMotor.get("left1");
        left2 = hardwareMap.dcMotor.get("left2");
        right1 = hardwareMap.dcMotor.get("right1");
        right2 = hardwareMap.dcMotor.get("right2");
        red = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get("red");

    }

    @Override
    public void loop() {
        if (time < 5)
        {
            right1.setPower(-1);
            right2.setPower(-1);
            left1.setPower(1);
            left2.setPower(1);
        }
        else if ()
        {
            right1.setPower(0);
            right2.setPower(0);
            left1.setPower(0);
            left2.setPower(0);
        }
    }
}
