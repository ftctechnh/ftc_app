package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="  float motors", group="Testing")
public class floatMotors extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            robot.driveEngine.back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.driveEngine.right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.driveEngine.left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.endEffector.pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.endEffector.contract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.driveEngine.stop();

            telemetry.addData("back", robot.driveEngine.back.getCurrentPosition());
            telemetry.addData("right", robot.driveEngine.right.getCurrentPosition());
            telemetry.addData("left", robot.driveEngine.left.getCurrentPosition());
            telemetry.addData("lift", robot.lift.getCurrentPosition());
            telemetry.addData("pivot", robot.endEffector.pivot.getCurrentPosition());
            telemetry.addData("contract", robot.endEffector.contract.getCurrentPosition());
            telemetry.update();
            robot.update();
            idle();
        }
    }
}

