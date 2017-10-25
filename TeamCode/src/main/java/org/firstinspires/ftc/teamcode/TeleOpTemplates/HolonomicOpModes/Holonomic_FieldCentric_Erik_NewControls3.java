package org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
Tests the Dpad Functions moving rotating (just top and right as of now)
 */

@TeleOp(name = "Holonomic Field-Centric Tele-Op Test 3", group = "holonomic Erik")
public class Holonomic_FieldCentric_Erik_NewControls3 extends OpMode
{
    Holonomic_Hardware robot;
    double angleFromDriver = Math.PI/2;
    double jTheta;
    double jp;
    double theta;
    boolean robotCentric = false;
    boolean toggle = false;
    int dpad = 0;
    int z = 1;

    @Override
    public void init ()
    {
        robot = new Holonomic_Hardware(hardwareMap, telemetry, true);
    }

    @Override
    public void loop ()
    {
        robot.updateGyro();

        jTheta = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);

        jp = Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y);

        if(jp > 1)
            jp = 1;

        theta = (jTheta + angleFromDriver - robot.heading);

        telemetry.addData("Ultra Turbo Mode Activated", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Direction", Math.toDegrees(jTheta));
        telemetry.addData("Joystick Magnitude", jp);
        telemetry.addData("Gyro Heading", robot.heading);
        telemetry.addData("toggle", toggle);
        telemetry.addData("robotCentric", robotCentric);

        if(gamepad1.dpad_up)
            dpad = 1;
        else if(gamepad1.dpad_right)
            dpad = 2;
        else if(gamepad1.dpad_down)
            dpad = 3;
        else if(gamepad1.dpad_left)
            dpad = 4;
        else
            dpad = 0;

        switch(dpad)
        {
            case 0:
                robot.drive(
                        (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
                );
                break;
            case 1: // 0
                if(robot.heading > 0 && robot.heading < Math.PI)
                {
                    while (robot.heading < 2*Math.PI && robot.heading > Math.PI)
                    {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z
                        );
                    }
                    dpad = 0;
                }
                else if(robot.heading > Math.PI)
                {
                    while (robot.heading < Math.PI) {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z
                        );
                    }
                    dpad = 0;
                }
                else
                    dpad =0;
                break;
            case 2: //270
                if(robot.heading > Math.PI/2 && robot.heading < 3*Math.PI/2)
                {
                    while (robot.heading < 3*Math.PI/2)
                    {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z
                        );
                    }
                    dpad = 0;
                }
                else if(robot.heading > 3*Math.PI/2 || robot.heading < Math.PI/2)
                {
                    while (robot.heading > 3*Math.PI/2) {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z
                        );
                    }
                    dpad = 0;
                }
                else
                    dpad =0;
                break;
            default:
                robot.drive(
                        (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
                );
        }

    }
}