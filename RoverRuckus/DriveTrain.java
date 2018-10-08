package org.firstinspires.ftc.teamcode.SeasonCode.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;




@TeleOp(name="DriveTrain", group="Linear Opmode")

public class DriveTrain extends LinearOpMode {


    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    public void runOpMode() {
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            double leftPower;
            double rightPower;


             leftPower  = -gamepad1.left_stick_y ;
             rightPower = -gamepad1.right_stick_y ;
             leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

        }
    }
}
