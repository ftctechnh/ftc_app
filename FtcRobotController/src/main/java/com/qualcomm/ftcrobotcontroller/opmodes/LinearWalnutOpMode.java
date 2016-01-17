package com.qualcomm.ftcrobotcontroller.opmodes;

import com.walnutHillsEagles.WalnutLibrary.DistanceMotor;
import com.walnutHillsEagles.WalnutLibrary.TimedMotor;
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
    private DistanceMotor leftDrive;
    private DistanceMotor rightDrive;

    public void runOpMode(){
        //Initilize  Hardware
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        //Assign Hardware
        leftDrive = new DistanceMotor(motorLeft,"Left Drive",true,false,4,1,1440);
        rightDrive = new DistanceMotor(motorRight,"Right Drive",true,true,4,1,1440);
        //Wait for Start
        try{
            waitForStart();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //SimpleDrive Forward
        rightDrive.operate(6,1);
        leftDrive.operate(6,1);

    }
}
