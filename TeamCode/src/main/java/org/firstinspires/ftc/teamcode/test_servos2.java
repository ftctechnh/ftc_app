package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="test servos2", group="Testing")
public class test_servos2 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    double pinch = .44;
    double swing = 0.25;
    double drop = 0;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;

        waitForStart();
        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            if(t < 1) {
                robot.endEffector.swing.setPosition(0);
                robot.endEffector.pinch.setPosition(0);
                robot.drop.setPosition(0);
            }
            else if(t < 3) {
                robot.endEffector.swing.setPosition(.5);
                robot.endEffector.pinch.setPosition(.5);
                robot.drop.setPosition(.5);
            }
            else if(t < 5) {
                robot.endEffector.swing.setPosition(.25);
                robot.endEffector.pinch.setPosition(.25);
                robot.drop.setPosition(.25);
            }
            else if(t < 7) {
                robot.endEffector.swing.setPosition(.75);
                robot.endEffector.pinch.setPosition(.75);
                robot.drop.setPosition(.75);
            }
            else if(t < 9) {
                robot.endEffector.swing.setPosition(1);
                robot.endEffector.pinch.setPosition(1);
                robot.drop.setPosition(1);
            }
            else if(t < 11) {
                robot.endEffector.swing.setPosition(0);
                robot.endEffector.pinch.setPosition(0);
                robot.drop.setPosition(0);
            }


            telemetry.addData("time", t);
            telemetry.addData("pinch", robot.endEffector.pinch.getPosition());
            telemetry.addData("swing", robot.endEffector.swing.getPosition());
            telemetry.addData("drop", robot.drop.getPosition());


            telemetry.update();
            robot.update();
            idle();
        }
    }
}

