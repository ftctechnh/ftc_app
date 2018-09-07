package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Red_Back_Auto", group = "LinearOpMode")

public class CRI_Red_Back_Auto extends LinearOpMode {

    private Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this);
        robot.setJewelArmPosition(0.98);
        sleep(500);

        waitForStart();

//  ====================================== AUTONOMOUS ==============================================

        // bump the jewel
        robot.bumpJewel("red");

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
        robot.encoderDrive(-20, 0.15);
        sleep(500);

        robot.gyroTurn(88, "RIGHT");

        // raise hopper
//        robot.setHopperPosition(0);
        sleep(500);

        // drive to column
        if(cryptoKey.equals("KeyRight")){
            robot.encoderDrive(9, 0.2);
        }
        else if(cryptoKey.equals("KeyLeft")){
            robot.encoderDrive(21, 0.2);
        }
        else if(cryptoKey.equals("KeyCenter")){
            robot.encoderDrive(17, 0.2);
        }
        else if(cryptoKey.equals("KeyUnknown")){
            robot.encoderDrive(17, 0.2);
        }

        sleep(500);

        // turn away
        robot.gyroTurn(-45, "LEFT");
        sleep(300);
        robot.encoderDrive(-4, 0.2); // drive back into column
        sleep(300);
        robot.setHopperPosition(0); // raise hopper
        sleep(1000);
        robot.encoderDrive(-2, 0.2); // drive back into column
        sleep(300);
        robot.encoderDrive(7, 0.2); // drive away from column
        sleep(300);
        robot.setHopperPosition(0.65); // lower hopper
        sleep(300);
        robot.gyroTurn(45, "RIGHT");
        sleep(300);

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

//        // turn to parallel with cryptobox
//        robot.gyroTurn(-45, "LEFT");
//
//        robot.setGripperPosition(0);
//        sleep(500);
//
//        robot.runIntake(-1);
//        sleep(1200);
//        robot.runIntake(0);
//        robot.setGripperPosition(0.65);
//
//        robot.encoderDrive(25, 0.2);
////        robot.setHopperPosition(0);
//
//        if(cryptoKey.equals("KeyLeft") || cryptoKey.equals("KeyCenter") || cryptoKey.equals("KeyUnknown")){
//            sleep(300);
//            robot.gyroTurn(45, "RIGHT"); // turn away
//            sleep(300);
//            robot.encoderDrive(-4, 0.2); // drive back into column
//            sleep(300);
//            robot.setHopperPosition(0); // raise hopper
//            sleep(1000);
//            robot.encoderDrive(-2, 0.2); // drive back into column
//            sleep(300);
//            robot.encoderDrive(7, 0.2); // drive away from column
//            sleep(300);
//            robot.setHopperPosition(0.65); // lower hopper
//            sleep(300);
//            robot.gyroTurn(40, "RIGHT");
//            sleep(300);
//        }
//        else if (cryptoKey.equals("KeyRight")){
//            sleep(300);
//            robot.gyroTurn(130, "RIGHT"); // turn away
//            sleep(300);
//            robot.encoderDrive(-4, 0.2); // drive back into column
//            sleep(300);
//            robot.setHopperPosition(0); // raise hopper
//            sleep(1000);
//            robot.encoderDrive(-2, 0.2); // drive back into column
//            sleep(300);
//            robot.encoderDrive(7, 0.2); // drive away from column
//            sleep(300);
//            robot.setHopperPosition(0.65); // lower hopper
//            sleep(300);
//            robot.gyroTurn(-35, "LEFT");
//            sleep(300);
//        }
    }
}