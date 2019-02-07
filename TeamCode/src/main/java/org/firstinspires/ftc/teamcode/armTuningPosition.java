package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  armTuningPosition", group="Testing")
public class armTuningPosition extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        waitForStart();
        double deltaX = 0;
        double deltaZ = 0;


        while (opModeIsActive())
        {
            telemetry.addData("pivot position", robot.endEffector.pivot.getCurrentPosition());
            telemetry.addData("contract position", robot.endEffector.contract.getCurrentPosition());
            telemetry.addData("pinch position", robot.endEffector.pinch.getPosition());
            telemetry.addData("swing position", robot.endEffector.swing.getPosition());

            deltaX += gamepad1.left_stick_x;
            deltaZ += gamepad1.left_stick_y;

            robot.endEffector.moveToPosition(deltaX, deltaZ);

            telemetry.update();
            robot.update();
            idle();
        }
    }

}

