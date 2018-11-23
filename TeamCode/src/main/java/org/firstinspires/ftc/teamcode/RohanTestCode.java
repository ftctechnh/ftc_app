package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "RohanTestCode", group = "Rohan")
public class RohanTestCode extends LinearOpMode {
    HardwareBruinBot hwMap = new HardwareBruinBot();
public void runOpMode () {
    hwMap.init(hardwareMap);
    move(0.5,0,0);
    sleep(1000);
    stopBot();
    move(-0.5,0,0);
    sleep(1000);
    stopBot();
    move(-0.5,0,0);
    sleep(1000);
    stopBot();
    move(0.5,0,0);
    sleep(1000);
    stopBot();
    move(0,0,0.5);
    sleep(1000);
    stopBot();
    move(0,0,-0.5);
    sleep(1000);
    stopBot();
    move(0,0,-0.5);
    sleep(1000);
    stopBot();
    move(0,0,0.5);
    sleep(1000);
    stopBot();
    }
    public void move(double drive, double rotate, double strafe)
    {
        // This module takes inputs, normalizes them to 1, and drives the motors
        float maxDrive;
        float frontMax;
        float rearMax;

        // In addition to normalize, cube the values so that small inputs allow fine control
        drive = Math.pow(drive, 3);
        strafe = Math.pow(strafe, 3);
        rotate = Math.pow(rotate, 3);

        // Find the maximum value of the inputs and normalize
        frontMax = Math.max(Math.abs((float)drive + (float)strafe + (float)rotate), Math.abs((float)drive - (float)strafe - (float)rotate));
        rearMax = Math.max(Math.abs((float)drive - (float)strafe + (float)rotate), Math.abs((float)drive + (float)strafe - (float)rotate));
        maxDrive = Math.max(frontMax, rearMax);
        maxDrive = Math.max(maxDrive,1);
        drive = drive/maxDrive;
        strafe = strafe/maxDrive;
        rotate = rotate/maxDrive;

        hwMap.leftFrontDrive.setPower(drive + strafe + rotate);
        hwMap.leftRearDrive.setPower(drive - strafe + rotate);
        hwMap.rightFrontDrive.setPower(drive - strafe - rotate);
        hwMap.rightRearDrive.setPower(drive + strafe - rotate);

    }
    public void stopBot()
    {
        // This function stops the robot
        hwMap.leftFrontDrive.setPower(0);
        hwMap.leftRearDrive.setPower(0);
        hwMap.rightFrontDrive.setPower(0);
        hwMap.rightRearDrive.setPower(0);
    }
}
