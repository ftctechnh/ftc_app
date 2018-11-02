package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.pixycam.PixyCam;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

import java.util.ArrayList;


public class PixySystem extends System {
    private final int YELLOW_SIGNATURE = 3;
    private final int WHITE_SIGNATURE = 4;
    private PixyCam pixy;
    private double yellowPos;
    private double white1Pos;
    private double white2Pos;

    double yellowCount = -1;
    double yellowSum = 0;
    double white1Count = -1;
    double white1Sum = 0;
    double white2Count = -1;
    double white2Sum = 0;
    // private MecanumDriveSystem driveSystem;

    public PixySystem(OpMode opMode) {
        super(opMode, "PixySystem");
        pixy = hardwareMap.get(PixyCam.class, "PixyCam");
        pixy.setBlockCount(3);


        //driveSystem = new MecanumDriveSystem(opMode);
    }

    public void runPixySystem() {

        telemetry.log("Pixy System","running PixySystem");
        telemetry.write();
        printValues();
        centerYellow();
        driveForward();
    }

    public void printValues() {
        telemetry.log("Pixy System","yellow: " + yellowPos);

        telemetry.log("Pixy System", "yellowSum: " + yellowSum);
        telemetry.log("Pixy System", "yellowCount: " + yellowCount);

        telemetry.log("Pixy System","white1: " + white1Pos);
        telemetry.log("Pixy System","white2: " + white2Pos);
        telemetry.write();
    }

    private void getValues() {


        for (int i = 0; i < 3; i++) {
            int whiteVal1 = 0;
            int whiteVal2 = 0;
            ArrayList<PixyCam.Block> blocks = pixy.getBlocks();
            telemetry.log("Pixy System", blocks.size());
            telemetry.write();
            for (PixyCam.Block block : blocks) {
                telemetry.log("Pixy System","sig: " + block.signature + "   " + "xCenter: " + block.xCenter);
                telemetry.write();
                if (block.signature == YELLOW_SIGNATURE) {
                    yellowCount++;
                    yellowSum += block.xCenter;
                } else if (block.signature == WHITE_SIGNATURE) {
                    if (whiteVal1 == 0) {
                        whiteVal1 = block.xCenter;
                    } else {
                        whiteVal2 = block.xCenter;
                    }
                }

                if (whiteVal1 > whiteVal2 && whiteVal2 != 0) {
                    white1Sum += whiteVal2;
                    white1Count++;
                    white2Sum = whiteVal1;
                    white2Count++;
                }
            }
        }

        yellowPos = yellowSum / yellowCount;

        telemetry.write();
        white1Pos = white1Sum / white1Count;
        white2Pos = white2Sum / white2Count;

        printValues();
    }

    public void centerYellow() {
        // if yellow is visible but not centered, center it
        if(yellowPos > 0) {
            telemetry.log("Pixy System","centering yellow -- yellow is visible");
            while (yellowPos < 255 / 2 - 5) {
                // driveSystem.turn(-5, 0.1);
                telemetry.log("Pixy System", "turning clockwise");
                telemetry.write();
                getValues();
            }
            while (yellowPos > 255 / 2 + 5) {
                // driveSystem.turn(5, 0.1);
                telemetry.log("Pixy System", "turning counterclockwise");
                telemetry.write();
                getValues();
            }

        }
        // if yellow isnt visible but both white are and they're next to each other
        else if(white2Pos != 0 && white1Pos != 0 && white2Pos - white1Pos < 100) {
            telemetry.log("Pixy System","centering yellow -- white next to each other");
            telemetry.write();
            if(white2Pos < 2 * 255 / 3) {
                while(white2Pos > 255 / 3) {
                    // driveSystem.turn(5, 0.1);
                    telemetry.log("Pixy System", "turning counterclockwise");
                    telemetry.write();
                    getValues();
                }
            } else if(white1Pos > 255 / 3) {
                while(white1Pos < 2 * 255 / 3) {
                    // driveSystem.turn(-5, 0.1);
                    telemetry.log("Pixy System", "turning clockwise");
                    telemetry.write();
                    getValues();
                }
            }
        }
        // if yellow isnt visible but both white are and they're not next to each other
        else if (white2Pos - white1Pos >= 100) {
            telemetry.log("Pixy System","centering yellow -- yellow in middle");
            telemetry.write();
            while (white1Pos > 255 - white2Pos) {
                // driveSystem.turn(5, 0.1);
                telemetry.log("Pixy System", "turning counterclockwise");
                telemetry.write();
                getValues();
            }
            while (white1Pos < 255 - white2Pos) {
                // driveSystem.turn(-5, 0.1);
                telemetry.log("Pixy System", "turning clockwise");
                telemetry.write();
                getValues();
            }
        }
    }

    public void driveForward() {
        telemetry.log("Pixy System","driving forward");
        telemetry.write();
        //driveSystem.driveToPositionInches(4, 0.1);
    }
}
