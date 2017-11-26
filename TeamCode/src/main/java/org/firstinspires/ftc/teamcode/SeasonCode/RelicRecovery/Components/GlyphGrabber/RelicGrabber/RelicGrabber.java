package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.RelicGrabber;
import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.directcurrent.core.gamecontroller.Controller;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;

/**
 * Created by AlexPC on 11/25/2017.
 */

public class RelicGrabber extends RobotComponent
{
     private Controller _controller1 = new Controller();
    private Controller _controller2 = new Controller();

    private CRServo Grabber;
    private CRServo Twister;


    public enum State
    {
        InG,
        Out,
        StopG
    }

    public enum State2
    {
        TUp,
        TDown,
        TStill
    }

    @Override
    public void init(final RobotBase BASE) {
        super.init(BASE);

        Grabber = mapper.mapCRServo("RelicGrabber", CRServo.Direction.FORWARD);
        Twister = mapper.mapCRServo("Twister", CRServo.Direction.FORWARD);
    }

    public void setState(final State STATE)
    {
        switch (STATE)
        {
            case InG:
                Grabber.setPower(1);

                break;

            case Out:
            Grabber.setPower(-1);

            break;

            case StopG:
                Grabber.setPower(0);
        }
    }




    public void setState(final State2 STATE2)
    {
        switch(STATE2)
        {
            case TUp:
            Twister.setPower(1);

            break;

            case TDown:
            Twister.setPower(-1);

            break;

            case TStill:
            Twister.setPower(0);

        }

    }

}






