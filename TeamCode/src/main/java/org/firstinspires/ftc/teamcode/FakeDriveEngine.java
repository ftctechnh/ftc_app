package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class FakeDriveEngine extends DriveEngine{

    private double theta = 0;
    private double cumulativeSpin = 0;
    private double motorSpacing = 2 * Math.PI /3;
    private double root3 = Math.sqrt(3);
    private ArrayList<Boolean> checkpoint = new ArrayList<>();

    double xDistance = 0, yDistance = 0, sDistance = 0;

    FakeDriveEngine(HardwareMap hardwareMap, Telemetry telemetry)
    {
        this.telemetry = telemetry;
    }

    @Override
    void drive(double... args) {
        drive(false, args);
    }

    @Override
    void rotate(double spin) {
        drive(false, spin);
    }

    @Override
    void stop() {
        drive(0);
    }

    @Override
    void drive(boolean op, double... args) {
        double x = 0,y = 0,spin = 0;
        switch (args.length)    //assign x, y and spin
        {
            case 3:
                spin = args[2];
            case 2:
                x = args[0];
                y = args[1];
                break;
            case 1:
                spin = args[0];
                break;
            default:
                stop();
                return;
        }
        double xPrime = x * Math.cos(theta) - y * Math.sin(theta); //adjust for angle
        double yPrime = x * Math.sin(theta) + y * Math.cos(theta);

        x = xPrime;
        y = yPrime;

        double backPower  = x                + spin;
        double rightPower = -x/2 + y*root3/2 + spin;
        double leftPower  = -x/2 - y*root3/2 + spin;

        double max = Math.max(Math.max(Math.abs(backPower),Math.abs(rightPower)),Math.abs(leftPower));
        if(max > 1 || (op && Math.hypot(x,y) > .90))
        {
            backPower  /= max;    // Adjust all motors to less than one
            rightPower /= max;    // Or maximize a motor to one
            leftPower  /= max;
        }

        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        telemetry.addData("backPower", backPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.addData("leftPower", leftPower);
    }


    @Override
    void resetDistances() {
        cumulativeSpin += spinAngle();
        xDistance = 0;
        yDistance = 0;
        sDistance = 0;
    }

    @Override
    double spinToTarget(double targetAngle) {
        return super.spinToTarget(targetAngle);
    }

    @Override
    double spinToZero() {
        return super.spinToZero();
    }

    @Override
    void orbit(double radius, double angle, double speed) {
        super.orbit(radius, angle, speed);
    }

    @Override
    double[] distances() {
        return super.distances();
    }
}
