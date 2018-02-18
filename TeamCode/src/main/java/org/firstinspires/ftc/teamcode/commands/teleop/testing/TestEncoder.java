package org.firstinspires.ftc.teamcode.commands.teleop.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.systems.tools.Direction;

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
            this.mecanumDrive.drive(0.5, 0.5, Direction.FORWARD);
        } else {
            this.mecanumDrive.stop();
        }
        telemetry();
    }

    private void telemetry() {
    }

    @Override
    public void stop() {
        this.mecanumDrive.stop();
    }
}
