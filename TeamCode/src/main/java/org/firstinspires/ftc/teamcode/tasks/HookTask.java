package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Hook;
import org.firstinspires.ftc.teamcode.robotutil.Logger;

/**
 * Created by Howard on 10/15/16.
 */
public class HookTask extends TaskThread {
    private Hook hook;

    public HookTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();

        boolean retracted = false;

        while (opMode.opModeIsActive() && this.running) {
            if (opMode.gamepad1.a) {
                if (retracted) {
                    hook.attach();
                    retracted = false;
                } else {
                    hook.retract();
                    retracted = true;
                }
            }
        }
    }

    @Override
    public void initialize() {
        this.hook = new Hook(this.opMode);
    }
}


