package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 9/22/2016.
 */
@TeleOp(name = "OmniTest", group = "Tests")
public class TestOmniDrive extends OpMode
{
    private TestOmniDriveBot robot = new TestOmniDriveBot();

    public void init()
    {
        robot.init(hardwareMap);
        gamepad1.setJoystickDeadzone(0.01f);
        robot.getfL().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getfR().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getbL().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getbR().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
