package org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
Program where using the bumpers makes it robot centric
 */

@TeleOp(name = "Holonomic Field-Centric Tele-Op Test 2", group = "holonomic Erik")
public class Holonomic_FieldCentric_Erik_NewControls2 extends OpMode
{
    Holonomic_Hardware robot;
    double angleFromDriver = Math.PI/2;
    double jTheta;
    double jp;
    double theta;
    boolean robotCentric = false;

    @Override
    public void init ()
    {
        robot = new Holonomic_Hardware(hardwareMap, telemetry, true);

    }

    @Override
    public void loop ()
    {
        if(gamepad1.right_bumper)
            robotCentric = true;
        else
            robotCentric = false;

        robot.updateGyro();

        jTheta = (double) Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);

        jp = (double) Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y);

        if(jp > 1)
            jp = 1;

          //Literally makes it robot Centric
        if(robotCentric == false)
            theta = (jTheta + angleFromDriver - robot.heading);
        else if(robotCentric)
            theta = jTheta;

        robot.drive(
                (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
        );

        telemetry.addData("Ultra Turbo Mode Activated", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Direction", Math.toDegrees(jTheta));
        telemetry.addData("Joystick Magnitude", jp);
        telemetry.addData("Gyro Heading", robot.heading);
        telemetry.addData("robotCentric", robotCentric);
    }
}