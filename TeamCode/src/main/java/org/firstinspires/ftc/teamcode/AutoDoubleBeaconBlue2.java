package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoDoubleBeaconBlue_Beacon", group = "Concept")
public class AutoDoubleBeaconBlue2 extends LinearOpMode {
    double OneFoot = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();
        //robot.turnDegrees(45);
        //robot.turnDegrees(-45);
        robot.ballBooster1.setPower(1);
        robot.ballBooster2.setPower(1);
        robot.driveForward((OneFoot * 2.25) - 1);

        //shooting the balls

        sleep(2000);
        robot.triggered();
        sleep(500);
        robot.detriggered();
        sleep(2000);
        robot.triggered();
        sleep(500);
        robot.detriggered();

        //turns off shooter

        robot.ballBooster1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.ballBooster1.setPower(0);
        robot.ballBooster2.setPower(0);

        //Turns to right to drive directly towards beacon, without hitting the ball
        robot.turnDegrees(145);
        robot.driveForward(OneFoot * -1.00);

        //code for meet 1 pushing the ball and driving toward beacon
        // robot.driveForward(OneFoot * 1);
       // robot.turnDegrees(-90);
       // robot.driveForward(OneFoot * 2.25);

       // UNCOMMENT OUT THiS CODE LATER
       while(robot.leftLightSensor.getLightDetected() < .25 || robot.rightLightSensor.getLightDetected() < .25){
            robot.setDrivePower(.25);
        }
        robot.setDrivePower(0);
        robot.turnDegrees(-105);
        /*
        robot.driveForward(.5);
        telemetry.addData("value of driveReverse", OneFoot + " inches");
        telemetry.update();
        */
        robot.setDrivePower(0);

    }
}
