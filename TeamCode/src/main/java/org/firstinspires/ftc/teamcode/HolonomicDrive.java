package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.*;

public class HolonomicDrive {

    public HolonomicDrive(DcMotor...motors){
        this.motors=motors;
    }

    void setValues(float x, float y, float r) {
        double flValue = y + r + x;
        double rlValue = y + r - x;
        double frValue = y - r - x;
        double rrValue = -r + x + y;

        double maxValue = Math.max(
                Math.max(Math.abs(flValue), Math.abs(rlValue)),
                Math.max(Math.abs(frValue), Math.abs(rrValue))
        );
        if (maxValue > 1.0) {
            flValue /= maxValue;
            rlValue /= maxValue;
            frValue /= maxValue;
            rrValue /= maxValue;
        }
        motors[M_FRONT_LEFT].setPower(flValue);
        motors[M_FRONT_RIGHT].setPower(frValue);
        motors[M_REAR_LEFT].setPower(rlValue);
        motors[M_REAR_RIGHT].setPower(rrValue);

    }
    DcMotor motors[];
}
