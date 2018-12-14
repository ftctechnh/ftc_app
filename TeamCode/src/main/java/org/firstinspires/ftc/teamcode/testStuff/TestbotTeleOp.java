package org.firstinspires.ftc.teamcode.testStuff;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.testStuff.TestbotHardware;

@TeleOp(name = "TestbotTeleOp", group = "Testbot")
//@Disabled


public class TestbotTeleOp extends OpMode   //OpMode for teleOp and linearOpMode for autonomous
{
    TestbotHardware robot = new TestbotHardware(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() //what happens during initiation
    {
        robot.init(hardwareMap);
        telemetry.addData("initialization", "Complete");
    }

    @Override
    public void init_loop() //not needed for now
    {}

    @Override
    public void start()
    {
     runtime.reset(); //resets runtime
    }

    @Override
    public void loop()// runs opMode
    {
        robot.leftDrive.setPower(gamepad1.left_stick_y);
        robot.rightDrive.setPower(gamepad1.right_stick_y);
    }
}
