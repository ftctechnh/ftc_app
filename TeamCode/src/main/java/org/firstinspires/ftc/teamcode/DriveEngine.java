package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

class DriveEngine {
    ArrayList<DcMotor> motors = new ArrayList<>();

    Sensors sensors;

    static double effectiveWheelDiameter = 5.32;
    static double effectiveRobotRadius = 7.15;

    private double motorSpacing;

    static double ticksPerRev = 1120;
    static double inPerRev(){return Math.PI * effectiveWheelDiameter;}
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


    DriveEngine() {
    }

    ArrayList<double[][]> potentials = new ArrayList<>();
    ArrayList<Integer> precedences = new ArrayList<>();

    void stop(){
        drive(2,false, 0.);
    }

    void rotate(double spin){
        drive(spin);
    }

    void rotate(double precedence, double spin){
        drive(precedence, spin);
    }

    void drive(double... args) {
        drive(false,args);
    }

    void drive(boolean op, double ... args){drive(0, op, args);}

    void drive(int precedence, boolean op, double ... args) {
        if(precedences.size() != 0){
            if (precedence < MyMath.max(precedences))
                return;
            if(MyMath.absoluteMax(args) == 0)
                return;
        }
        precedences.add(precedence);
        double[][] potential = new double[2][];
        potential[0] = new double[]{op? 1.:0.};
        potential[1] = args;
        potentials.add(0, potential);
    }

    void update(){
        telemetry.addLine("updating");
        drive();
        precedences.clear();
        potentials.clear();
        smoothAdditions = 0;
    }
    protected void drive(){
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
        reportPositionsToScreen();
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

    ArrayList<Boolean> checkpoints = new ArrayList<>();
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

    /*
        Continuous means that the function will never return true and restart,
        it will continue to correct for error.

        c: true  – When you continually test moving to different points.
        c: false – When the code changes to a new task after completion.
     */
    boolean moveOnPath(boolean continuous, double[] ... args)
    {
        if(checkpoints.isEmpty()){
            for (double[] arg : args) checkpoints.add(false);
        }

        int c = 0;
        for(boolean b: checkpoints)
            if(b)
                c++;

        telemetry.addData("checkpoints count", c);
        if(c == checkpoints.size())
        {
            checkpoints.clear();
            return true;
        }

        double targetAngle = forward;
        switch (args[c].length)
        {
            case 1:
                targetAngle = forward + args[c][0];

                if(Math.abs(MyMath.loopAngle(targetAngle, spinAngle())) < MyMath.radians(2)) {
                    if(continuous && c == checkpoints.size() - 1)
                        ;
                    else{
                        stop();
                        forward += args[c][0];
                        sumSpinError = 0;
                        checkpoints.set(c, true);
                        cumulativeDistance = new double[]{0,0};
                        resetDistances();
                        break;
                    }
                }
                rotate(face(targetAngle));
                break;
            case 3:
                targetAngle = forward + args[c][2];

            case 2:
                double[] point = args[c];
                double spin = face(targetAngle);

                double deltaX = point[0] + cumulativeDistance[0] - xDist();
                double deltaY = point[1] + cumulativeDistance[1] - yDist();
                double r = Math.hypot(deltaX, deltaY);

                double[] drive = move(deltaX, deltaY);

                //smooth the driving when revving to a high speed, then reset the average to 0.
                if(Math.hypot(drive[0],drive[1]) > .3)
                    drive = smoothDrive(drive[0], drive[1], 1, false);
                else
                    smoothDrive(0,0, 1, false);

                double driveX = drive[0];
                double driveY = drive[1];

                telemetry.addData("deltaX", deltaX);
                telemetry.addData("deltaY", deltaY);

                if(r <= .75 || Math.hypot(driveX, driveY) == 0) { //happens when theta changes rapidly
                    if(continuous && c == checkpoints.size() - 1) {
                        drive(driveX, driveY, spin);
                        drdtArray = new ArrayList<>();
                        dThdtArray = new ArrayList<>();
                    }
                    else {
                        stop();
                        if(args[c].length == 3) {
                            forward += args[c][2];
                            sumSpinError = 0;
                        }
                        this.checkpoints.set(c, true);
                        cumulativeDistance[0] += args[c][0];
                        cumulativeDistance[1] += args[c][1];
                        drdtArray = new ArrayList<>();
                        dThdtArray = new ArrayList<>();
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

        if(thetaAve >  Math.PI) thetaAve -= 2 * Math.PI;
        if(thetaAve < -Math.PI) thetaAve += 2 * Math.PI;

        //find the nearest value: must be within pi
        if(theta - thetaAve > Math.PI) theta -= 2 * Math.PI;
        if(thetaAve - theta > Math.PI) theta += 2 * Math.PI;

        thetaAve = thetaAlpha * theta + (1-thetaAlpha) * thetaAve;

        return new double[]{Math.cos(smoothTheta? thetaAve : theta) * rAve,
                            Math.sin(smoothTheta? thetaAve : theta) * rAve};
    }

    ArrayList<Double> smoothRList = new ArrayList<>();
    ArrayList<Double> smoothThetaList = new ArrayList<>();
    ArrayList<Double> smoothSpinList = new ArrayList<>();
    double[] smoothDrive2(double x, double y, double spin, double rSeconds, boolean smoothTheta)
    {
        int rListSize = (int)Math.round(rSeconds / Bogg.averageClockTime);
        int thetaListSize = (int)Math.round(.66 / Bogg.averageClockTime);
        int spinListSize = (int)Math.round(4 / Bogg.averageClockTime);

        while(smoothRList.size() > rListSize)
            smoothRList.remove(0);

        while(smoothThetaList.size() > thetaListSize)
            smoothThetaList.remove(0);

        while(smoothSpinList.size() > spinListSize)
            smoothSpinList.remove(0);

        double r = Math.hypot(x,y);
        double theta = Math.atan2(y, x);

        if(r > 1)
            r = 1;
        if(r == 0) {
            for (int i = 0; i < rListSize; i++)
                smoothRList.set(i, 0.0);

            smoothThetaList.clear();
        }

        if(spin > 1)
            spin = 1;
        if(spin == 0) {
            for (int i = 0; i < spinListSize; i++)
                smoothSpinList.set(i, 0.0);
        }

        smoothRList.add(r);
        smoothSpinList.add(spin);

        thetaAve = 0;
        for (int n = 0; n < thetaListSize; n++) {
            thetaAve += MyMath.loopAngle(smoothThetaList.get(n), thetaAve) / (n+1);
        }

        drive(precedence, op,Math.cos(theta) * r, Math.sin(theta) * r, spin);
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

        if(MyMath.degrees(deltaAngle) < 5) //if we are in the controllable zone
            return face(target, true);

        else
            sumSpinError = 0;  //if we need to catch up
            return face(target,false);
    }

    private double sumSpinError = 0;
    private double lastSpinError = 0;
    private double lastSpinTime = 0;

    double sP = .20; // .20 per radian
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
    double mD = 0.1;  //fully account for this much time in the future at current error decreasing rate
    double tD = 0.05;

    ArrayList<Double> drdtArray = new ArrayList<>();
    ArrayList<Double> dThdtArray = new ArrayList<>();

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
        double dTheta = MyMath.loopAngle(theta, lastTheta);

        double drdt = dr / dt;
        drdtArray.add(drdt);

        if(drdtArray.size() > 5) //keep the size to 5
            drdtArray.remove(0);


        double dThdt =  dTheta / dt;
        dThdtArray.add(dThdt);

        if(dThdtArray.size() > 11) //keep the size to 11
            dThdtArray.remove(0);


        telemetry.addData("theta", theta);
        telemetry.addData("dTheta", dTheta);

        //negative because our target is zero
        double tangentPower = -Range.clip(mP * (tD * MyMath.ave(dThdtArray) * r), -.1, .1);
        //dTheta*r / dt is in inches / sec
        //therefore tD is in seconds

        if(Math.abs(dTheta / dr / r ) > 2){ //2 rad per inch^2
            telemetry.addData("dtheta/dt / r", dTheta/dt/r);
            return new double[]{0,0};
        }

        double power = mP * (r +  mD * MyMath.ave(drdtArray));
        telemetry.addData("moveD term", mD * MyMath.ave(drdtArray));
        telemetry.addData("power", power);
        telemetry.addData("moveD / power",mD * MyMath.ave(drdtArray) / power);

        telemetry.addData("moveT term", tangentPower);
        if(power > .4) power = .4;
        //Don't worry, it's smoothed

        lastR = r;
        lastT = t;

        return new double[]{Math.cos(theta) * power - Math.sin(theta) * tangentPower,
                            Math.sin(theta) * power + Math.cos(theta) * tangentPower};
    }

    

    void orbit(double radius, double angle, double speed)
    {
        telemetry.addData("orbit radius", radius);
        radius /= effectiveRobotRadius;

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