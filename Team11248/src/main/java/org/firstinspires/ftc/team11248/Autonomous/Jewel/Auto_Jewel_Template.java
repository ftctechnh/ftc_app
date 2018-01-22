package org.firstinspires.ftc.team11248.Autonomous.Jewel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.team11248.Hardware.Claw;
import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/28/17.
 */

public class Auto_Jewel_Template extends LinearOpMode {

    private boolean isBlueAlliance;
    DcMotor lift;
    Claw claw;

    public Auto_Jewel_Template (boolean isBlueAlliance){
        this.isBlueAlliance = isBlueAlliance;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        Robot11248 robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.activateColorSensors();

        if(isBlueAlliance){
            lift = robot.frontLift;
            claw = robot.frontClaw;

        }else{
            lift = robot.backLift;
            claw = robot.backClaw;
        }

        waitForStart();


        robot.lowerJewelArm();

        claw.grab();
        sleep(200);
        lift.setPower(1);
        sleep(375);
        lift.setPower(0);

        sleep(1500);
        boolean isLeftJewelRed = robot.jewelColor.isRed();
        boolean isLeftJewelBlue = robot.jewelColor.isBlue();


        if( !(isLeftJewelBlue == isLeftJewelRed) ) {

            robot.drive(0, .5 * ((isBlueAlliance?isLeftJewelRed:isLeftJewelBlue) ? 1 : -1), 0);
            sleep(500);
            robot.setDriftMode(true);

            robot.stop();
            robot.setDriftMode(false);
            robot.raiseJewelArm();


            sleep(500);

            if(isLeftJewelBlue){
                robot.drive(0,-1 * (isBlueAlliance?1:-1), 0);
                sleep(750);
                robot.stop();

            } else {
                robot.drive(0, isBlueAlliance?1:-1, 0);
                sleep(1500);

                robot.setDriftMode(true);
                sleep(500);
                robot.stop();
                robot.setDriftMode(false);


                robot.drive(0,-1 * (isBlueAlliance?1:-1), 0);
                sleep(750);
                robot.stop();
            }

        } else { // if doesnt sense anythig do park code

            robot.raiseJewelArm();
            sleep(500);
            robot.drive(0,-1 * (isBlueAlliance?1:-1), 0);
            sleep(1100);
            robot.stop();
        }

        robot.deactivateColorSensors();

    }
}
