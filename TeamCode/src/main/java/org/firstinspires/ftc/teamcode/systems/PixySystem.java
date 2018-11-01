package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

import java.util.ArrayList;

public class PixySystem extends System {
    public static final int YELLOW_SIGNATURE = 3;
    public static final int WHITE_SIGNATURE = 4;

    private LinearOpMode opMode;
    private PixyCam pixyCam;
    private MecanumDriveSystem driveSystem;

    private int yellowPos = 0;
    private int white1Pos = 0;
    private int white2Pos = 0;

    public PixySystem (OpMode opMode) {
        super(opMode, "PixySystem");
    }

    public void runPixySystem(LinearOpMode oMode) {
        opMode = oMode;
        pixyCam = map.get(PixyCam.class, "PixyCam");
        pixyCam.setBlockCount(3);

        ArrayList<PixyCam.Block> blocks = pixyCam.getBlocks();
        for(PixyCam.Block block : blocks){
            telemetry.addData("sig: ", block.signature);
            telemetry.addData("block: ", block.xCenter);
        }
        telemetry.update();

        /*
        driveSystem = new MecanumDriveSystem(opMode);
        telemetry.addLine("called RunPixySystem");

        driveSystem.turn(90, 0.3);
        telemetry.addLine("turning 90 degs to start");
        telemetry.update();
        spin(); */
    }

    // get values from PixyCam
    public void getValues() {
        ArrayList<ArrayList<PixyCam.Block>> valuesArray = new ArrayList<>();

        int yellowSum = 0;
        int yellowCount = -1;
        int white1Sum = 0;
        int white1Count = -1;
        int white2Sum = 0;
        int white2Count = -1;

        for (int i = 0; i < 3; i++) {
            PixyCam.Block yellowBlock = null;
            PixyCam.Block whiteBlock1 = null;
            PixyCam.Block whiteBlock2 = null;

            ArrayList<PixyCam.Block> blocks = pixyCam.getBlocks();
            valuesArray.add(blocks);
            if (!blocks.isEmpty()) {
                telemetry.addLine("getting values from blocks: " + blocks.toString());
                for (PixyCam.Block block : blocks) {
                    if (block.signature == YELLOW_SIGNATURE) {
                        yellowBlock = block;

                        yellowSum += yellowBlock.xCenter;
                        if (yellowBlock.xCenter != 0) {
                            yellowCount++;
                        }
                        telemetry.addLine("yellow: " + yellowBlock.xCenter);
                        telemetry.update();
                    } else if (whiteBlock1 == null && block.signature == WHITE_SIGNATURE) {
                        whiteBlock1 = block;
                        white1Sum += whiteBlock1.xCenter;
                        if (whiteBlock1.xCenter != 0) {
                            white1Count++;
                        }
                        telemetry.addLine("white1: " + whiteBlock1.toString());
                        telemetry.update();
                    } else if (whiteBlock2 == null && block.signature == WHITE_SIGNATURE) {
                        whiteBlock2 = block;
                        white2Sum += whiteBlock2.xCenter;
                        if (whiteBlock2.xCenter != 0) {
                            white2Count++;
                        }
                        telemetry.addData("white2: ", whiteBlock2);
                        telemetry.update();
                    }
                }
            }
        }

        yellowPos = yellowSum / yellowCount;
        white1Pos = white1Sum / white1Count;
        white2Pos = white2Sum / white2Count;

        // change names so whiteBlock1 is on the left and whiteBlock2 is on the right
        if (white1Pos != 0 && white2Pos != 0) {
            if (white1Pos > white2Pos) {
                int tempPos = white1Pos;
                white1Pos = white2Pos;
                white2Pos = tempPos;
            }
        }
    }

    public void spin() {
        telemetry.addLine("spinning");
        int angle = 0;

        // if yellow isn't there or it's too far left, turn clockwise
        while ((yellowPos == 0 || yellowPos < 255 / 2 - 10) && angle <= 360) {
            driveSystem.turn(-10, 0.2);

            angle += 10;
            getValues();
            telemetry.addLine("angle: " + angle);
            telemetry.update();
        }

        // if yellow is too far right, turn counterclockwise
        while (yellowPos > 255 / 2 + 10) {
            driveSystem.turn(10, 0.2);
            angle += 10;
            getValues();
            telemetry.addLine("angle: " + angle);
            telemetry.update();
        }

        // if yellow's centered, drive forward
        if (yellowPos != 0 && yellowPos > 255 / 2- 10 && yellowPos < 255 / 2 + 10) {
            driveSystem.driveToPositionInches(2, 0.1);
            telemetry.addLine("driving forward");
            telemetry.update();
        }

        // make the white balls equidistant from either side
        if (angle > 360) {
            if (yellowPos == 0) {
                while (white1Pos > 255 / 2 - white2Pos) {
                    driveSystem.turn(5, 0.1);
                    telemetry.addLine("yellow is null   turning counterclockwise");
                    telemetry.update();
                }
                while (white1Pos < 255 / 2 - white2Pos) {
                    driveSystem.turn(-5, 0.1);
                    telemetry.addLine("yellow is null   turning clockwise");
                    telemetry.update();
                }
            }

        }
        telemetry.update();
    }
/*
    public void center() {
        // center yellow in the frame
        if(yellowBlock != null) {
            while (yellowBlock.xCenter < 255 - 10 && yellowBlock.xCenter > 255 + 10) {
                if(yellowBlock.xCenter < 255 - 10) {
                    driveSystem.mecanumDriveXY(0.1, 0);
                    sleep(10);
                    driveSystem.mecanumDriveXY(0, 0);
                }
            }
        } else {
            if (whiteBlock2.xCenter < 2 * 255 / 3) {
                getYellow("right");
            } else if(whiteBlock1.xCenter > 255 / 3) {
                getYellow("left");
            }
        }
    }

    public void getBlocks() {
        while ((yellowBlock == null && whiteBlock1 == null) ||
               (whiteBlock1 == null && whiteBlock2 == null) ||
               (yellowBlock == null && whiteBlock1 == null)) {
            driveSystem.turn(10, 0.1);
            getValues();
        }
    }

    public void getYellow(String direction) {
        int angle = 0;
        while (yellowBlock == null && angle <= 360) {
            if (direction.equalsIgnoreCase("right")) {
                driveSystem.turn(-10, 0.1);
            } else if (direction.equalsIgnoreCase("left")) {
                driveSystem.turn(10, 0.1);
            }
        }
    }
*/

    /*
    if the yellow block isn't visible, figure out if the yellow block is on the far left or right
        if it's on the far right, move right until the yellow block is visible
        if it's on the far left, move left until the yellow block is visible
    center the yellow block
    drive forward
     */

/*
    public PixySystem(LinearOpMode oMode) {
    //public void initPixy() {
        opMode = oMode;
        pixyCam = hardwareMap.get(PixyCam.class, "PixyCam");
        //driveSystem = dSystem;
        //driveSystem = new MecanumDriveSystem(opMode);
    }

    public void runPixySystem() {
        // initPixy();
        getValues();
        center();
    }

    public void getValues() {
        yellowBlock = pixyCam.GetBiggestBlock(3);
        telemetry.addData("yellow: ", yellowBlock.toString());
        whiteBlock = pixyCam.GetBiggestBlock(4);
        telemetry.addData("white: ", whiteBlock.toString());
        telemetry.update();
    }


    // if the yellow block isn't in the center of the pixy camera, move the robot so the yellow
    //      block is centered, then drive forward
    public void center() {
        // get the yellow block in the field of vision
        while ((yellowBlock.x == 0 && yellowBlock.y == 0) ||
               (yellowBlock.width < 5 || yellowBlock.height < 5)) {
            if (whiteBlock.x < 255 / 2) {
                // strafe right
                telemetry.addLine("driving right to see yellow");
                // driveSystem.mecanumDriveXY(1, 0);
                sleep(10);
                // driveSystem.mecanumDrive(0, 0)
            } else {
                // strafe left
                telemetry.addLine("driving left to see yellow");
                // driveSystem.mecanumDriveXY(-1, 0);
                sleep(10);
                // driveSystem.mecanumDrive(0, 0)
            }
        }

        // center the yellow block
        while (!(yellowBlock.x < 255 + 5 || yellowBlock.x > 255 - 5)) {
            if (yellowBlock.x < 255 / 2) {
                // strafe right
                telemetry.addLine("driving right");
                // driveSystem.mecanumDriveXY(1, 0);
                sleep(10);
                // driveSystem.mecanumDrive(0, 0)
            } else if (yellowBlock.x > 255 / 2) {
                // strafe left
                telemetry.addLine("driving left");
                // driveSystem.mecanumDriveXY(-1, 0);
                sleep(10);
                // driveSystem.mecanumDrive(0, 0)
            }
        }

        // drive forward to hit the block
        telemetry.addLine("driving forward");
        // driveSystem.mecanumDriveXY(0, 1);
        sleep(10);
        // driveSystem.mecanumDrive(0, 0)
    }
  */



}
