package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Flywheel;

/**
 * Created by Howard on 10/15/16.
 */
public class FlywheelTask extends TaskThread {
    private Flywheel flywheel;

    public FlywheelTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive() && this.running) {
            if (opMode.gamepad1.a || opMode.gamepad2.a ){
                flywheel.setPower(1);
            }else if (opMode.gamepad1.b || opMode.gamepad2.b ){
                flywheel.setPower(-1);
            }else{
                flywheel.setPower(0);
            }
        }
    }
    @Override
    public void initialize() {
        this.flywheel = new Flywheel(this.opMode);
    }
}


