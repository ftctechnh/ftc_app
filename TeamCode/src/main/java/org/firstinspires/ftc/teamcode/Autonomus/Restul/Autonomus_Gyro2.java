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

package org.firstinspires.ftc.teamcode.Autonomus.Restul;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.HardwareRobot;

/*
 * This is an example LinearOpMode that shows how to use the Modern Robotics Gyro.
 *
 * The op mode assumes that the gyro sensor is attached to a Device Interface Module
 * I2C channel and is configured with a name of "gyro".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
*/
@TeleOp(name = "GYRO AAAAAAAAAAAAAA", group = "TEST")
//@Disabled
public class Autonomus_Gyro2 extends LinearOpMode {

  HardwareRobot robot = new HardwareRobot();
  ElapsedTime timer = new ElapsedTime();


  static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
  static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
  static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
  static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                         (WHEEL_DIAMETER_INCHES * 3.1415);
  static final double     DRIVE_SPEED             = 0.7;     // Nominal speed for better accuracy.
  static final double     TURN_SPEED              = 0.5;     // Nominal half speed for better accuracy.

  static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
  static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
  static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

  public void runOpMode() {

    boolean lastResetState = false;
    boolean curResetState  = false;

    robot.init(hardwareMap);
    // If you're only interested int the IntegratingGyroscope interface, the following will suffice.
    // gyro = hardwareMap.get(IntegratingGyroscope.class, "gyro");
    // A similar approach will work for the Gyroscope interface, if that's all you need.

    telemetry.log().add("Gyro Calibrating. Do Not Move!");
    robot.modernRoboticsI2cGyro.calibrate();

    timer.reset();
    while (!isStopRequested() && robot.modernRoboticsI2cGyro.isCalibrating())  {
      telemetry.addData("calibrating", "%s", Math.round(timer.seconds())%2==0 ? "|.." : "..|");
      telemetry.update();
      sleep(50);
    }

    telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
    telemetry.clear(); telemetry.update();

    waitForStart();
    telemetry.log().clear();
    telemetry.log().add("Press A & B to reset heading");

    while (opModeIsActive()) {

      curResetState = (gamepad1.a && gamepad1.b);

      if (curResetState && !lastResetState) {
        robot.modernRoboticsI2cGyro.resetZAxisIntegrator();
      }
      lastResetState = curResetState;

      robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



      while (!isStarted()) {
        telemetry.addData(">", "Robot Heading = %d", robot.modernRoboticsI2cGyro.getIntegratedZValue());
        telemetry.update();
      }

      robot.modernRoboticsI2cGyro.resetZAxisIntegrator();
      gyroDrive(DRIVE_SPEED, 48.0, 0.0);    // Drive FWD 48 inches
      /*
      gyroHold(TURN_SPEED, 45, 1);
      gyroDrive(DRIVE_SPEED, 48.0, 0.0);    // Drive FWD 48 inches
      gyroTurn( TURN_SPEED, -45.0);         // Turn  CCW to -45 Degrees
      gyroHold( TURN_SPEED, -45.0, 0.5);    // Hold -45 Deg heading for a 1/2 second
      gyroDrive(DRIVE_SPEED, 12.0, -45.0);  // Drive FWD 12 inches at 45 degrees
      gyroTurn( TURN_SPEED,  45.0);         // Turn  CW  to  45 Degrees
      gyroHold( TURN_SPEED,  45.0, 0.5);    // Hold  45 Deg heading for a 1/2 second
      gyroTurn( TURN_SPEED,   0.0);         // Turn  CW  to   0 Degrees
      gyroHold( TURN_SPEED,   0.0, 1.0);    // Hold  0 Deg heading for a 1 second
      gyroDrive(DRIVE_SPEED,-48.0, 0.0);    // Drive REV 48 inches
    */

      telemetry.addData("Path", "Complete");
      telemetry.update();

      // The raw() methods report the angular rate of change about each of the
      // three axes directly as reported by the underlying sensor IC.
      int rawX = robot.modernRoboticsI2cGyro.rawX();
      int rawY = robot.modernRoboticsI2cGyro.rawY();
      int rawZ = robot.modernRoboticsI2cGyro.rawZ();
      int heading = robot.modernRoboticsI2cGyro.getHeading();
      int integratedZ = robot.modernRoboticsI2cGyro.getIntegratedZValue();

      AngularVelocity rates = robot.gyro.getAngularVelocity(AngleUnit.DEGREES);
      float zAngle = robot.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;


      // Read administrative information from the gyro
      int zAxisOffset = robot.modernRoboticsI2cGyro.getZAxisOffset();
      int zAxisScalingCoefficient = robot.modernRoboticsI2cGyro.getZAxisScalingCoefficient();

      telemetry.addLine()
        .addData("dx", formatRate(rates.xRotationRate))
        .addData("dy", formatRate(rates.yRotationRate))
        .addData("dz", "%s deg/s", formatRate(rates.zRotationRate));
      telemetry.addData("angle", "%s deg", formatFloat(zAngle));
      telemetry.addData("heading", "%3d deg", heading);
      telemetry.addData("integrated Z", "%3d", integratedZ);
      telemetry.addLine()
        .addData("rawX", formatRaw(rawX))
        .addData("rawY", formatRaw(rawY))
        .addData("rawZ", formatRaw(rawZ));
      telemetry.addLine().addData("z offset", zAxisOffset).addData("z coeff", zAxisScalingCoefficient);
      telemetry.update();


    }
  }
  public void gyroDrive( double speed,
                         double distance,
                         double angle) {

    int     f_newLeftTarget;
    int     f_newRightTarget;
    int     b_newLeftTarget;
    int     b_newRightTarget;
    int     moveCounts;
    double  max;
    double  error;
    double  steer;
    double  f_leftSpeed;
    double  f_rightSpeed;
    double  b_leftSpeed;
    double  b_rightSpeed;

    // Ensure that the opmode is still active
    if (opModeIsActive()) {

      // Determine new target position, and pass to motor controller
      moveCounts = (int)(distance * COUNTS_PER_INCH);
      f_newLeftTarget = robot.FrontLeftMotor.getCurrentPosition() + moveCounts;
      f_newRightTarget = robot.FrontRightMotor.getCurrentPosition() + moveCounts;
      b_newLeftTarget = robot.BackLeftMotor.getCurrentPosition() + moveCounts;
      b_newRightTarget = robot.BackRightMotor.getCurrentPosition() + moveCounts;

      // Set Target and Turn On RUN_TO_POSITION
      robot.FrontLeftMotor.setTargetPosition(f_newLeftTarget);
      robot.FrontRightMotor.setTargetPosition(f_newRightTarget);
      robot.BackLeftMotor.setTargetPosition(b_newLeftTarget);
      robot.BackRightMotor.setTargetPosition(b_newRightTarget);

      robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

      // start motion.
      speed = Range.clip(Math.abs(speed), 0.0, 1.0);
      robot.FrontLeftMotor.setPower(speed);
      robot.FrontRightMotor.setPower(speed);
      robot.BackLeftMotor.setPower(speed);
      robot.BackRightMotor.setPower(speed);
      // keep looping while we are still active,and BOTH motors are running.
      while (opModeIsActive() &&
              (robot.FrontLeftMotor.isBusy() && robot.FrontRightMotor.isBusy())) {

        // adjust relative speed based on heading error.
        error = getError(angle);
        steer = getSteer(error, P_DRIVE_COEFF);

        // if driving in reverse, the motor correction also needs to be reversed
        if (distance < 0)
          steer *= -1.0;

        b_leftSpeed = speed-steer;
        b_rightSpeed = speed-steer;
        f_leftSpeed = speed - steer;
        f_rightSpeed= speed + steer;

        // Normalize speeds if either one exceeds +/- 1.0;
        max = Math.max(Math.max(Math.abs(f_leftSpeed), Math.abs(f_rightSpeed)), Math.max(Math.abs(b_leftSpeed), Math.abs(b_rightSpeed)));
        if (max > 1.0)
        {
          f_leftSpeed /= max;
          f_rightSpeed /= max;
          b_leftSpeed /= max;
          b_rightSpeed /=max;
        }

        robot.FrontLeftMotor.setPower(f_leftSpeed);
        robot.FrontRightMotor.setPower(f_rightSpeed);
        robot.BackLeftMotor.setPower(b_leftSpeed);
        robot.BackRightMotor.setPower(b_rightSpeed);

        // Display drive status for the driver.
        telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
        telemetry.addData("Target",  "%7d:%7d",      f_newLeftTarget,  f_newRightTarget, b_newLeftTarget,b_newRightTarget);
        telemetry.addData("Actual",  "%7d:%7d",      robot.FrontLeftMotor.getCurrentPosition(),
                robot.FrontRightMotor.getCurrentPosition(), robot.BackLeftMotor.getCurrentPosition(), robot.BackRightMotor.getCurrentPosition());
        telemetry.addData("Speed",   "%5.2f:%5.2f",  f_leftSpeed, f_rightSpeed, b_leftSpeed, b_rightSpeed);
        telemetry.update();
      }

      // Stop all motion;
      robot.FrontLeftMotor.setPower(0);
      robot.FrontRightMotor.setPower(0);
      robot.BackLeftMotor.setPower(0);
      robot.BackRightMotor.setPower(0);

      // Turn off RUN_TO_POSITION
      robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
  }
  public void gyroTurn (  double speed, double angle) {

    // keep looping while we are still active, and not on heading.
    while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
      // Update telemetry & Allow time for other processes to run.
      telemetry.update();
    }
  }
  public void gyroHold( double speed, double angle, double holdTime) {

    ElapsedTime holdTimer = new ElapsedTime();

    // keep looping while we have time remaining.
    holdTimer.reset();
    while (opModeIsActive() && (holdTimer.time() < holdTime)) {
      // Update telemetry & Allow time for other processes to run.
      onHeading(speed, angle, P_TURN_COEFF);
      telemetry.update();
    }

    // Stop all motion;
    robot.FrontLeftMotor.setPower(0);
    robot.FrontRightMotor.setPower(0);
    robot.BackLeftMotor.setPower(0);
    robot.BackRightMotor.setPower(0);
  }

  boolean onHeading(double speed, double angle, double PCoeff) {
    ///GOOD BOY IUA
    // PCOEFF ?
    double   error ;
    double   steer ;
    boolean  onTarget = false ;
    double f_leftSpeed;
    double f_rightSpeed;
    double b_leftSpeed;
    double b_rightSpeed;

    // determine turn power based on +/- error
    error = getError(angle);

    if (Math.abs(error) <= HEADING_THRESHOLD) {
      steer = 0.0;
      f_leftSpeed  = 0.0;
      f_rightSpeed = 0.0;
      b_leftSpeed = 0.0;
      b_rightSpeed = 0.0;
      onTarget = true;
    }
    else {

        steer = getSteer(error, PCoeff);
        f_rightSpeed  = speed * steer;
        f_leftSpeed   = -f_rightSpeed;
        b_rightSpeed  = speed * steer;
        b_leftSpeed   = -b_rightSpeed;
    }

    // Send desired speeds to motors.
    robot.FrontLeftMotor.setPower(f_leftSpeed);
    robot.FrontRightMotor.setPower(f_rightSpeed);
    robot.BackLeftMotor.setPower(b_leftSpeed);
    robot.BackRightMotor.setPower(b_rightSpeed);

    // Display it for the driver.
    telemetry.addData("Target", "%5.2f", angle);
    telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
    telemetry.addData("Speed.", "%5.2f:%5.2f", f_leftSpeed, f_rightSpeed,b_leftSpeed,b_rightSpeed);

    return onTarget;
  }
  public double getError(double targetAngle) {
    ///GOOD BOY IUA
    double robotError;

    // calculate error in -179 to +180 range  (
    robotError = targetAngle - robot.modernRoboticsI2cGyro.getIntegratedZValue();
    while (robotError > 180)  robotError -= 360;
    while (robotError <= -180) robotError += 360;
    return robotError;
  }
  public double getSteer(double error, double PCoeff) {
    return Range.clip(error * PCoeff, -1, 1);
  }

  String formatRaw(int rawValue) {
    return String.format("%d", rawValue);
  }

  String formatRate(float rate) {
    return String.format("%.3f", rate);
  }

  String formatFloat(float rate) {
    return String.format("%.3f", rate);
  }

}

