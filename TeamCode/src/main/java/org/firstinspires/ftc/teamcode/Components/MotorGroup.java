package org.firstinspires.ftc.teamcode.Components;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorGroup {

    private DcMotor[] motors;
    public MotorGroup(DcMotor[] motorArray){
        this.motors = motorArray;
    }

    public void useEncoders(){
        for(DcMotor motor : this.motors){
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void withoutEncoders(){
        for(DcMotor motor : this.motors){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void setBrake(){
        for(DcMotor motor : this.motors){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void setCoast(){
        for(DcMotor motor : this.motors){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void resetEncoders(){
        for(DcMotor motor: this.motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void runToPosition(){
        for(DcMotor motor: this.motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void stopAll(){
        for(DcMotor motor : this.motors){
            motor.setPower(0);
        }
    }

    public void setPower(double power){
        for(DcMotor motor : this.motors){
            motor.setPower(power);
        }
    }

}
