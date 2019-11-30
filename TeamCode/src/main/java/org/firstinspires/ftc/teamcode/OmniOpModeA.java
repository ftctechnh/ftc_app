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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file is used for an omnidrive.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoOmni", group="Linear Opmode")
//@Disabled

public class OmniOpModeA extends LinearOpMode {

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


  
  private void driveRht(double force,int time){
    
    driveNW.setPower(-force);
    driveNE.setPower(-force);
    driveSE.setPower(force);
    driveSW.setPower(force);
    
    sleep(time);
  }
  
  private void driveFwd(double force,int time){
    
    driveNW.setPower(-force);
    driveNE.setPower(force);
    driveSE.setPower(force);
    driveSW.setPower(-force);
    
    sleep(time);
  }
  
  private void driveC(double force,int time){
    
    driveNW.setPower(force);
    driveNE.setPower(force);
    driveSE.setPower(force);
    driveSW.setPower(force);
    
    sleep(time);
  }
  
  private void driveRst(){
    
    driveNW.setPower(0);
    driveNE.setPower(0);
    driveSE.setPower(0);
    driveSW.setPower(0);
    
  }

  private void grab(boolean shouldGrab){
    
    if(shouldGrab){
      grabber.setPosition(0.7);
    }else{
      grabber.setPosition(0);
    }
    
    sleep(1000);
  }
  
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

    driveRht(1,450);
    
    grab(false);

    driveFwd(1,300);
    
    driveRst();
    
    grab(true);

    driveRht(-1,450);

    grab(true);

    driveFwd(-1,900);

    grab(true);

    driveC(-1,700)

    grab(true);
    
    driveNW.setPower(0);
    driveNE.setPower(0);
    driveSE.setPower(0);
    driveSW.setPower(0);
    lslider.setPower(0);
  }
}