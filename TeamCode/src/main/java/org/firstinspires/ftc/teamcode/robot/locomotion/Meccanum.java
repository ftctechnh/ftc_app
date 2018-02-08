package org.firstinspires.ftc.teamcode.robot.locomotion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;
import org.firstinspires.ftc.teamcode.robot.peripherals.Drivetrain;

import java.util.Map;

/**
 * Created by Derek on 12/7/2017.
 */

public class Meccanum implements Drivetrain {
    DcMotor A,B,C,D;
    DriveMode driveMode = DriveMode.STATIC;


    @Override
    public void update() {

    }

    @Override
    public DriveMode getDriveMode() {
        return null;
    }

    @Override
    public void setDriveMode() {

    }

    @Override
    public Vec3 getIntegrator() {
        return null;
    }

    @Override
    public void setIntegrator() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean test() {
        return false;
    }
}
