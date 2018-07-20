package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Blue_Front_Auto", group = "LinearOpMode")

public class CRI_Blue_Front_Auto extends LinearOpMode {

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
        robot.encoderDrive(22, 0.1);
        sleep(500);

        // drive to column
        if(cryptoKey.equals("KeyLeft")){
            robot.encoderDrive(11, 0.2);
        }
        else if(cryptoKey.equals("KeyRight")){
            robot.encoderDrive(4, 0.2);
        }
        else if(cryptoKey.equals("KeyCenter")){
            robot.encoderDrive(17, 0.2);
        }
        else if(cryptoKey.equals("KeyUnknown")){
            robot.encoderDrive(17, 0.2);
        }

        if(cryptoKey.equals("KeyLeft") || cryptoKey.equals("KeyCenter") || cryptoKey.equals("KeyUnknown")){
            sleep(300);
            robot.gyroTurn(45, "RIGHT"); // turn away
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
            robot.gyroTurn(40, "RIGHT");
            sleep(300);
        }
        else if (cryptoKey.equals("KeyRight")){
            sleep(300);
            robot.gyroTurn(130, "RIGHT"); // turn away
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
            robot.gyroTurn(-35, "LEFT");
            sleep(300);
        }




        robot.runIntake(-1);
        robot.encoderDrive(42, 0.3);
        sleep(2000);
        robot.encoderDrive(7, 0.2);
        sleep(2000);
        robot.runIntake(1);
        sleep(100);
        robot.runIntake(-1);
        if(cryptoKey.equals("KeyRight")){
//            robot.gyroTurn(5, "RIGHT");
        } else {
            robot.gyroTurn(-7, "LEFT");
        }
        sleep(300);
        robot.encoderDrive(-57, 0.3);
        sleep(1000);

        // raise hopper
        robot.setHopperPosition(0);
        sleep(2000);
        robot.setHopperPosition(0.65);
        sleep(300);
        robot.runIntake(0);
        // drive away from column
        robot.encoderDrive(5, 0.2);
        sleep(300);



    }
}
