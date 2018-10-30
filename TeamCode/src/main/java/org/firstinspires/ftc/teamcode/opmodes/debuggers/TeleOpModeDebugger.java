package org.firstinspires.ftc.teamcode.opmodes.debuggers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class TeleOpModeDebugger extends OpMode
{
    private Debugger debugger;

    public TeleOpModeDebugger() {
        debugger = new Debugger();
    }

    @Override
    public void init() {

    }

    public abstract void initialize();

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
