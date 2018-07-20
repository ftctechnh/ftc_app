package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Blue_Back_Auto", group = "LinearOpMode")

public class CRI_Blue_Back_Auto extends LinearOpMode {

    private Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this);
        sleep(500);

        waitForStart();

//  ====================================== AUTONOMOUS ==============================================

        // bump the jewel
        robot.bumpJewel("blue");

        //Read the vumark
        robot.setJewelPivotPosition(0.42);
        String cryptoKey = robot.activateVuforia();           //Obtain the cryptokey and display it
        telemetry.addData("Cryptokey", cryptoKey);
        telemetry.update();
        robot.setJewelPivotPosition(0.5);

        // release intake slides
        robot.runLift(1);
        sleep(400);
        robot.runLift(-1);
        sleep(400);
        robot.runLift(0);

        // drive off the ramp
        robot.encoderDrive(20, 0.15);
        sleep(500);

        robot.gyroTurn(88, "RIGHT");

        // raise hopper
        robot.setHopperPosition(0);
        sleep(500);

        // drive to column
        if(cryptoKey.equals("KeyLeft")){
            robot.encoderDrive(11, 0.2);
        }
        else if(cryptoKey.equals("KeyRight")){
            robot.encoderDrive(17, 0.2);
        }
        else if(cryptoKey.equals("KeyCenter")){
            robot.encoderDrive(14, 0.2);
        }
        else if(cryptoKey.equals("KeyUnknown")){
            robot.encoderDrive(14, 0.2);
        }

        sleep(500);

        // turn away
        robot.gyroTurn(45, "RIGHT");
        sleep(500);

        // drive back into column
        robot.encoderDrive(-7, 0.2);
        sleep(500);

        // release glyph
        robot.setGripperPosition(0);
        sleep(500);

        // drive away from column
        robot.encoderDrive(7, 0.2);
        robot.setGripperPosition(0.65);
        sleep(500);

        robot.setHopperPosition(0.65);
        sleep(500);

        // turn to parallel with cryptobox
        robot.gyroTurn(-45, "LEFT");

        robot.setGripperPosition(0);
        sleep(500);

        robot.runIntake(-1);
        sleep(1200);
        robot.runIntake(0);
        robot.setGripperPosition(0.65);

        robot.encoderDrive(25, 0.2);
        robot.setHopperPosition(0);


        sleep(500);
        // turn away
        robot.gyroTurn(45, "RIGHT");
        sleep(500);

        // drive back into column
        robot.encoderDrive(-7, 0.2);
        sleep(500);

        // release glyph
        robot.setGripperPosition(0);
        sleep(500);

        // drive away from column
        robot.encoderDrive(7, 0.2);
        robot.setGripperPosition(0.65);
        sleep(500);

    }
}
