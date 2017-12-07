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
//    Servo servo1;
//    Servo servo2;

    TouchSensor touchBottom;
    TouchSensor touchTop;

    double pinch = 0.2;
    //linear arm
    static final double INCREMENT   = 0.1;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.2;     // Minimum rotational position
    double position1 = 0;
    double position2 = 0;
    Servo servo1;
    Servo servo2;

    private void open()
    {
        servo2.setPosition(1);
        servo1.setPosition(1);
    }

    private void close()
    {
        servo2.setPosition(0.2);
        servo1.setPosition(0.2);
    }

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
//                // Old Glyph Grabber- outdated, delete soon
//                else if(gamepad1.right_bumper) {
//                    pinch += .01;
//                    if (pinch > 1) {
//                        pinch = 1;
//                    }
//                servo2.setPosition(pinch);
//                servo1.setPosition(pinch);
//                }
//                else if(gamepad1.right_trigger > 0) {
//                    pinch -= .1;
//                    if (pinch < 0){
//                        pinch = 0;
//                    }
//
//                servo1.setPosition(pinch);
//                }
//                else if(gamepad1.x)
//                {
//                    close();
//                }
                else if  (gamepad1.x && position1 < MAX_POS)
                {
                    position1 = MAX_POS;
                }
                else if(gamepad1.b && position1 > 0)
                {
                    position1 = MIN_POS;
                }
                else if (gamepad2.dpad_up && position2 < MAX_POS)
                {
                    position2 = MAX_POS;
                }
                else if(gamepad2.dpad_down && position2 > 0)
                {
                    position2 = MIN_POS;
                }
                else
                {
                    x = gamepad1.left_stick_x;
                    y = gamepad1.left_stick_y;
                    z = 0;
                }

                engine.drive(x,y);
                lift.setPower(z);
                servo1.setPosition(position1);
                servo2.setPosition(position2);
            }

            // Display the current value
            telemetry.addData("Servo Position 1", "%5.2f", position1);
            telemetry.addData("Servo Position 2", "%5.2f", position2);

            telemetry.addData("pinch: ", pinch);
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
        close();
    }
}

