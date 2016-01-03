package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class TeleTest extends OpMode {
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor spinners;
    private DcMotor slides;

    private Servo belt;
    private Servo doors;

    @Override
    public void init() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("slideRight");
        spinners = hardwareMap.dcMotor.get("spinners");
        slides = hardwareMap.dcMotor.get("slideLeft");

        belt = hardwareMap.servo.get("belt");
        doors = hardwareMap.servo.get("doors");
        belt.setPosition(0.5);
        doors.setPosition(0.5);
    }

    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
       */
    @Override
    public void init_loop() {
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        if (Math.abs(gamepad1.left_stick_y) > 0.05) {
            motorLeft.setPower(-gamepad1.left_stick_y);
        } else
            motorLeft.setPower(0);
        if (Math.abs(gamepad1.right_stick_y) > 0.05)
            motorRight.setPower(gamepad1.right_stick_y);
        else
            motorRight.setPower(0);

        if (Math.abs(gamepad2.left_stick_y) > 0.25)
            slides.setPower(-gamepad2.left_stick_y);
        else
            slides.setPower(0);

        if (gamepad2.a) {
            spinners.setPower(-1);
        } else if (gamepad2.b)
            spinners.setPower(0);
        belt.setPosition(1-(gamepad2.right_stick_x+1.0)/2.0);
        doors.setPosition((gamepad2.left_stick_x/2.0)+0.5);
    }
}
