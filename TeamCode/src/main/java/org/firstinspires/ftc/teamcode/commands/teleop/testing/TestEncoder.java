package org.firstinspires.ftc.teamcode.commands.teleop.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.teleop.MecanumDrive;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by nycfirst on 1/9/18.
 */

public class TestEncoder extends OpMode {
    private MecanumDriveSystem mecanumDrive;

    @Override
    public void init() {
        this.mecanumDrive = new MecanumDriveSystem(this);
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if(gamepad1.dpad_up) {
            this.mecanumDrive.drive(0.5, 0.5, 0.5, 0.5);
        } else {
            this.mecanumDrive.stop();
        }

        telemetry.addData("front left motor tick", this.mecanumDrive.getFrontLeftMotorEncoderTick());
        telemetry.addData("rear left motor tick", this.mecanumDrive.getRearLeftMotorEncoderTick());
        telemetry.addData("front right motor tick", this.mecanumDrive.getFrontRightMotorEncoderTick());
        telemetry.addData("rear right motor tick", this.mecanumDrive.getRearRightMotorEncoderTick());
    }

    @Override
    public void stop() {
        this.mecanumDrive.stop();
    }
}
