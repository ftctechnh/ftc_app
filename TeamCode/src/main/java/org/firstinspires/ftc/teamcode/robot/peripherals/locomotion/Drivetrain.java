package org.firstinspires.ftc.teamcode.robot.peripherals.locomotion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;
import org.firstinspires.ftc.teamcode.robot.peripherals.Peripheral;

/**
 * Created by Derek on 2/7/2018.
 */

public interface Drivetrain extends Peripheral{

    enum DriveMode {
        INTEGRATOR,STATIC;
    }

    DriveMode getDriveMode();

    void setDriveMode(DriveMode driveMode);

    void setZeroPowerBehaivor(DcMotor.ZeroPowerBehavior powerBehavior);

    Vec3 getIntegrator();

    void stop();

    void setIntegrator(Vec3 integrator);

    void setRunMode(DcMotor.RunMode runMode);

    boolean isBusy();


}
