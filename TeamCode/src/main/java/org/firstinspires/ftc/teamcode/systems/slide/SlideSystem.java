package org.firstinspires.ftc.teamcode.systems.slide;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.LogarithmicRamp;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
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
    private final double MinPower = 0.000001;
    private final double MaxPower = 0.3;

    private boolean hasSetOrigin;
    private double winchOrigin;

    private SlideState currentState;
    private Ramp motionRamp;

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
        motionRamp.getPoint1().setX(EncoderBottom);
        if (!isAtTop())
        {
            winch.setPower(winchOrigin - winch.getCurrentPosition() + EncoderBottom);
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
        motionRamp = new LogarithmicRamp(new Point(winchOrigin, MinPower), new Point(winchOrigin, MaxPower));
    }

    public void slideDown()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motionRamp.getPoint1().setX(winchOrigin - EncoderBottom + Double.MIN_VALUE);
        if (hasSetOrigin && !isAtBottom())
        {
            winch.setPower(-winch.getCurrentPosition());
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
        motionRamp.getPoint1().setX(winchOrigin - EncoderLoad);
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
                -motionRamp.scaleX(winch.getCurrentPosition()) :
                motionRamp.scaleX(2 * EncoderLoad - winch.getCurrentPosition());
    }
}
