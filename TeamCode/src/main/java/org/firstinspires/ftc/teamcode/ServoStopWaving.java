package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by wildgirls on 2/8/2018.
 */


@Autonomous(name="ServoStopWaving",group="Jeff" )
public class ServoStopWaving extends LinearOpMode {

    public void runOpMode() throws InterruptedException {

        int number = 1000;
        Servo left;
        Servo right;
        left = hardwareMap.servo.get("left");
        right = hardwareMap.servo.get("right");

        waitForStart();

        while(opModeIsActive()){
            right.setPosition(1); //closed
            sleep(1000);
            telemetry.addData("Right Servo", right.getPosition());
            telemetry.update();
            right.setPosition(0); //open
            sleep(1000);
            telemetry.addData("Right Servo", right.getPosition());
            telemetry.update();
            left.setPosition(-1); //closed
            sleep(1000);
            telemetry.addData("Left Servo", left.getPosition());
            telemetry.update();
            left.setPosition(1); //open
            sleep(1000);
            telemetry.addData("Left Servo", left.getPosition());
            telemetry.update();

            telemetry.addData("Right Servo", right.getPosition());
            telemetry.addData("Left Servo", left.getPosition());
            telemetry.addData("Nora is cool", number);
            telemetry.update();
        }
    }
}
