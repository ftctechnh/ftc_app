package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

class DriveEngine {
    ArrayList<DcMotor> motors = new ArrayList<>();

    Sensors sensors;

    static double wheelDiameter = 5;
    static double robotRadius = 7.15;

    private double motorSpacing;

    static double ticksPerRev = 1120;
    static double inPerRev(){return Math.PI * wheelDiameter;}
    static double inPerTicks(){return inPerRev() / ticksPerRev;}

    double spinAve = 0;
    double rAve = 0;
    double thetaAve = 0;
    private double theta = 0;
    private double forward = 0;
    ElapsedTime timer;
    Telemetry telemetry;

    double trueX;
    double trueY;

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

    //If we ever use weirdly placed wheels
    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors, double[]... motorParams) {
        this.telemetry = telemetry;
        this.sensors = sensors;

        int n = 0;
        for (double[] motorParam : motorParams) {
            double radius = motorParam[0];
            double angle = motorParam[1];

            motors.add(hardwareMap.dcMotor.get("motor" + n));
        }


        for(DcMotor motor: motors)
        {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotor.Direction.FORWARD);
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

        for (int i = 0; i < motors.size(); i++)
            powers[i] = x * Math.cos(i * motorSpacing)
                    +   y * Math.sin(i * motorSpacing)
                    +   spin;


        double max = MyMath.absoluteMax(powers);

        if(max > 1 || (op && Math.hypot(x,y) > .90))
            for (int i = 0; i < powers.length; i++)   // Adjust all motors to less than one
                powers[i] /= max;            // Or maximize a motor to one



        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        for (int i = 0; i < motors.size(); i++) {
            motors.get(i).setPower(powers[i]);
            telemetry.addData("motor" + i + " power", powers[i]);
        }

        reportPositionsToScreen();
    }


    void driveAtAngle(double theta)
    {
        this.theta = theta;
    }


    void floatMotors()
    {
        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    boolean justRestarted;
    void resetDistances()
    {
        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        justRestarted = true;
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
        if(checkpoint.isEmpty()){
            for (double[] arg : args) checkpoint.add(false);
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
                if(Math.abs(MyMath.loopAngle(targetAngle, spinAngle())) < MyMath.radians(2)) {
                    stop();
                    forward += args[c][0];
                    sumSpinError = 0;
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

                double deltaX = point[0] + cumulativeDistance[0] - xDist();
                double deltaY = point[1] + cumulativeDistance[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                double[] drive = move(deltaX, deltaY);
                drive = smoothDrive(drive[0], drive[1], .2, false);

                double driveX = drive[0];
                double driveY = drive[1];

                telemetry.addData("deltaX", deltaX);
                telemetry.addData("deltaY", deltaY);

                if(r <= .5 || Math.hypot(driveX, driveY) == 0) { //happens when theta changes rapidly
                    if(continuous)
                        drive(driveX, driveY, spin);
                    else {
                        stop();
                        if(args[c].length == 3) {
                            try {
                                forward += args[c][2];
                            }
                            catch (ArrayIndexOutOfBoundsException i){
                                telemetry.addLine("In case 2");
                            }
                            sumSpinError = 0;
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

        return new double[]{Math.cos(smoothTheta? thetaAve : theta) * rAve,
                            Math.sin(smoothTheta? thetaAve : theta) * rAve};
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

        if(deltaAngle < MyMath.radians(5)) //if we are in the controllable zone
            return face(target, true);

        else
            sumSpinError = 0;  //if we need to catch up
            return face(target,false);
    }

    private double sumSpinError = 0;
    private double lastSpinError = 0;
    private double lastSpinTime = 0;

    double sP = .20; // .25 per radian
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
        double de = e - lastSpinError; //change in angle
        double t = timer.seconds();
        double dt = t - lastSpinTime;  //change in t
        sumSpinError += e * dt;        //cumulative error

        double power = sP * (e  +  1/ i * sumSpinError +  sD * de/dt);
        //e is current error, sumSpinError is the integral, de/dt is the derivative

        lastSpinError = e;
        lastSpinTime = t;
        return power;
    }
    void resetForward()
    {
        forward = spinAngle();
        sumSpinError = 0;
    }


    private double lastR = 0;
    private double lastTheta = 0;
    private double lastT = 0;
    double mP = .015; //power per inch
    double mD = 0.04;  //fully account for this much time in the future at current error decreasing rate

    ArrayList<Double> drdtArray = new ArrayList<>();
    double[] move(double deltaX, double deltaY)
    {
        //    u(t) = MV(t) = P *( r(t) + 1/D *dr(t)/dt )
        //    where
        //    P is the proportional gain, a tuning parameter,
        //    D is the time the equation predicts to correct for future error, a tuning parameter,
        //    r(t) = set point - current point
        //    t is the time or instantaneous time

        double r = Math.hypot(deltaX, deltaY);
        double theta = Math.atan2(deltaY, deltaX);
        telemetry.addData("radius", r);
        double dr = r - lastR;
        double t = timer.seconds();
        double dt = t - lastT;

        double drdt = dr / dt;
        drdtArray.add(drdt);

        if(drdtArray.size() > 7) //keep the size to 7
            drdtArray.remove(0);

        double dTheta = MyMath.loopAngle(theta, lastTheta);
        telemetry.addData("theta", theta);
        telemetry.addData("dTheta", dTheta);

//        if(Math.abs(dTheta / dt) > 2){ //2 rad per second
//            telemetry.addData("dtheta/dt", dTheta/dt);
//            return new double[]{0,0};
//        }

        double power = mP * (r +  mD * MyMath.ave(drdtArray));
        telemetry.addData("moveD term", mD * MyMath.ave(drdtArray));
        telemetry.addData("power", power);
        telemetry.addData("moveD / power",mD * MyMath.ave(drdtArray) / power);

        if(power > .4) power = .4;
        //Don't worry, it's smoothed

        lastR = r;
        lastT = t;

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
        return (xDist() * Math.cos(angle) + yDist() * Math.sin(angle)) * inPerTicks();
    }

    ArrayList<Double> xDistances = new ArrayList<>();
    double xDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.cos(i * motorSpacing));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.cos(i * motorSpacing);
        }

        double xDistance = sum / smallSum * inPerTicks();
        telemetry.addData("xDist", xDistance);
        xDistances.add(xDistance);

        if(xDistances.size() > 5) //keep the size to 5
            xDistances.remove(0);

        return MyMath.median(xDistances);
    }

    ArrayList<Double> yDistances = new ArrayList<>();
    double yDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.sin(i * motorSpacing));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.sin(i * motorSpacing);
        }

        double yDistance = sum / smallSum * inPerTicks();
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

    double lastX = 0;
    double lastY = 0;
    void reportPositionsToScreen()
    {
        for (int i = 0; i < motors.size(); i++) {
            telemetry.addData("motor " + i + " position", motors.get(i).getCurrentPosition());
        }

        double x = xDist();
        double y = yDist();
        double spin = spinAngle();
        double dX = x - lastX;
        double dY = y - lastY;

        if(justRestarted){
            justRestarted = false;
            dX = 0;
            dY = 0;
        }

        double xPrime = dX * Math.cos(spin) - dY * Math.sin(spinAngle());
        double yPrime = dX * Math.sin(spin) + dY * Math.cos(spinAngle());

        trueX += xPrime;
        trueY += yPrime;

        moveRobotOnScreen();

        lastX = x;
        lastY = y;
    }

    void moveRobotOnScreen()
    {
        FtcRobotControllerActivity.moveRobot(trueX * 4.5, -trueY * 5.5, MyMath.degrees(spinAngle()));
    }
}