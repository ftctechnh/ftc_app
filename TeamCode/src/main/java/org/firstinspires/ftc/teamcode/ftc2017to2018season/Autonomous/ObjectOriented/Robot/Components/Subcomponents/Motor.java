package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Motor {

    public DcMotor motor;


    public Motor() {
        this.motor = motor;
    }

    public void setEncoderPosition(int encoderPosition) {

        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(encoderPosition);
    }

    public void setSpeed(double speed){
        motor.setPower(speed);
    }

    public void setMotorDirection(Direction direction){
        switch (direction) {
            case REVERSE:
                motor.setDirection(DcMotor.Direction.REVERSE);
                break;
            case FORWARD:
                motor.setDirection(DcMotor.Direction.FORWARD);
                break;
            default:
                motor.setDirection(DcMotor.Direction.FORWARD);
        }
    }

    public void setRunMode(runMode ModeToRun) {
        switch (ModeToRun) {
            case ENCODER_TO_POS:
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                break;
            case RUN_WITHOUT_ENCODERS:
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                break;
            case RUN_WITH_ENCODERS:
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
                break;
            case STOP_RESET_ENCODERS:
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                break;
            default:
                motor.setMode(motor.getMode());
                break;
        }
    }

    public void setZeroPowerBehavior(zeroPowerModes zeroPowerBehavior){
        switch (zeroPowerBehavior) {
            case BREAK:
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                break;
            case FLOAT:
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                break;
            default:
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                break;
        }
    }

    private enum runMode {
        ENCODER_TO_POS,
        RUN_WITH_ENCODERS,
        RUN_WITHOUT_ENCODERS,
        STOP_RESET_ENCODERS;
    }

    private enum Direction {
        FORWARD,
        REVERSE;
    }

    private enum zeroPowerModes {
        BREAK,
        FLOAT;
    }
}