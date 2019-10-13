package org.firstinspires.ftc.teamcode.Scotts_Things;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareFile2019 {

    HardwareMap hwMap = null;


    public static DcMotor frontLeft = null;
    public static DcMotor frontRight = null;
    public static DcMotor backLeft = null;
    public static DcMotor backRight = null;

    public HardwareFile2019() {

    }

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        frontLeft = hwMap.get(DcMotor.class, "mfl");
        backLeft = hwMap.get(DcMotor.class, "mbl");
        frontRight = hwMap.get(DcMotor.class, "mfr");
        backRight = hwMap.get(DcMotor.class, "mbr");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        wheelStop();

    }

    public void wheelStop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void mapHardware(HardwareMap aHwMap) {
        init(aHwMap);
    }

    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
