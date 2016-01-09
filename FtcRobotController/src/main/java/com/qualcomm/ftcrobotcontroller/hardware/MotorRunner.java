package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.ftcrobotcontroller.units.Unit;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by tucker on 1/7/16.
 */
public class MotorRunner {

    public static void run(DcMotor motor, double power, Unit unit) {
        if (motor != null) {
            if (unit instanceof TimeUnit) {
                try {
                    motor.setPower(power);
                    Thread.sleep(unit.getValue());
                    motor.setPower(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (unit instanceof EncoderUnit) {
                motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motor.setTargetPosition((int) unit.getValue());
                motor.setPower(power);
                while (motor.isBusy()) ;
                motor.setPower(0);
                motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }

    }

    public static void setPower(DcMotor motor, double power) {
        if (motor != null)
            motor.setPower(power);
    }

    public static double getPower(DcMotor motor) {
        if (motor != null)
            return motor.getPower();
        return 0;
    }

}
