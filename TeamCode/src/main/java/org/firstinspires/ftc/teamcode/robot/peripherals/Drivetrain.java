package org.firstinspires.ftc.teamcode.robot.peripherals;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;

/**
 * Created by Derek on 2/7/2018.
 */

public interface Drivetrain extends Peripheral {
    enum DriveMode {
        INTEGRATOR,STATIC;
    }

    DriveMode getDriveMode();

    void setDriveMode(DriveMode driveMode);

    Vec3 getIntegrator();

    void setIntegrator(Vec3 integrator);
}
