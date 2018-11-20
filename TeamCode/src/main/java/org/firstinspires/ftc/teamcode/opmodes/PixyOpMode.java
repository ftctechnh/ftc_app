package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.components.pixycam.LegoPixyCam;
//import org.firstinspires.ftc.teamcode.components.pixycam.PixyCam;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.LinearOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

import java.util.ArrayList;

@Autonomous(name = "PixyOpMode", group = "Bot")
public class PixyOpMode extends LinearOpMode {
    LegoPixyCam pixy;

    MecanumDriveSystem driveSystem;


    public void runOpMode() {
        pixy = this.hardwareMap.get(LegoPixyCam.class, "PixyCam");
        driveSystem = new MecanumDriveSystem(this);
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addLine("geofa");
            telemetry.addLine("waiting for start");
            telemetry.addLine("called runPixySystem");
            telemetry.update();

            yellow();
        }
    }

    private void yellow() {
        int yellowX = -1;
        // ArrayList<PixyCam.Block> blocks = pixy.getBlocks();

        yellowX = pixy.GetBiggestBlock(3).x;

        /*
        for(PixyCam.Block block : blocks) {
            telemetry.addLine("Pixy Cam: " + "block: "+ block.xCenter);
            if (block.signature == 3) {
                yellowX = block.xCenter;
            }
            telemetry.addLine();
        } */

        if (yellowX < 255 / 2 - 10) {
            telemetry.addLine("Pixy System: " + "turn counterclockwise");
            telemetry.update();
            driveSystem.turn(5, 0.1);
            yellow();
        } else if (yellowX > 255 / 2 + 10) {
            telemetry.addLine("Pixy System: " +  "turn clockwise");
            telemetry.update();
            driveSystem.turn(-5, 0.1);
            yellow();
        }


    }
}