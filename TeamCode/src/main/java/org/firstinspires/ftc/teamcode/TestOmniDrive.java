package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 9/22/2016.
 */
@TeleOp(name = "OmniTest", group = "Tests")
public class TestOmniDrive extends OpMode
{
    private TestOmniDriveBot robot = new TestOmniDriveBot();
    public float leftYIn;
    public float leftXIn;
    public float rightXIn;

    public void init()
    {
        robot.init(hardwareMap);
        leftYIn = -gamepad1.left_stick_y;
        leftXIn = gamepad1.left_stick_x;
        rightXIn = gamepad1.right_stick_x;
        gamepad1.setJoystickDeadzone(0.01f);
    }

    public void loop()
    {
        telemetry.addData("Gamepad left x", gamepad1.left_stick_x);
        telemetry.addData("Gamepad left y", gamepad1.left_stick_y);
        telemetry.addData("Gamepad right x", gamepad1.right_stick_x);
        telemetry.update();

        robot.getfR().setPower((leftYIn - leftXIn - rightXIn));
        robot.getfL().setPower(-leftYIn - leftXIn - rightXIn);
        robot.getbR().setPower(leftYIn + leftXIn - rightXIn);
        robot.getbL().setPower(-leftYIn + leftXIn - rightXIn);
    }
}
