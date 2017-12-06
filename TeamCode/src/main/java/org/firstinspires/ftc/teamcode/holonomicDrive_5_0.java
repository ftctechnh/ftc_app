package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorDigitalTouch;
import org.firstinspires.ftc.teamcode.DriveEngine;
@Disabled
@TeleOp(name="holonomicDrive_5_0", group="Testing")
public class holonomicDrive_5_0 extends LinearOpMode
{
    DriveEngine engine;
    double x = 0;
    double y = 0;
    double z = 0;

    DcMotor lift;
//    Servo pincherR;
    Servo pincherL;

    TouchSensor touchBottom;
    TouchSensor touchTop;

    double pinch = 0.2;

    private void open()
    {
        pincherL.setPosition(0);
//        pincherR.setPosition(1);
    }

    private void close()
    {
        pincherL.setPosition(1);
//        pincherR.setPosition(0);
    }

    @Override
    public void runOpMode()
    {
        lift = hardwareMap.dcMotor.get("lift");
        pincherL = hardwareMap.servo.get("pincherL");
        //pincherR = hardwareMap.servo.get("pincherR");
        touchBottom = hardwareMap.touchSensor.get("touchBottom");
        touchTop = hardwareMap.touchSensor.get("touchTop");
        engine = new DriveEngine(hardwareMap);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        open();
        while (opModeIsActive())
        {

            if(gamepad1.right_stick_y != 0 ){
                engine.rotate(gamepad1.right_stick_y);
            }
            else {

                if(gamepad1.dpad_up)
                {
                    x = 0;
                    y = .5;
                    z = 0;
                }
                else if(gamepad1.dpad_down)
                {
                    x = 0;
                    y = -.5;
                    z = 0;

                }
                else if(gamepad1.dpad_left)
                {
                    x = .5;
                    y = 0;
                    z = 0;

                }
                else if(gamepad1.dpad_right)
                {
                    x = -.5;
                    y = 0;
                    z = 0;

                }
                //Scissor lift arm
                else if(gamepad1.a) {
                    if(!touchTop.isPressed()){
                        z = 1;
                    }
                    else{
                        z = 0;
                    }

                    x = 0;
                    y = 0;
                }
                else if(gamepad1.y) {
                    if(!touchBottom.isPressed()){
                        z = -1;
                    }
                    else{
                        z =0;
                    }
                    x = 0;
                    y = 0;
                }
                //Glyph Grabber
                else if(gamepad1.right_bumper) {
                    pinch += .01;
                    if (pinch > 1) {
                        pinch = 1;
                    }
//                pincherL.setPosition(pinch);
//                pincherR.setPosition(pinch);
                }
                else if(gamepad1.right_trigger > 0) {
                    pinch -= .1;
                    if (pinch < 0){
                        pinch = 0;
                    }

//                pincherR.setPosition(pinch);
                }
                else if(gamepad1.x)
                {
                    close();
                }
                else
                {
                    x = gamepad1.left_stick_x;
                    y = gamepad1.left_stick_y;
                    z = 0;
                }
                pincherL.setPosition(pinch);
//                pincherR.setPosition(1);
                engine.drive(x,y);
                lift.setPower(z);
            }

            telemetry.addData("pinch: ", pinch);
            telemetry.addData("pincherL Position: ", pincherL.getPosition());
//            telemetry.addData("pincherR Position: ", pincherR.getPosition());
            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.update();
            idle();
        }
        close();
    }
}

