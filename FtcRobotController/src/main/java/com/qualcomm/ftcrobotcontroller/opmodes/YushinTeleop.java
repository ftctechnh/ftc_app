package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/* Created by yushin on 10/30/15.
*/
public class YushinTeleop extends OpMode {

    private DcMotor motorRight;
    private DcMotor motorRight2;
    private DcMotor motorLeft2;
    private DcMotor motorLeft;
    private DcMotor beaterBar;
    private Servo servo1;
    private Servo servo2;
    private DcMotor motorLift;
    private DcMotor motorRetractor;
    private TouchSensor sensorTouch;

    private boolean exerciseComplete = false;  // for Matt


    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("rightBack");
        motorRight2 = hardwareMap.dcMotor.get("rightFront");
        motorLeft = hardwareMap.dcMotor.get("leftBack");
        motorLeft2 = hardwareMap.dcMotor.get("leftFront");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLeft2.setDirection(DcMotor.Direction.REVERSE);
        //motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //motorRight2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        beaterBar = hardwareMap.dcMotor.get("beaterBar");
        beaterBar.setDirection(DcMotor.Direction.REVERSE);

        servo1 = hardwareMap.servo.get("servo1");
        //servo2 = hardwareMap.servo.get("servo2");

        motorLift = hardwareMap.dcMotor.get("motorLift");
//        motorLift.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorRetractor = hardwareMap.dcMotor.get("retractor");
        motorRetractor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRetractor.setDirection(DcMotor.Direction.REVERSE);
        //motorRetractor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        sensorTouch = hardwareMap.touchSensor.get("touch");
    }

    // for Matty
    private boolean exerciseMotors(boolean forward) { // return true when all motors have been exercised in one direction
        double retractorPower = forward ? 0.2 : -0.2;
        int retractorTarget = forward ? 2000 : 0;
        int retractorTolerance = 100;
        double liftPower = forward ? 0.2 : 0.0;
        double bbarPower = forward ? 0.0 : 0.2;
        double bucketTarget = forward ? 0.8 : 0.6;

        boolean complete = true;

        // move retractor with encoders
        if (Math.abs(motorRetractor.getCurrentPosition() - retractorTarget) > retractorTolerance && ! sensorTouch.isPressed()) {
            motorRetractor.setPower(retractorPower);
            complete = false;
        } else if (sensorTouch.isPressed()){
            motorRetractor.setPower(-retractorPower);
            complete = false;
        } else {
            motorRetractor.setPower(0.0);
        }

        if (complete) {
            beaterBar.setPower(0.0);
            motorLift.setPower(0.0);
        } else {
            beaterBar.setPower(bbarPower);
            motorLift.setPower(liftPower);
            servo1.setPosition(bucketTarget);
        }

        telemetry.addData("Speed", " retract=" + motorRetractor.getPower());
        telemetry.addData("Encoders", " retract=" + motorRetractor.getCurrentPosition());
        return complete;
    }

    @Override
    public void loop() {

        if (true) {
            if (!exerciseComplete) { exerciseComplete = exerciseMotors(true); } // forward exercise
            else { exerciseMotors(false); }// backward exercise

        } else {

            float rightBack = gamepad1.right_stick_y;
            float rightFront = gamepad1.right_stick_y;
            motorRight.setPower(rightBack);
            motorRight2.setPower(rightFront);
            float leftBack = gamepad1.left_stick_y;
            float leftFront = gamepad1.left_stick_y;
            motorLeft.setPower(leftBack);
            motorLeft2.setPower(leftFront);


            if (gamepad2.a) {
                beaterBar.setPower(1);
            } else {
                beaterBar.setPower(0);
            }


            if (gamepad2.dpad_up) {
                motorLift.setPower(.5);
            } else if (gamepad2.dpad_right) {
                motorLift.setPower(0);
            } else if (gamepad2.dpad_down) {//&& !sensorTouch.isPressed()){
                motorLift.setPower(-.5);
            }
            //else (gamepad1.dpad_down && sensorTouch.isPressed()){
            //motorLift.setPower(0);
//}
//}

            if (gamepad1.dpad_right) {
                servo1.setPosition(0);
                ;
            } else if (gamepad1.dpad_left) {
                servo1.setPosition(.9);
            }

            if (gamepad1.dpad_up) {
                servo2.setPosition(.9);
            } else if (gamepad1.dpad_down) {
                servo2.setPosition(0);
            }
        }
    }
}