package org.firstinspires.ftc.teamcode.robot.locomotion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;
import org.firstinspires.ftc.teamcode.robot.peripherals.Drivetrain;

import java.util.Map;

/**
 * Created by Derek on 12/7/2017.
 */

public class Meccanum implements Drivetrain {
    private DcMotor A,B,C,D;
    private DriveMode driveMode = DriveMode.STATIC;
    private Vec3 integrator;
    private String name;

    public Meccanum(String name,DcMotor A,DcMotor B,DcMotor C,DcMotor D) {
        this.name = name;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    @Override
    public void update() {
        double side = 0,angle = 0,forward = 0;
        switch (driveMode) {
            case STATIC:
                forward = integrator.y;
                side = integrator.x;
                angle = integrator.z;
                break;
            case INTEGRATOR:
                System.err.println("Not yet implemented, switching to static");
                driveMode = DriveMode.STATIC;
                break;
        }

        A.setPower((forward - side) + angle);
        B.setPower((forward + side) + angle);
        C.setPower((forward - side) - angle);
        D.setPower((forward + side) - angle);
    }

    @Override
    public DriveMode getDriveMode() {
        return driveMode;
    }

    @Override
    public void setDriveMode(DriveMode driveMode) {
        this.driveMode = driveMode;
    }

    @Override
    public Vec3 getIntegrator() {
        return new Vec3(integrator);
    }

    @Override
    public void setIntegrator(Vec3 integrator) {
        this.integrator = integrator.clamp(1);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean test() {
        return (A != null) && (B!=null) && (C!=null) && (D!= null);
    }
}
