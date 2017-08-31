package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//Under Construction

/**
 * Created by Eric on 8/28/2017.
 */
@TeleOp (name="EricJeepSensor", group="Test_JeepSensor")
@Disabled
public class Eric_JeepSensorCompetition extends LinearOpMode{
    @Override
    ModernRoboticsI2cRangeSensor jp;

    public void runOpMode() throws InterruptedException {

        jp = hardwareMap.ModernRoboticsI2cRangeSensor.get("sensor_jp");

        waitForStart();

        while (opModeIsActive()){

            telemetry.addData(jp.getDistance());
            telemetry.update();

        }

    }
}
