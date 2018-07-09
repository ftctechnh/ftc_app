package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class AbstractDrivetrain {

    protected DcMotor lfDriveM, rfDriveM, lbDriveM, rbDriveM;
    protected HardwareMap hwMap;

    public AbstractDrivetrain(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        this.lfDriveM  = hwMap.get(DcMotor.class, "FL");
        this.rfDriveM = hwMap.get(DcMotor.class, "FR");
        this.lbDriveM  = hwMap.get(DcMotor.class, "BL");
        this.rbDriveM = hwMap.get(DcMotor.class, "BR");
        lfDriveM.setDirection(DcMotor.Direction.FORWARD);
        lbDriveM.setDirection(DcMotor.Direction.FORWARD);
        rfDriveM.setDirection(DcMotor.Direction.REVERSE);
        rbDriveM.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        stop();

        // Set all motors to run without encoders.
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lfDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public abstract void drive(double xVelocity, double yVelocity, double wVelocity);

    public abstract void encoderDrive(double xVelocity, double yVelocity, double wVelocity, double distance);

    public void stop(){
        lfDriveM.setPower(0);
        rfDriveM.setPower(0);
        lbDriveM.setPower(0);
        rbDriveM.setPower(0);
    }

    public void setMotorMode(DcMotor.RunMode mode) {
        rfDriveM.setMode(mode);
        rbDriveM.setMode(mode);
        lfDriveM.setMode(mode);
        lbDriveM.setMode(mode);
    }


}
