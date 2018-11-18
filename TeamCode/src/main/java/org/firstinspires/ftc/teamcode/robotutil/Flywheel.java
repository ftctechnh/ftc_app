package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by antonlin on 9/9/18.
 */

public class Flywheel {
    private DcMotor flywheelMotor;
    LinearOpMode opMode;

    public Flywheel(LinearOpMode opMode) {
        this.opMode = opMode;
        flywheelMotor = opMode.hardwareMap.dcMotor.get("flywheel");
        flywheelMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPower(double power){
        this.flywheelMotor.setPower(power);
    }


}
