package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by shrey on 2017-11-05.
 */

public class DriveMethods extends HardwarePushBot{

        public static void driveRight(double speed){
            rightFrontDrive.setPower(speed);
            rightBackDrive.setPower(speed);
        }

        public static void driveLeft(double speed){
            leftFrontDrive.setPower(speed);
            leftBackDrive.setPower(speed);
        }

    /*
     * This method is used to stop the drive motors in order for the robot to stop when in between
     * the two thresholds. It's important to do this so the robot will stop, safely
     */
        public static void stopRightMotors() { //always zero
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);
        }

        public static void stopLeftMotors(){
            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
        }
}
