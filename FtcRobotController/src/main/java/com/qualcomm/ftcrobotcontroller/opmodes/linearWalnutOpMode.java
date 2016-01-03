package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.walnutLibrary.DistanceMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class linearWalnutOpMode extends LinearOpMode {
    //Initilize HardWare
    private DcMotor motorLeft;
    private DcMotor motorRight;

    //Assign Hardware
    private DistanceMotor leftDrive;
    private DistanceMotor rightDrive;

    public void runOpMode(){
        //Initilize  Hardware
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("slideRight");
        //Assign Hardware
        leftDrive = new DistanceMotor(motorLeft,"Left Drive",true,true,4,1,1440);
        rightDrive = new DistanceMotor(motorRight,"Right Drive",true,false,4,1,1440);
        //Wait for Start
        try{
            waitForStart();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //Drive Forward
        leftDrive.operate(24,1);
        rightDrive.operate(24,1);


    }
}
