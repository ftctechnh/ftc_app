package org.firstinspires.ftc.teamcode.commands.teleop.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by Mahim on 1/3/2018.
 */

@TeleOp(name = "testing: mecanum drive", group = "testing")
public class MecanumDrive extends OpMode {
    private MecanumDriveSystem mecanumDriveSystem;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        this.mecanumDriveSystem = new MecanumDriveSystem(hardwareMap, gamepad1);
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() { runtime.reset(); }

    @Override
    public void loop() {
        this.mecanumDriveSystem.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        telemetry.addData("front left speed", mecanumDriveSystem.getFrontLeftSpeed());
        telemetry.addData("rear left speed", mecanumDriveSystem.getRearLeftSpeed());
        telemetry.addData("front right speed", mecanumDriveSystem.getFrontRightSpeed());
        telemetry.addData("rear right speed", mecanumDriveSystem.getRearRightSpeed());

    }

    @Override
    public void stop() {
        this.mecanumDriveSystem.stop();
    }
}
