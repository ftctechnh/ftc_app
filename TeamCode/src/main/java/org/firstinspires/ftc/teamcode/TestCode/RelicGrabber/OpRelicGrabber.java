package org.firstinspires.ftc.teamcode.TestCode.RelicGrabber;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp(name = "Relic Grabber" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpRelicGrabber extends LinearOpMode
{
    @SuppressWarnings("FieldCanBeLocal")
    private CRServo _servo = null;

    @Override
    public void runOpMode() throws InterruptedException
    {
        _servo = hardwareMap.crservo.get("servo");

        waitForStart();


        while(opModeIsActive())
        {
            _servo.setPower(-gamepad1.left_stick_y);
        }
    }
}
