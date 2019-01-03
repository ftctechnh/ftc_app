package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    static double wheelDiameter = 5;
    static double robotRadius = 8;

    private double motorSpacing = 2 * Math.PI /3;
    private double root3 = Math.sqrt(3);

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

        back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

    void stop(){
        drive(0);
    }

    void drive(boolean op, double ... args) {
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

        back.setPower(backPower);
        right.setPower(rightPower);
        left.setPower (leftPower);
    }

    void driveTrue(double ... args) {
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
                stop();
                return;
        }
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        x = xprime;
        y = yprime;

        double root3 = Math.sqrt(3);
        double backPower  = 2*x              + spin;
        double rightPower = -x + y*root3*2/3 + spin;
        double leftPower  = -x - y*root3*2/3 + spin;

        double max = Math.max(Math.max(Math.abs(backPower),Math.abs(rightPower)),Math.abs(leftPower));
        if(max > 1)
        {
            backPower  /= max;
            rightPower /= max;
            leftPower  /= max;
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
            stop();
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
                    driveTrue(deltaX / r * .2, deltaY / r * .2, spin);
                }
                else if(r > .5) {
                    driveTrue(deltaX / r * .1, deltaY / r * .1, spin);
                }
                else if(r <= .5){
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
        }
        return false;
    }

    private double[] cumulativeCheckpoints = new double[3];

    boolean moveOnCumulativePath(double[] ... args)
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
            stop();
            return true;
        }

        double spin;
        switch (args[c].length)
        {
            case 1:
                spin = spinToTarget(cumulativeCheckpoints[2] + args[c][0]);
                rotate(spin);
                if(Math.abs(spin) -.02 < 1/2 * (2) * Math.PI /180) { //the two is in degrees, the rest is formula
                    cumulativeCheckpoints[2] += args[c][0];
                    cumulativeSpin -= args[c][0];
                    checkpoint.set(c, true);
                }
                break;
            case 2:
                double[] point = args[c];
                point[0] += cumulativeCheckpoints[0];
                point[1] += cumulativeCheckpoints[1];
                spin = spinToTarget(point[2]);

                double deltaX = point[0] - xDist();
                double deltaY = point[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("radius", r);
                telemetry.addData("error correctability", cumulativeSpin);

                if(r > 4) {
                    driveTrue( deltaX / r * .2, deltaY / r * .2, spin);
                }
                else if(r > .5) {
                    driveTrue(deltaX / r * .1, deltaY / r * .1, spin);
                }
                else if(r <= .5){
                    cumulativeCheckpoints[0] += args[c][0];
                    cumulativeCheckpoints[1] += args[c][1];
                    checkpoint.set(c, true);
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
        return spinToTarget(0);
    }

    void orbit(double radius, double angle, double speed)
    {
        telemetry.addData("orbit radius", radius);
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

//    B = X*2 + S
//    R = Y*2/3*√3 – X + S  These are solved for the motor distances
//    L = -Y*2/3*√3 – X + S


    double getDistance(double angle)
    {
        double[] coefficients = new double[]
                {Math.cos(angle), Math.cos(angle + motorSpacing), Math.cos(angle - motorSpacing)};
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
        double pi = Math.PI;
        return getDistance(theta - pi /2); // sin(theta) == cos(theta - pi/2)
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