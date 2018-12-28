package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.DistanceSensor2m;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class MineralLift {

    private SlewDcMotor liftMotor;

    private Servo gateServo;

    private DistanceSensor2m distanceSensor;

    public MineralLift(HardwareMap hardwareMap) {
        liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("mineral_lift"));
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(0);
        liftMotor.setPower(0);

        gateServo = hardwareMap.servo.get("mineral_gate");
        gateServo.setDirection(Servo.Direction.FORWARD);
        gateServo.setPosition(0);

        distanceSensor = new DistanceSensor2m("Distance1");
    }

    public double getDistance() {
        return distanceSensor.getDistanceIN();
    }

    public void setTargetPosition(int position) {
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);
        liftMotor.setTargetPosition(position);
    }

    public void setCurrentPosition(int position) {
        liftMotor.setCurrentPosition(position);
    }

    public int getCurrentPosition() {
        return liftMotor.getCurrentPosition();
    }

    public boolean isLiftInProgress() {
        return liftMotor.isBusy();
    }

    public void resetPosition() {
        liftMotor.setPower(0);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setGateServoPosition(double position) {
        gateServo.setPosition(position);
    }

    public void stop() {
        liftMotor.stop();
    }
}
