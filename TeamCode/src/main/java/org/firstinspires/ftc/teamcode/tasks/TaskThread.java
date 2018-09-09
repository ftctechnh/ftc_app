package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Anton on 10/15/17.
 */

public abstract class TaskThread extends Thread{
    public volatile boolean running = true;
    public LinearOpMode opMode;
    public volatile boolean teleOp = false;
    public static volatile double voltage = 13;
    public static final double EXPECTED_VOLTAGE = 13;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public void sleep (int ms) {
        timer.reset();
        while (opMode.opModeIsActive() && timer.time() < ms);
    }

    public static void calculateVoltage(LinearOpMode opMode) {
        double mc7 = opMode.hardwareMap.voltageSensor.get("frontDrive").getVoltage();
        double mc6 = opMode.hardwareMap.voltageSensor.get("backDrive").getVoltage();
        double mc3 = opMode.hardwareMap.voltageSensor.get("cap").getVoltage();
        double mc2 = opMode.hardwareMap.voltageSensor.get("flywheels").getVoltage();
        voltage = (mc7 + mc6 + mc3 + mc2) / 4;
    }



    public abstract void initialize();

}
