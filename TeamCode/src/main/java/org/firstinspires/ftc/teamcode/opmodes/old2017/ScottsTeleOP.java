package org.firstinspires.ftc.teamcode.opmodes.old2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.hardware.HardwareRatbot;

/**
 * Created by bremm on 10/6/2017.
 */

@TeleOp(name="ratbot2017", group="Ratbot")
@Disabled
public class ScottsTeleOP extends OpMode {

    HardwareRatbot robot       = new HardwareRatbot();

    @Override
    public void init() {

        robot.init(hardwareMap);

        telemetry.addData("Say", "Hello Driver");    //
    }

    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
    }


    @Override
    public void loop() {
        double left;
        double right;

        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;

        robot.leftDrive.setPower(left);
        robot.rightDrive.setPower(right);

    }
    @Override
    public void stop() {
    }
}
