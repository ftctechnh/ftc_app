package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Peter on 9/22/2016.
 */
@TeleOp(name = "OmniTest", group = "Tests")
public class OmniDriveTeleOp extends OpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void init()
    {
        robot.init(hardwareMap);
        gamepad1.setJoystickDeadzone(0.01f);
    }

    public void loop()
    {
        robot.setLeftYIn(gamepad1.left_stick_y);
        robot.setLeftXIn(gamepad1.left_stick_x);
        robot.setRightXIn(gamepad1.right_stick_x);

        robot.drive();

        telemetry.addData("fR Power", robot.getfRPower());
        telemetry.addData("fL Power", robot.getfLPower());
        telemetry.addData("bR Power", robot.getbRPower());
        telemetry.addData("bL Power", robot.getbLPower());
        telemetry.update();
    }
}
