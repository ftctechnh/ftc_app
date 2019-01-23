package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
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
    Servo dServo;

    static double averageClockTime = 0;
    double liftAlpha = .12;
    double liftAve = 0;
    double raisePosition;
    double lastClockTime = 0;
    double lastTime = 0;
    double n = 0;

    boolean rotating = false;


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
        Fauxbot
    }

    public Bogg(HardwareMap hardwareMap, Telemetry telemetry, Name whichRobot)
    {
        this.telemetry = telemetry;
        timer = new ElapsedTime();
        sensors = new Sensors(hardwareMap);

        switch (whichRobot)
        {
            case Bogg:
                driveEngine = new DriveEngine3Wheels(hardwareMap, telemetry, sensors);
                endEffector = new EndEffector(hardwareMap, telemetry, sensors);
                lift  = hardwareMap.dcMotor.get("lift");
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                brake = hardwareMap.servo.get("brake");
                drop = hardwareMap.servo.get("drop");
                dServo = hardwareMap.get(Servo.class, "dServo");
                break;

            case MiniBogg:
                driveEngine = new DriveEngine3Wheels(hardwareMap, telemetry, sensors);
                lift  = hardwareMap.dcMotor.get("lift");
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                break;

            case Fauxbot:
                driveEngine = new DriveEngine(hardwareMap, telemetry, sensors, 2);
                break;
        }
    }


    private double smoothLift(double l)
    {
        if(l* liftAve < 0 || l == 0)
            liftAve = 0;
        else if(l == -.02)
        {
            liftAve = liftAlpha * l + (1-liftAlpha) * liftAve;
        }
        else
            liftAve = liftAlpha/3 * l + (1-liftAlpha/3) * liftAve;
        return liftAve;
    }

    void manualLift(boolean up, boolean down)
    {
        if(up && !sensors.touchTopIsPressed())
        {
            lift.setPower(smoothLift(1));
        }
        else if(down)
        {
            if (sensors.touchBottomIsPressed())
            {
                lift.setPower(smoothLift(-.02));
            } else {
                lift.setPower(smoothLift(-1));
            }
        }
        else
            lift.setPower(smoothLift(0));
    }

    void lift(double power)
    {
        if(power > 0  && !sensors.touchTopIsPressed())
            lift.setPower(smoothLift(power));
        else if(power < 0 && !sensors.touchBottomIsPressed())
            lift.setPower(smoothLift(power));
        else
            lift.setPower(smoothLift(0));
    }

    void setBrake(Direction d)
    {
        switch (d) {
            case On:
                brake.setPosition(.5);
                break;
            case Off:
                brake.setPosition(.6);
        }
    }

    void rotateMobile(Direction direction)
    {
        switch (direction)
        {
            case Left:
                dServo.setPosition(-.6);
                break;
            case Straight:
                dServo.setPosition(0);
                break;
            case Right:
                dServo.setPosition(.6);
                break;
        }
    }


    void dropMarker(Direction direction)
    {
        switch (direction)
        {
            case Down:
                drop.setPosition(.6);
                break;
            case Up:
            default:
                drop.setPosition(0);
                break;
        }
    }

    void manualDrive(boolean op, double x, double y)
    {
        double[] drive = driveEngine.smoothDrive(x, y, op? 1:3, true);
        double leftX = drive[0];
        double leftY = drive[1];

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addLine("Note: y is negated");

        driveEngine.drive(op, leftX, leftY);
    }

    void manualDriveAutoCorrect(boolean op, double x, double y)
    {
        double[] drive = driveEngine.smoothDrive(x, y, op? 1:3, true);
        double leftX = drive[0];
        double leftY = drive[1];
        double spin = driveEngine.faceForward();
        driveEngine.drive(op, leftX, leftY, spin);

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addLine("Note: y is negated");
    }

    void manualCurvy(Gamepad gDrive, Gamepad gRotate)
    {
        boolean op = gDrive.left_stick_button;
        double[] drive = driveEngine.smoothDrive(gDrive.left_stick_x, -gDrive.left_stick_y, op? 1:3, true);
        double leftX = drive[0];
        double leftY = drive[1];
        double spin = driveEngine.smoothSpin(-gRotate.right_stick_x/3);

        telemetry.addData("gamepad x", gDrive.left_stick_x);
        telemetry.addData("gamepad y", gDrive.left_stick_y);
        telemetry.addData("gamepad spin", gRotate.right_stick_x);
        telemetry.addLine("Note: y and spin are negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    boolean manualRotate(boolean button, double stick)
    {
        if(stick == 0) {  //if we're not rotating
            if(rotating){  //but if the boolean says we are
                rotating = false;
                driveEngine.resetForward(); //resets forward after rotating so we can auto-correct
            }
            return false;
        }
        rotating = true;

        if(button)
            driveEngine.rotate(-stick/3);
        else
            driveEngine.rotate(driveEngine.smoothSpin(-stick/3));

        telemetry.addData("gamepad spin", stick);
        telemetry.addLine("Note: spin is negated");
        return true;
    }

    private double derivedRadius = 12;
    void updateRadius()
    {
        derivedRadius = endEffector.getRadius();
    }


    void manualDriveVarOrbit(Gamepad gDrive, Gamepad gRotate, boolean orbit)
    {
        double y = gDrive.left_stick_y;
        double x = gDrive.left_stick_x;
        double max = Math.max(Math.abs(x), Math.abs(y));

        if(orbit) {
            if (max == Math.abs(y))
                driveEngine.orbit(derivedRadius + driveEngine.xDist(), 0, -gDrive.left_stick_y);
            else
                driveEngine.drive(gDrive.left_stick_x, 0);
        }
        else
            manualCurvy(gDrive, gRotate);
    }

    
    boolean manualEffect(Gamepad g)
    {
        //deals with the length of the arm
        endEffector.extend(g.right_stick_x);

        //deals with the servos to release the minerals
        if(g.y || g.b || g.right_bumper)
            endEffector.open();
        else
            endEffector.close();

        //deals with the angle of the arm
        raisePosition += (g.left_stick_x + g.left_stick_y) * 5 * endEffector.getAngleInDegrees()
                * averageClockTime * EndEffector.gearRatio;

        if(Math.hypot(g.left_stick_x,g.left_stick_y) > 0)
        {
            endEffector.pivot((int) raisePosition);
        }

        if(g.dpad_down)
            endEffector.pivot(0);
        if(g.dpad_up)
            endEffector.pivot((int)raisePosition);

        return Math.abs(g.right_stick_x) != 0 || g.y || g.b || g.right_bumper || g.dpad_down || g.dpad_up ||
                Math.hypot(g.left_stick_x, g.left_stick_y) > 0;
    }




    /**
     * Should only be called once per loop
     * @return the average time for one loop
     */
    void update()
    {
        double t = timer.seconds();
        double clockTime = .5 * lastClockTime + .5 * (t - lastTime); // exponential average
        lastTime = t;

        if(averageClockTime == 0)
            averageClockTime = clockTime;
        averageClockTime = (clockTime + n * averageClockTime) / (n+1); //cumulative average

    }

    static double getAlpha(double seconds)
    {
        return 1 - Math.pow(.05, averageClockTime/seconds); //reaches 95% in this many seconds
    }
}
