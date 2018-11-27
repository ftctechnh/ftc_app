package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class RobotLift {

    SlewDcMotor liftMotor ;

    public RobotLift(HardwareMap hardwareMap){

        liftMotor= new SlewDcMotor(hardwareMap.dcMotor.get("robot_lift"));
        liftMotor.setSlewSpeed(2);
        liftMotor.setPower(0);
    }

    public void setLiftPower(double power){
        liftMotor.setPower(power);

    }


    public void stop(){
        liftMotor.stop();

    }
}
