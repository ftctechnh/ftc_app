package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Drive_Test", group="Pushbot")

public class TeleOpTest extends OpMode {

    RoverDrive robot       = new RoverDrive();

    @Override
    public void init() {
        robot.init(hardwareMap);
        //the lines below allow the control drive method to work
        robot.leftMid.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightMid.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Hello","this is a test");
    }
    @Override
    public void loop() {
        robot.controlDrive(gamepad1.left_stick_y,gamepad1.right_stick_y);
    }
    @Override
    public void stop(){
        robot.leftMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
