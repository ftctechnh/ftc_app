package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name="holonomicDrive_6_0", group="Testing")
public class holonomicDrive_6_0 extends LinearOpMode
{
    DriveEngine engine;
    double x = 0;
    double y = 0;
    double z = 0;

    DcMotor lift;
    Servo servo1;
    Servo servo2;

    TouchSensor touchBottom;
    TouchSensor touchTop;

    static final double MAX_POS     =  .8;     // Maximum rotational position
    static final double MIN_POS     =  .354;     // Minimum rotational position

    double position = 0;

    @Override
    public void runOpMode()
    {
        lift = hardwareMap.dcMotor.get("lift");
        servo2 = hardwareMap.servo.get("servo2");
        servo1 = hardwareMap.servo.get("servo1");
        touchBottom = hardwareMap.touchSensor.get("touchBottom");
        touchTop = hardwareMap.touchSensor.get("touchTop");
        engine = new DriveEngine(hardwareMap);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
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
                else if(gamepad1.x)
                {
                    position = MAX_POS;
                }
                else if(gamepad1.b)
                {
                    position = MIN_POS;
                }
                else
                {
                    x = gamepad1.left_stick_x;
                    y = gamepad1.left_stick_y;
                    z = 0;
                }

                engine.drive(x,y);
                lift.setPower(z);
                servo1.setPosition(position);
                servo2.setPosition(position);
            }

            // Display the current value
            telemetry.addData("Servos Positions 1", "%5.2f", position);
            telemetry.addData("servo2 Position: ", servo2.getPosition());
            telemetry.addData("servo1 Position: ", servo1.getPosition());
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

