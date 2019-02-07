package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  armTuningCode", group="Testing")
public class armTuningCode extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        waitForStart();
        double pivot = robot.endEffector.getAngle();
        double deltaY = 0;
        double deltaZ = 0;


        while (opModeIsActive())
        {
            telemetry.addData("pivot position", robot.endEffector.pivot.getCurrentPosition());
            telemetry.addData("contract position", robot.endEffector.contract.getCurrentPosition());
            telemetry.addData("pinch position", robot.endEffector.pinch.getPosition());
            telemetry.addData("swing position", robot.endEffector.swing.getPosition());

            pivot += gamepad1.right_stick_y / 10;
            robot.endEffector.pivot(pivot);

            robot.endEffector.extend(2* gamepad1.left_stick_y);

            telemetry.update();
            robot.update();
            idle();
        }
    }

}

