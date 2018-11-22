package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class sparks extends OpMode
{
    hardwareMap robot = new hardwareMap(DcMotor.RunMode.RUN_USING_ENCODER);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        telemetry.addData("Status ", "Initializing");

        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status ", "Initialized");
    }

    @Override
    public void start()
    {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        double leftPower;
        double rightPower;

        double drive = gamepad1.left_stick_y;
        double turn  = gamepad1.right_stick_x;

        leftPower   = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower  = Range.clip(drive - turn, -1.0, 1.0) ;

        robot.leftFront.setPower(leftPower);
        robot.rightFront.setPower(rightPower);
        robot.leftBack.setPower(leftPower);
        robot.rightBack.setPower(rightPower);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }
}
