package org.firstinspires.ftc.teamcode.systems.slide;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.systems.base.System;

public class SlideSystem extends System
{

    /*
       TODO:
       - ramp the encoder values
     */

    private DigitalChannel limitTop;
    private DigitalChannel limitMiddle;
    private DcMotor winch;

    private final int EncoderBottom = 10810;
    private final int EncoderLoad = 3340;

    private boolean hasSetOrigin;
    private double winchOrigin;

    private SlideState currentState;

    public SlideSystem(OpMode opMode)
    {
        super(opMode, "SlideSystem");
        winch = hardwareMap.dcMotor.get("winch");
        limitTop = hardwareMap.get(DigitalChannel.class, "limitTop");
        limitMiddle = hardwareMap.get(DigitalChannel.class, "limitMiddle");

        hasSetOrigin = false;
        setState(SlideState.IDLE);

        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitMiddle.setMode(DigitalChannel.Mode.INPUT);
    }

    public void setState(SlideState state)
    {
        currentState = state;
    }

    public void run()
    {
        switch (currentState) {
            case WINCHING_TO_TOP:
                slideUp();
                break;
            case WINCHING_TO_BOTTOM:
                slideDown();
                break;
            case WINCHING_TO_LOAD:
                slideLoad();
                break;
        }
        telemetry.write();
    }

    public void slideUp()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtTop())
        {
            winch.setPower(0.5);
        }
        else
        {
            setState(SlideState.IDLE);
            setOrigin();
        }
    }

    private boolean isAtTop() {
        return limitTop.getState() || limitMiddle.getState();
    }

    private void setOrigin() {
        winch.setPower(0.0);
        hasSetOrigin = true;
        winchOrigin = winch.getCurrentPosition();
    }

    public void slideDown()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (hasSetOrigin && !isAtBottom())
        {
            winch.setPower(-0.4);
        }
        else
        {
            setState(SlideState.IDLE);
            winch.setPower(0);
        }
    }

    private boolean isAtBottom() {
        return winch.getCurrentPosition() > winchOrigin - EncoderBottom;
    }

    public void slideLoad()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (hasSetOrigin && !isInLoadRange())
        {
            winch.setPower(getLoadPower());
        }
        else
        {
            setState(SlideState.IDLE);
            winch.setPower(0);
        }
    }

    private boolean isInLoadRange() {
        return winch.getCurrentPosition() <= winchOrigin - EncoderLoad + 100 &&
                winch.getCurrentPosition() >= winchOrigin - EncoderLoad - 100;
    }

    private double getLoadPower()
    {
        return winch.getCurrentPosition() > winchOrigin - EncoderLoad ?
                -0.5 :
                0.5;
    }
}
