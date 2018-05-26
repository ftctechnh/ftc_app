package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class AbstractDrivetrain {

    protected DcMotor lfDriveM = null;
    protected DcMotor rfDriveM = null;
    protected DcMotor lbDriveM = null;
    protected DcMotor rbDriveM = null;

    protected HardwareMap hwMap =  null;

    public AbstractDrivetrain(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        this.lfDriveM  = hwMap.get(DcMotor.class, "fl_drive");
        this.rfDriveM = hwMap.get(DcMotor.class, "fr_drive");
        this.lbDriveM  = hwMap.get(DcMotor.class, "bl_drive");
        this.rbDriveM = hwMap.get(DcMotor.class, "br_drive");
        lfDriveM.setDirection(DcMotor.Direction.FORWARD);
        lbDriveM.setDirection(DcMotor.Direction.FORWARD);
        rfDriveM.setDirection(DcMotor.Direction.REVERSE);
        rbDriveM.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        stop();

        // Set all motors to run without encoders.
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
