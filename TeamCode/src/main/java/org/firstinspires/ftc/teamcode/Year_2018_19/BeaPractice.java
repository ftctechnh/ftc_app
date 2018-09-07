package org.firstinspires.ftc.teamcode.Year_2018_19;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "BeaPractice1", group = "TeleOp mode")

public class BeaPractice extends OpMode
{

    public DcMotor Bob;
    public DcMotor Richard;

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

    }

    public void stop ()
    {

    }
}
