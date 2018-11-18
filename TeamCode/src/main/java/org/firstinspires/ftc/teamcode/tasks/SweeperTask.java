package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;

/**
 * Created by Howard on 10/15/16.
 */
public class SweeperTask extends TaskThread {
    private Sweeper sweeper;

    public SweeperTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        boolean triggerIntake,triggerReverse;
        timer.reset();
        while (opMode.opModeIsActive() && this.running) {
            triggerIntake = opMode.gamepad1.x || opMode.gamepad2.x;
            triggerReverse = opMode.gamepad1.y || opMode.gamepad2.y;

            if (triggerIntake){
                sweeper.setPower(1);
            }else if (triggerReverse){
                sweeper.setPower(-1);
            }else{
                sweeper.setPower(0);
            }
        }
    }
    @Override
    public void initialize() {
        this.sweeper = new Sweeper(this.opMode);
    }
}


