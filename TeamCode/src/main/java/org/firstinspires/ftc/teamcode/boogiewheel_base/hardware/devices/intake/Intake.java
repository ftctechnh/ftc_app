package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class Intake {

    private SlewDcMotor intakeMotor;

    private Servo liftServo;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));

        intakeMotor.setSlewSpeed(2);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setPower(0);

        liftServo = hardwareMap.servo.get("intake_lift");
        liftServo.setDirection(Servo.Direction.FORWARD);
        liftServo.setPosition(1);
    }

    public void setLiftServoPosition(double position) {
        liftServo.setPosition(position);

    }

    public void setIntakePower(double power) {
        intakeMotor.setPower(power);
    }

    public void stop() {
        intakeMotor.stop();
    }
}
