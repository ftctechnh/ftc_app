package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.walnutLibrary.DistanceMotor;
import com.qualcomm.ftcrobotcontroller.walnutLibrary.TimedMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class LinearWalnutOpMode extends LinearOpMode {
    //Initilize HardWare
    private DcMotor motorLeft;
    private DcMotor motorRight;

    //Assign Hardware
    private TimedMotor leftDrive;
    private TimedMotor rightDrive;

    public void runOpMode(){
        //Initilize  Hardware
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        //Assign Hardware
        leftDrive = new TimedMotor(motorLeft,"Left Drive",false,false);
        rightDrive = new TimedMotor(motorRight,"Right Drive",false,true);
        //Wait for Start
        try{
            waitForStart();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //SimpleDrive Forward
        leftDrive.operate(3,1);
        rightDrive.operate(3,1);
    }
}
