package org.firstinspires.ftc.teamcode.commands.teleop.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by Mahim on 1/9/18.
 */
@TeleOp(name = "testing encoders", group = "testing")
public class TestEncoder extends OpMode {
    private MecanumDriveSystem mecanumDrive;

    @Override
    public void init() {
        this.mecanumDrive = new MecanumDriveSystem(hardwareMap);
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
            this.mecanumDrive.driveForward(0.5, 0.5);
        } else {
            this.mecanumDrive.stop();
        }
        telemetry();
    }

    private void telemetry() {
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
