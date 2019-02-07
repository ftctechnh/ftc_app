package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Manually Move Arm", group="Testing")
public class manuallyMoveArm extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        waitForStart();

        robot.endEffector.pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.endEffector.contract.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive())
        {
            telemetry.addData("pivot position", robot.endEffector.pivot.getCurrentPosition());
            telemetry.addData("contract position", robot.endEffector.contract.getCurrentPosition());
            telemetry.addData("pinch position", robot.endEffector.pinch.getPosition());
            telemetry.addData("swing position", robot.endEffector.swing.getPosition());


            robot.endEffector.pivot.setPower(gamepad1.right_stick_y);
            robot.endEffector.contract.setPower(gamepad1.left_stick_y);

            telemetry.update();
            robot.update();
            idle();
        }
    }

}

