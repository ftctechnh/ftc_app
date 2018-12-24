package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class RobotLift {

    private SlewDcMotor liftMotor;

    private Servo ratchetServo;

    public RobotLift(HardwareMap hardwareMap) {
        liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("robot_lift"));
        liftMotor.setSlewSpeed(2);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setTargetPosition(0);
        liftMotor.setPower(0);

        ratchetServo = hardwareMap.servo.get("lift_pawl");
        ratchetServo.setDirection(Servo.Direction.REVERSE);
        ratchetServo.setPosition(Constants.ROBOT_LIFT_PAWL_ENGAGED);
    }

    public void setLiftPower(double power) {
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setPower(power);
    }

    public void setLiftNoEncoderPower(double power) {
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setPower(power);
    }

    public void setPosition(int position) {
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.5);
        liftMotor.setTargetPosition(position);
    }

    public int getTargetPosition() {
        return liftMotor.getTargetPosition();
    }

    public int getCurrentPosition() { return liftMotor.getCurrentPosition(); }

    public void setServoPosition(double servoPosition) {
        ratchetServo.setPosition(servoPosition);
    }

    public void stop() {
        liftMotor.stop();
    }
}
