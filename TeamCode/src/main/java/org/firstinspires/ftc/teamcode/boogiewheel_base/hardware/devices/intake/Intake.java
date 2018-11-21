package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class Intake {

    private SlewDcMotor intakeMotor;

    public Intake(HardwareMap hardwareMap){
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));

        intakeMotor.setSlewSpeed(2);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setPower(0);
    }

    public void setIntakePower(double power){
        intakeMotor.setPower(power);
    }

    public void stop(){
        intakeMotor.stop();
    }
}
