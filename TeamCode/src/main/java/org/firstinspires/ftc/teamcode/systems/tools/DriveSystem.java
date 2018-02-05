package org.firstinspires.ftc.teamcode.systems.tools;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Mahim on 1/12/2018.
 */

public class DriveSystem {
    private DcMotor frontRightMotor, rearRightMotor, frontLeftMotor, rearLeftMotor;

    public DriveSystem(DcMotor frontLeftMotor, DcMotor rearLeftMotor,
                DcMotor frontRightMotor, DcMotor rearRightMotor) {
        this.frontLeftMotor     = frontLeftMotor;
        this.rearLeftMotor      = rearLeftMotor;
        this.frontRightMotor    = frontRightMotor;
        this.rearRightMotor     = rearRightMotor;
    }

    public void setMode(DcMotor.RunMode runMode) {
        this.frontLeftMotor.setMode(runMode);
        this.rearLeftMotor.setMode(runMode);
        this.frontRightMotor.setMode(runMode);
        this.rearRightMotor.setMode(runMode);
    }

    public void stop() {
        this.frontLeftMotor.setPower(0.0);
        this.rearLeftMotor.setPower(0.0);
        this.frontRightMotor.setPower(0.0);
        this.rearRightMotor.setPower(0.0);
    }

    public boolean isBusy() {
        return  this.frontLeftMotor.isBusy()    && this.rearLeftMotor.isBusy() &&
                this.frontRightMotor.isBusy()   && this.rearRightMotor.isBusy();
    }
}
