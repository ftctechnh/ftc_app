package org.firstinspires.ftc.teamcode.TestCode.RelicGrabber;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Relic Grabber" , group = "Prototypes")
@SuppressWarnings("unused")
public class OpRelicGrabber extends LinearOpMode
{
    @SuppressWarnings("FieldCanBeLocal")
    private Servo _servo = null;

    @Override
    public void runOpMode() throws InterruptedException
    {
        _servo = hardwareMap.servo.get("servo");

        waitForStart();


        while(opModeIsActive())
        {
            _servo.setPosition(.5 + -gamepad1.left_stick_y / 2);
        }
    }
}
