/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Linear Tele Op Mode
 * <p>
 * Enables control of the robot via the gamepad.
 */
@TeleOp(name="Asynch Test (Linear)", group="Swerve Tests")
@Disabled
public class AsynchTest extends LinearOpMode {

  DcMotor motorRight;
  DcMotor motorLeft;

  private void resetEncoder(DcMotor dcMotor)  {
    double startTime, stopTime = 0;
    startTime = this.getRuntime();
    dcMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    stopTime = this.getRuntime();
    DbgLog.msg(String.format("TIE - RESET_ENCODERS for %s, delta time = %.03f msec",
            dcMotor.getDeviceName(),
            (stopTime - startTime)));
  }

  private void setTargetPosition(DcMotor dcMotor, int tgtPosition)  {
    double startTime, stopTime = 0;
    startTime = this.getRuntime();
    dcMotor.setTargetPosition(tgtPosition);
    stopTime = this.getRuntime();
    DbgLog.msg(String.format("TIE - setTargetPosition (%d)for %s, delta time = %.03f msec",
            tgtPosition,
            dcMotor.getDeviceName(),
            (stopTime - startTime)));
  }

  private void setPower(DcMotor dcMotor, double tgtPower)  {
    double startTime, stopTime = 0;
    startTime = this.getRuntime();
    dcMotor.setPower(tgtPower);
    stopTime = this.getRuntime();
    DbgLog.msg(String.format("TIE - setPower (%f) for %s, delta time = %.03f msec",
            tgtPower,
            dcMotor.getDeviceName(),
            (stopTime - startTime)));
  }

  private void runToPosition(DcMotor dcMotor)  {
    double startTime, stopTime = 0;
    startTime = this.getRuntime();
    dcMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    stopTime = this.getRuntime();
    DbgLog.msg(String.format("TIE - RUN_TO_POSITION for %s, delta time = %.03f msec",
            dcMotor.getDeviceName(),
            (stopTime - startTime)));
  }

  private int distanceToTarget(DcMotor dcMotor)  {
    int val = Math.abs(dcMotor.getCurrentPosition() - dcMotor.getTargetPosition());
    return val;
  }

  private final int PULSES_PER_REV = 1440;
  private final double TGT_POWER = 0.10;
  private final int SLEEP_TIME = 5000;
  private final int POS_THRESHOLD = 5;

  @Override
  public void runOpMode() throws InterruptedException {
    int tgtPosition = 0;

    //get references to motors.
    motorLeft = hardwareMap.dcMotor.get("motorLeft");
    motorRight = hardwareMap.dcMotor.get("motorRight");
    motorLeft.setDirection(DcMotor.Direction.REVERSE);

    // wait for start command from driver station.
    waitForStart();

    // reset encoders.
    resetEncoder(motorLeft);
    resetEncoder(motorRight);

    // don't wait after resetting encoders.
    // set target position
    // do five full rotations.
    tgtPosition = 5 * PULSES_PER_REV;
    setTargetPosition(motorLeft, tgtPosition);
    setTargetPosition(motorRight, tgtPosition);

    setPower(motorLeft, TGT_POWER);
    setPower(motorRight, TGT_POWER);

    runToPosition(motorLeft);
    runToPosition(motorRight);

    while (distanceToTarget(motorLeft) > POS_THRESHOLD && distanceToTarget(motorRight) > POS_THRESHOLD)  {
      telemetry.addData("ltPos", String.format("% 07d", motorLeft.getCurrentPosition()));
      telemetry.addData("rtPos", String.format("% 07d", motorRight.getCurrentPosition()));
      sleep(50);
    }

    setPower(motorLeft, 0);
    setPower(motorRight, 0);

    // reset encoders.
    resetEncoder(motorLeft);
    resetEncoder(motorRight);

    // don't wait after resetting encoders.
    // set target position
    // do one reverse full rotations.
    tgtPosition = -1 * PULSES_PER_REV;
    setTargetPosition(motorLeft, tgtPosition);
    setTargetPosition(motorRight, tgtPosition);

    setPower(motorLeft, TGT_POWER);
    setPower(motorRight, TGT_POWER);

    runToPosition(motorLeft);
    runToPosition(motorRight);

    while (distanceToTarget(motorLeft) > POS_THRESHOLD && distanceToTarget(motorRight) > POS_THRESHOLD)  {
      telemetry.addData("ltPos", String.format("% 07d", motorLeft.getCurrentPosition()));
      telemetry.addData("rtPos", String.format("% 07d", motorRight.getCurrentPosition()));
      sleep(50);
    }

    setPower(motorLeft, 0);
    setPower(motorRight, 0);

    // reset encoders.
    resetEncoder(motorLeft);
    resetEncoder(motorRight);

    // don't wait after resetting encoders.
    // set target position
    // do three full rotations.
    tgtPosition = 3 * PULSES_PER_REV;
    setTargetPosition(motorLeft, tgtPosition);
    setTargetPosition(motorRight, tgtPosition);

    setPower(motorLeft, TGT_POWER);
    setPower(motorRight, TGT_POWER);

    runToPosition(motorLeft);
    runToPosition(motorRight);

    while(opModeIsActive())  {
      telemetry.addData("ltPos", String.format("% 07d", motorLeft.getCurrentPosition()));
      telemetry.addData("rtPos", String.format("% 07d", motorRight.getCurrentPosition()));
      Thread.sleep(50);
    }
  }
}
