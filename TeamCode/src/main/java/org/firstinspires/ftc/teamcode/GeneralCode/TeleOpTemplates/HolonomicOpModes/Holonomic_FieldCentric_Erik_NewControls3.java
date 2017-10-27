package org.firstinspires.ftc.teamcode.GeneralCode.TeleOpTemplates.HolonomicOpModes;

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

        robot.drive(
                (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
        );

        if(gamepad1.dpad_up)
        {
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
            }
        }

        telemetry.addData("Ultra Turbo Mode Activated", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Direction", Math.toDegrees(jTheta));
        telemetry.addData("Joystick Magnitude", jp);
        telemetry.addData("Gyro Heading", robot.heading);
        telemetry.addData("toggle", toggle);
        telemetry.addData("robotCentric", robotCentric);

    }
}