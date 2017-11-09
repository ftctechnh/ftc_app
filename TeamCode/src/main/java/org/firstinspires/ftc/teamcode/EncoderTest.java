package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by Owner on 11/9/2017.
 */
@Autonomous (name = "Encoder test")
public class EncoderTest extends LinearOpMode {

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    public void runOpMode() {
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

//        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(10);
        rightDrive.setTargetPosition(10);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(1);
        rightDrive.setPower(1);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);

//        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
