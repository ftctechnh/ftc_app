package org.firstinspires.ftc.teamcode.opmodes.debuggers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class TeleOpModeDebugger extends OpMode
{
    private Debugger debugger;

    public TeleOpModeDebugger() {
        debugger = new Debugger();
    }

    public abstract void init();

    @Override
    public void loop()
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
