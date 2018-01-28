package org.firstinspires.ftc.teamcode;

/**
 * Created by andrew on 1/12/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
@Autonomous(name = "AutoMove")
public class AutoMove extends LinearOpMode {
    private DcMotor leftDriveFront = null;
    private DcMotor rightDriveFront = null;
    public DcMotor leftDriveBack = null;
    public DcMotor rightDriveBack = null;
    public CRServo cvn1 = null;
    public CRServo cvn2 = null;
    public DcMotor elev1 = null;
    public DcMotor elev2 = null;

    public void runOpMode() throws InterruptedException {
        //telemetry.addData("Status", "Initialized");
        //telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftDriveFront = hardwareMap.get(DcMotor.class, "m1");
        rightDriveFront = hardwareMap.get(DcMotor.class, "m2");
        leftDriveBack = hardwareMap.get(DcMotor.class, "m3");
        rightDriveBack = hardwareMap.get(DcMotor.class, "m4");
        //cvn1 = hardwareMap.get(CRServo.class, "s1");
        //cvn2 = hardwareMap.get(CRServo.class, "s2");
        //elev1 = hardwareMap.get(DcMotor.class, "e1");
        //elev2 = hardwareMap.get(DcMotor.class, "e2");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDriveFront.setDirection(DcMotor.Direction.FORWARD);
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE);
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.REVERSE);
        //cvn1.setDirection(CRServo.Direction.REVERSE);
        //elev1.setDirection(DcMotor.Direction.REVERSE);

        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        leftDriveBack.setPower(0);
        rightDriveFront.setPower(0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        leftDriveBack.setPower(.6);
        rightDriveBack.setPower(.6);
        leftDriveFront.setPower(.6);
        rightDriveFront.setPower(.6);
        Thread.sleep(5000);

        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        leftDriveBack.setPower(0);
        rightDriveFront.setPower(0);
        Thread.sleep(5000);

        leftDriveBack.setPower(-.6);
        rightDriveBack.setPower(-.6);
        leftDriveFront.setPower(.6);
        rightDriveFront.setPower(.6);
        Thread.sleep(5000);

        leftDriveBack.setPower(.6);
        rightDriveBack.setPower(.6);
        leftDriveFront.setPower(-.6);
        rightDriveFront.setPower(-.6);
        Thread.sleep(5000);

        leftDriveBack.setPower(-.6);
        rightDriveBack.setPower(-.6);
        leftDriveFront.setPower(-.6);
        rightDriveFront.setPower(-.6);
        Thread.sleep(5000);
    }
    public void init(HardwareMap ahwMap)
        {

            leftDriveFront.setPower(0);
            rightDriveBack.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveFront.setPower(0);
        }


}

