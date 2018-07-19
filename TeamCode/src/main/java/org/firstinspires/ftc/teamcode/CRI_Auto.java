package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "CRI_autonomous", group = "LinearOpMode")

public class CRI_Auto extends LinearOpMode {

    private Robot robot = new Robot();

    private boolean pictographScanned = false;

    private int selection;
    private int setAlliance;
    private int setBumpType;
    private int setStone;
    private int setMoreGlyphs;

    private String[] allianceList = {"blue", "red"};
    private String[] jewelBumpTypeList = {"correct", "wrong", "none"};
    private String[] stoneList = {"left", "right"};
    private String[] getMoreGlyphsList = {"Yes", "No"};

    private String alliance = "";
    private String jewelOrder = "";
    private String jewelBumpType = "";
    private String stone = "";
    private String getMoreGlyphs = "";
    private String cryptokey;



    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this);

        // =======================BEGIN SELECTION===================================================
        /* Custom List-style selection for autonomous. This way we can easily adjust
         * an incorrect value without having to restart the program */
        while(!gamepad1.a && !isStarted()) {
            String[] selector = {"   ", "   ", "   ", "   "};
            selector[selection] = "-> ";
            telemetry.addData("Alliance", selector[0] + allianceList[setAlliance]);
            telemetry.addData("Stone", selector[1] + stoneList[setStone]);
            telemetry.addData("Jewel Hit Type", selector[2] + jewelBumpTypeList[setBumpType]);
            telemetry.addData("Extra Glyph Load?", selector[3] + getMoreGlyphsList[setMoreGlyphs]);
            telemetry.addData("Use the Dpad buttons to set your autonomous program", "");
            telemetry.addData("Press A if this is ok", "");
            telemetry.update();
            if (isStopRequested()) {
                break;
            }
            if(gamepad1.dpad_down && selection < 3) {
                selection++;
                sleep(250);
            } else if(gamepad1.dpad_up && selection > 0) {
                selection--;
                sleep(250);
            }

            if(gamepad1.dpad_right) {
                switch (selection) {
                    case 0:
                        if(setAlliance < allianceList.length - 1) {
                            setAlliance++;
                        }
                        break;
                    case 1:
                        if(setStone < stoneList.length - 1) {
                            setStone++;
                        }
                        break;
                    case 2:
                        if(setBumpType < jewelBumpTypeList.length - 1) {
                            setBumpType++;
                        }
                        break;
                    case 3:
                        if(setMoreGlyphs < getMoreGlyphsList.length - 1) {
                            setMoreGlyphs++;
                        }
                        break;
                }
                sleep(250);
            } else if(gamepad1.dpad_left) {
                switch (selection) {
                    case 0:
                        if(setAlliance > 0) {
                            setAlliance--;
                        }
                        break;
                    case 1:
                        if(setStone > 0) {
                            setStone--;
                        }
                        break;
                    case 2:
                        if(setBumpType > 0) {
                            setBumpType--;
                        }
                        break;
                    case 3:
                        if(setMoreGlyphs > 0) {
                            setMoreGlyphs--;
                        }
                        break;
                }
                sleep(250);
            }
        }

        alliance = allianceList[setAlliance];
        stone = stoneList[setStone];
        jewelBumpType = jewelBumpTypeList[setBumpType];
        getMoreGlyphs = getMoreGlyphsList[setMoreGlyphs];
        sleep(500);

        waitForStart();

//  ====================================== AUTONOMOUS ==============================================

        // bump the jewel
        robot.bumpJewel(alliance);

        //Read the vumark
        robot.setJewelPivotPosition(0.42);
        String cryptoKey = robot.activateVuforia();           //Obtain the cryptokey and display it
        telemetry.addData("Cryptokey", cryptoKey);
        telemetry.update();
        robot.setJewelPivotPosition(0.5);

        // drive off the ramp
        robot.encoderDrive(20, 0.2);
        sleep(500);

        robot.gyroTurn(90, "RIGHT");

        // raise hopper
        robot.setHopperPosition(0);
        sleep(500);

        // drive to column
        if(cryptoKey.equals("KeyLeft")){
            robot.encoderDrive(8, 0.2);
        }
        else if(cryptoKey.equals("KeyRight")){
            robot.encoderDrive(16, 0.2);
        }
        else if(cryptoKey.equals("KeyCenter")){
            robot.encoderDrive(12, 0.2);
        }
        else if(cryptoKey.equals("KeyUnknown")){
            robot.encoderDrive(12, 0.2);
        }

        sleep(500);

        // turn away
        robot.gyroTurn(50, "RIGHT");
        sleep(500);

        // drive back into column
        robot.encoderDrive(-7, 0.2);
        sleep(500);

        // release glyph
        robot.setGripperPosition(0);
        sleep(500);

        // drive away from column
        robot.encoderDrive(7, 0.2);
        sleep(500);

        // turn to parallel with cryptobox
        robot.gyroTurn(-50, "LEFT");
        sleep(500);

    }
}
