package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by andrew on 10/27/17.
 */

public class AutonomousEncoder extends LinearOpMode{

    DcMotor motorRight = null;
    DcMotor motorLeft = null;
    HardwareRobot robot = new HardwareRobot();
    HardwareMap hwMap = null;
    //hello!

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hwMap);


    }

    public void drive(int leftPower, int rightPower, int inches) throws InterruptedException {

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftDrive.setTargetPosition((int)(robot.COUNTS_PER_INCH) * inches);
        robot.rightDrive.setTargetPosition((int)(robot.COUNTS_PER_INCH) * inches);
        robot.change();
        robot.leftDrive.setPower(leftPower);
        robot.rightDrive.setPower(rightPower);

    }

}
