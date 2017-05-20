
package org.firstinspires.ftc.teamcode.oldbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/* This purpose of this program is to move the ball off the center platform, then drive up the ramp
 * on the blue side
 * the program does this by using encoders and the equation 1/2WS * 2pi * (TurnAngle/360)
 * this equation is used so that it is easier to calculate how far each wheel must turn inorder to
 * reach the desired turn angle of the robot without having to recalculate it by hand every time a different
 * turn angle is needed
 * the steps in this program are
  * Drive forward 45 inches
  * Turn 90deg to the right
  * Turn 90deg to the left
  * Drive backwards 24 inches
  * Turn 90deg to the right
  * Drive forward 36 inches
  * Turn 45deg to the right
  * Drive forward 26 inches
  * Stop on ramp
 */

@Autonomous(name="AutoRed(Ramp)", group="ROUS Robot")
@Disabled
public class ROUSAutoDriveByEncoder_PurpleRed extends LinearOpMode {

    /* Declare OpMode members. */
    ROUSAutoHardware_WithoutServos robot   = new ROUSAutoHardware_WithoutServos();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = .625 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = .5;   //dont set to .9 or above, wiggle room is needed
    //static final double     TURN_SPEED              = 1;
    //static final double     WheelSpace              = 17.25;
    //static final double     Pi                      = 3.1415;
    //static final double     Turn180Deg              = (WheelSpace/2) * (2*Pi) *((180/360));
    //static final double     Turn90Deg               = (WheelSpace/2) * (2*Pi) *((90/360)/2);
   // static final double     Turn45Deg               = (WheelSpace/2) * (2*Pi) *((45/360)/2);

    @Override
    public void runOpMode() throws InterruptedException {


        // wait for the start button to be pressed.
        waitForStart();

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {



            telemetry.update();
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
            robot.init(hardwareMap);

            // Send telemetry message to signify robot waiting;
            telemetry.addData("Status", "Resetting Encoders");    //
            telemetry.update();

            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            idle();

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Send telemetry message to indicate successful Encoder reset
            telemetry.addData("Path0", "Starting at %7d :%7d",
                    robot.leftMotor.getCurrentPosition(),
                    robot.rightMotor.getCurrentPosition());
            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED, 45, 45, 3);                // S1: Forward 45 Inches with 20 Sec timeout
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 17.37693437, -17.37693437, 20);  // S2: Turn 90deg to the right with 20 Sec timeout
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 60, 60, 5);
            stop();


        }
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();



                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
}
