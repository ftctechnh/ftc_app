package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left motor"
 * Motor channel:  Right drive motor:        "right motor"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class HardwareDM
{
    /* Public OpMode members. */

    /* Drive train motors */
    public DcMotor  lfDrive = null;
    public DcMotor  lrDrive = null;
    public DcMotor  rfDrive = null;
    public DcMotor  rrDrive = null;

    /* Shooter motors */
    public DcMotor lShoot = null;
    public DcMotor rShoot = null;

    /* Shooter feed cam */
    public CRServo fire = null;

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareDM() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        lfDrive   = hwMap.dcMotor.get("lf motor");
        lrDrive   = hwMap.dcMotor.get("lr motor");
        rfDrive   = hwMap.dcMotor.get("rf motor");
        rrDrive   = hwMap.dcMotor.get("rr motor");
        lShoot    = hwMap.dcMotor.get("l shoot");
        rShoot    = hwMap.dcMotor.get("r shoot");

        // Define and initialize servos
        fire = hwMap.crservo.get("fire");


        // Set all motors to zero power
        lfDrive.setPower(0.0);
        lrDrive.setPower(0.0);
        rfDrive.setPower(0.0);
        rrDrive.setPower(0.0);
        lShoot.setPower(0.0);
        rShoot.setPower(0.0);
        fire.setPower(0.0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        lfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set drive train motor directions
        lfDrive.setDirection(DcMotor.Direction.REVERSE);
        lrDrive.setDirection(DcMotor.Direction.REVERSE);
        rfDrive.setDirection(DcMotor.Direction.FORWARD);
        rrDrive.setDirection(DcMotor.Direction.FORWARD);
        lShoot.setDirection(DcMotor.Direction.FORWARD);
        rShoot.setDirection(DcMotor.Direction.REVERSE);

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
    public void waitForTick(long periodMs)  throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
