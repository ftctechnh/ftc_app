package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Bogg
{
    Gamepad gamepad = null;
    HardwareMap hardwareMap = null;
    DriveEngine driveEngine = null;
    DcMotor lift = null;
    Camera camera = null;
    Sensors sensors;
    Servo brake;
    Servo push;

    double alpha = 0.5;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;
    Telemetry telemetry;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        driveEngine = new DriveEngine(hardwareMap);
        lift  = hardwareMap.dcMotor.get("lift");
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
        push = hardwareMap.servo.get("push");
    }



    private double smoothX(double x)
    {
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    private double smoothY(double y)
    {
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
            liftAve = .12 * l + (1-.12) * liftAve;
        }
        else
            liftAve = .04 * l + (1-.04) * liftAve;
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

    void setBrake(double position)
    {
        brake.setPosition(position);
    }

    void push(boolean out)
    {
        if(out)
            push.setPosition(.6);
        else
            push.setPosition(-.4);
    }

    void manualDrive()
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(gamepad.left_stick_x, -gamepad.left_stick_y,true);
        else
            driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(-gamepad.left_stick_y));
    }

    boolean driveToTarget(double target_x, double target_y, double speed, double target_radius)
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
            double target_heading = heading_of_robot_on_field - heading_of_target_from_robot_location;

            driveEngine.drive(Math.sin(target_heading) * speed, Math.cos(target_heading) * speed);

        }
         return false;
    }

    boolean rotateToTarget(double target_x, double target_y, double accuracy_angle)
    {
        double[] location = camera.getLocation();
        if(location != null) {

            double robot_x = location[0];
            double robot_y = location[1];

            double delta_x = target_x - robot_x;
            double delta_y = target_y - robot_y;


            //the direction a compass would tell us
            double heading_of_robot_on_field = camera.getHeading();

            //where a map would tell us a mountain is, relative to us
            double heading_of_target_from_robot_location = Math.atan2(delta_y, delta_x);

            //where compass would say the mountain is located considering our compass isn't pointed north
            double target_heading = heading_of_robot_on_field - heading_of_target_from_robot_location;

            if (Math.abs(target_heading) < accuracy_angle * Math.PI/180) {
                driveEngine.rotate(0);
                return true;
            }

            driveEngine.rotate(.1 * Math.signum(target_heading)); //if target is more counterclockwise, we want to move counterclockwise.
        }
        return false;
    }
    boolean rotateToWall(double accuracy_angle)
    {
        double[] location = camera.getLocation();
        double targetHeading;
        if(location != null)
        {

            //the direction a compass would tell us
            double currentHeading = camera.getHeading() * 180 / Math.PI;
            targetHeading = Math.round(currentHeading / 90) * 90;

            double headingDifference = targetHeading - currentHeading;

            if(Math.abs(headingDifference) < accuracy_angle)
                return true;
            else
            {
                if(headingDifference < 0)
                    driveEngine.rotate(.2);
                else
                    driveEngine.rotate(-.2);
            }

            return false;
        }
        return true;
    }

    double[] getMoveToWall()
    {
        VuforiaTrackable target = null;
        double[] targetLocation = new double[2];
        double[] unitTargetLocation = new double[2];
        double target_x, target_y;

        double[] location = camera.getLocation();
        if(location != null) {

            double heading = camera.getHeading();

            if (camera.targetVisible(0))
                target = camera.allTrackables.get(0);
            else if (camera.targetVisible(1))
                target = camera.allTrackables.get(1);
            else if (camera.targetVisible(2))
                target = camera.allTrackables.get(2);
            else if (camera.targetVisible(3))
                target = camera.allTrackables.get(3);

            targetLocation[0] = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
            targetLocation[1] = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

            unitTargetLocation[0] = Math.signum(targetLocation[0]);
            unitTargetLocation[1] = Math.signum(targetLocation[1]);

            double[] wallTarget = new double[]{(5 * 12 + 3) * unitTargetLocation[0], (5 * 12 + 3) * unitTargetLocation[1]};
            target_x = wallTarget[0];
            target_y = wallTarget[1];

            double robot_x = location[0];
            double robot_y = location[1];

            double delta_x = target_x - robot_x;
            double delta_y = target_y - robot_y;


            //the direction a compass would tell us
            double heading_of_robot_on_field = heading;

            //where a map would tell us a mountain is, relative to us
            double heading_of_target_from_robot_location = Math.atan2(delta_y, delta_x);

            //where compass would say the mountain is located considering our compass isn't pointed north
            double target_heading = heading - heading_of_target_from_robot_location;

            double wall_target_heading = heading - Math.atan2(targetLocation[1] - robot_y, targetLocation[0] - robot_x);

            double wall_heading = heading - Math.round(heading / (Math.PI / 2)) * Math.PI / 2;

            telemetry.addData("Loc. x", location[0]);
            telemetry.addData("Loc. y", location[1]);
            telemetry.addData("Loc. z", location[2]);
            telemetry.addData("heading", heading * 180 / Math.PI);

            telemetry.addData("target x:", targetLocation[0]);
            telemetry.addData("target y:", targetLocation[1]);
            telemetry.addData("destination x:", wallTarget[0]);
            telemetry.addData("destination y:", wallTarget[1]);
            telemetry.addData("heading from location", heading_of_target_from_robot_location);
            telemetry.addData("target heading", target_heading * 180 / Math.PI);
             if (Math.abs(delta_x) > 4 || Math.abs(delta_y) > 4)
             {
                telemetry.addData("drive x", Math.sin(target_heading) * .10);
                telemetry.addData("drive y", Math.cos(target_heading) * .10);
                telemetry.update();
                return new double[]{Math.sin(target_heading) * .10, Math.cos(target_heading) * .10};
            }
            else if (Math.abs(delta_x) > .5 || Math.abs(delta_y) > .5) {
                telemetry.addData("drive x2", Math.sin(target_heading) * .10);
                telemetry.addData("drive y2", Math.cos(target_heading) * .10);
                telemetry.update();
                return new double[]{Math.sin(target_heading) * .10, Math.cos(target_heading) * .10};

            }
            else {
                telemetry.addLine("Move to wall complete!");
                return new double[]{0, 0, 0};
            }
//
//
//            telemetry.addData("Loc. x", location[0]);
//            telemetry.addData("Loc. y", location[1]);
//            telemetry.addData("Loc. z", location[2]);
//            telemetry.addData("heading", heading * 180 / Math.PI);
//
//            telemetry.addData("target x:", targetLocation[0]);
//            telemetry.addData("target y:", targetLocation[1]);
//            telemetry.addData("destination x:", wallTarget[0]);
//            telemetry.addData("destination y:", wallTarget[1]);
//            telemetry.addData("heading from location", heading_of_target_from_robot_location);
//            telemetry.addData("target heading", target_heading * 180 / Math.PI);
//
//            telemetry.update();
        }
        return new double[]{};
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

    void manualRotate()
    {
        if(gamepad.right_stick_button)
            driveEngine.rotate(-gamepad.right_stick_x);
        else
            driveEngine.rotate(smoothSpin(-gamepad.right_stick_x));
    }
}
