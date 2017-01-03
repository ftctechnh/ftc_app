package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Kota Baer on 12/7/2016.
 */
@Autonomous(name = "LightTest", group = "Concept")
@Disabled
public class LinesFellow extends LinearOpMode{
    double speedmulti = 1;
    double turnSpeed = .17 * speedmulti;

    @Override
    public void runOpMode(){
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        telemetry.addData("Init", "true");
        telemetry.update();
        waitForStart();
        while(robot.leftLightSensor.getLightDetected() < .25 || robot.rightLightSensor.getLightDetected() < .25 ){
            robot.setDrivePower(.25);
        }
        robot.setDrivePower(0);
        /*
        while(opModeIsActive()){
            telemetry.addData("IsRunning","true");

            if (robot.leftLightSensor.getLightDetected() > .25) {
                robot.leftMotor.setPower(-turnSpeed);
                robot.rightMotor.setPower(turnSpeed);
            }
            if (robot.rightLightSensor.getLightDetected() > .25) {
                robot.rightMotor.setPower(-turnSpeed);
                robot.leftMotor.setPower(turnSpeed);
            }
            if (robot.rightLightSensor.getLightDetected() > .25 && robot.leftLightSensor.getLightDetected() > .25) {
                robot.rightMotor.setPower(-turnSpeed);
                robot.leftMotor.setPower(turnSpeed);
            }

            telemetry.addData("LightValueL", robot.leftLightSensor.getLightDetected());
            telemetry.addData("LightValueR", robot.rightLightSensor.getLightDetected());
            telemetry.update();
        }
    */
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }
}
