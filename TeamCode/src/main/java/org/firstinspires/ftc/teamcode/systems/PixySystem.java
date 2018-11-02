package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.components.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.systems.base.System;

import java.util.ArrayList;

//@Autonomous(name = "PixySystem", group = "Bot")
public class PixySystem extends System {
    public static final int YELLOW_SIGNATURE = 3;
    public static final int WHITE_SIGNATURE = 4;

    private LinearOpMode opMode;
    private PixyCam pixyCam;
    private PixyCam.Block yellowBlock;
    private PixyCam.Block whiteBlock1;
    private PixyCam.Block whiteBlock2;
    private MecanumDriveSystem driveSystem;

    public PixySystem (OpMode opMode) {
        super(opMode, "PixySystem");
    }

    public void runPixySystem(LinearOpMode oMode) {
        opMode= oMode;
        pixyCam = opMode.hardwareMap.get(PixyCam.class, "PixyCam");
        driveSystem = new MecanumDriveSystem(opMode);
        telemetry.log("Pixy System","called RunPixySystem");

        driveSystem.turn(90, 0.3);
        telemetry.log("Pixy System","turning 90 degs to start");
        telemetry.write();
        spin();

    }

    // get values from PixyCam
    public void getValues() {
        ArrayList<PixyCam.Block> blocks = pixyCam.getBlocks();
        if (!blocks.isEmpty()) {
            telemetry.log("Pixy Cam","getting values from blocks: {0}", blocks.toString());
            for (PixyCam.Block block : blocks) {
                if (block.signature == YELLOW_SIGNATURE) {
                    yellowBlock = block;
                    telemetry.log("Pixy System","yellow: {0}", yellowBlock);
                    telemetry.log("Pixy Systen","yellow is null, turning counterclockwise");
                } else if (whiteBlock1 == null && block.signature == WHITE_SIGNATURE) {
                    whiteBlock1 = block;
                    telemetry.log("Pixy System","white1: {0}", whiteBlock1);
                    telemetry.log("Pixy System","yellow is null, turning counterclockwise");
                } else if (whiteBlock2 == null && block.signature == WHITE_SIGNATURE) {
                    whiteBlock2 = block;
                    telemetry.log("Pixy System","white2: {0}", whiteBlock2);
                    telemetry.log("Pixy System", "yellow is null, turning counterclockwise");
                }
            }

            // change names so whiteBlock1 is on the left and whiteBlock2 is on the right
            if (whiteBlock1 != null && whiteBlock2 != null) {
                if (whiteBlock1.xCenter > whiteBlock2.xCenter) {
                    PixyCam.Block temp = whiteBlock1;
                    whiteBlock1 = whiteBlock2;
                    whiteBlock2 = temp;
                }
            }
        }
    }

    public void spin() {
        telemetry.log("Pixy System","spinning");
        int angle = 0;

        // if yellow isn't there or it's too far left, turn clockwise
        while ((yellowBlock == null || yellowBlock.xCenter < 255 / 2 - 10) && angle <= 360) {
            driveSystem.turn(-10, 0.2);

            angle += 10;
            getValues();
            telemetry.log("Pixy System","angle: " + angle);
            telemetry.write();
        }

        // if yellow is too far right, turn counterclockwise
        while (yellowBlock.xCenter > 255 / 2 + 10) {
            driveSystem.turn(10, 0.2);
            angle += 10;
            getValues();
            telemetry.log("Pixy System","angle: " + angle);
            telemetry.write();
        }

        // if yellow's centered, drive forward
        if (yellowBlock != null && yellowBlock.xCenter > 255 / 2- 10 && yellowBlock.xCenter < 255 / 2 + 10) {
            driveSystem.driveToPositionInches(2, 0.1);
            telemetry.log("Pixy System","driving forward");
            telemetry.write();
        }

        // make the white balls equidistant from either side
        if (angle > 360) {
            if (yellowBlock == null) {
                while (whiteBlock1.xCenter > 255 / 2 - whiteBlock2.xCenter) {
                    driveSystem.turn(5, 0.1);
                    telemetry.log("Pixy System","yellow is null, turning counterclockwise");
                    telemetry.write();
                }
                while (whiteBlock1.xCenter < 255 / 2 - whiteBlock2.xCenter) {
                    driveSystem.turn(-5, 0.1);
                    telemetry.log("Pixy System","yellow is null   turning clockwise");
                    telemetry.write();
                }
            }

        }
        telemetry.write();
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
