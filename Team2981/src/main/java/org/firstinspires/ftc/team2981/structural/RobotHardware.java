package org.firstinspires.ftc.team2981.structural;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by 200462069 on 9/12/2017.
 */

public class RobotHardware {

    public DcMotor fL = null, fR = null, bL = null, bR = null;
    private final int CPR = 0;                                                        //encoder counts per revolution
    private final double DIAMETER = 0;                                                  //encoded drive wheel circumference
    private final double GEARING = 1;
    public final double CPI = (CPR * GEARING) / (DIAMETER * Math.PI);
    private HardwareMap map = null;

    public RobotHardware(HardwareMap map){
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

    public void drive(double power){
        driveLeft(power);
        driveRight(power);
    }

    public void stop(){
        drive(0);
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

        int counts = (int) (dist * CPI);
        fL.setTargetPosition(counts);
        fR.setTargetPosition(counts);

        drive(power);
        resetEnc();
    }

    public boolean isBusy(){
        return fL.isBusy() || fR.isBusy();
    }


}
