package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="holonomicDrive Deliver Valentines", group="Testing")
public class holonomicDrive_0_5 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    double driveAngle, initialAngle = Math.PI;

    @Override
    public void runOpMode()
    {

        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(driveAngle);
        g1 = gamepad1;
        waitForStart();

        robot.endEffector.pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.endEffector.contract.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive())
        {
            robot.manualDrive2(g1.left_stick_button, g1.left_stick_x, g1.left_stick_y, g1.right_stick_x);

            if(g1.x)
                driveAngle = initialAngle + robot.sensors.getImuHeading();

            if(robot.name == Bogg.Name.Bogg)
            {
                if(g1.dpad_down)
                    robot.setBrake(Bogg.Direction.On);
                else if(g1.dpad_up)
                    robot.setBrake(Bogg.Direction.Off);

                if(g1.right_bumper)
                    robot.endEffector.pivot.setPower(.5);
                if(g1.left_bumper)
                    robot.endEffector.pivot.setPower(-.5);

                robot.endEffector.contract.setPower(-g1.right_stick_y);

                if(g1.left_stick_button)
                    robot.endEffector.close();
                if(g1.right_stick_button)
                    robot.endEffector.open();

                robot.manualLift(g1.y, g1.a);
            }


            robot.update();
            idle();
        }
    }
}

