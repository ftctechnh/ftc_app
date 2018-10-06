package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.ModernRoboticsRobot;

@TeleOp(name="ModernRoboticsTeleOp", group="TeleOpMode")
//@Disabled

public class ModernRoboticsTeleOp extends OpMode {

    private ModernRoboticsRobot robot = new ModernRoboticsRobot();

    @Override
    public void init()
    {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Robot has initialized!");
        telemetry.update();
    }

    @Override
    public void start()
    {
        telemetry.addData("Status", "Robot has started running!");
        telemetry.update();
    }

    @Override
    public void loop()
    {
        robot.leftDrive.setPower(-gamepad1.left_stick_y);
        robot.rightDrive.setPower(-gamepad1.right_stick_y);

        telemetry.addData("Left Drive", robot.leftDrive.getPower());
        telemetry.addData("Right Drive", robot.rightDrive.getPower());

        telemetry.update();
    }

    @Override
    public void stop() {
        robot.safetyStop();
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }
}
