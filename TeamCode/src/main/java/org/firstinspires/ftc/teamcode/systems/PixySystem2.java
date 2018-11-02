package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

import java.util.ArrayList;


public class PixySystem2 extends System {
    private final int YELLOW_SIGNATURE = 3;
    private final int WHITE_SIGNATURE = 4;
    private Telemetry telemetry;
    private PixyCam pixy;
    private int yellowPos;
    private int white1Pos;
    private int white2Pos;
    private MecanumDriveSystem driveSystem;

    public PixySystem2 (OpMode opMode) {
        super(opMode, "PixySystem2");
        pixy = hardwareMap.get(PixyCam.class, "PixyCam");
        pixy.setBlockCount(3);
        driveSystem = new MecanumDriveSystem(opMode);
    }

    public void runPixySystem() {

        telemetry.addLine("running PixySystem");
        printValues();
        centerYellow();
        driveForward();
    }

    public void printValues() {
        telemetry.addData("yellow: ", yellowPos);
        telemetry.addData("white1: ", white1Pos);
        telemetry.addData("white2: ", white2Pos);
    }

    private void getValues() {
        int yellowCount = -1;
        int yellowSum = 0;
        int white1Count = -1;
        int white1Sum = 0;
        int white2Count = -1;
        int white2Sum = 0;

        for (int i = 0; i < 3; i++) {
            int whiteVal1 = 0;
            int whiteVal2 = 0;
            ArrayList<PixyCam.Block> blocks = pixy.getBlocks();
            for (PixyCam.Block block : blocks) {
                telemetry.addLine("sig: " + block.signature + "   " + "xCenter: " + block.xCenter);
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
        white1Pos = white1Sum / white1Count;
        white2Pos = white2Sum / white2Count;

        printValues();
    }

    public void centerYellow() {
        // if yellow is visible but not centered, center it
        if(yellowPos > 0) {
            telemetry.addLine("centering yellow -- yellow is visible");
            while (yellowPos < 255 / 2 - 5) {
                driveSystem.turn(-5, 0.1);
                getValues();
            }
            while (yellowPos > 255 / 2 + 5) {
                driveSystem.turn(5, 0.1);
                getValues();
            }

        }
        // if yellow isnt visible but both white are and they're next to each other
        else if(white2Pos != 0 && white1Pos != 0 && white2Pos - white1Pos < 100) {
            telemetry.addLine("centering yellow -- white next to each other");
            if(white2Pos < 2 * 255 / 3) {
                while(white2Pos > 255 / 3) {
                    driveSystem.turn(5, 0.1);
                    getValues();
                }
            } else if(white1Pos > 255 / 3) {
                while(white1Pos < 2 * 255 / 3) {
                    driveSystem.turn(-5, 0.1);
                    getValues();
                }
            }
        }
        // if yellow isnt visible but both white are and they're not next to each other
        else if (white2Pos - white1Pos >= 100) {
            telemetry.addLine("centering yellow -- yellow in middle");
            while (white1Pos > 255 - white2Pos) {
                driveSystem.turn(5, 0.1);
                getValues();
            }
            while (white1Pos < 255 - white2Pos) {
                driveSystem.turn(-5, 0.1);
                getValues();
            }
        }
    }

    public void driveForward() {
        telemetry.addLine("driving forward");
        driveSystem.driveToPositionInches(4, 0.1);
    }
}
