package org.firstinspires.ftc.team8200;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by admin on 2/4/17.
 *
 * This was created to get comfortable with the encoders
 */
@Autonomous(name="8200: Encoder Test Mode", group="K9bot")

public class EncoderTestMode extends LinearOpMode{




    /**
     * Created by student on 1/12/17.
     *
     * The goal of this opmode is to shoot two particles in the center vortex, drive to
     * Red Alliance Beacon, press the correct beacon color, turn, go to the next beacon, turn
     * again, and press that beacon. Finally, the robot will turn toward the center vortex and park on the center vortex,
     * knocking away the capball in the process.
     */




        HardwareK9bot robot = new HardwareK9bot(); // Hardware Device Object
        private ElapsedTime runtime = new ElapsedTime();

        //static variables (to avoid Magic Number issues)
        static final double DRIVE_SPEED = 0.5;
        static final double DRIVE_SLOW_SPEED = 0.1;
        static final double TURN_SPEED = 0.25;

        static final String allianceColor = "blue"; // takes either value "red" or "blue"

        //static variables for encoders
        static final double COUNTS_PER_MOTOR_REV = 1040;    // according to NeveRest 40 spec sheet
        static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP, pretty sure direct drive is 1.0?
        static final double WHEEL_DIAMETER_INCHES = 2.8;     // For figuring circumference, needs to be updated
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);




        @Override

        public void runOpMode() {

            robot.init(hardwareMap); // Do not erase to avoid NullPointerException. This MUST be first in runOpMode()
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            waitForStart(); //pre-written function, waits for opmode to start
            stopResetEncoders();
            encoderDrive(DRIVE_SPEED, 6, 6, 5000);
            stopResetEncoders();
            encoderDrive(DRIVE_SPEED, 12, 12, 10000);
            stopResetEncoders();
            encoderDrive(DRIVE_SLOW_SPEED, -12, -12, 10000);
            stopResetEncoders();
            encoderDrive(DRIVE_SLOW_SPEED, -6, -6, 10000);



        }



    /* stopResetEncoders() is a utility function that kills all motor activity and resets the encoder position to zero.

     */

        public void stopResetEncoders() {
            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            sleep(50); //small wait here to make sure it truly resets

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }

        /* encoderDrive() is a function that allows the robot to move according to the number of encoder ticks
            the speed should be one of the static variables defined above
            leftInches and rightInches is the distance in actual inches; however, the wheel diameter and gearing
            ratio must be accurate in order to ensure this works properly
            timeoutS is the failsafe for if the encoders are not working properly; they should not be used as a
            replacement for our typical motor timing algorithm which uses the ElapsedTime class)

          */
        public void encoderDrive(double speed,
                                 double leftInches, double rightInches,
                                 double timeoutS) {
            int newLeftTarget;
            int newRightTarget;

            // Ensure that the opmode is still active
            if (opModeIsActive()) {

                // Determine new target position, and pass to motor controller
                newLeftTarget = robot.leftMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newRightTarget = robot.rightMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
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
                    telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                    telemetry.addData("Path2", "Running at %7d :%7d",
                            robot.leftMotor.getCurrentPosition(),
                            robot.rightMotor.getCurrentPosition());
                    telemetry.update();
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


