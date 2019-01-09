package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Bogg
{
    DriveEngine driveEngine;
    EndEffector endEffector;
    DcMotor lift;
    Camera camera = null;
    Sensors sensors;
    ElapsedTime timer;
    Servo brake;
    Servo drop;
    Servo dServo;

    double liftAlpha = .12;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;
    double raisePosition;
    double lastClockTime = 0;
    double lastTime = 0;
    double averageClockTime = 0;
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
        double alpha = getAlpha(1);
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * multiplier * x + (1- alpha * multiplier) * xAve;
        return xAve;
    }

    private double smoothY(double y, double multiplier)
    {
        double alpha = getAlpha(1);
        if(y * yAve < 0 || y == 0)
            yAve = 0;
        else
            yAve = alpha * multiplier * y + (1-alpha * multiplier) * yAve;
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
        double leftX = smoothX(x, op? 1.5:1);
        double leftY = smoothY(-y, op? 1.5:1);
        double spin = 0;

        telemetry.addData("gamepad x", x);
        telemetry.addData("gamepad y", y);
        telemetry.addData("gamepad spin", spin);
        telemetry.addLine("Note: y is negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    private double cumulativeCorrection;
    void manualDriveAutoCorrect(Gamepad g)
    {
        boolean op = g.left_stick_button;
        driveEngine.drive(op, smoothX(g.left_stick_x, op? 1.5:1), smoothY(g.left_stick_y, op? 1.5:1), driveEngine.spinToZero());
        cumulativeCorrection += driveEngine.spinToZero();

        telemetry.addData("cumulative correction", cumulativeCorrection);
        telemetry.addData("gamepad x", g.left_stick_x);
        telemetry.addData("gamepad y", g.left_stick_y);
        telemetry.addLine("Note: y is negated");
    }

    void manualCurvy(Gamepad gDrive, Gamepad gRotate)
    {
        boolean op = gDrive.left_stick_button;
        double leftX = smoothX(gDrive.left_stick_x, op? 1.5:1);
        double leftY = smoothY(-gDrive.left_stick_y, op? 1.5:1);
        double spin = smoothSpin(-gRotate.right_stick_x/2);

        telemetry.addData("gamepad x", gDrive.left_stick_x);
        telemetry.addData("gamepad y", gDrive.left_stick_y);
        telemetry.addData("gamepad spin", gRotate.right_stick_x);
        telemetry.addLine("Note: y and spin are negated");

        driveEngine.drive(op, leftX, leftY, spin);
    }

    private double derivedRadius = 48;
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
        double max = Math.max(Math.abs(gDrive.left_stick_x), Math.abs(gDrive.left_stick_y));

        if(orbit) {
            if (max == gDrive.left_stick_y)
                driveEngine.orbit(derivedRadius + driveEngine.xDist(), 0, -gDrive.left_stick_y);
            else
                driveEngine.drive(gDrive.left_stick_x, 0);
        }
        else
            manualCurvy(gDrive, gRotate);
    }
    
    void autoEffect()
    {
        endEffector.moveToLength(); //we want to keep the arm at a fixed length
        if(goingUp)
        {
            if(endEffector.raise(raisePosition))
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
            if(endEffector.lowerAllTheWay())
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
        raisePosition += (g.left_stick_x + g.left_stick_y) * 5 * endEffector.getAngleInDegrees();
        if(Math.hypot(g.left_stick_x,g.left_stick_y) > 0.01)
            endEffector.raise(raisePosition);

        if(g.dpad_down)
            endEffector.lowerAllTheWay();
        if(g.dpad_up)
            endEffector.raise(raisePosition);

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

    boolean rotateToWall(double accuracy_angle)
    {
        Double wallHeading = camera.headingToWall();
        telemetry.addData("wall heading", wallHeading);
        if(wallHeading != null)
        {
            if(Math.abs(wallHeading) < accuracy_angle)
                return true;
            else
            {
                if(wallHeading > 0)
                    driveEngine.rotate(.08);
                else
                    driveEngine.rotate(-.08);
            }

            return false;
        }
        return false;
    }

    boolean driveCurvyToWall()
    {
        double[] location = camera.getLocation();

        double wallTargetX, wallTargetY;
        double[] unitTargetLocation = new double[2];
        if(location != null)
        {
            VuforiaTrackable target = null;
            for(int i = 0; i < 4; i++)
                if(camera.targetVisible(i))
                    target = camera.allTrackables.get(i);

            double headingDifference =  camera.headingToTarget(location);

            wallTargetX = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
            wallTargetY = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

            unitTargetLocation[0] = Math.signum(wallTargetX);
            unitTargetLocation[1] = Math.signum(wallTargetY);
            double[] driveTarget = new double[]{(6*12 - 9) * unitTargetLocation[0], (6*12 - 9) * unitTargetLocation[1]};
            double r = Math.hypot(driveTarget[0],driveTarget[1]);

            double headingToTarget = camera.headingToTarget(location, driveTarget[0], driveTarget[1]);

            if(Math.abs(headingDifference) < 5 && r < .5)
                return true;
            else
            {
                driveEngine.drive(Math.sin(headingToTarget) * .05, Math.cos(headingToTarget) * .05, headingDifference / 100);
            }
        }
        return false;
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
