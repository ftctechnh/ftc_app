package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MotorTelemetry {
    private DcMotor motor;
    private Telemetry.Item motorPower,motorPos;
    private String motorName;
    private Telemetry telemetry;


    public MotorTelemetry(DcMotor motor,Telemetry telemetry){
        this.motor = motor;
        this.motorName = motor.getDeviceName();
        this.telemetry = telemetry;
        createTelemetry();

    }

    private void createTelemetry(){
        this.motorPower = telemetry.addData("%s PRW",this.motor.getPower());
        this.motorPos = telemetry.addData("%s PRW",this.motor.getCurrentPosition());
    }

    public void updateTel(){
        this.motorPower.setValue(this.motor.getPower());
        this.motorPos.setValue(this.motor.getCurrentPosition());
    }

    public DcMotor getMotor() {
        return motor;
    }

    public void setMotor(DcMotor motor) {
        this.motor = motor;
    }

    public Telemetry.Item getMotorPower() {
        return motorPower;
    }

    public void setMotorPower(Telemetry.Item motorPower) {
        this.motorPower = motorPower;
    }

    public Telemetry.Item getMotorPos() {
        return motorPos;
    }

    public void setMotorPos(Telemetry.Item motorPos) {
        this.motorPos = motorPos;
    }

    public String getMotorName() {
        return motorName;
    }

    public void setMotorName(String motorName) {
        this.motorName = motorName;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
}
