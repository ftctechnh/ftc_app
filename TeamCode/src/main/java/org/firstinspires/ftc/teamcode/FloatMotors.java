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
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.floatMotors();
        robot.driveEngine.stop();

        waitForStart();

        while (opModeIsActive())
        {
            robot.driveEngine.floatMotors();
            robot.driveEngine.reportPositionsToScreen();

            if(robot.name == Bogg.Name.Bogg) {
                robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.endEffector.pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.endEffector.contract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                telemetry.addData("lift", robot.lift.getCurrentPosition());
                telemetry.addData("pivot", robot.endEffector.pivot.getCurrentPosition());
                telemetry.addData("contract", robot.endEffector.contract.getCurrentPosition());
            }
            telemetry.update();
            robot.update();
            idle();
        }
    }
}

