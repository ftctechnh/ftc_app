package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Dumper;

/**
 * Created by Howard on 10/15/16.
 */
public class DumperTask extends TaskThread {
    private Dumper dumper;

    public DumperTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        boolean retracted = true;

        boolean triggered;
        while (opMode.opModeIsActive() && !opMode.isStopRequested()
                && this.running) {

            triggered = opMode.gamepad1.start || opMode.gamepad2.start;

            if (triggered) {
                if (retracted) {
                    dumper.dump();
                    retracted = false;
                } else {
                    dumper.retract();
                    retracted = true;
                }
                sleep(300);

            }
        }
    }

    @Override
    public void initialize() {
        this.dumper = new Dumper(this.opMode);
    }
}


