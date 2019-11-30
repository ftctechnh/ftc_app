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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file is used for an omnidrive.
 */

@TeleOp(name="XingOmni", group="Linear Opmode")
//@Disabled

public class OmniOpModeX extends LinearOpMode {

  // Declare OpMode members.
  private ElapsedTime runtime = new ElapsedTime();
  private DcMotor driveNW = null;
  private DcMotor driveNE = null;
  private DcMotor driveSE = null;
  private DcMotor driveSW = null;
  private DcMotor lslider = null;
  private Servo grabber = null;
  
  double driveRht = 0;
  double driveFwd = 0;
  double driveC = -0.5;
  
  boolean isGrabbing = false;

  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    // Initialize the hardware variables. Note that the strings used here as parameters
    // to 'get' must correspond to the names assigned during the robot configuration
    // step (using the FTC Robot Controller app on the phone).
    driveNW = hardwareMap.get(DcMotor.class, "driveNW");
    driveNE  = hardwareMap.get(DcMotor.class, "driveNE");
    driveSE = hardwareMap.get(DcMotor.class, "driveSE");
    driveSW = hardwareMap.get(DcMotor.class, "driveSW");
    lslider = hardwareMap.get(DcMotor.class, "lslider");
    grabber = hardwareMap.get(Servo.class, "grabber");

    // Most robots need the motor on one side to be reversed to drive forward
    // Reverse the motor that runs backwards when connected directly to the battery
    driveNW.setDirection(DcMotor.Direction.FORWARD);
    driveNE.setDirection(DcMotor.Direction.FORWARD);
    driveSE.setDirection(DcMotor.Direction.FORWARD);
    driveSW.setDirection(DcMotor.Direction.FORWARD);

    // Wait for the game to start (driver presses PLAY)
    waitForStart();
    runtime.reset();

    // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {

      if(gamepad1.x||gamepad2.x){ //panic button

        driveNW.setPower(0);
        driveNE.setPower(0);
        driveSW.setPower(0);
        driveSE.setPower(0);
        sleep(1000);

      }else{
        
        driveRht = - ( gamepad1.left_stick_x + gamepad1.right_stick_x + gamepad2.left_stick_x + gamepad2.right_stick_x ) / 2;
        driveFwd = ( gamepad1.left_stick_y + gamepad1.right_stick_y + gamepad2.left_stick_y + gamepad2.right_stick_y ) / 2;

        if(gamepad1.left_bumper||gamepad2.left_bumper||gamepad1.left_stick||gamepad2.left_stick){
          driveNW.setPower(-driveC);
          driveNE.setPower(-driveC);
          driveSE.setPower(-driveC);
          driveSW.setPower(-driveC);
        }else if(gamepad1.right_bumper||gamepad2.right_bumper||gamepad1.right_stick||gamepad2.right_stick){
          driveNW.setPower(driveC);
          driveNE.setPower(driveC);
          driveSE.setPower(driveC);
          driveSW.setPower(driveC);
        }else{
          driveNW.setPower(driveRht + driveFwd);
          driveNE.setPower(driveRht - driveFwd);
          driveSE.setPower(- driveRht - driveFwd);
          driveSW.setPower(- driveRht + driveFwd);
        }
        
        if(gamepad1.dpad_left||gamepad2.dpad_left){
          lslider.setPower(1);
        }else if(gamepad1.dpad_right||gamepad2.dpad_right){
          lslider.setPower(-1);
        }else{
          lslider.setPower(0);
        }


        if(gamepad1.dpad_up||gamepad2.dpad_up){
          isGrabbing = false;
        }else if(gamepad1.dpad_down||gamepad2.dpad_down){
          isGrabbing = true;
        }
        
        if(isGrabbing){
          grabber.setPosition(0.7);
        }else{
          grabber.setPosition(0);
        }

      }
    }
  }
}