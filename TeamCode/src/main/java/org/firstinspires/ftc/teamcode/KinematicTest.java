package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



import java.util.Timer;
import java.util.TimerTask;

/**
 * This file provides basic Kinematic Test
 * The code is structured as an Iterative OpMode
 * <p>
 * This class helps calculate your max velocity and max acceleration
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Kinematic Tester", group = "MPbot")
@Disabled
public class KinematicTest extends OpMode {

    final double timeInterval = 0.02; // Time in seconds
    /* Declare OpMode members. */
    RRVHardwarePushbot robot = new RRVHardwarePushbot(); // use the class created to define a bot's hardware
    java.util.Timer time = new Timer();

    double lastLeftVel = 0, lastRightVel = 0;
    int lastLeftPosition, lastRightPosition;
    private double maxVel = Double.MIN_VALUE, maxAccel = Double.MIN_VALUE;
    private double leftVel, rightVel, avgVel, leftAccel, rightAccel, avgAccel;
    private int currLeftPosition, currRightPosition;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        lastLeftPosition = robot.leftFront.getCurrentPosition();
        lastRightPosition = robot.rightFront.getCurrentPosition();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currLeftPosition = robot.leftFront.getCurrentPosition();
                currRightPosition = robot.rightFront.getCurrentPosition();

                leftVel = rotationToMeasurement(ticksToRotation(currLeftPosition - lastLeftPosition)) / timeInterval;
                rightVel = rotationToMeasurement(ticksToRotation(currRightPosition - lastRightPosition)) / timeInterval;
                avgVel = (leftVel + rightVel) / 2;

                leftAccel = (leftVel - lastLeftVel) / timeInterval;
                rightAccel = (rightVel - lastRightVel) / timeInterval;
                avgAccel = (leftAccel + rightAccel) / 2;

                if (avgVel > maxVel) {
                    maxVel = avgVel;
                }

                if (avgAccel > maxAccel) {
                    maxAccel = avgAccel;
                }

                lastLeftPosition = currLeftPosition;
                lastRightPosition = currRightPosition;

                lastLeftVel = leftVel;
                lastRightVel = rightVel;


            }
        }, 0, (long) (timeInterval * 1000));
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;
        double drive;
        double turn;
        double max;
        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        // This way it's also easy to just drive straight, or just turn.

        drive = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;

        // Combine drive and turn for blended motion.
        left = drive + turn;
        right = drive - turn;

        // Normalize the values so neither exceed +/- 1.0
        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }

        // Output the safe vales to the motor drives.
        robot.setLeftRight(left,right);

        // Send telemetry message to signify robot running
        telemetry.addData("left", "%.2f", left);
        telemetry.addData("right", "%.2f", right);

        telemetry.addData("Left Position", currLeftPosition);
        telemetry.addData("Right Position", currRightPosition);

        telemetry.addData("Left Velocity", leftVel);
        telemetry.addData("Right Velocity", rightVel);
        telemetry.addData("Average Velocity", avgVel);

        telemetry.addData("Left Acceleration", leftAccel);
        telemetry.addData("Right Acceleration", rightAccel);
        telemetry.addData("Average Acceleration", avgAccel);

        telemetry.addData("Max Velocity", maxVel);
        telemetry.addData("Max Acceleration", maxAccel);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        time.cancel(); 
    }

    //Measurement can be inches or whichever unit you measure the wheel diameter with
    private double rotationToMeasurement(double rotation) {
        return rotation * robot.wheel_diameter;
    }

    private double ticksToRotation(int ticks) {
        return (double) ticks / robot.ticks_per_revolution;
    }
}