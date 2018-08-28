package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Drive Now", group="Basic OP Mode")
@Disabled

public class DriveNow extends LinearOpMode {

    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;

    @Override
    public void runOpMode(){

        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();

        motor1 = hardwareMap.get(DcMotor.class,                "motor1");
        motor2 = hardwareMap.get(DcMotor.class,                "motor2");
        motor3 = hardwareMap.get(DcMotor.class,                "motor3");
        motor4 = hardwareMap.get(DcMotor.class,                "motor4");

        waitForStart();

        while (opModeIsActive()){

            double leftPower;
            double rightPower;

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_y;

            leftPower = Range.clip(     drive + turn,   -0.5,   0.5);
            rightPower = Range.clip(    drive - turn,   -0.5, 0.5);
            if (gamepad1.dpad_right){
                motor1.setPower(1);
            }
            if (gamepad1.dpad_up){
                motor2.setPower(1);
            }
            if (gamepad1.dpad_left){
                motor3.setPower(1);
            }
            if (gamepad1.dpad_down){
                motor4.setPower(1);
            }

            if (gamepad1.right_bumper){
                motor1.setPower(1);
                motor3.setPower(1);
            }
            if (gamepad1.left_bumper){
                motor2.setPower(1);
                motor4.setPower(1);
            }


            }


            }
        }


