package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 11/4/2017.
 */

@TeleOp
public class TeleOPDrive extends OpMode {
    private DriveSystem driveSystem;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        this.driveSystem = new DriveSystem(hardwareMap, gamepad1);
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() { runtime.reset(); }

    @Override
    public void loop() {
        this.driveSystem.rcCarDrive();
        telemetry.addData("left speed", driveSystem.getLeftSpeed());
        telemetry.addData("right speed", driveSystem.getRightSpeed());
    }

    @Override
    public void stop() {
        driveSystem.stop();
    }
}