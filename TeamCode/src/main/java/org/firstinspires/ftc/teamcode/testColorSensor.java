package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="testColorSensor", group="Testing")
public class testColorSensor extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        MyColorSensor myColorSensor = new MyColorSensor(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("hue", myColorSensor.getHue());
            telemetry.addData("color", myColorSensor.getColor().name());
            telemetry.addData("typicalHue", myColorSensor.getColor().typicalHue);

            robot.update();
            idle();
        }
    }
}

