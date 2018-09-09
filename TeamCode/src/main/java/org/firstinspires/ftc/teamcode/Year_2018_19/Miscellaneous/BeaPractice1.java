package org.firstinspires.ftc.teamcode.Year_2018_19.Miscellaneous;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "BeaPractice1", group = "TeleOpMode")
@Disabled

public class BeaPractice1 extends OpMode
{

    private DcMotor Bob;
    private DcMotor Richard;

    public void init ()
    {
        Bob =  hardwareMap.get(DcMotor.class, "Bob");
        Richard = hardwareMap.get(DcMotor.class, "Richard");
        Bob.setDirection(DcMotor.Direction.REVERSE);
    }

    public void start ()
    {

    }

    public void loop ()
    {
        //TODO: Make the drive motors move when the gamepad's left and right analogs are moved!
    }

    public void stop ()
    {

    }
}
