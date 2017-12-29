package org.firstinspires.ftc.teamcode.SwagsterWagster_UltimateCode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Autonomous.Autonomous_Functions;

// Created by Swagster_Wagster on 12/2/17
//
//
// Last edit: 12/2/17 BY MRINAAL RAMACHANDRAN



@TeleOp(name="Self Driving", group="We Love Pi")

public class SelfDriving extends LinearOpMode {


    /*
    This is our bleeding edge Self Driving Code and all its glory.

        - The plan is to use sensors to perfectly align the robot to cryptobox
        - Then the robot will drop the block in all while using sensors.
        - If implemented correctly, this can remove all room for error in cryptobox dropping

    This might not be implemented for a while but will be in production ;D.
     */

    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        Autonomous_Functions af = new Autonomous_Functions();

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");


        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
            telemetry.addData("inch", "%.2f cm", rangeSensor.getDistance(DistanceUnit.INCH));
            telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();

            while (rangeSensor.rawUltrasonic() < 50.8) {

                af.moveMotor(0.5, Constants.right);
            }

            while (rangeSensor.rawUltrasonic() > 68.57) {

                af.moveMotor(0.5, Constants.left);
            }

            if (rangeSensor.rawUltrasonic() >= 20.00 && rangeSensor.rawUltrasonic() <= 27) {

                af.moveMotor(0, Constants.forward);

            }
        }
    }
}

