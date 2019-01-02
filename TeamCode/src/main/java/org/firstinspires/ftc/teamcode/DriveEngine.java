package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.MotionDetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    static double wheelDiameter = 5;
    static double robotRadius = 8;

    double motorSpacing = 2 * Math.PI /3;
    double root3 = Math.sqrt(3);
    double pi = Math.PI;

    private static final double ticksPerRev = 1120;
    private static double inPerRev = Math.PI * wheelDiameter;
    static final double inPerTicks = inPerRev / ticksPerRev;

    private double theta;
    private double cumulativeSpin = 0;
    Telemetry telemetry;

    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        back = hardwareMap.dcMotor.get("back");
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        theta = 0;

        back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection(DcMotor.Direction.FORWARD);

        back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void drive(double... args) {
        drive(false,args);
    }

    void rotate(double spin){
        drive(false, spin);
    }

    void drive(boolean op, double ... args) {
        double x = 0,y = 0,spin = 0;
        switch (args.length)
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
                return;
        }
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        x = xprime;
        y = yprime;

        double r = Math.hypot(x,y);
        if(r > 1)
        {
            x /= r;
            y /= r;
            r = 1;
        }

        double root3 = Math.sqrt(3);
        double backPower  = x                + spin;
        double rightPower = -x/2 + y*root3/2 + spin;
        double leftPower  = -x/2 - y*root3/2 + spin;

        double max = Math.max(Math.max(Math.abs(backPower),Math.abs(rightPower)),Math.abs(leftPower));
        if(max > 1)
        {
            backPower  /= max;
            rightPower /= max;
            leftPower  /= max;
        }

        if(op && r > .90)
        {
            backPower  *= 1 / max;
            rightPower *= 1 / max;
            leftPower  *= 1 / max;
        }

        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        back.setPower(backPower);
        right.setPower(rightPower);
        left.setPower (leftPower);
    }

    void driveAtAngle(double theta)
    {
        this.theta = theta;
    }

    void resetDistances()
    {
        cumulativeSpin += spinAngle();

        back.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private ArrayList<Boolean> checkpoint = new ArrayList<>();

    boolean moveOnPath(double[] ... args)
    {
        if(checkpoint.size() == 0){
            for (int i = 0; i < args.length; i++)
                checkpoint.add(false);

            resetDistances();
        }

        int c = 0;
        for(boolean b: checkpoint)
            if(b)
                c++;

        telemetry.addData("checkpoint count", c);
        if(c == checkpoint.size())
        {
            checkpoint.clear();
            return true;
        }

        double spin;
        switch (args[c].length)
        {
            case 1:
                spin = spinToTarget(args[c][0]);
                rotate(spin);
                if(Math.abs(spin) -.02 < 1 * Math.PI /180) {
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
            case 2:
                double[] point = args[c];
                spin = spinToTarget(point[2]);

                double deltaX = point[0] - xDist();
                double deltaY = point[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("radius", r);
                telemetry.addData("error correctability", cumulativeSpin);

                if(r > 4) {
                    drive(false, deltaX / r * .2, deltaY / r * .2, spin);
                }
                else if(r > .5) {
                    drive(false, deltaX / r * .1, deltaY / r * .1, spin);
                }
                else if(r <= .5){
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
        }
        return false;
    }

    private double spinToTarget(double targetAngle)
    {
        telemetry.addData("current angle", spinAngle());
        double deltaAngle = targetAngle - spinAngle();

        return Math.signum(deltaAngle) * Math.max(Math.abs(deltaAngle)/2, .3);
    }

    double spinToZero()
    {
        telemetry.addData("current angle", spinAngle());
        double deltaAngle = 0 - spinAngle();

        return Math.signum(deltaAngle) * Math.max(Math.abs(deltaAngle)/2, .3);
    }

    void orbit(double radius, double angle, double speed)
    {
        radius /= robotRadius;
        double backPower = 1 + radius * Math.sin(angle);
        double rightPower = 1 + radius * Math.sin(angle + motorSpacing);
        double leftPower = 1 + radius * Math.sin(angle - motorSpacing);

        double max = Math.max(Math.abs(backPower),Math.max(Math.abs(rightPower),Math.abs(leftPower)));

        back.setPower(backPower * speed / max);
        right.setPower(rightPower * speed / max);
        left.setPower(leftPower * speed / max);
    }


//    X = B—L/2—R/2
//    Y = R*√3/2—L*√3/2   These are the force and torque equations
//    S = B+R+L

//    B = X*2/3 + S/3
//    R = Y*√3/3 – X/3 + S/3  These are solved for the motor powers
//    L = -Y*√3/3 – X/3 + S/3


    //No matter how we changed the power to the motors,
    // the distances will still follow these equations, divided by a certain magnitude.
    //For example, if there were two motors pointed one direction, we could add them and divide by two.
    //If they were pointing opposite direction, we would add one, subtract one, and divide by two.
    //So this magnitude is the "number of motors" pointed in that direction.

    // Say that we want to move the robot a distance of 1 in the direction of x.
    // Find the distance each motor must move to reach x==1, call it d.
    // 1 divided by d is the fraction of the distance in the direction of x.
    // Assign this as a coefficient to each motor.
    // Create a constant equal to the sum of the terms, multiplying each coefficient by one.
    // Call this constant k.
    // k is therefore equal to the number of motors involved, and dividing by k will give 1.
    // For any distance x, dividing the total x-distance by the number of motors will give x.

//    X = (B—L/2—R/2) /3
//    Y = (R*√3/2—L*√3/2) /2   These are the distance equations
//    S = (B+R+L) /3


    double getDistance(double angle)
    {
        double[] coefficients = new double[]
                {Math.cos(angle), Math.cos(angle + 2*pi/3), Math.cos(angle + 4*pi/3)};
        double motors = 0;
        for (double c: coefficients) {
            motors += c==0 ? 0 : 1;
        }
        return dotProduct(distances(), coefficients) / motors * DriveEngine.inPerTicks;
    }

    double xDist()
    {
        return getDistance(theta);
    }

    double yDist()
    {
        return getDistance(theta - pi/2); // sin(theta) == cos(theta - pi/2)
    }

    double spinAngle()
    {
        double distance = dotProduct(distances(),new double[]{1,1,1})
                /3 * DriveEngine.inPerTicks;
        return distance / robotRadius; //TODO: Find radius
    }

    private double[] distances()
    {
        return new double[]{
                back.getCurrentPosition(),
                right.getCurrentPosition(),
                left.getCurrentPosition()};
    }

    private double dotProduct(double[] distances, double[] coefficients)
    {
        double sum = 0;
        for(int i = 0; i < distances.length; i++)
        {
            sum += distances[i] + coefficients[i];
        }
        return sum;
    }
}