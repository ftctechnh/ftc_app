/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.concurrent.TimeUnit;

import static com.qualcomm.robotcore.hardware.DistanceSensor.distanceOutOfRange;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 * <p>
 * The code REQUIRES that you DO have encoders on the wheels,
 * otherwise you would use: PushbotAutoDriveByTime;
 * <p>
 * This code ALSO requires that the drive Motors have been configured such that a positive
 * power command moves them forwards, and causes the encoders to count UP.
 * <p>
 * The desired path in this example is:
 * - Drive forward for 48 inches
 * - Spin right for 12 Inches
 * - Drive Backwards for 24 inches
 * - Stop and close the claw.
 * <p>
 * The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 * that performs the actual movement.
 * This methods assumes that each movement is relative to the last stopping place.
 * There are other ways to perform encoder based moves, but this method is probably the simplest.
 * This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

//@TeleOp(name = "Alhambra (Side A)", group = "Linear Opmode")
//@Disabled
public abstract class Autonomous_Alhambra extends LinearOpMode {
    static final double DRIVE_SPEED = 1d;
    private static final double COUNTS_PER_MOTOR_REV = 288d;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 26d / 15d;     // This is < 1.0 if geared UP, eg. 26d/10d
    private static final double WHEEL_DIAMETER_INCHES = 3.54331d;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159265359d);
    private static final double HEADING_THRESHOLD = 1d;      // As tight as we can make it with an integer gyro
    private static final double P_TURN_COEFF = 0.09d;     // Larger is more responsive, but also less stable
    private static final double P_DRIVE_COEFF = 0.16d;     // Larger is more responsive, but also less stable
    private static final double DISTANCE_THRESHOLD = 10d; // 10cm

    private final double SERVO_CYCLE = 50d;
    private final double SERVO_INCREMENT_MIN = 0.005d;
    private final double SERVO_INCREMENT_MAX = 0.01d;
    // Declare OpMode members.
    protected ElapsedTime runtime = new ElapsedTime();
    protected HardwareAlhambra robot = new HardwareAlhambra();
    private ElapsedTime armTime = new ElapsedTime();
    private ElapsedTime handTime = new ElapsedTime();
    private ElapsedTime driveTime = new ElapsedTime();
    protected double lastRuntime = 0d;
    private boolean wasBeep = false, doorFlag = false, handFlag = false;
    private boolean aPressed = false, yPressed = false;
    private int armSequence = 0;

    protected boolean TimeOK() {
        double elapsed = runtime.time(TimeUnit.SECONDS);
        return (elapsed <= 120d);
    }

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        resetMotors();

        while (!robot.imu.isGyroCalibrated()) {
            idle();
        }

        Telemetry.Item headingItem = telemetry.addData("Heading: ", "%.4f", robot.getHeading());

        robot.beep();

        // Wait for the game to start (driver presses PLAY)
        while (!isStarted()) {
            headingItem.setValue("%.4f", robot.getHeading());
            telemetry.update();
        }

        if (opModeIsActive()) {
            runtime.reset();

            //move arm up
            moveArm(0.988d);
            turnAndDrive(50d, 0d);
            turnAndDrive(50d, 90d);
            turnAndDrive(25d, 180d);
            turnAndDrive(15d, 270d);
            turnAndDrive(25d, 180d);
            turnAndDrive(35d, 90d);
            turnAndDrive(50d, 0d);
            turnAndDrive(35d, 90d);
            turnAndDrive(45d, 180d);
            turnAndDrive(20d, 90d);
            turnAndDrive(0d, 0d);
            //scooping stuff
            //move hand
            robot.handServo.setPosition(0.13d);
            //move arm servo
            robot.armServo.setPosition(0.7578d);
            //open door
            robot.doorServo.setPosition(1d);
            sleep(100L);

            //move arm down
            moveArm(2.329d);
            turnAndDrive(26d, 0d);

            //close door
            robot.doorServo.setPosition(0.3d);
            sleep(1000L);

            //move arm middle
            moveArm(1.5d);

            //move arm servo
            robot.armServo.setPosition(0.25d);

            //move arm up
            moveArm(0.988d);

            turnAndDrive(-25d, 0d);
            turnAndDrive(-25d, 90d);
            turnAndDrive(-50d, 180d);
            turnAndDrive(-37.5d, 90d);
            turnAndDrive(-50d, 0d);
            turnAndDrive(-35d, 90d);
            turnAndDrive(-25d, 180d);
            turnAndDrive(-15d, 270d);
            turnAndDrive(-25d, 180d);
            turnAndDrive(-40d, 90d);
            turnAndDrive(-50d, 0d);

            robot.beep();
        }

        while (opModeIsActive()) {
            DriveControl();

            ArmControl();
        }
    }

    protected void moveArm(double targetPosition) {
        ElapsedTime armTimeout = new ElapsedTime();
        armTimeout.reset();
        HardwareAlhambra.ArmInfo armInfo = robot.setArmTarget(targetPosition);
        robot.setArmPower(armInfo.PowerToSet);
        while (robot.armDrive.isBusy() &&
                TimeOK() &&
                (armInfo.Done == false) &&
                (armTimeout.time(TimeUnit.SECONDS) < 5d)) {
            armInfo = robot.setArmTarget(targetPosition);
            robot.armDrive.setPower(armInfo.PowerToSet);
            idle();
        }
        robot.armDrive.setPower(0d);
    }

    protected void turnAndDrive(double distance, double angle) {
        //robot.speak("turn " + angle);
        gyroTurn(DRIVE_SPEED, angle);
        //robot.speak("drive " + distance);

        boolean successful = gyroDrive(DRIVE_SPEED, distance, angle);
        if (!successful) {
            double distanceToMove = distance > 0 ? -1d : 1d;
            encoderDrive(DRIVE_SPEED, distanceToMove, distanceToMove, 1d);
        }
    }

    protected final void encoderDrive(double speed,
                                      double leftInches, double rightInches,
                                      double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        ElapsedTime runtimeEncoder = new ElapsedTime();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtimeEncoder.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    TimeOK() &&
                    (runtimeEncoder.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {
                idle();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    final void resetMotors() {
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private boolean checkingFrontClearance() {
        boolean result = true;
        if (robot.digitalFront.getState() == false) {
            result = false;
            robot.beep();
        } else {
            double frontDistance = robot.sensorDistance.getDistance(DistanceUnit.CM);
            if (frontDistance != distanceOutOfRange && frontDistance <= DISTANCE_THRESHOLD) {
                result = false;
                robot.beep();
            }
        }
        return result;
    }

    private boolean checkingRearClearance() {
        boolean result = true;
        if (robot.digitalRear.getState() == false) {
            result = false;
            robot.beep();
        } else {
            double rearDistance = robot.sensorRange.getDistance(DistanceUnit.CM);
            if (rearDistance != distanceOutOfRange && rearDistance <= DISTANCE_THRESHOLD) {
                result = false;
                robot.beep();
            }
        }
        return result;
    }

    /*
     * @param distance inch
     */
    final boolean gyroDrive(double speed,
                            double distance,
                            double angle) {

        int newLeftTarget;
        int newRightTarget;
        int moveCounts;
        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;
        boolean result = true;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int) (distance * COUNTS_PER_INCH);
            newLeftTarget = robot.leftDrive.getCurrentPosition() + moveCounts;
            newRightTarget = robot.rightDrive.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            robot.leftDrive.setPower(speed);
            robot.rightDrive.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    TimeOK() &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                result = distance > 0d ? checkingFrontClearance() : checkingRearClearance();
                if (!result) {
                    break;
                }

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if either one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0) {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                robot.leftDrive.setPower(leftSpeed);
                robot.rightDrive.setPower(rightSpeed);

                // Display drive status for the driver.
                /*telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target", "%7d:%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Actual", "%7d:%7d", robot.leftDrive.getCurrentPosition(),
                        robot.rightDrive.getCurrentPosition());
                telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
                telemetry.update();*/
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        return result;
    }

    private void gyroTurn(double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (TimeOK() && opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    private boolean onHeading(double speed, double angle, double PCoeff) {
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSteer(error, PCoeff);
            rightSpeed = speed * steer;
            leftSpeed = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.leftDrive.setPower(leftSpeed);
        robot.rightDrive.setPower(rightSpeed);

        // Display it for the driver.
        /*telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);*/

        return onTarget;
    }

    private double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - robot.getHeading();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    private double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    protected void DriveControl() {
        double drive = 0d;
        double turn = 0d;
        if (gamepad1.dpad_down || gamepad2.dpad_down) {
            drive = -0.5d - (driveTime.milliseconds() / 800d);
        } else if (gamepad1.dpad_up || gamepad2.dpad_up) {
            drive = 0.5d + (driveTime.milliseconds() / 800d);
        }

        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            turn = -0.5d - (driveTime.milliseconds() / 1200d);
        } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
            turn = 0.5d + (driveTime.milliseconds() / 1200d);
        }

        if (!gamepad1.dpad_down && !gamepad2.dpad_down &&
                !gamepad1.dpad_up && !gamepad2.dpad_up &&
                !gamepad1.dpad_right && !gamepad2.dpad_right &&
                !gamepad1.dpad_left && !gamepad2.dpad_left) {
            driveTime.reset();
        }

        double leftPower = Range.clip(drive - turn, -1d, 1d);
        double rightPower = Range.clip(drive + turn, -1d, 1d);

        // Send calculated power to wheels
        robot.leftDrive.setPower(leftPower);
        robot.rightDrive.setPower(rightPower);
    }

    protected void ArmControl() {
        double armPower = 0d;
        double armPosition = robot.armServo.getPosition();
        double handPosition = robot.handServo.getPosition();

        if (gamepad2.left_bumper || gamepad1.left_bumper) { //set arm to drop mineral
            if (armSequence == 0) {
                HardwareAlhambra.ArmInfo armInfo = robot.setArmTarget(1.5d);
                armPower = armInfo.PowerToSet;
                if (armInfo.Done) {
                    armPosition = 0.25d;
                    armSequence = 1;
                }

            }
            if (armSequence == 1) {
                armPower = robot.setArmTarget(1d).PowerToSet;
            }
        } else if (gamepad2.right_bumper || gamepad1.right_bumper) { //set arm to pickup mineral
            armPower = robot.setArmTarget(2.329d).PowerToSet;
            armPosition = 0.7578d;
            handPosition = 0.1284d;
        } else {
            robot.armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wasBeep = false;
            armSequence = 0;

            if (gamepad2.left_stick_y < 0d && armTime.milliseconds() > SERVO_CYCLE) {
                armTime.reset();
                armPosition += Range.scale(gamepad2.left_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            } else if (gamepad2.left_stick_y > 0d && armTime.milliseconds() > SERVO_CYCLE) {
                armTime.reset();
                armPosition -= Range.scale(gamepad2.left_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            }

            if (gamepad2.right_stick_y < 0d && handTime.milliseconds() > SERVO_CYCLE) {
                handTime.reset();
                handPosition += Range.scale(gamepad2.right_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            } else if (gamepad2.right_stick_y > 0d && handTime.milliseconds() > SERVO_CYCLE) {
                handTime.reset();
                handPosition -= Range.scale(gamepad2.right_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            }

            if (gamepad2.left_trigger > 0d) { //Arm going up
                armPower = Range.clip(gamepad2.left_trigger, 0d, 1d);
            } else if (gamepad2.right_trigger > 0d) { //Arm going down
                armPower = -Range.clip(gamepad2.right_trigger, 0d, 1d);
            } else if (gamepad1.left_trigger > 0d) { //Arm going up
                armPower = Range.clip(gamepad1.left_trigger, 0d, 1d);
            } else if (gamepad1.right_trigger > 0d) { //Arm going down
                armPower = -Range.clip(gamepad1.right_trigger, 0d, 1d);
            }
        }

        if (gamepad2.a || gamepad1.a) {
            if (!aPressed) {
                aPressed = true;
                doorFlag = !doorFlag;
                robot.doorServo.setPosition(doorFlag ? 0.3d : 1d);
            }
        } else {
            aPressed = false;
        }

        if (gamepad2.y || gamepad1.y) {
            if (!yPressed) {
                yPressed = true;
                handFlag = !handFlag;
                handPosition = handFlag ? 0.5d : 0.15d;
            }
        } else {
            yPressed = false;
        }

        robot.setArmPower(armPower);
        robot.armServo.setPosition(armPosition);
        robot.handServo.setPosition(handPosition);
    }
}
