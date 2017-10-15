package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

// Created by Swagster_Wagster on 9/29/17

// Last edit: 10/13/17 BY MRINAAL RAMACHANDRAN

@Autonomous(name = "Holonomic_Autonomous",group="We Love Pi")
public class HolonomicAutonomous extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    @Override
    public void runOpMode() throws InterruptedException {

        af.init(hardwareMap);

        waitForStart();

        af.moveMotorWithEncoder(.2, 3000, Constants.forward);

        
    }
}
