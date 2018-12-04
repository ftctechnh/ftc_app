package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "CompTeleV1")
public class CompTeleV1 extends OpMode
{

    CompRobot compRobot;
    @Override
    public void init()
    {
        compRobot = new CompRobot(hardwareMap);
    }

    @Override
    public void loop()
    {
        telemetry.addData("LJoyStick= ", -gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", -gamepad1.right_stick_y);
        telemetry.addData("Right Encod Ct: ", compRobot.getDriveRightOne().getCurrentPosition());
        telemetry.addData("Left Encod Ct: ", compRobot.getDriveLeftOne().getCurrentPosition());
        telemetry.update();
        compRobot.driveMotors(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

    }

    @Override
    public void stop()
    {
        compRobot.stopDriveMotors();
        super.stop();
    }
}
