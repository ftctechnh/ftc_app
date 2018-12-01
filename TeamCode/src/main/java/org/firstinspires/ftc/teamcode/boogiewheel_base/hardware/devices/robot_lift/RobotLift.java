package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class RobotLift {

    SlewDcMotor liftMotor;

    public RobotLift(HardwareMap hardwareMap) {
        //liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("robot_lift"));
        //liftMotor.setSlewSpeed(2);
        //liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //liftMotor.setTargetPosition(0);
        //liftMotor.setPower(0);
    }

    public void setLiftPower(double power) {
        //TODO we'll need to change this later
        //liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //liftMotor.setPower(power);
    }

    public void setPosition(int position) {
        //liftMotor.setPower(0.5);
        //liftMotor.setTargetPosition(position);
    }

    public void stop() {
        //liftMotor.stop();
    }
}
