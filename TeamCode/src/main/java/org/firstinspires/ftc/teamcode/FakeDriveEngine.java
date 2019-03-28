package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FakeDriveEngine extends OmniWheelDriveEngine {

    private double root3 = Math.sqrt(3);

    private double xDistance = 0, yDistance = 0, sDistance = 0;

    FakeDriveEngine(Telemetry telemetry)
    {
        this.telemetry = telemetry;
        timer = new ElapsedTime();
    }


    @Override
    double[] processMotorPowersFromDriveValues(double[] driveValues){
        boolean op = driveValues[0] == 1;
        double x = driveValues[1];
        double y = driveValues[2];
        double spin = driveValues[3];

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

        //Estimated 4 feet per second at full power
        double dX = (4 * 12 ) * x * Bogg.averageClockTime;
        double dY = (4 * 12 ) * y * Bogg.averageClockTime;
        double dS = (2 * 12 ) * spin * Bogg.averageClockTime;

        xDistance += dX;
        yDistance += dY;
        sDistance += dS;

        reportPositionsToScreen();

        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        telemetry.addData("backPower", backPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.addData("leftPower", leftPower);

        return new double[]{};
    }

    @Override
    void resetDistances() {
        xDistance = 0;
        yDistance = 0;
        justRestarted = true;
    }

    @Override
    void floatMotors() {}

    @Override
    double spinAngle() {
        return sDistance / effectiveRobotRadius;
    }


    @Override
    void orbit(double radius, double angle, double speed) {}

    @Override
    double xDist() {
        return xDistance;
    }

    @Override
    double yDist() {
        return yDistance;
    }

    @Override
    void reportPositionsToScreen() {
        double dX = xDist() - lastX;
        double dY = yDist() - lastY;

        if(justRestarted){
            justRestarted = false;
            dX = 0;
            dY = 0;
        }

        double spin = spinAngle();
        double xPrime = dX * Math.cos(spin) - dY * Math.sin(spin);
        double yPrime = dX * Math.sin(spin) + dY * Math.cos(spin);

        trueX += xPrime;
        trueY += yPrime;

        moveRobotOnScreen();

        lastX = xDist();
        lastY = yDist();
    }

}
