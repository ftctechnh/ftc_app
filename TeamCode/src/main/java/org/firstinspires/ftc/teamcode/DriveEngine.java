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
    static void setEffectiveWheelDiameter(double inches, double ticks)
    {
        double revolutions = ticks / ticksPerRev;
        double circumference = inches / revolutions;
        effectiveWheelDiameter = circumference / Math.PI;
    }

    private double spinAve,rAve,thetaAve,theta,forward;
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
            //We reset the encoders to 0
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //We tell the motors to brake, not spin or float. They resist movement with friction.
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //The drive engine motors should always move forward. Always!
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            //We run using velocity, not power.
            //The motors do this by checking the speed using change in position over time.
            //They use PID control to keep the speed constant.
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        timer = new ElapsedTime();
    }

    //Used in the FakeDriveEngine
    DriveEngine() {
    }

    //Potential drive values
    ArrayList<double[][]> potentialDriveValues = new ArrayList<>();

    //Answers the question: which drive value do we choose?
    ArrayList<Integer> precedences = new ArrayList<>();

    //Explicit stopping takes high precedence
    void stop(){
        drive(2,false, 0.);
    }

    //Rotation is done in the same drive method.
    void rotate(double spin){
        drive(spin);
    }

    //We can also give a rotation precedence
    void rotate(double precedence, double spin){
        drive(precedence, spin);
    }

    //The... means you can put in any number of values.
    void drive(double... args) {
        drive(false,args);
    }

    //Op means overpowered: When op, a motor is maxed out if the overall power > .9
    void drive(boolean op, double ... args){drive(0, op, args);}

    void drive(int precedence, boolean op, double ... args) {
        //If we've already logged power values this loop
        if(precedences.size() != 0){
            //If our precedence is too low, we break out, no more math needed.
            if (precedence < MyMath.max(precedences))
                return;
            //Driving with non-zero values takes precedence over stopping.
            //If the values are zero, we break out.
            if(MyMath.absoluteMax(args) == 0)
                return;
        }
        //If we've made it to this point, we want to keep our drive values.
        //We save the precedence
        precedences.add(precedence);
        //We save op and args into an array.
        double[][] potential = new double[2][];
        potential[0] = new double[]{op? 1.:0.};
        potential[1] = args;
        //We put our drive values in the first spot in the potential ArrayList.
        potentialDriveValues.add(0, potential);
    }

    void update(){
        //If all is working, this line should appear.
        //It might not if the update method is left out of an OpMode.
        telemetry.addLine("updating");
        //One per loop, we update the motor powers.
        drive();
        //We prepare for the next loop by clearing one-loop lists and counters.
        precedences.clear();
        potentialDriveValues.clear();
        smoothAdditions = 0;
    }
    protected void drive(){
        double x = 0,y = 0,spin = 0;

        //If we haven't been given any drive values, we stop.
        if(potentialDriveValues.size() == 0){
            potentialDriveValues.add(new double[][]{new double[]{0}, new double[]{0}});
        }

        //We convert op back to a boolean: Does it equal one?
        boolean op = potentialDriveValues.get(0)[0][0] == 1;
        double[] args = potentialDriveValues.get(0)[1];

        switch (args.length)    //assign x, y and spin
        {
            case 3:
                spin = args[2];  //x,y,spin
            case 2:
                x = args[0];     //x,y
                y = args[1];
                break;
            case 1:
                spin = args[0];  //spin
                break;
            default:      //This shouldn't happen
                telemetry.addLine("Argument length of zero in drive");
                break;
        }

        if(MyMath.absoluteMax(x, y) == 0) {  //If we are to stop,
            smoothThetaList.clear();         //Reset our direction: no delay
            MyMath.fill(smoothRList, 0);  //Stop immediately
        }
        if(spin == 0)
            MyMath.fill(smoothSpinList, 0);

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


        blackValues = new double[]{x,y,spin};
        telemetry.addData("driveE x", x);
        telemetry.addData("driveE y", y);
        telemetry.addData("driveE rotate", spin);

        for (int i = 0; i < motors.size(); i++) {
            motors.get(i).setPower(powers[i]);
            telemetry.addData("motor" + i + " power", powers[i]);
        }

        reportPositionsToScreen();
    }

    double[] blackValues = new double[3];

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

    double[] cumulativeDistance = new double[]{0,0};

    boolean justResetTarget = false;
    ArrayList<Boolean> checkpoints = new ArrayList<>();
    private ArrayList<String> keyList = new ArrayList<>();
    boolean moveOnPath(double[] ... args){
        return moveOnPath(false, false,args);
    }
    boolean moveOnPath(String key, double[] ... args){
        if(keyList.contains(key))
            return true;
        if(moveOnPath(false, false, args)){
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
    boolean moveOnPath(boolean continuous, boolean absolute, double[] ... args)
    {
        if(checkpoints.isEmpty()){
            for (double[] arg : args) checkpoints.add(false);
            justResetTarget = true;
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
        double currentAngle = spinAngle();
        switch (args[c].length)
        {
            case 1:
                targetAngle = forward + args[c][0];

                if(Math.abs(MyMath.loopAngle(targetAngle, currentAngle)) < MyMath.radians(2)) {
                    if(continuous && c == checkpoints.size() - 1)
                        ;
                    else{
                        stop();
                        forward += args[c][0];
                        sumSpinError = 0;
                        checkpoints.set(c, true);
                        cumulativeDistance = new double[]{0,0};
                        resetDistances();
                        justResetTarget = true;
                        break;
                    }
                }
                rotate(face(targetAngle));
                justResetTarget = false;
                break;
            case 3:
                targetAngle = forward + args[c][2];

            case 2:
                double[] point = args[c];
                double spin = face(targetAngle);

                double deltaX;
                double deltaY;

                if(absolute)
                {
                    double angle = currentAngle;
                    double trueDeltaX = point[0] - trueX;
                    double trueDeltaY = point[1] - trueY;
                    deltaX =  trueDeltaX * Math.cos(-angle) - trueDeltaY * Math.sin(-angle);
                    deltaY =  trueDeltaX * Math.sin(-angle) + trueDeltaY * Math.cos(-angle);
                }
                else
                {
                    deltaX = point[0] + cumulativeDistance[0] - xDist();
                    deltaY = point[1] + cumulativeDistance[1] - yDist();
                }
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
                        justResetTarget = true;
                    }
                }
                else
                    drive(driveX, driveY, spin);
                justResetTarget = false;
                break;
        }
        return false;
    }

    boolean moveToAbsolutePosition(double x, double y)
    {
        double angle = spinAngle();
        double trueDeltaX = x - trueX;
        double trueDeltaY = y - trueY;
        double newDeltaX =  trueDeltaX * Math.cos(-angle) - trueDeltaY * Math.sin(-angle);
        double newDeltaY =  trueDeltaX * Math.sin(-angle) + trueDeltaY * Math.cos(-angle);
        return moveOnPath(new double[]{xDist() +newDeltaX - cumulativeDistance[0],
                yDist() +newDeltaY - cumulativeDistance[1]});
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
    private int smoothAdditions = 0;
    void smoothDrive2(boolean op, double x, double y, double spin,
                          double rSeconds, boolean smoothSpin, boolean smoothTheta, int precedence)
    {
        if(precedence < MyMath.max(precedences))
            return;
        if(MyMath.absoluteMax(x, y, spin) == 0) {
            drive(precedence, op, 0);
            return;
        }
        if(smoothAdditions > 0) {
            smoothRList.remove(smoothRList.size() - 1);
            smoothSpinList.remove(smoothSpinList.size() - 1);
            smoothThetaList.remove(smoothThetaList.size() - 1);
        }
        smoothAdditions++;

        double r = Math.hypot(x,y);
        double theta = Math.atan2(y, x);

        smoothRList.add(r);
        smoothSpinList.add(spin);
        smoothThetaList.add(theta);

        MyMath.trimFromFront(smoothRList,     (int)Math.round(rSeconds / Bogg.averageClockTime));
        MyMath.trimFromFront(smoothSpinList,  (int)Math.round(       4 / Bogg.averageClockTime));
        MyMath.trimFromFront(smoothThetaList, (int)Math.round(     .66 / Bogg.averageClockTime));

        r     = (r==0)?      0 : MyMath.ave(smoothRList);
        spin  = (spin==0 || !smoothSpin)?   spin : MyMath.ave(smoothSpinList);
        theta = smoothTheta? MyMath.loopAve(smoothThetaList) : theta;

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
        return face(forward);
    }

    private double sumSpinError = 0;
    private double lastSpinError = 0;
    private double lastSpinTime = 0;

    double sP = .20; // .20 per radian
    double sI = 8; //Time to correct past error
    double sD = .4; //fully account for this much time in the future at current error decreasing rate

    private double face(double angle)
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
        double i = sI;

        double e = MyMath.loopAngle(angle, spinAngle());
        double de = e - lastSpinError; //change in angle
        double t = timer.seconds();
        double dt = t - lastSpinTime;  //change in t
        telemetry.addData("de/dt", de/dt);
        sumSpinError += e * dt;        //cumulative error

        if(Math.abs(e) > MyMath.radians(5)) {
            sumSpinError = 0;
            i = 10000;
        }
        telemetry.addData("sum Spin Error", sumSpinError);

        double power = sP * (e  +  1/ i * sumSpinError +  sD * de/dt);
        telemetry.addData("correction Power", power);
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
    double mP = .02; //power per inch
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
        if(justResetTarget)
            dTheta = 0;
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

        telemetry.addData("dtheta/dt / r", dTheta/dr/r);
        if(dr != 0 && Math.abs(dTheta / dr / r ) > 2){ //2 rad per inch^2
            return new double[]{0,0};
        }

        double power = mP * (r +  mD * MyMath.ave(drdtArray));
        telemetry.addData("moveD term", mD * MyMath.ave(drdtArray));
        telemetry.addData("power", power);
        telemetry.addData("moveD / power",mD * MyMath.ave(drdtArray) / power);

        telemetry.addData("moveT term", tangentPower);
        if(power > .5) power = .5;
        //Don't worry, it's smoothed

        lastR = r;
        lastT = t;
        lastTheta = theta;

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