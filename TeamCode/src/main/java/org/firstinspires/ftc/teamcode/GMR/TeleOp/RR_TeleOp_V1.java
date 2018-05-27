package org.firstinspires.ftc.teamcode.GMR.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 11/18/2017
 */

public class RR_TeleOp_V1 extends OpMode {

    private Robot robot;

    private boolean letterButton = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
    }

    @Override
    public void loop() {

        robot.blockLift.lift(gamepad1.right_bumper, gamepad1.right_trigger, telemetry);
        robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.setServos();
    }

}