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

    double averageClockTime = 0;
    double liftAlpha = .12;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;
    double raisePosition;
    double lastClockTime = 0;
    double lastTime = 0;
    double n = 0;

    boolean goingUp = true;


    Telemetry telemetry;

    enum Direction
    {
        Left,
        Straight,
        Right,
        Down,
        Up
    }

    public Bogg(HardwareMap hardwareMap, Telemetry telemetry)
    {
        this.telemetry = telemetry;
        driveEngine = new DriveEngine(hardwareMap, telemetry);
        endEffector = new EndEffector(hardwareMap, telemetry);
        lift  = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
        drop = hardwareMap.servo.get("drop");
        dServo = hardwareMap.get(Servo.class, "dServo");
        timer = new ElapsedTime();
    }

    private double smoothX(double x, double multiplier)
    {
        double alpha = getAlpha(1 / multiplier);
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1- alpha) * xAve;
        return xAve;
    }

    private double smoothY(double y, double multiplier)
    {
        double alpha = getAlpha(1 / multiplier);
        if(y * yAve < 0 || y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
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

    private double smoothSpin(double spin)
    {
        double alpha = getAlpha(1.5);
        if(spin * spinAve < 0 || spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
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

    void setBrake(boolean on)
    {
        if(on)
            brake.setPosition(.5);
        else //off
            brake.setPosition(.6);
    }

    void manualBrake(boolean trueG, boolean falseG)
    {
        if(trueG)
            setBrake(true);
        if(falseG)
            setBrake(false);
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
            case Left:
                drop.setPosition(-.4);
                break;
            case Right:
                drop.setPosition(.4);
                break;
            case Up:
            default:
                drop.setPosition(0);
                break;
        }
    }

    void push(Direction direction)
    {
        switch (direction)
        {
            case Up:
                break;
            case Down:
                break;
        }
    }

    void manualDrive(boolean op, double x, double y)
    {
        double leftX = smoothX(x, op? 4:1);
        double leftY = smoothY(-y, op? 4:1);
        double spin = 0;

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addData("gamepad spin", spin);
        telemetry.addLine("Note: y is negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    private double cumulativeCorrection;
    void manualDriveAutoCorrect(boolean op, double x, double y)
    {
        double leftX = smoothX(x, op? 4:1);
        double leftY = smoothY(-y, op? 4:1);
        double spin = driveEngine.spinToZero();
        driveEngine.drive(op, leftX, leftY, spin);

        cumulativeCorrection += driveEngine.spinToZero();

        telemetry.addData("cumulative correction", cumulativeCorrection);
        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addLine("Note: y is negated");
    }

    void manualCurvy(Gamepad gDrive, Gamepad gRotate)
    {
        boolean op = gDrive.left_stick_button;
        double leftX = smoothX(gDrive.left_stick_x, op? 4:1);
        double leftY = smoothY(-gDrive.left_stick_y, op? 4:1);
        double spin = smoothSpin(-gRotate.right_stick_x/2);

        telemetry.addData("gamepad x", gDrive.left_stick_x);
        telemetry.addData("gamepad y", gDrive.left_stick_y);
        telemetry.addData("gamepad spin", gRotate.right_stick_x);
        telemetry.addLine("Note: y and spin are negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    private double derivedRadius = 12;
    void updateRadius()
    {
        derivedRadius = endEffector.getRadius();
    }

    boolean dPadOrbit(boolean left, boolean right)
    {
        if(left )
        {
            driveEngine.orbit(48,0, -.3);
            return true;
        }
        else if(right)
        {
            driveEngine.orbit(48,0, .3);
            return true;
        }
        return false;
    }

    void spinEffector()
    {
        endEffector.spin(.5);
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
    
    void autoEffect()
    {
        double x = derivedRadius + driveEngine.xDist();
        double z = goingUp? 33 : 0;

        endEffector.moveToPosition(x, z);

        if(goingUp)
        {
            if(endEffector.isUp())
            {
                if(timer.seconds() < 1)
                    endEffector.open();
                else
                    goingUp = false;
            }
            else
                timer.reset(); //keep the timer at zero while we're still moving
        }
        else //if going down
        {
            endEffector.close();
            if(endEffector.isDown())
            {
                if(timer.seconds() > 2)
                    goingUp = true;
            }
            else
                timer.reset(); //keep the timer at zero while we're still moving
        }
    }
    
    void manualEffect(Gamepad g)
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

    }

    boolean manualRotate(boolean button, double stick)
    {
        if(stick == 0)
            return false;

        if(button)
            driveEngine.rotate(-stick);
        else
            driveEngine.rotate(smoothSpin(-stick));

        telemetry.addData("gamepad spin", stick);
        telemetry.addLine("Note: spin is negated");
        return true;
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

    double getAlpha(double seconds)
    {
        return 1 - Math.pow(.05, averageClockTime/seconds); //reaches 95% in this many seconds
    }
}
