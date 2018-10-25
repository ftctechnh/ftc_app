package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
    DcMotor lift;
    Camera camera;
//    Sensors sensors;

    double alpha = 0.0039;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        driveEngine = new DriveEngine(hardwareMap);
        lift  = hardwareMap.dcMotor.get("lift");
//        sensors = new Sensors(hardwareMap);
    }

    public double smoothX(double x)
    {
        if(x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    public double smoothY(double y)
    {
        if(y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }

    public double smoothSpin(double spin)
    {
        if(spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
    }

    public void lift()
    {
        if(gamepad.y)
            lift.setPower(.7);
        else if(gamepad.a)
            lift.setPower(-.7);
        else
            lift.setPower(0);
    }

    public void manualDrive()
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y);
        else
            driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(gamepad.left_stick_y));
    }

    public void driveToTarget(double target_x, double target_y, double speed, double precision_radius)
    {
        double[] location = camera.getLocation();
        if(location != null)
        {

            double robot_x = location[0];
            double robot_y = location[1];

            double delta_x = target_x - robot_x;
            double delta_y = target_y - robot_y;

            if(delta_x < precision_radius && delta_y < precision_radius)
            {
                driveEngine.drive(0,0);
                return;
            }

            //the direction a compass would tell us
            double heading_of_robot_on_field = camera.getHeading();

            //where a map would tell us a mountain is, relative to us
            double heading_of_target_from_robot_location = Math.atan2(delta_x,delta_y);

            //where compass would say the mountain is located considering our compass isn't pointed north
            double heading_of_target_from_robot_perspective = heading_of_target_from_robot_location - heading_of_robot_on_field;

            double target_heading = heading_of_target_from_robot_perspective;
            driveEngine.drive(Math.cos(target_heading) * speed, Math.sin(target_heading) * speed);

        }
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
