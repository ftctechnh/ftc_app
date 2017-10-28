package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jodasue on 10/28/17.
 */
@TeleOp(name = "fauxbot", group = "Testing")
public class fauxbot extends LinearOpMode {

    DcMotor left;
    DcMotor right;
    Servo glyph;

    public void runOpMode() {
        Servo glyph = hardwareMap.servo.get("glyph");

        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        double temp = 0;

        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.dpad_up)
            {
                temp+=.01;
            }
            if(gamepad1.dpad_down)
            {
                temp-=.01;
            }
            if(temp < 0)
            {
                temp = 0;
            }
            if(temp > 1)
            {
                temp = 1;
            }
            glyph.setPosition(temp);

            left.setPower(gamepad1.left_stick_y);
            right.setPower(gamepad1.right_stick_y);

            telemetry.addData("temp: ", temp);
            telemetry.update();
            idle();
        }
    }
}
