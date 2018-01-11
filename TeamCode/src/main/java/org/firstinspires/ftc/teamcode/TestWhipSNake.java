package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Test whip snake", group="Diagnostics")
public class TestWhipSnake extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        waitForStart();

        double i = 0;
        while (opModeIsActive() && i <= 1) {
            setPos(i);
            robot.sleep(750);
            i += 0.05;
        }
    }

    public void setPos(double d) {
        telemetry.addData("Position", d);
        telemetry.update();
        robot.leftWhipSnake.setPosition(d);
    }
}
