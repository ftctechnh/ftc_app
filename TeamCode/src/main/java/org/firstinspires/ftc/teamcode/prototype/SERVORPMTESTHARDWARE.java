package org.firstinspires.ftc.teamcode.prototype;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a ROUS robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left drive motor:         "left motor"
 * Motor channel:  Right drive motor:        "right motor"
 * Motor channel:  Intake motor:             "intake"
 * Servo channel:  Left side Servo:          "left servo"
 * Servo channel:  Right side Servo:         "right servo"
 * Note: the configuration of the servos is such that:
 *   As the Right side servo approaches 1, it moves towards the pressing position (facing away from the robot  |-->).
 *   As the Left side servo approaches 0, it moves towards the pressing position  (facing away from the robot  |-->).
 */
public class SERVORPMTESTHARDWARE
{
    /* Public OpMode members. */
    public Servo   servo      = null;
    public DcMotor leftshooter = null;
    public DcMotor rightshooter = null;
    public DcMotor Intake      = null;





    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public SERVORPMTESTHARDWARE(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors


        // Set all motors to zero power






        // Define and initialize ALL installed servos.
        servo = hwMap.servo.get("flip");
        leftshooter = hwMap.dcMotor.get("left shooter");
        rightshooter = hwMap.dcMotor.get("right shooter");
        Intake = hwMap.dcMotor.get("intake");

        // PressR = hwMap.servo.get("right servo");
        //Ex. Servo                  leftClaw = hwMap.servo.get("left_hand");
        //Ex. Servo position set     leftClaw.setPosition(MID_SERVO);

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

