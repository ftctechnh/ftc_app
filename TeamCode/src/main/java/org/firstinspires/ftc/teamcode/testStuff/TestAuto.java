package org.firstinspires.ftc.teamcode.testStuff;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.core.Functions;


@Autonomous (name = "TestAutonomous", group = "Nessie")

public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Functions.OffLander(0.3);
        //scan for number 1-3 from jewels
        Functions.turn(-30, 1);
        Functions.move(33, 1);
        Functions.move(-9, 1);
        Functions.turn(-30, 1);
        Functions.move(43, 1);
        Functions.turn(-30, 1);
        Functions.move(35, 1);
        Functions.turn(180, 1);
    }}