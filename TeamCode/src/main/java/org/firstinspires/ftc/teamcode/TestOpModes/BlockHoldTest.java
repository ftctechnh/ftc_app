package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by FTC 4316 on 3/18/2018.
 */

public class BlockHoldTest extends OpMode {
    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
    }

    @Override
    public void loop() {
        robot.blockLift.hold(gamepad1.a);
        robot.blockLift.brake(gamepad1.b);
    }
}