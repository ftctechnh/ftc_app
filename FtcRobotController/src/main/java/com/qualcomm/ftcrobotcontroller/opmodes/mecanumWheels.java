package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by pranavb on 10/8/15.
 */
public class mecanumWheels extends OpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor rearLeft;
    DcMotor rearRight;
    float frontLeftPwr;
    float frontRightPwr;
    float rearLeftPwr;
    float rearRightPwr;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("fMotorL");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight = hardwareMap.dcMotor.get("fMotorR");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearLeft = hardwareMap.dcMotor.get("rMotorL");
        rearRight = hardwareMap.dcMotor.get("rMotorR");

    }

    @Override
    public void loop(){
        float strafeDirection = -gamepad1.left_stick_x;
        float throttle1 = -gamepad1.left_stick_y;
        float throttle2 = -gamepad1.right_stick_y;
        float direction = -gamepad1.right_stick_x;

        strafeDirection = Range.clip(strafeDirection, -1, 1);
        throttle1 = Range.clip(throttle1, -1, 1);
        throttle2 = Range.clip(throttle2, -1, 1);
        direction = Range.clip(direction, -1, 1);

        frontLeftPwr = Range.clip(throttle1 + strafeDirection, -1, 1) - Range.clip(throttle2 + direction, -1, 1);
        frontRightPwr = Range.clip(strafeDirection - throttle1, -1, 1) + Range.clip(throttle2 + direction, -1, 1);
        rearLeftPwr = Range.clip(strafeDirection - throttle1, -1, 1) - Range.clip(throttle2 - direction, -1, 1);
        rearRightPwr = Range.clip(throttle1 + strafeDirection, -1, 1) + Range.clip(throttle2 - direction, -1, 1);

        frontLeft.setPower(frontLeftPwr);
        rearRight.setPower(rearRightPwr);
        frontRight.setPower(frontRightPwr);
        rearLeft.setPower(rearLeftPwr);
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
