package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name="okay", group="TeleOp")
public class okay extends OpMode {
    Hardware750 robot = new Hardware750();
    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void loop() {
        if (gamepad1.a) {robot.setAllMotors(.25); } else {robot.setAllMotors(0);}
        telemetry.addData("pos:", robot.rrDrive.getCurrentPosition());
        telemetry.addData("pos:", robot.rlDrive.getCurrentPosition());
        telemetry.addData("pos:", robot.frDrive.getCurrentPosition());
        telemetry.addData("pos:", robot.flDrive.getCurrentPosition());

    }

}
