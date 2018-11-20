package org.firstinspires.ftc.teamcode.systems.slide;

import android.transition.Slide;

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
    private final double WinchPower = -0.01;

    private DigitalChannel limitTop;
    private DigitalChannel limitMiddle;
    private DcMotor winch;

    private SlideState state;

    private double winchOrigin;

    public SlideSystem(OpMode opMode)
    {
        super(opMode, "SlideSystem");
        winch = hardwareMap.dcMotor.get("winch");
        limitTop = hardwareMap.get(DigitalChannel.class, "limitTop");
        limitMiddle = hardwareMap.get(DigitalChannel.class, "limitMiddle");

        winchOrigin = winch.getCurrentPosition();
        setState(SlideState.IDLE);
        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitMiddle.setMode(DigitalChannel.Mode.INPUT);
    }

    public void setState(SlideState state) {
        this.state = state;
    }

    public void run() {
        switch (state) {
            case WINCHING_TO_TOP:
                slideUp();
            case WINCHING_TO_BOTTOM:
                slideDown();
            default:
                stop();
        }
    }

    public void slideUp()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtTop())
        {
            winch.setPower(WinchPower);
        }
        else
        {
            winch.setPower(0.0);
        }
    }

    private boolean isAtTop() {
        return !limitTop.getState() && !limitMiddle.getState();
    }

    public void slideDown()
    {
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtBottom())
        {
            winch.setPower(-WinchPower);
        }
        else
        {
            winch.setPower(0);
        }
    }

    private boolean isAtBottom() {
        return winch.getCurrentPosition() >= winchOrigin;
    }

    public void stop() {
        winch.setPower(0);
    }
}
