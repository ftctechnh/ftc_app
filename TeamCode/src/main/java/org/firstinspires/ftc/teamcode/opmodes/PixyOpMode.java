package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.systems.PixySystem;

@Autonomous(name = "PixyOpMode", group = "Bot")
public class PixyOpMode extends LinearOpMode {
    private PixySystem pixySystem;


    public void initPixy() {
       pixySystem = new PixySystem(this);
    }

    @Override
    public void runOpMode() {
        initPixy();
        telemetry.addLine("waiting for start");
        waitForStart();
        pixySystem.runPixySystem(this);
    }
}