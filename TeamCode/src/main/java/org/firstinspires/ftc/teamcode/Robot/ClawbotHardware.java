package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */

public class ClawbotHardware {

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.30 ;
    public static final double ARM_DOWN_POWER  = -0.15 ;
    public static final double MIN_SAFE_CLAW_OFFSET = -0.5;
    public static final double MAX_SAFE_CLAW_OFFSET = 0.14;

    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor armMotor = null;
    public Servo claw = null;

    private ElapsedTime period  = new ElapsedTime();
    private HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("left_drive");
        rightMotor = hwMap.dcMotor.get("right_drive");
        armMotor = hwMap.dcMotor.get("left_arm");
        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        armMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        claw = hwMap.servo.get("left_hand");
    }

    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();

}

}
