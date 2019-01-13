package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

import Jama.Matrix;

class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    static double wheelDiameter = 5;
    static double robotRadius = 7.15;

    private double motorSpacing = 2 * Math.PI /3;
    private double root3 = Math.sqrt(3);

    private static final double ticksPerRev = 1120;
    private static double inPerRev = Math.PI * wheelDiameter;
    static final double inPerTicks = inPerRev / ticksPerRev;

    private double theta = 0;
    private double cumulativeSpin = 0;
    Telemetry telemetry;

    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        back = hardwareMap.dcMotor.get("back");
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");

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

    DriveEngine() {
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
    private ArrayList<String> keyList = new ArrayList<>();
    boolean moveOnPath(double[] ... args){
        return moveOnPath(false, args);
    }
    boolean moveOnPath(String key, double[] ... args){
        if(keyList.contains(key))
            return true;
        if(moveOnPath(false, args)){
            keyList.add(key);
            return true;
        }
        return false;
    }
    boolean moveOnPath(boolean continuous, double[] ... args)
    {
        telemetry.addData("back", back.getCurrentPosition());
        telemetry.addData("left", left.getCurrentPosition());
        telemetry.addData("right", right.getCurrentPosition());
        if(checkpoint.isEmpty()){
            for (double[] arg : args) checkpoint.add(false);
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

        double spinAngle = 0;
        double spin;
        switch (args[c].length)
        {
            case 1:
                spin = spinToTarget(args[c][0]);
                rotate(spin);
                if(Math.abs(spin) < 1 * Math.PI /180) {
                    stop();
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
            case 3:
                spinAngle = args[c][2];
            case 2:
                double[] point = args[c];
                spin = spinToTarget(spinAngle);

                double deltaX = point[0] - xDist();
                double deltaY = point[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("currentX", xDist());
                telemetry.addData("currentY", yDist());
                telemetry.addData("radius", r);

                if(r > 4) {
                    drive(deltaX / r * .15, deltaY / r * .15, spin);
                }
                else if(r > 2){
                    drive(deltaX * .15 / 2, deltaY * .15 /2, spin);
                }
                else if(r <= .5){
                    if(continuous)
                        drive(deltaX * .15/2, deltaY * .15/2, spin);
                    else {
                        stop();
                        this.checkpoint.set(c, true);
                        resetDistances();
                    }
                }
                break;
        }
        return false;
    }

    private double[] cumulativeCheckpoints = new double[3];

    boolean moveOnCumulativePath(double[] ... args)
    {
        if(checkpoint.isEmpty()){
            for (double[] arg : args) checkpoint.add(false);
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
                if(Math.abs(spin) < 1 * Math.PI /180) {
                    stop();
                    cumulativeCheckpoints[2] += args[c][0];
                    cumulativeSpin -= args[c][0];
                    checkpoint.set(c, true);
                }
                break;
            case 2:
                double[] point = args[c];
                point[0] += cumulativeCheckpoints[0];
                point[1] += cumulativeCheckpoints[1];
                spin = spinToZero();

                double deltaX = point[0] - xDist();
                double deltaY = point[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("radius", r);
                telemetry.addData("error correctability", cumulativeSpin);

                if(r > 4) {
                    drive(deltaX / r * .15, deltaY / r * .15, spin);
                }
                else if(r > 2){
                    drive(deltaX * .15 / 2, deltaY * .15 /2, spin);
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

    double spinToTarget(double targetAngle)
    {
        telemetry.addData("current angle", spinAngle());
        double deltaAngle = targetAngle - spinAngle();

        return deltaAngle/2;
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



    // Say that the robot moves a distance of 1 in the direction of x.
    // Find the distance each motor moved, call it d.
    // Assign this as a coefficient to each motor.
    // Create a constant equal to the sum of the terms,
    // multiplying each coefficient by the distance each motor moved
    // Call this constant k.
    // Divide the equation by k to give 1 when x = 1.

//    B = X + S
//    R = Y*√3/2 – X/2 + S  These are the motor distances
//    L = -Y*√3/2 – X/2 + S

//    X = (B—R/2—L/2) * 2/3
//    Y = (R*√3/2—L*√3/2) * 2/3  These are solved for the directional distances
//    S = (B+R+L) /3


    double getDistance(double angle)
    {
        DcMotor[] motors = new DcMotor[]{back,right,left};
        Matrix powers = new Matrix(motors.length,3);
        for (int i = 0; i < motors.length; i++) {                   //for each motor
            powers.set(i, 0, Math.cos(theta + i* motorSpacing)); //sets x
            powers.set(i, 1, Math.sin(theta + i* motorSpacing)); //sets y
            powers.set(i, 2, 1);                              //sets spin
        }

        Matrix realDistances = new Matrix(distances(),1);

        Matrix directional = powers.solve(realDistances);   //solves for x y and spin
        double x = directional.get(0,0);
        double y = directional.get(0,1);
        double[] vectorA = new double[]{x,y};
        double[] vectorB = new double[]{Math.cos(angle), Math.sin(angle)};

        //finds the projection onto angle
        return dotProduct(vectorA, vectorB) / Math.hypot(x,y) * DriveEngine.inPerTicks;
    }

    double xDist()
    {
        double sum = 0;
        sum += back.getCurrentPosition() * 2/3 * DriveEngine.inPerTicks;
        sum += right.getCurrentPosition() * -1/3 * DriveEngine.inPerTicks;
        sum += left.getCurrentPosition() * -1/3 * DriveEngine.inPerTicks;
        telemetry.addData("xDist", sum);
        return sum;
    }

    double yDist()
    {
        double sum = 0;
        sum += back.getCurrentPosition() * 0 * DriveEngine.inPerTicks;
        sum += right.getCurrentPosition() * root3/3 * DriveEngine.inPerTicks;
        sum += left.getCurrentPosition() * -root3/3 * DriveEngine.inPerTicks;
        telemetry.addData("yDist", sum);
        return sum;
    }

    double spinAngle()
    {
        double sum = 0;
        sum += back.getCurrentPosition() /3 * DriveEngine.inPerTicks;
        sum += right.getCurrentPosition() /3 * DriveEngine.inPerTicks;
        sum += left.getCurrentPosition() /3 * DriveEngine.inPerTicks;
        return sum / robotRadius; //TODO: Find radius
    }

    double[] distances()
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
            sum += distances[i] * coefficients[i];
        }
        return sum;
    }
}