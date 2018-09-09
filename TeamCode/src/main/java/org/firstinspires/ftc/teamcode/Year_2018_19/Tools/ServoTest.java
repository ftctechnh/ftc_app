package org.firstinspires.ftc.teamcode.Year_2018_19.Tools;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoTest", group = "TestMode")

public class ServoTest extends OpMode
{
    private Servo servo;

    public void init()
    {
        servo = hardwareMap.get(Servo.class, "servo");
        telemetry.addData("Status", "Servo has successfully initialized!");
        servo.setPosition(0);
    }

    public void start()
    {
        servo.setPosition(0.5);
    }

    public void loop() { }

    public void stop()
    {
        servo.setPosition(1);
    }
}
