package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class auto extends OpMode{
    int state = 0;
    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    int leftAverage;
    int rightAverage;

    public void init(){
    }
    public void loop(){
        switch(state){
            //resetting all encoders
            case 0:
                telemetry.addData("Case", "0");
               reset_all_encoders();
                state++;
                break;
            //moving forward
            case 1:
                telemetry.addData("Case", "1");
                set_motor_power(.5,.5,.5,.5);
                if (leftAverage >= 2000 && rightAverage >= 2000){
                    set_motor_power(0,0,0,0);
                     state++;
                }
                break;
            //turning 45 degrees to the left
            case 2:
                telemetry.addData("Case", "2");
                reset_all_encoders();
                set_motor_power(.5,.5,-.5,-.5);
                if (rightAverage >= 1000 ){
                    set_motor_power(0,0,0,0);
                    state++;
                }
                break;
            //moving parallel to the center line
            case 3:
                telemetry.addData("Case", "3");
                reset_all_encoders();
                set_motor_power(.5,.5,.5,.5);
                if (leftAverage >= 2000 && rightAverage >= 2000){
                    set_motor_power(0,0,0,0);
                   state++;
                }
                break;
            //turning 90 degrees to the left
            case 4:
                telemetry.addData("Case", "4");
                reset_all_encoders();
                set_motor_power(.5,.5,-.5,-.5);
                if (rightAverage >=2000){
                    set_motor_power(0,0,0,0);
                    state++;
                }
                break;
            case 5:
                telemetry.addData("Case", "5");
                reset_all_encoders();
                set_motor_power(.5,.5,.5,.5);
                if (leftAverage >= 3000 && rightAverage >= 3000){
                    set_motor_power(0,0,0,0);
                    state++;
                }
                break;
            case 6:
                telemetry.addData("The" , "End");
        }
        leftAverage = (motorLeftBack.getCurrentPosition()+motorLeftFront.getCurrentPosition())/2;
        rightAverage = (motorRightBack.getCurrentPosition()+motorRightFront.getCurrentPosition())/2;
    }
    public void reset_all_encoders(){
        motorRightBack.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRightFront.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeftBack.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeftFront.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void set_motor_power(double mRB_enc,double mRF_enc, double mLB_enc,double mLF_enc){
        motorRightBack.setPower(mRB_enc);
        motorRightFront.setPower(mRF_enc);
        motorLeftBack.setPower(mLB_enc);
        motorLeftFront.setPower(mLF_enc);
    }
}