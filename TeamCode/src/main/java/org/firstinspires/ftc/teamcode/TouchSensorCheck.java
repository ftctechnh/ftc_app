package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by thund on 10/19/2017.
 */

@TeleOp(name="Touch_Sensor_Check",group="Testing" )
@Disabled
public class TouchSensorCheck extends LinearOpMode{

    DigitalChannel sq0;
    DigitalChannel sq1;
    DigitalChannel sq2;
    DigitalChannel sq3;
    DigitalChannel sq4;
    DigitalChannel sq5;
    DigitalChannel sq6;
    DigitalChannel sq7;


    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(); //wait for the button to be pushed

        while (opModeIsActive()) {
            sq0 = hardwareMap.digitalChannel.get("sq0");
            sq1 = hardwareMap.digitalChannel.get("sq1");
            sq2 = hardwareMap.digitalChannel.get("sq2");
            sq3 = hardwareMap.digitalChannel.get("sq3");
            sq4 = hardwareMap.digitalChannel.get("sq4");
            sq5 = hardwareMap.digitalChannel.get("sq5");
            sq6 = hardwareMap.digitalChannel.get("sq6");
            sq7 = hardwareMap.digitalChannel.get("sq7");
            telemetry.addData("sq0", sq0.getState());
            telemetry.addData("sq1", sq1.getState());
            telemetry.addData("sq2", sq2.getState());
            telemetry.addData("sq3", sq3.getState());
            telemetry.addData("sq4", sq4.getState());
            telemetry.addData("sq5", sq5.getState());
            telemetry.addData("sq6", sq6.getState());
            telemetry.addData("sq7", sq7.getState());
            telemetry.update();
        }

    }

}
