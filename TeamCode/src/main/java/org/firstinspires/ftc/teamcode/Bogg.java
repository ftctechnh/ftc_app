package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bogg
{
    DriveEngine driveEngine;
    EndEffector endEffector;
    DcMotor lift;
    Sensors sensors;
    ElapsedTime timer;
    Servo brake;
    Servo drop;
    Name name;

    static double averageClockTime = 0;
    private double liftAve = 0;

    private boolean rotating = false;


    Telemetry telemetry;

    enum Direction
    {
        Left,
        Straight,
        Right,
        Down,
        Up,
        On,
        Off
    }

    enum Name
    {
        Bogg,
        MiniBogg,
        Fauxbot,
        Fakebot
    }

    public Bogg(HardwareMap hardwareMap, Telemetry telemetry, Name whichRobot)
    {
        this.telemetry = telemetry;
        this.name = whichRobot;
        timer = new ElapsedTime();

        switch (whichRobot)
        {
            case Bogg:
                lift  = hardwareMap.dcMotor.get("lift");
                lift.setDirection(DcMotorSimple.Direction.FORWARD);
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                brake = hardwareMap.servo.get("brake");
                drop = hardwareMap.servo.get("drop");
                break;

            case MiniBogg:
                lift  = hardwareMap.dcMotor.get("lift");
                lift.setDirection(DcMotorSimple.Direction.FORWARD);
                break;

            case Fauxbot:
                driveEngine.driveAtAngle(-Math.PI / 2);
                break;

            case Fakebot:
                driveEngine = new FakeDriveEngine(telemetry);
                break;
        }

        sensors = new Sensors(hardwareMap, whichRobot);

        switch (whichRobot)
        {
            case Bogg:
                driveEngine = new OmniWheelDriveEngine(hardwareMap, telemetry, sensors, 3);
                endEffector = new EndEffector(hardwareMap, telemetry, sensors);
                break;

            case MiniBogg:
                driveEngine = new OmniWheelDriveEngine(hardwareMap, telemetry, sensors, 3);
                driveEngine.mP *= 5;
                OmniWheelDriveEngine.ticksPerRev = 290;
                OmniWheelDriveEngine.effectiveWheelDiameter = 3.5;
                break;

            case Fauxbot:
                driveEngine = new OmniWheelDriveEngine(hardwareMap, telemetry, sensors, 2);
                break;
        }
    }

    public static Bogg determineRobot(HardwareMap hardwareMap, Telemetry telemetry)
    {
        Bogg robot;
        try {
            robot = new Bogg(hardwareMap, telemetry, Name.Bogg);
        } catch (Exception e) {
            telemetry.addLine(e.toString());
            try {
                robot = new Bogg(hardwareMap, telemetry, Name.MiniBogg);
            } catch (Exception e1) {
                telemetry.addLine(e1.toString());
                try {
                    robot = new Bogg(hardwareMap, telemetry, Name.Fauxbot);
                } catch (Exception e2) {
                    telemetry.addLine(e2.toString());
                    robot = new Bogg(hardwareMap, telemetry, Name.Fakebot);
                }
            }
        }
        telemetry.addData("Name", robot.name);
        return robot;
    }


    private double smoothLift(double l)
    {
        double liftAlpha = .12;
        if(l* liftAve < 0 || l == 0)
            liftAve = 0;
        else if(l == -.02)
        {
            liftAve = liftAlpha * l + (1- liftAlpha) * liftAve;
        }
        else
            liftAve = liftAlpha /3 * l + (1- liftAlpha /3) * liftAve;
        return liftAve;
    }

    /**
     * @param up is whether the up button is pushed
     * @param down is whether the down button is pushed
     * @return if pulling arm down
     */
    boolean manualLift(boolean up, boolean down)
    {
        if(name == Name.Bogg)
        if(up && !sensors.touchTopIsPressed())
        {
            lift.setPower(smoothLift(1));
        }
        else if(down)
        {
            if (sensors.touchBottomIsPressed())
            {
                lift.setPower(smoothLift(-.02));
                return true;
            } else {
                lift.setPower(smoothLift(-1));
                return true;
            }
        }
        else
            lift.setPower(smoothLift(0));

        return false;
    }

    void lift(double power)
    {
        if(name == Name.Bogg)
        if(power > 0  && !sensors.touchTopIsPressed())
            lift.setPower(smoothLift(power));
        else if(power < 0 && !sensors.touchBottomIsPressed())
            lift.setPower(smoothLift(power));
        else
            lift.setPower(smoothLift(0));
    }

    void setBrake(Direction d)
    {
        if(name == Name.Bogg)
        switch (d) {
            case On:
                brake.setPosition(.73);
                break;
            case Off:
                brake.setPosition(.60);
        }
    }

    void dropMarker(Direction direction)
    {
        if(name == Name.Bogg)
        switch (direction)
        {
            case Down:
                drop.setPosition(.3);
                break;
            case Up:
            default:
                drop.setPosition(0);
                break;
        }
    }

    void manualDrive(boolean op, double x, double y)
    {
        double[] drive = driveEngine.smoothDrive(x, -y, op? 1:3);
        double leftX = drive[0];
        double leftY = drive[1];

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addLine("Note: y is negated");

        driveEngine.drive(op, leftX, leftY);
    }

    ElapsedTime spinTimer = new ElapsedTime();

    /**
     * The default precedence is 0.
     * @param op: Determines if 
     * @param x: Power in the x direction
     * @param y: Power in the y direction
     * @param spin: Power towards rotation
     */
    void manualDrive2(boolean op, double x, double y, double spin){
        manualDrive2(op,x,y,spin,0);
    }

    /**
     *
     * @param op:
     * @param x: Power in the x direction
     * @param y: Power in the y direction
     * @param spin: Power towards rotation
     * @param precedence
     */
    void manualDrive2(boolean op, double x, double y, double spin, int precedence)
    {
        if(spin != 0)
            spinTimer.reset();
        boolean smoothSpin = true;
        if(spinTimer.seconds() < 1)
            driveEngine.resetForward();
        else {
            spin = -driveEngine.faceForward();
            smoothSpin = false;
        }
        driveEngine.smoothDrive2(op, x, -y, -spin, op? 1:2.5, smoothSpin, true, precedence);

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addData("gamepad spin", spin);
        telemetry.addLine("Note: y and spin are negated");
    }

    void manualDriveAutoCorrect(boolean op, double x, double y, double t)
    {
        if(t < 1){
            driveEngine.resetForward();
            manualDrive(op, x, y);
        }

        double[] drive = driveEngine.smoothDrive(x, -y, op? 1:3);
        double leftX = drive[0];
        double leftY = drive[1];
        double spin = driveEngine.faceForward();
        driveEngine.drive(op, leftX, leftY, spin);

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addLine("Note: y is negated");
    }

    void manualCurvy(boolean op, double x, double y, double s)
    {
        double[] drive = driveEngine.smoothDrive(x, -y, op? 1:3);
        double leftX = drive[0];
        double leftY = drive[1];
        double spin = driveEngine.smoothSpin(-s/3);

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addData("gamepad spin", s);
        telemetry.addLine("Note: y and spin are negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    boolean manualRotate(boolean button, double spin)
    {
        if(spin == 0) {  //if we're not rotating
            if(rotating){  //but if the boolean says we are
                rotating = false;
                driveEngine.resetForward(); //resets forward after rotating so we can auto-correct
            }
            return false;
        }
        rotating = true;

        if(button)
            driveEngine.rotate(-spin/3);
        else
            driveEngine.rotate(driveEngine.smoothSpin(-spin/3));

        telemetry.addData("gamepad spin", spin);
        telemetry.addLine("Note: spin is negated");
        return true;
    }

    private double derivedRadius = 12;
    void updateRadius()
    {
        derivedRadius = endEffector.getRadius();
    }

    void manualDriveVarOrbit(boolean op, double y, double x, double spin, boolean orbit)
    {
        double max = Math.max(Math.abs(x), Math.abs(y));

        if(orbit) {
            if (max == Math.abs(y))
                driveEngine.orbit(derivedRadius + driveEngine.xDist(), 0, y / 2);
            else
                driveEngine.drive(x, 0);
        }
        else
            manualCurvy(op, x, y, spin);
    }

    void floatMotors()
    {
        driveEngine.floatMotors();
        if(name == Name.Bogg)
        {
            endEffector.floatMotors();
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            telemetry.addData("lift position", lift.getCurrentPosition());
        }
    }


    double n = 0;
    double lastTime = 0;
    /**
     * Should only be called once per loop
     * @return the average time for one loop
     */
    void update()
    {
        double t = timer.seconds();
        double clockTime = t - lastTime;
        lastTime = t;

        if(n==0){
            n++;
            return;
        }

        averageClockTime = (clockTime * .02 + averageClockTime * .98); //expontential average

        n++;
        driveEngine.update();
        telemetry.update();
        telemetry.addData("currentClockTime", clockTime);
        telemetry.addData("averageClockTime", averageClockTime);
    }

    static double getAlpha(double seconds)
    {
        return 1 - Math.pow(.05, averageClockTime/seconds); //reaches 95% in this many seconds
    }
}
