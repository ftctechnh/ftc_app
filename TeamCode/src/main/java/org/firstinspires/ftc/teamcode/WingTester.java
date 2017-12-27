package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by JeremyYao on 12/20/2017.
 */
@Disabled
@TeleOp(name = "WingTesterTele", group = "Test")
public class WingTester extends OpMode
{
    NewRobotFinal newRobot;

    public void init()
    {
        newRobot = new NewRobotFinal(hardwareMap);
    }

    public void start()
    {

    }

    public void loop()
    {
        telemetry.addData("WingEncoders= ", newRobot.getWingMotor().getCurrentPosition());
        telemetry.addData("CONTROLS BELOW", null);
        telemetry.addData("A = WING DOWN", null);
        telemetry.addData("B = WING UP", null);
        telemetry.update();

        if (gamepad1.a)
            newRobot.moveWing(true);
        else if (gamepad1.b)
            newRobot.moveWing(false);
    }

    public void stop()
    {
        newRobot.stopAllMotors();
        newRobot.kill();
    }
}
