package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
/**
 * Created by Robotics on 12/21/2016.
 */

public class MitchellStraight extends OpMode {

    //CONST

    public static final double OneEncRotation = 1440;
    public static final double DistanceOneRotation = 0;
    public static final double ninetyTurn = 0;
    public static final double speed = .75d;
    public static final double turnSpeed = .5d;
    public int VState = 0;
    //Hardware init
    HardwarePushbot robot = new HardwarePushbot();

    //init
    public void init() {


        robot.init(hardwareMap);
        Reset_Encoders();

    }
    //loop
    public void loop() {
        switch(VState) {
            case 0 :
                Reset_Encoders();
                VState ++;
                break;
            case 1:
                Run_using_enc();

                double distace = OneEncRotation;

                Drive_enc_reached(distace,distace);

                    Reset_Encoders();

                break;

        }


    }

    public void Reset_Encoders(){
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void Set_drive_power(double Lspeed,double Rspeed){
        robot.leftMotor.setPower(Lspeed);
        robot.rightMotor.setPower(Rspeed);
    }

    public void Run_using_enc() {
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void Drive_enc_reached(double Lval, double Rval){

        //Targets loc
        Lval = robot.leftMotor.getCurrentPosition()+ Lval;
        Rval = robot.rightMotor.getCurrentPosition() + Rval;

        //seting target loc
        robot.leftMotor.setTargetPosition((int) Lval);
        robot.rightMotor.setTargetPosition((int) Rval);

        //setting to RUN to Position
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set speed
        Set_drive_power(speed,speed);

        //while loop
        while(robot.leftMotor.isBusy() && robot.rightMotor.isBusy()){

        }

        Set_drive_power(0,0);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

}
