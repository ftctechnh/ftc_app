package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.DriveEngine;

@TeleOp(name="holonomicDrive_5_0", group="Testing")
public class holonomicDrive_5_0 extends LinearOpMode
{
    DriveEngine engine;
    double x = 0;
    double y = 0;
    DcMotor lift = hardwareMap.dcMotor.get("lift");
    Servo pincherL = hardwareMap.servo.get("pincherL");
    Servo pincherR = hardwareMap.servo.get("pincherR");

    DcMotor extend = hardwareMap.dcMotor.get("extend");
    double open = 0.7;
    double close = 0.05;
    double pinch= 0;
    @Override
    public void runOpMode()
    {
        engine = new DriveEngine(hardwareMap);
        extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extend.setDirection(DcMotor.Direction.FORWARD);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();

        extend.setTargetPosition(1); //how far out the arm will go
        while (opModeIsActive())
        {

            if(gamepad1.right_stick_y != 0 ){
                engine.rotate(gamepad1.right_stick_y);
            }
            else {

                if(gamepad1.dpad_up)
                {
                    x = 0;
                    y = 1;
                }
                else if(gamepad1.dpad_down)
                {
                    x = 0;
                    y = -1;
                }
                else if(gamepad1.dpad_left)
                {
                    x = -1;
                    y = 0;
                }
                else if(gamepad1.dpad_right)
                {
                    x = 1;
                    y = 0;
                }
                //Scissor lift arm
                else if(gamepad1.left_bumper == true) {
                    lift.setPower(1);
                    x = 0;
                    y = 0;
                }
                else if(gamepad1.left_trigger > 0) {
                    lift.setPower(-1);
                    x = 0;
                    y = 0;
                }
                //Glyph Grabber
                else if(gamepad1.right_bumper == true) {
                    pinch += .05;
                    if (pinch > .5)
                    {pinch = .5;}
                    x = 0;
                    y = 0;
                }
                else if(gamepad1.right_trigger > 0) {
                    pinch -= .05;
                    if (pinch < 0)
                    {pinch = 0;}
                    x = 0;
                    y = 0;
                }
                else
                {
                    x = gamepad1.left_stick_x;
                    y = gamepad1.left_stick_y;
                }
                engine.drive(x,y);
                pincherL.setPosition(pinch);
                pincherR.setPosition(1-pinch);
            }

            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.update();
            idle();
        }

    }
}

