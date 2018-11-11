package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
    DcMotor lift;
    Camera camera;
    Sensors sensors;
    Servo brake;

    double alpha = 0.039;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;
    boolean goingUp;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        camera = new Camera(hardwareMap, telemetry);
        driveEngine = new DriveEngine(hardwareMap);
        lift  = hardwareMap.dcMotor.get("lift");
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
    }

    public double smoothX(double x)
    {
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    public double smoothY(double y)
    {
        if(y * yAve < 0 || y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }

    public double smoothLift(double l)
    {
        if(l* liftAve < 0 || l == 0)
            liftAve = 0;
        else if(l == -.02)
        {
            liftAve = alpha*3 * l + (1-alpha*3) * liftAve;
        }
        else
            liftAve = alpha * l + (1-alpha) * liftAve;
        return liftAve;
    }

    public double smoothSpin(double spin)
    {
        if(spin * spinAve < 0 || spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
    }

    public void manualLift()
    {
        if(gamepad.y)
        {
            goingUp = true;
        }

        if(gamepad.a)
        {
            goingUp = false;
        }

        if(gamepad.y ) //!sensors.touchTop.isPressed() && goingUp)
        {
            lift.setPower(smoothLift(1));
        }
        else if(gamepad.a  && this.sensors.dMobile.getDistance(DistanceUnit.INCH) < 8)
        {
            goingUp = false;
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

    public void lift(double power)
    {
        if(power > 0  && !sensors.touchTop.isPressed())
            lift.setPower(smoothLift(power));
        else if(power < 0 && this.sensors.dMobile.getDistance(DistanceUnit.INCH) < 8 && !sensors.touchBottom.isPressed())
            lift.setPower(smoothLift(power));
        else
            lift.setPower(smoothLift(0));
    }

    public void setBrake(boolean position)
    {
        if(position == true)
            brake.setPosition(.5);
        else
            brake.setPosition(.6);
    }

    public void setBrake(double position)
    {
        brake.setPosition(position);
    }

    public void manualDrive()
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y);
        else
            driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(gamepad.left_stick_y));
    }
    public void manualDrive(double theta)
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y, theta);
        else
            driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(gamepad.left_stick_y));
    }

    public boolean driveToTarget(double target_x, double target_y, double speed, double target_radius)
    {
        double[] location = camera.getLocation();
        if(location != null)
        {

            double robot_x = location[0];
            double robot_y = location[1];

            double delta_x = target_x - robot_x;
            double delta_y = target_y - robot_y;

            if(Math.abs(delta_x) < target_radius && Math.abs(delta_y) < target_radius)
            {
                driveEngine.drive(0,0);
                return true;
            }

            //the direction a compass would tell us
            double heading_of_robot_on_field = camera.getHeading();

            //where a map would tell us a mountain is, relative to us
            double heading_of_target_from_robot_location = Math.atan2(delta_y,delta_x);

            //where compass would say the mountain is located considering our compass isn't pointed north
            double heading_of_target_from_robot_perspective = heading_of_target_from_robot_location - heading_of_robot_on_field;

            double target_heading = heading_of_target_from_robot_perspective;
            driveEngine.drive(Math.cos(target_heading) * speed, Math.sin(target_heading) * speed);

        }
            return true;
    }

    public boolean rotateToTarget(double targetHeading, double accuracy_angle)
    {
        double[] location = camera.getLocation();
        if(location != null)
        {

            //the direction a compass would tell us
            double currentHeading = camera.getHeading() * 180 / Math.PI;

            double headingDifference = currentHeading - targetHeading;

            if(Math.abs(headingDifference) < accuracy_angle)
                return true;
            if(headingDifference > 1)
                driveEngine.rotate(.2);
            else
                driveEngine.rotate(-.2);
            return false;
        }
        return true;
    }

    public void incAlpha()
    {
        if(alpha + alphaInc<1)
            alpha += alphaInc;
    }

    public void decAlpha()
    {
        if(alpha - alphaInc>0)
            alpha -= alphaInc;
    }

    public double getAlpha()
    {
        return alpha;
    }

    public double getSmoothSpin() {
        return smoothSpin(gamepad.right_stick_x);
    }

    public void manualRotate()
    {
        if(gamepad.right_stick_button)
            driveEngine.rotate(gamepad.right_stick_x);
        else
            driveEngine.rotate(smoothSpin(gamepad.right_stick_x));
    }
}
