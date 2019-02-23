package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FakeDriveEngine extends DriveEngine {

    private double theta = 0;
    private double root3 = Math.sqrt(3);

    double xDistance = 0, yDistance = 0, sDistance = 0;

    FakeDriveEngine(HardwareMap hardwareMap, Telemetry telemetry)
    {
        this.telemetry = telemetry;
        timer = new ElapsedTime();
    }


    @Override
    protected void drive() {
        double x = 0,y = 0,spin = 0;

        if(potentials.size() == 0){
            potentials.add(new double[][]{new double[]{0}, new double[]{0}});
        }

        boolean op = potentials.get(0)[0][0] == 1;
        double[] args = potentials.get(0)[1];


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

        if(MyMath.absoluteMax(x, y, spin) == 0) {
            smoothThetaList.clear();
            MyMath.fill(smoothRList, 0);
            MyMath.fill(smoothSpinList, 0);
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

        //Estimated 4 feet per second at full power
        double dX = (1.5 * 12 ) * x * Bogg.averageClockTime;
        double dY = (1.5 * 12 ) * y * Bogg.averageClockTime;
        double dS = (1.5 * 12 ) * spin * Bogg.averageClockTime;
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
    }

    @Override
    void resetDistances() {
        xDistance = 0;
        yDistance = 0;
        justRestarted = true;
    }

    @Override
    void floatMotors() {
        ;
    }

    @Override
    double spinAngle() {
        return sDistance / effectiveRobotRadius;
    }


    @Override
    void orbit(double radius, double angle, double speed) {
        ;
    }

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
