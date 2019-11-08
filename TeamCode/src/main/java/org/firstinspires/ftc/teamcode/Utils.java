package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class Utils {

    // Computes the current battery voltage
    static double getBatteryVoltage(HardwareMap hardwareMap) {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    void MoveToEncoderPosition(DcMotor motor, int position) {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(position);
        motor.setPower(1);
    }

}
