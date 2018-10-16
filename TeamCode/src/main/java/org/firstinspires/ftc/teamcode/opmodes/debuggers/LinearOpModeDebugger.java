package org.firstinspires.ftc.teamcode.opmodes.debuggers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class LinearOpModeDebugger extends LinearOpMode
{
    private Debugger debugger;

    public LinearOpModeDebugger() {
        debugger = new Debugger();
    }

    @Override
    public void runOpMode() throws InterruptedException
    {
        try {
            run();
        } catch (Exception e) {
            debugger.debug(e);
            throw e;
        }
    }

    public abstract void run();
}
