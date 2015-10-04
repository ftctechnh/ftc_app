package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by pranavb on 9/27/15.
 */
public class armRobot extends OpMode {
    final float ARM_MIN_RANGE = 0;
    float ARM_MAX_RANGE = 1;
    float CLAW_MIN_RANGE = 0;
    float CLAW_MAX_RANGE = 1;

    double armPosition;
    double armDelta = 0.01;
    double clawPosition;
    double clawDelta = 0.01;

    DcMotor frontMotorLeft;
    DcMotor frontMotorRight;
    DcMotor rearMotorLeft;
    DcMotor rearMotorRight;
    Servo arm;
    Servo claw;
    OpticalDistanceSensor distSensor;

    public void wallFollow(){

    }


    public armRobot() {

    }
    @Override
    public void init(){
        frontMotorLeft = hardwareMap.dcMotor.get("fMotorL");
        frontMotorRight = hardwareMap.dcMotor.get("fMotorR");
        frontMotorRight.setDirection(DcMotor.Direction.REVERSE);
        rearMotorLeft = hardwareMap.dcMotor.get("rMotorL");
        rearMotorLeft.setDirection(DcMotor.Direction.REVERSE);
        rearMotorRight = hardwareMap.dcMotor.get("rMotorR");
        arm = hardwareMap.servo.get("arm");
        claw = hardwareMap.servo.get("claw");
        distSensor = hardwareMap.opticalDistanceSensor.get("distSensor");

        armPosition = 0.0;
        clawPosition = 0.0;
    }

    @Override
    public void loop(){
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        frontMotorLeft.setPower(left);
        rearMotorLeft.setPower(right);
        frontMotorRight.setPower(right);
        rearMotorRight.setPower(right);

        if(gamepad2.dpad_up){
            armPosition += armDelta;
        }
        if(gamepad2.dpad_down){
            armPosition -= armDelta;
        }
        if(gamepad2.y){
            clawPosition += clawDelta;
        }
        if(gamepad2.a){
            clawPosition -= clawDelta;
        }
        if(gamepad1.left_bumper){

        }

        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        // write position values to the wrist and claw servo
        arm.setPosition(armPosition);
        claw.setPosition(clawPosition);

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
    }
    @Override
    public void stop() {

    }
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
}
