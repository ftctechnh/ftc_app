package org.firstinspires.ftc.teamcode.Year_2018_19.Tools;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoTest", group = "TestMode")
//@Disabled

public class ServoTest extends OpMode
{
    private Servo servo;

    public void init()
    {
        servo = hardwareMap.get(Servo.class, "servo");
        telemetry.addData("Status", "Servo has successfully initialized!");
    }

    public void start()
    {
        servo.setPosition(0.5);
    }

    public void loop()
    {
        if (gamepad1.a) {servo.setPosition(0);}

        else if (gamepad1.b) {servo.setPosition(0.25);}

        else if (gamepad1.y) {servo.setPosition(0.75);}

        else if (gamepad1.x) {servo.setPosition(1);}

        telemetry.addData("Servo", servo.getPosition());
        telemetry.update();
    }
}
