package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ButtonPusher
{
    private ElapsedTime timer = null;

    DcMotor frontPusher = null;
    Gamepad gamepad = null;
    Sensors sensors = null;

    private DcMotor.Direction frontDirection = DcMotor.Direction.REVERSE;

    private double frontPower = 0;  //[0.0, 1.0]

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCH   = 1.25 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH        = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCH * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    public ButtonPusher(HardwareMap hardwareMap, Gamepad gamepad, Sensors sensors)
    {
        this.gamepad = gamepad;

        this.sensors = sensors;

        frontPusher  = hardwareMap.dcMotor.get("front_push");

        frontPusher.setPower(frontPower);

        frontPusher.setDirection(frontDirection);
        timer = new ElapsedTime();
    }

    public Double getFrontPower()
    {
        return frontPower;
    }

    public void invertDirection()
    {
        if (frontDirection == DcMotor.Direction.FORWARD)
            frontDirection = DcMotor.Direction.REVERSE;
        else
            frontDirection = DcMotor.Direction.FORWARD;
    }

    public void setPower(double fPower) {
        frontPower = fPower;

        frontPusher.setPower(frontPower);
    }

    public void stop()
    {
        frontPower = 0;
        frontPusher.setPower(frontPower);
    }

    public void encoderDrive(double speed,
                             double inches) {
        int newTarget;
        frontPusher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Determine new target position, and pass to motor controller
        newTarget = frontPusher.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        frontPusher.setTargetPosition(newTarget);

        // Turn On RUN_TO_POSITION
        frontPusher.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // start motion.
        frontPusher.setPower(Math.abs(speed));


        // Stop all motion;
        frontPusher.setPower(0);

        // Turn off RUN_TO_POSITION
        frontPusher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }
    
}
