package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Anton on 10/15/17.
 */

public abstract class TaskThread extends Thread{
    public volatile boolean running = true;
    LinearOpMode opMode;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public void sleep (int ms) {
        timer.reset();
        while (opMode.opModeIsActive() && timer.time() < ms){
            opMode.telemetry.update();
        }
    }
    public void stopThread(){
        this.running = false;
    }

    public abstract void initialize();

}
