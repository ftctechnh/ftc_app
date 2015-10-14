package org.swerverobotics.library.internal.tests;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by davegoldy on 9/1/15.
 * This program will implement an autonomous program to drive the robot forward for 1 second,
 * turn left 90 degrees and drive forward for 1 second. It will repeat these steps until the
 * robot has completed a square.
 */
public class DaveGoldyAutonomous extends OpMode {

    // TETRIX VALUES.
    protected DcMotor motorRight;
    protected DcMotor motorLeft;

    //Timing values in milliseconds
    protected double runningTime;
    protected long startTime;
    protected final long driveTime = 1500;
    protected final long turnTime = 700;
    protected final double drivePower = 1.0;
    protected final int delta = 50;

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

        motorRight = hardwareMap.dcMotor.get("rightMotor");
        motorLeft = hardwareMap.dcMotor.get("leftMotor");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
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
        runningTime = this.getRuntime();
        if (this.getRuntime() <= driveTime) {
            //Drive straight forward
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (driveTime + delta) && runningTime <= (driveTime + turnTime)) {
            //Turn left
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (driveTime + turnTime + delta) && runningTime <= (2 * driveTime + turnTime)) {
            //Drive straight forward
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (2 * driveTime + turnTime + delta) && runningTime <= (2 * driveTime + 2 * turnTime)) {
            //Turn Left
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (2 * driveTime + 2 * turnTime + delta) && runningTime <= (3 * driveTime + 2 * turnTime)) {
            //Drive straight forward
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (3 * driveTime + 2 * turnTime + delta) && runningTime <= (3 * driveTime + 3 * turnTime)) {
            //Turn Left
            left = -drivePower;
            right = drivePower;
        } else if (runningTime > (3 * driveTime + 3 * turnTime + delta) && runningTime <= (4 * driveTime + 3 * turnTime)) {
            //Drive straight forward
            left = drivePower;
            right = drivePower;
        } else if (runningTime > (4 * driveTime + 3 * turnTime + delta) && runningTime <= (4 * driveTime + 4 * turnTime)) {
            //Turn Left
            left = -drivePower;
            right = drivePower;
        } else {
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
