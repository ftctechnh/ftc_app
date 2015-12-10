package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class AutoShortRight extends OpMode {

  private ElapsedTime timer = new ElapsedTime();
  private DcMotor motorRightFront;
  private DcMotor motorRightBack;
  private DcMotor motorLeftFront;
  private DcMotor motorLeftBack;

  public enum State {
    Begin,
    Depart,
    Turn1,
    Straight,
    Turn2,
    Approach,
    Dock
  }

  private State state = State.Begin;
  private double speed = 0.0;
  private double insideSpeed = 0.0;

  private double leftPower  = 0.0;
  private double rightPower = 0.0;

  private void setLeftPower(double pwr) {
    leftPower = Range.clip(pwr,  -1, 1);
    motorLeftFront.setPower(leftPower);
    motorLeftBack.setPower(leftPower);
  }

  private void setRightPower(double pwr) {
    rightPower = Range.clip(pwr,  -1, 1);
    motorRightFront.setPower(rightPower);
    motorRightBack.setPower(rightPower);
  }

  private void setPower(double right, double left) {
    setLeftPower(left);
    setRightPower(right * 1.2);
  }

  public AutoShortRight() { }

  @Override
  public void init() {
    motorRightFront = hardwareMap.dcMotor.get("motor_right_front");
    motorRightBack  = hardwareMap.dcMotor.get("motor_right_back");
    motorLeftFront  = hardwareMap.dcMotor.get("motor_left_front");
    motorLeftBack   = hardwareMap.dcMotor.get("motor_left_back");
    motorRightFront.setDirection(DcMotor.Direction.REVERSE);
    motorRightBack.setDirection(DcMotor.Direction.REVERSE);
    motorLeftFront.setDirection(DcMotor.Direction.FORWARD);
    motorLeftBack.setDirection(DcMotor.Direction.FORWARD);
  }

  @Override
  public void loop() {
    double totalTime = 12.0; // seconds
    double departDur   =  0.06 * totalTime;
    double turn1Dur    =  0.72 * totalTime;
    double straightDur =  0.00 * totalTime;
    double turn2Dur    =  0.00 * totalTime;
    double approachDur =  0.22 * totalTime;

    double departEnd   = departDur;
    double turn1End    = departEnd   + turn1Dur;
    double straightEnd = turn1End    + straightDur;
    double turn2End    = straightEnd + turn2Dur;
    double approachEnd = turn2End    + approachDur;

    switch (state) {
      case Begin:
//        if (gamepad1.a)
//          speed = 0.04;
//        else if (gamepad1.b)
//          speed = 0.06;
//        else if (gamepad1.x)
//          speed = 0.08;
//        else if (gamepad1.y)
//          speed = 0.1;
//        else
//          break;
        speed = 0.15;
        insideSpeed = 5.0/8.0 * speed; // 4' outer-radius turn.
        timer.reset();
        setPower(speed, speed);
        state = State.Depart;
        // fall thru
      case Depart:
        if (timer.time() < departEnd)
          break;
        setPower(insideSpeed, speed);
        state = State.Turn1;
        // fall thru
      case Turn1:
        if (timer.time() < turn1End)
          break;
        setPower(speed, speed);
        state = State.Straight;
        // fall thru
      case Straight:
        if (timer.time() < straightEnd)
          break;
        setPower(insideSpeed, speed);
        state = State.Turn2;
        // fall thru
      case Turn2:
        if (timer.time() < turn2End)
          break;
        setPower(speed, speed);
        state = State.Approach;
        // fall thru
      case Approach:
        if (timer.time() < approachEnd)
          break;
        setPower(0.0, 0.0);
        state = State.Dock;
        // fall thru
      case Dock:
        break;
    }

    telemetry.addData("Text", "*** Robot Data***");
    telemetry.addData("state", String.format("%s",   state));
    telemetry.addData("left",  String.format("%.2f", leftPower));
    telemetry.addData("right", String.format("%.2f", rightPower));
  }

  @Override
  public void stop() { setPower(0.0, 0.0); }

} // AutoShortRight
