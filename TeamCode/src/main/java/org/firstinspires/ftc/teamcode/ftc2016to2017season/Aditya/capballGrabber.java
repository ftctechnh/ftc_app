package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Aditya, inspirationteam on 11/20/2016.
 */

@TeleOp(name = "Capball", group = "Robot")
@Disabled

public class capballGrabber extends OpMode {


    DcMotor slideLifter;
    Servo servo_left;
    Servo servo_right;

    double initialPos = slideLifter.getCurrentPosition();
    double initialRightServoPos = servo_right.getPosition();
    double initialLeftServoPos = servo_left.getPosition();
    // Setting up the servo and motor names and positions.

    final Boolean gamepadA = (gamepad2.a);

    @Override
    public void init()  {
        slideLifter = hardwareMap.dcMotor.get("slideLifter");
        slideLifter.setDirection(DcMotor.Direction.REVERSE );

        servo_left = hardwareMap.servo.get("servo_left");
        servo_right = hardwareMap.servo.get("servo_right");
        servo_left.setDirection(Servo.Direction.REVERSE);
        servo_right.setDirection(Servo.Direction.FORWARD);

    }

    @Override
    public void init_loop()  {

    }

    @Override
    public void start()     {

    }

    @Override
    public void loop()  {
        servoGrab(0.7);
        /*while (buttonA = false) {slideLift();}
        servoGrab(0.3);*/
        }

    public void slideLift() {
        double power = gamepad2.left_stick_y;

                slideLifter.setPower(power);
                telemetry.addData("Current Motor Pos:", slideLifter.getCurrentPosition());
                telemetry.update();
    }

    public void servoGrab(double move_pos) {

        servo_right.setPosition(move_pos);
        servo_left.setPosition(move_pos);
    }

    public void servoClose() {
        servo_right.setPosition(initialRightServoPos);
        servo_left.setPosition(initialLeftServoPos);
    }

    @Override
    public void stop() {
        servoClose();
        while(slideLifter.getCurrentPosition()> initialPos)
        {
            slideLifter.setPower(-0.7);
        }
    }
}
