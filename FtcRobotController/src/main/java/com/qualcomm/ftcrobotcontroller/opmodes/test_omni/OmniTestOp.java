package com.qualcomm.ftcrobotcontroller.opmodes.test_omni;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Nathan.Smith.19 on 9/7/2016.
 */
public class OmniTestOp extends OpMode {

    private DcMotor wheel_n, wheel_s, wheel_e, wheel_w;
    double motor_power_x, motor_power_y;

    @Override
    public void init() {
        telemetry.clearData();
        //assign DcMotor to each wheel.
        wheel_n = hardwareMap.dcMotor.get("wheel_n");
        wheel_s = hardwareMap.dcMotor.get("wheel_s");
        wheel_e = hardwareMap.dcMotor.get("wheel_e");
        wheel_w = hardwareMap.dcMotor.get("wheel_w");
    }

    @Override
    public void loop() {
        //get x-axis and y-axis of left controller stick (gamepad 1).
        updateMotorDir();
        //send basic info to the controller phone.
        telemetry();

        //update wheel powers. N & S control the 'x-axis', E & W control the 'y-axis'.
        wheel_n.setPower(motor_power_x);
        wheel_s.setPower(motor_power_x);
        wheel_w.setPower(motor_power_y);
        wheel_e.setPower(motor_power_y);
    }

    public void updateMotorDir(){
        //grab the left-stick y & x axis, and clip to stop any number too large/small errors.
        motor_power_x = -Range.clip(gamepad1.left_stick_x, -1, 1);
        motor_power_y = -Range.clip(gamepad1.left_stick_y, -1, 1);
    }

    public void telemetry(){
        telemetry.addData("X_POWER", motor_power_x);
        telemetry.addData("Y_POWER", motor_power_y);
    }
}
