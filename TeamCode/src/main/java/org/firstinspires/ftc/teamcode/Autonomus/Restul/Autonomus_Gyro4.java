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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
@Autonomous(name = "GYRO4", group = "TEST")
@Disabled
public class Autonomus_Gyro4 extends LinearOpMode {

  HardwareRobot robot = new HardwareRobot();
  ElapsedTime timer = new ElapsedTime();


  static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
  static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
  static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
  static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
          (WHEEL_DIAMETER_INCHES * 3.1415);
  static final double     COUNTE_PER_CM = COUNTS_PER_INCH / 2.54;
  static final double DRIVE_SPEED = 0.5;     // Nominal speed for better accuracy.
  static final double TURN_SPEED = 0.15;     // Nominal half speed for better accuracy.

  public void runOpMode() throws InterruptedException{

    robot.init(hardwareMap);
    telemetry.log().add("Gyro Calibrating. Do Not Move!");
    robot.modernRoboticsI2cGyro.calibrate();

    timer.reset();
    while (!isStopRequested() && robot.modernRoboticsI2cGyro.isCalibrating()) {
      telemetry.addData("calibrating", "%s", Math.round(timer.seconds()) % 2 == 0 ? "|.." : "..|");
      telemetry.update();
      sleep(50);
    }

    telemetry.log().clear();
    telemetry.log().add("Gyro Calibrated. Press Start.");
    telemetry.clear();
    telemetry.update();

    waitForStart();
    telemetry.log().clear();

    while (opModeIsActive()) {

      robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

      while (!isStarted()) {
        telemetry.addData(">", "Robot Heading = %d", robot.modernRoboticsI2cGyro.getIntegratedZValue());
        telemetry.update();
      }

      robot.modernRoboticsI2cGyro.resetZAxisIntegrator();

      turnAbsolute(90);///face dreapta
      sleep(1000);
      turnAbsolute(180);///face dreapta
      sleep(1000);
      turnAbsolute(270);///face dreapta
      sleep(1000);
      turnAbsolute(0);///face stanga (sau invers nush sigur)
      sleep(1000);
      telemetry.addData("Path", "Complete");
      telemetry.update();


    }
  }

  public void turnAbsolute(int target)throws InterruptedException {

    int zAccumulated = robot.modernRoboticsI2cGyro.getIntegratedZValue();

    while (Math.abs(zAccumulated - target) > 3 && opModeIsActive() )
    {
      if (zAccumulated > target)
      {
        robot.FrontLeftMotor.setPower(TURN_SPEED);
        robot.BackLeftMotor.setPower(TURN_SPEED);
        robot.FrontRightMotor.setPower(-TURN_SPEED);
        robot.BackRightMotor.setPower(-TURN_SPEED);
      }
      else if (zAccumulated < target)
      {
        robot.FrontLeftMotor.setPower(-TURN_SPEED);
        robot.BackLeftMotor.setPower(-TURN_SPEED);
        robot.FrontRightMotor.setPower(TURN_SPEED);
        robot.BackRightMotor.setPower(TURN_SPEED);
      }

      zAccumulated = robot.modernRoboticsI2cGyro.getIntegratedZValue();
      telemetry.addData("integratedZ", zAccumulated);
    }

    robot.FrontLeftMotor.setPower(0);
    robot.BackLeftMotor.setPower(0);
    robot.FrontRightMotor.setPower(0);
    robot.BackRightMotor.setPower(0);

    telemetry.addData("integratedZ", zAccumulated);
    telemetry.update();
  }
}

