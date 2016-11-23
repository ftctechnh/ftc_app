package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Kota Baer on 11/22/2016.
 */
@TeleOp(name = "TestMode")
public class TestMode extends OpMode  {
    Hardware5035 robot = new Hardware5035();

    @Override
    public void init() {
            robot.init(hardwareMap);

    }

    @Override
    public void loop() {
        if (gamepad1.x)
        {
            robot.popUp.setPosition(Math.min(robot.popUp.getPosition() + 0.001, 1));
        }
        if (gamepad1.y)
        {
            robot.popUp.setPosition(Math.max(robot.popUp.getPosition() + -0.001, 0));
        }
        telemetry.addData("Position of server", robot.popUp.getPosition());
        telemetry.update();
    }
}
