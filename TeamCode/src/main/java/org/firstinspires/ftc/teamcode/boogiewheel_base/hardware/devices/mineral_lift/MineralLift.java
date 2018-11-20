package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class MineralLift {

    SlewDcMotor liftMotor;

    public MineralLift(HardwareMap hardwareMap){
        liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("mineral_lift"));
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(0);
        liftMotor.setPower(0);
    }

    public void setPosition(int position){
        liftMotor.setPower(0.3);
        liftMotor.setTargetPosition(position);
    }

    public void stop(){
        liftMotor.stop();
    }
}
