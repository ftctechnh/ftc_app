package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// Created by Swagster_Wagster on 9/29/17


// Last edit: 10/15/17 BY MRINAAL RAMACHANDRAN


@Autonomous(name = "Holonomic_Autonomous",group="We Love Pi")
public class HolonomicAutonomous extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    @Override
    public void runOpMode() throws InterruptedException {

        af.init(hardwareMap);

        telemetry.addData("ACTION COMPLETED", "READY FOR ACTION");

        waitForStart();

        af.moveMotorWithEncoder(.1, 2000, Constants.forward);
        af.stopMotor();
        af.moveMotorWithEncoder(.1, 1300, Constants.spinLeft);
        af.stopMotor();


    }
}

