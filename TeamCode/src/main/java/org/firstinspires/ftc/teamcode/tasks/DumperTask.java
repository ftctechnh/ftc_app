package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Dumper;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;

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

        while (opMode.opModeIsActive() && !opMode.isStopRequested()
                && this.running) {
            if (opMode.gamepad1.y || opMode.gamepad2.y) {
                if (retracted) {
                    dumper.dump();
                    retracted = false;
                } else {
                    dumper.retract();
                    retracted = true;
                }
            }
        }
    }

    @Override
    public void initialize() {
        this.dumper = new Dumper(this.opMode);
    }
}


