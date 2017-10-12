package org.firstinspires.ftc.team2981.structural;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by 200462069 on 9/12/2017.
 */

public class Hardware {

    DcMotor fL = null, fR = null, bL = null, bR = null;
    int cpr = 0;                                                        //encoder counts per revolution
    double circum = 0;                                                  //encoded drive wheel circumference


    HardwareMap map = null;

    public void init(HardwareMap map){
        this.map = map;

        fL = map.get(DcMotor.class, "fL");
        fR = map.get(DcMotor.class, "fR");
        bL = map.get(DcMotor.class, "bL");
        bR = map.get(DcMotor.class, "bR");

        fR.setDirection(DcMotorSimple.Direction.FORWARD);
        bR.setDirection(DcMotorSimple.Direction.FORWARD);
        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);

        resetEnc();
    }

    public void driveLeft(double power){
        power = Range.clip(power, -1, 1);
        fL.setPower(power);
        bL.setPower(power);
    }

    public void driveRight(double power){
        power = Range.clip(power, -1, 1);
        fR.setPower(power);
        bR.setPower(power);
    }

    public void driveAll(double power){
        driveLeft(power);
        driveRight(power);
    }

    public void stop(){
        driveAll(0);
    }

    public void resetEnc(){
        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveDist(double dist, double power){
        resetEnc();
        fL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int counts = (int) ((dist * circum) / cpr);
        fL.setTargetPosition(counts);
        fR.setTargetPosition(counts);

        driveAll(power);
    }

    public boolean isBusy(){
        return fL.isBusy() || fR.isBusy();
    }


}
