package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="test servos", group="Testing")
public class test_servos extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    double pinch = .44;
    double swing = 0.25;
    double drop = 0;
    double brake = .55;
    double brake2 = .5;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addLine(robot.name.toString());
            pinch += .05 * g1.left_stick_y * Bogg.averageClockTime;
            telemetry.addData("pinch", pinch);

            robot.endEffector.pinch.setPosition(pinch);

            swing += .05 * g1.right_stick_y * Bogg.averageClockTime;
            telemetry.addData("swing", swing);

            robot.endEffector.swing.setPosition(swing);

            if(g1.y && drop < 1)
                drop += .1 * Bogg.averageClockTime;
            if(g1.a && drop > -1)
                drop -= .1 * Bogg.averageClockTime;

            robot.drop.setPosition(drop);
            telemetry.addData("drop", drop);

            if(gamepad1.left_stick_button)
                brake -= .1 * Bogg.averageClockTime;
            if(gamepad1.right_stick_button)
                brake += .1 * Bogg.averageClockTime;

            if(gamepad1.x)
                brake2 -= .1 * Bogg.averageClockTime;
            if(gamepad1.b)
                brake2 += .1 * Bogg.averageClockTime;

            robot.brake.setPosition(brake);
            robot.brake2.setPosition(brake2);
            telemetry.addData("brake", brake);
            telemetry.addData("brake2", brake2);


            sleep(50);
            robot.update();
            idle();
        }
    }
}

