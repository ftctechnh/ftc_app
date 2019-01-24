package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

class DriveEngine {
    ArrayList<DcMotor> motors = new ArrayList<>();

    Sensors sensors;

    static double wheelDiameter = 5;
    static double robotRadius = 7.15;

    private double initialAngle = 0;
    private double motorSpacing;

    private static final double ticksPerRev = 1120;
    private static double inPerRev = Math.PI * wheelDiameter;
    static final double inPerTicks = inPerRev / ticksPerRev;

    double spinAve = 0;
    double rAve = 0;
    double thetaAve = 0;
    private double theta = 0;
    private double forward = 0;
    private ElapsedTime timer;
    Telemetry telemetry;

    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors, int numMotors) {
        this.telemetry = telemetry;
        this.sensors = sensors;

        switch (numMotors) //order matters, we go counterclockwise
        {
            case 4:
                motors.add(hardwareMap.dcMotor.get("back"));
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("front"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI / 2;
                break;
            case 3:
                motors.add(hardwareMap.dcMotor.get("back"));
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI * 2/3;
                break;
            case 2:
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI;
                initialAngle = Math.PI / 2;
                break;
            default:
                //starts at zero, motor0 is on x-axis.
                for (int i = 0; i < numMotors; i++) {
                    motors.add(hardwareMap.dcMotor.get("motor" + i));
                }
        }

        for(DcMotor motor: motors)
        {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        timer = new ElapsedTime();
    }

    //If we ever use collinear motors
    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors, int[] numMotors) {
        this.telemetry = telemetry;
        this.sensors = sensors;

        int n = 0;
        for (int numMotor : numMotors) {
            if (numMotor == 0)
                ;

            else if (numMotor == 1)
                motors.add(hardwareMap.dcMotor.get("motor" + n));

            else {
                ArrayList<DcMotor> motorGroup = new ArrayList<>();
                for (int j = 0; j < numMotor; j++) {
                    motorGroup.add(hardwareMap.dcMotor.get("motor" + n));
                    n++;
                }
                motors.add(new CollinearMotorGroup(motorGroup));
            }
            n++;
        }


        for(DcMotor motor: motors)
        {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        timer = new ElapsedTime();
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

        double[] powers = new double[motors.size()];

        for (int i = 0; i < motors.size(); i++) {
            powers[i] = x * Math.sin(initialAngle + i * motorSpacing)
                    +   y * Math.cos(initialAngle + i * motorSpacing)
                    +   spin;
        }

        double max = MyMath.absoluteMax(powers);

        if(max > 1 || (op && Math.hypot(x,y) > .90))
        {
            for (int i = 0; i < powers.length; i++) {  // Adjust all motors to less than one
                powers[i] /= max;            // Or maximize a motor to one
            }
        }

        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        for (int i = 0; i < motors.size(); i++)
        {
            motors.get(i).setPower(powers[i]);
        }
    }


    void driveAtAngle(double theta)
    {
        this.theta = theta;
    }


    void floatMotors()
    {
        for (DcMotor motor: motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    void resetDistances()
    {
        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private double[] cumulativeDistance = new double[]{0,0};

    private ArrayList<Boolean> checkpoint = new ArrayList<>();
    private ArrayList<String> keyList = new ArrayList<>();
    boolean moveOnPath(double[] ... args){
        return moveOnPath(false, true, args);
    }
    boolean moveOnPath(String key, double[] ... args){
        if(keyList.contains(key))
            return true;
        if(moveOnPath(false, true, args)){
            keyList.add(key);
            return true;
        }
        return false;
    }
    boolean moveOnPath(boolean continuous, boolean correctSpin, double[] ... args)
    {
        reportPositionsToScreen();

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

        double targetAngle = forward;
        switch (args[c].length)
        {
            case 1:
                targetAngle = forward + args[c][0];
                rotate(face(targetAngle));
                if(Math.abs(MyMath.loopAngle(targetAngle, spinAngle())) < 2 * Math.PI /180) {
                    stop();
                    forward += args[c][0];
                    sumSE = 0;
                    checkpoint.set(c, true);
                    cumulativeDistance = new double[]{0,0};
                    resetDistances();
                }
                break;
            case 3:
                targetAngle = forward + args[c][2];
            case 2:
                double[] point = args[c];
                double spin = correctSpin? face(targetAngle) : 0;

                double deltaX = point[0] - xDist();
                double deltaY = point[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                double[] drive = move(deltaX, deltaY);
                drive = smoothDrive(drive[0], drive[1], 2, false);

                double driveX = drive[0];
                double driveY = drive[1];

                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("currentX", xDist());
                telemetry.addData("currentY", yDist());

                if(r <= .5 || Math.hypot(driveX, driveY) == 0) { //happens when theta changes rapidly
                    if(continuous)
                        drive(driveX, driveY, spin);
                    else {
                        stop();
                        if(args[c].length == 3) {
                            forward += args[c][2];
                            sumSE = 0;
                        }
                        this.checkpoint.set(c, true);
                        cumulativeDistance[0] += args[c][0];
                        cumulativeDistance[1] += args[c][1];
                    }
                }
                else
                    drive(driveX, driveY, spin);
                break;
        }
        return false;
    }

    double[] smoothDrive(double x, double y, double rSeconds, boolean smoothTheta)
    {
        double alpha = Bogg.getAlpha(rSeconds);
        double thetaAlpha = Bogg.getAlpha(.25);
        double r = Math.hypot(x,y);
        double theta = Math.atan2(y, x);

        if(r > 1)
            r = 1;
        if(r == 0)
            rAve = 0;
        else
            rAve = alpha * r + (1-alpha) * rAve;

        if(thetaAve > Math.PI)  thetaAve -= 2 * Math.PI;
        if(thetaAve < -Math.PI) thetaAve += 2 * Math.PI;

        //find the nearest value: must be within pi
        if(theta - thetaAve > Math.PI) theta -= 2 * Math.PI;
        if(thetaAve - theta > Math.PI) theta += 2 * Math.PI;

        thetaAve = thetaAlpha * theta + (1-thetaAlpha) * thetaAve;
        telemetry.addData("Math.cos(smoothTheta? thetaAve : theta) * rAve", Math.cos(smoothTheta? thetaAve : theta) * rAve);
        telemetry.addData("Math.sin(smoothTheta? thetaAve : theta) * rAve", Math.sin(smoothTheta? thetaAve : theta) * rAve);

        return new double[]{Math.cos(smoothTheta? thetaAve : theta) * rAve, Math.sin(smoothTheta? thetaAve : theta) * rAve};
    }


    double smoothSpin(double spin)
    {
        double alpha = Bogg.getAlpha(4);
        if(spin * spinAve < 0 || spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
    }


    double faceForward(){
        return face(forward, true);
    }
    double face(double target)
    {
        double current = spinAngle();
        telemetry.addData("current angle", current);
        if(target == forward)
            return face(forward, true);

        double deltaAngle = Math.abs(MyMath.loopAngle(target, current));

        if(deltaAngle < Math.PI * 5 / 180) //if we are in the controllable zone
            return face(target, true);

        sumSE = 0;  //if we need to catch up
        return face(target,false);
    }

    private double sumSE = 0;
    private double lastSE = 0;
    private double lastSt = 0;

    double sP = .25; // .25 per radian
    double sI = 8; //Time to correct past error
    double sD = .05; //fully account for this much time in the future at current error decreasing rate

    private double face(double angle, boolean Integral)
    {
        //    u(t) = MV(t) = P *( e(t) + 1/I* integral(0,t) (e(tau) *dtau) + 1/D *de(t)/dt )
        //    where
        //    P is the proportional gain, a tuning parameter,
        //    I is the time to correct past error, a tuning parameter,
        //    D is the time the equation predicts to correct for future error, a tuning parameter,
        //    e(t) = set point - current point
        //    t is the time or instantaneous time
        //    tau is the variable of integration (takes on values from time zero to the present t).
        //power per degree
        double i = Integral? sI : 10000;

        double e = MyMath.loopAngle(angle, spinAngle());
        double de = e - lastSE;
        double t = timer.seconds();
        double dt = t - lastSt;
        sumSE += e * dt;

        double power = sP * (e  +  1/ i * sumSE +  sD * de/dt);
        //e is current error, sumSE is the integral, de/dt is the derivative

        lastSE = e;
        lastSt = t;
        return power;
    }
    void resetForward()
    {
        forward = spinAngle();
        sumSE = 0;
    }


    private double lastR = 0;
    private double lastTheta = 0;
    private double lastT = 0;
    double mP = .01; //power per inch
    double mD = 2;  //fully account for this much time in the future at current error decreasing rate

    double[] move(double deltaX, double deltaY)
    {
        //    u(t) = MV(t) = P *( r(t) + 1/D *dr(t)/dt )
        //    where
        //    P is the proportional gain, a tuning parameter,
        //    D is the time the equation predicts to correct for future error, a tuning parameter,
        //    r(t) = set point - current point
        //    t is the time or instantaneous time

        double r = Math.hypot(deltaX, deltaY);
        double theta = Math.atan2(deltaY, deltaX) * .25 + lastTheta * .75;
        telemetry.addData("radius", r);
        double dr = r - lastR;
        double t = timer.seconds();
        double dt = t - lastT;
        double dTheta = theta - lastTheta;
        telemetry.addData("theta", theta);
        telemetry.addData("dTheta", dTheta);

//        if(Math.abs(dTheta / dr) > 2){ //Only possible when there is a significant circle-tangent movement
//            telemetry.addData("dtheta/dr", dTheta/dr);
//            return new double[]{0,0};
//        }

        double power = mP * (r +  mD * dr/dt);
        telemetry.addData("moveD term", mD * dr/dt);
        telemetry.addData("power", power);
        //r is current error, dr/dt is the derivative
        if(power > .7) power = .7;


        lastR = r;
        lastT = t;
        telemetry.addData("cos(theta) * power", Math.cos(theta) * power);
        telemetry.addData("sin(theta) * power", Math.sin(theta) * power);

        return new double[]{Math.cos(theta) * power, Math.sin(theta) *  power};
    }

    

    void orbit(double radius, double angle, double speed)
    {
        telemetry.addData("orbit radius", radius);
        radius /= robotRadius;

        double[] powers = new double[motors.size()];

        for (int i = 0; i < powers.length; i++) {
            powers[i] = 1 + radius * Math.sin(angle + i * motorSpacing);
        }

        double max = MyMath.absoluteMax(powers);

        for (int i = 0; i < motors.size(); i++) {
            motors.get(i).setPower(powers[i] * speed / max);
        }
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

    /**
     *
     * @param angle
     * @return
     */
    double getDistance(double angle)
    {
        return (xDist() * Math.cos(angle) + yDist() * Math.sin(angle)) * inPerTicks;
    }

    ArrayList<Double> xDistances = new ArrayList<>();
    double xDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.cos(i));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.cos(i);
        }

        telemetry.addData("xDist", sum / smallSum);
        xDistances.add(sum / smallSum);

        if(xDistances.size() > 5) //keep the size to 5
            xDistances.remove(0);

        return MyMath.median(xDistances);
    }

    ArrayList<Double> yDistances = new ArrayList<>();
    double yDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.sin(i));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.sin(i);
        }

        double yDistance = sum / smallSum;
        telemetry.addData("yDist", yDistance);

        yDistances.add(yDistance);
        if(yDistances.size() > 5) //keep the size to 5
            yDistances.remove(0);

        return MyMath.median(yDistances);
    }

    double spinAngle()
    {
        if(sensors.usingImu)
            return sensors.getImuHeading();

        else
        {
            double sum = 0;
            for (int i = 0; i < motors.size(); i++)
                sum += motors.get(i).getCurrentPosition();

            return sum / motors.size();
        }
    }

    void reportPositionsToScreen()
    {
        for (int i = 0; i < motors.size(); i++) {
            telemetry.addData("motor " + i + " position", motors.get(i).getCurrentPosition());
        }
    }

}