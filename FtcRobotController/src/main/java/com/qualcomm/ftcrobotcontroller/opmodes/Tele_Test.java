package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by dimpledhawan on 2/13/16.
 */
public class Tele_Test extends OpMode
{
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor spinners;
    private DcMotor slides;
    //private DcMotor slideLeft;
    private DcMotor slideRight;

    private Servo belt;
    private Servo doors;
    private Servo hook;
    private Servo climber;

    @Override
    public void init()
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        spinners = hardwareMap.dcMotor.get("spinners");
        slides = hardwareMap.dcMotor.get("slideLeft");
        //slideLeft = hardwareMap.dcMotor.get("slideLeft");
        slideRight = hardwareMap.dcMotor.get("slideRight");

        slideRight.setDirection(DcMotor.Direction.REVERSE);

        belt = hardwareMap.servo.get("belt");
        doors = hardwareMap.servo.get("doors");
        hook = hardwareMap.servo.get("hook");
        climber = hardwareMap.servo.get("climber");

        //belt.setPosition(0.5);
        doors.setPosition(0.5);
        hook.setPosition(0.5);
        climber.setPosition(.42);
    }

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init_loop()
    {

    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */

    @Override
    public void loop()
    {
        if (Math.abs(gamepad1.left_stick_y) > 0.05)
            motorLeft.setPower(-gamepad1.left_stick_y);
        else
            motorLeft.setPower(0);

        if (Math.abs(gamepad1.right_stick_y) > 0.05)
            motorRight.setPower(gamepad1.right_stick_y);
        else
            motorRight.setPower(0);

        if (Math.abs(gamepad2.left_stick_y) > 0.25)
        {
            slides.setPower(-gamepad2.left_stick_y);
            //slideLeft.setPower(-gamepad2.left_stick_y);
            slideRight.setPower(-gamepad2.left_stick_y);
        }
        else
        {
            slides.setPower(0);
            //slideLeft.setPower(0);
            slideRight.setPower(0);
        }

        if (gamepad2.a)
            spinners.setPower(-1); //reaching
        else if (gamepad2.b)
            spinners.setPower(1); //reaching
        else if (gamepad2.y)
            spinners.setPower(0); //Y not working. Reaching? YES!

        if (Math.abs(gamepad1.left_trigger) > 0.05)
            hook.setPosition(.9);
        else
            hook.setPosition(.5);

        if (Math.abs(gamepad1.right_trigger) > 0.05)
            hook.setPosition(1);
        else
            hook.setPosition(.5);

        if(gamepad2.left_bumper)
            doors.setPosition(.9);
        else
            doors.setPosition(.5);

        if(gamepad2.right_bumper)
            doors.setPosition(.1);
        else
            doors.setPosition(.5);

        belt.setPosition((gamepad2.right_stick_x + 1.0) / 2.0); //gamepad1 buttons work exactly same on gamepad2

        //belt.setPosition(1 - (gamepad2.right_stick_x + 1.0) *.5);
        //doors.setPosition((gamepad2.left_stick_x / 2.0) + 0.5); //doors = hook
        //doors.setPosition((gamepad2.right_stick_y / 2.0) + 0.5);

    }
}
