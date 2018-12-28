package org.firstinspires.ftc.teamcode;

import android.icu.lang.UProperty;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Bogg
{
    Gamepad gamepad = null;
    Gamepad gamepad2 = null;
    HardwareMap hardwareMap = null;
    DriveEngine driveEngine = null;
    DcMotor lift = null;
    Camera camera = null;
    Sensors sensors;
    Servo brake;
    Servo push;
    Servo dServo;

    double alpha = 0.5;
    double liftAlpha = .12;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;


    Telemetry telemetry;

    enum Direction
    {
        Left,
        Straight,
        Right
    }

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        driveEngine = new DriveEngine(hardwareMap, telemetry);
        lift  = hardwareMap.dcMotor.get("lift");
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
        push = hardwareMap.servo.get("dropMarker");
        dServo = hardwareMap.get(Servo.class, "dServo");
    }

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad, Gamepad gamepad2, Telemetry telemetry)
    {
        this.gamepad = gamepad;
        this.gamepad2 = gamepad2;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        driveEngine = new DriveEngine(hardwareMap, telemetry);
        lift  = hardwareMap.dcMotor.get("lift");
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
        push = hardwareMap.servo.get("dropMarker");
        dServo = hardwareMap.get(Servo.class, "dServo");
    }



    private double smoothX(double x, double multiplier)
    {
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * multiplier * x + (1- alpha * multiplier) * xAve;
        return xAve;
    }

    private double smoothY(double y, double multiplier)
    {
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
        if(spin * spinAve < 0 || spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
    }

    void manualLift()
    {
        if(gamepad.y && !sensors.touchTop.isPressed())
        {
            lift.setPower(smoothLift(1));
        }
        else if(gamepad.a)
        {
            if (sensors.touchBottom.isPressed())
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
        if(power > 0  && !sensors.touchTop.isPressed())
            lift.setPower(smoothLift(power));
        else if(power < 0 && !sensors.touchBottom.isPressed())
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
                push.setPosition(-.4);
                break;
            case Straight:
                push.setPosition(0);
                break;
            case Right:
                push.setPosition(.4);
                break;
        }
    }

    void manualDrive(Gamepad g)
    {
        if(g.left_stick_button)
            driveEngine.drive(smoothX(g.left_stick_x, 1.5), smoothY(-g.left_stick_y, 1.5),true);
        else
            driveEngine.drive(smoothX(g.left_stick_x, 1), smoothY(-g.left_stick_y, 1));
        telemetry.addData("gamepad x", g.left_stick_x);
        telemetry.addData("gamepad y", g.left_stick_y);
        telemetry.addLine("Note: y is negated");
    }

    void manualCurvy(Gamepad gDrive, Gamepad gRotate)
    {
        double leftX = smoothX(gDrive.left_stick_x, 1);
        double leftY = smoothY(-gDrive.left_stick_y, 1);
        double spin = smoothSpin(-gRotate.right_stick_x);

        telemetry.addData("gamepad x", gDrive.left_stick_x);
        telemetry.addData("gamepad y", gDrive.left_stick_y);
        telemetry.addData("gamepad spin", gRotate.right_stick_x);
        telemetry.addLine("Note: y and spin are negated");

        driveEngine.driveCurvy(leftX, leftY, spin);

    }

    void manualRotate(Gamepad g)
    {
        if(g.right_stick_button)
            driveEngine.rotate(-g.right_stick_x);
        else
            driveEngine.rotate(smoothSpin(-g.right_stick_x));

        telemetry.addData("gamepad spin", g.right_stick_x);
        telemetry.addLine("Note: spin is negated");
    }


    boolean driveToTarget(double target_x, double target_y, double speed, double target_radius)
    {
        double[] location = camera.getLocation();
        if(location != null)
        {
            double target_heading = camera.headingToTarget(location,target_x,target_y);

            driveEngine.drive(Math.sin(target_heading) * speed, Math.cos(target_heading) * speed);
        }
         return false;
    }

    boolean rotateToTarget(double target_x, double target_y, double accuracy_angle)
    {
        double[] location = camera.getLocation();
        if(location != null) {
            double target_heading = camera.headingToTarget(location,target_x,target_y);

            if (Math.abs(target_heading) < accuracy_angle * Math.PI/180) {
                driveEngine.rotate(0);
                return true;
            }

            driveEngine.rotate(.08 * Math.signum(target_heading)); //if target is more counterclockwise, we want to move counterclockwise.
        }
        return false;
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
        double targetHeading;

        double wallTargetX, wallTargetY = 0;
        double[] unitTargetLocation = new double[2];
        double target_x, target_y;
        if(location != null)
        {
            VuforiaTrackable target = null;
            if (camera.targetVisible(0))
                target = camera.allTrackables.get(0);
            else if (camera.targetVisible(1))
                target = camera.allTrackables.get(1);
            else if (camera.targetVisible(2))
                target = camera.allTrackables.get(2);
            else // if (camera.targetVisible(3))
                target = camera.allTrackables.get(3);


            //the direction a compass would tell us
            double currentHeading = camera.getHeading() * 180 / Math.PI;
            targetHeading = Math.round(currentHeading / 90) * 90;

            double headingDifference =  currentHeading - targetHeading;

            wallTargetX = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
            wallTargetY = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

            unitTargetLocation[0] = Math.signum(wallTargetX);
            unitTargetLocation[1] = Math.signum(wallTargetY);
            double[] driveTarget = new double[]{(6*12 - 9) * unitTargetLocation[0], (6*12 - 9) * unitTargetLocation[1]};
            target_x = driveTarget[0];
            target_y = driveTarget[1];

            double headingToTarget = camera.headingToTarget(location, target_x, target_y);

            if(Math.abs(headingDifference) < 5 && Math.hypot(target_x, target_y) < .5)
                return true;
            else
            {
                driveEngine.driveCurvy(Math.sin(headingToTarget) * .05, Math.cos(headingToTarget) * .05, headingDifference / 100);
            }

            return false;
        }
        return true;
    }


    void incAlpha()
    {
        if(alpha + alphaInc<1)
            alpha += alphaInc;
    }

    void decAlpha()
    {
        if(alpha - alphaInc>0)
            alpha -= alphaInc;
    }

    double getAlpha()
    {
        return alpha;
    }
}
