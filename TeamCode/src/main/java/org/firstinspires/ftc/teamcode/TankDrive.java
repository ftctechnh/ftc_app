package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.M_FRONT_LEFT;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.M_FRONT_RIGHT;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.M_REAR_LEFT;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.M_REAR_RIGHT;

public class TankDrive {

    public TankDrive(DcMotor...motors){
        this.motors=motors;
    }

    public void setValues(double ly,double ry){
        motors[M_FRONT_LEFT].setPower(ly);
        motors[M_FRONT_RIGHT].setPower(ry);
        motors[M_REAR_LEFT].setPower(ly);
        motors[M_REAR_RIGHT].setPower(ry);
    }
    DcMotor[] motors;
}
