package org.swerverobotics.library.internal.tests;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.swerverobotics.library.interfaces.*;

/**
 * Created by davegoldy on 9/1/15.
 * This program will implement an autonomous program to drive the robot forward for 1 second,
 * turn left 90 degrees and drive forward for 1 second. It will repeat these steps until the
 * robot has completed a square.
 */
@Autonomous(name = "Dave Goldy")
@Disabled
public class DaveGoldyAutonomous extends OpMode {

    public static String LOGGING_TAG = "Swerve: DaveGoldy";

    // TETRIX VALUES.
    protected DcMotor motorRight;
    protected DcMotor motorLeft;

    //Timing values in milliseconds
    protected double runningTime;
    protected long startTime;
    protected final long driveTime = 1500;
    protected final long turnTime = 700;
    protected final double drivePower = 1.0;
    protected final int delta = 0;

    /**
     * Constructor
     */
    public DaveGoldyAutonomous() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override public void start() {
        startTime = System.currentTimeMillis();
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        double left = 0.0;
        double right = 0.0;
        runningTime = System.currentTimeMillis() - startTime;

        if (runningTime <= driveTime) {
            log("forward 1");
            //Drive straight forward
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (driveTime + delta) && runningTime <= (driveTime + turnTime)) {
            //Turn left
            log("left 1");
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (driveTime + turnTime + delta) && runningTime <= (2 * driveTime + turnTime)) {
            //Drive straight forward
            log("forward 2");
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (2 * driveTime + turnTime + delta) && runningTime <= (2 * driveTime + 2 * turnTime)) {
            //Turn Left
            log("left 2");
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (2 * driveTime + 2 * turnTime + delta) && runningTime <= (3 * driveTime + 2 * turnTime)) {
            //Drive straight forward
            log("forward 3");
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (3 * driveTime + 2 * turnTime + delta) && runningTime <= (3 * driveTime + 3 * turnTime)) {
            //Turn Left
            log("left 3");
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (3 * driveTime + 3 * turnTime + delta) && runningTime <= (4 * driveTime + 3 * turnTime)) {
            //Drive straight forward
            log("forward 4");
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (4 * driveTime + 3 * turnTime + delta) && runningTime <= (4 * driveTime + 4 * turnTime)) {
            //Turn Left
            log("left 4");
            left = -drivePower;
            right = drivePower;
        } else {
            log("stop");
            //Stop
            left = 0.0;
            right = 0.0;
        }

        // write the values to the motors
        motorLeft.setPower(left);
        motorRight.setPower(right);
        //System.out.println("Running Time: " + runningTime + " Left = " + left + " Right = " + right);

    //telemetry.addData("Run Time","run time: "+String.format("%4d",runningTime));
    //telemetry.addData("left tgt pwr","left  pwr: "+String.format("%.2f",left));
    //telemetry.addData("right tgt pwr","right pwr: "+String.format("%.2f",right));
    }

    void log(String message) {
        // Log.d(LOGGING_TAG, String.format("%f: %s", this.runningTime, message));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }
}
