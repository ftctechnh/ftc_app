package org.firstinspires.ftc.teamcode.Scotts_Things;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareFile2019 {

    HardwareMap hwMap = null;


    public static DcMotor frontLeft = null;
    public static DcMotor frontRight = null;
    public static DcMotor backLeft = null;
    public static DcMotor backRight = null;
    public static double maxOutput = 1;
    public static double rightSideInversionModifier;

    double Z;

    public HardwareFile2019() {

    }

    public void mapHardware(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        frontLeft = hwMap.get(DcMotor.class, "mfl");
        backLeft = hwMap.get(DcMotor.class, "mbl");
        frontRight = hwMap.get(DcMotor.class, "mfr");
        backRight = hwMap.get(DcMotor.class, "mbr");

        resetEncoders();
        directionConfig();

        wheelStop();

    }

    public void directionConfig(){
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void wheelStop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }


    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        runWithEncoders();
    }

    public void runWithEncoders() {

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public double deadZone(double value, double zone) {
        if (Math.abs(value) < zone) {
            value = 0;
        }

        return value;
    }

    public double zManipulation(double zL, double zR) {

        if (zR > zL) {
            Z = zR;
        } else {
            Z = -1 * zL;
        }

        return Z;
    }

    public boolean RightSideInverted(){
        return frontRight.getDirection() == DcMotorSimple.Direction.REVERSE && backRight.getDirection() == DcMotorSimple.Direction.REVERSE;
    }

    public void setRightSideInversionModifier(){
        if (RightSideInverted()){
            rightSideInversionModifier = -1;
        }else{
            rightSideInversionModifier = 1;
        }
    }


}
