package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareDRive
{
    /* Public OpMode members. */
    public DcMotor FLMotor   = null;
    public DcMotor FRMotor  = null;
    public DcMotor BLMotor  = null;
    public DcMotor BRMotor  = null;
    public DcMotor SideMotor   = null;
    public DcMotor GreenMotor   = null;
    public Servo armleft = null;
    public Servo clawleft = null;
    public Servo clawright = null;
    public DcMotor arm = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareDRive(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FLMotor   = hwMap.dcMotor.get("FL");
        FRMotor  = hwMap.dcMotor.get("FR");
        BLMotor    = hwMap.dcMotor.get("BL");
        BRMotor   = hwMap.dcMotor.get("BR");
        arm = hwMap.dcMotor.get("arm");
        SideMotor = hwMap.dcMotor.get("sidedrive");
        armleft  = hwMap.get(Servo.class, "armleft");
        clawleft  = hwMap.get(Servo.class, "clawleft");
        clawright  = hwMap.get(Servo.class, "clawright");

        //BlueMotor   = hwMap.dcMotor.get("Blue");
        //GreenMotor   = hwMap.dcMotor.get("Green");
        FLMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        BLMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);
        arm.setPower(0);
        SideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //GreenMotor.setPower(0);
        //BlueMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //GreenMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //BlueMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
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

