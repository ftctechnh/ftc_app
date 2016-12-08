package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by Kota Baer on 12/6/2016.
 */
@Autonomous(name = "LineFollowingTest")
public class LineFollowingTest  extends LinearOpMode {
    double speedmulti = 1;
    double turnSpeed = .17 * speedmulti;


    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        //super.runOpMode();
        //robot.leftLightSensor.enableLed(true);
        //robot.rightLightSensor.enableLed(true);
        telemetry.addData("robot stat",robot.leftLightSensor.status() + " " + robot.rightLightSensor.status());
        telemetry.update();

        waitForStart();


        while(true) {
            telemetry.addData("LightValueL", robot.leftLightSensor.getLightDetected());
            telemetry.addData("LightValueL", robot.rightLightSensor.getLightDetected());
            telemetry.update();
        }

            /*
            if (robot.leftLightSensor.getLightDetected() > .5) {
                robot.leftMotor.setPower(-turnSpeed);
                robot.rightMotor.setPower(turnSpeed);
            }
            if (robot.rightLightSensor.getLightDetected() > .5) {
                robot.rightMotor.setPower(-turnSpeed);
                robot.leftMotor.setPower(turnSpeed);
            }
            if (robot.rightLightSensor.getLightDetected() > .7 && robot.leftLightSensor.getLightDetected() > .7) {
                robot.rightMotor.setPower(-turnSpeed);
                robot.leftMotor.setPower(turnSpeed);
            }*/

            /*
            telemetry.addData("RightSensor", leftLightSensor.getLightDetected());
            telemetry.addData("LeftSensor", rightLightSensor.getLightDetected());
            if (leftLightSensor.getLightDetected() > .5) turn = 'r';
            if (rightLightSensor.getLightDetected() > .5) turn = 'l';
            if (turn == 'r') {
                telemetry.addData("Left Turn", " false");
                telemetry.addData("Right Turn", " true");
            } else {
                telemetry.addData("Right Turn", " false");
            }
            if (turn == 'l') {
                telemetry.addData("Right Turn", " false");
                telemetry.addData("Left Turn", " true");
            } else {
                telemetry.addData("Left Turn", " false");
            }
            */
            //waitOneFullHardwareCycle();
        //}
    }
}
