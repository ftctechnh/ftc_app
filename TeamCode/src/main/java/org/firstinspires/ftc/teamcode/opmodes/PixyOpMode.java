package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.systems.PixySystem;
import org.firstinspires.ftc.teamcode.systems.PixySystem2;

@Autonomous(name = "PixyOpMode", group = "Bot")
public class PixyOpMode extends LinearOpMode {
    private PixySystem2 pixySystem;


    public void initPixy() {
       pixySystem = new PixySystem2(this);
    }

    @Override
    public void runOpMode() {
        initPixy();
        telemetry.addLine("waiting for start");
        waitForStart();
        telemetry.addLine("called runPixySystem");
        pixySystem.runPixySystem();
    }
}