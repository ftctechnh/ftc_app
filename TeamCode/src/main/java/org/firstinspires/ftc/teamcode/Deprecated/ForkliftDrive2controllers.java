package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Kaden on 10/19/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.DriveMecanum;
import org.firstinspires.ftc.teamcode.ForkLift;

@TeleOp(name = "ForkliftDrivewith2remotes", group = "linear OpMode")
@Disabled
public class ForkliftDrive2controllers extends OpMode {
    private ForkLift ForkLift;
    private DriveMecanum drive;

    @Override
    public void init() {
        drive = new DriveMecanum(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        ForkLift.init();
    }

    @Override
    public void loop() {
        drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
        if (gamepad2.a) {
            ForkLift.closeClaw();

        }
        if (gamepad2.b) {
            ForkLift.openClaw();
        }
        ForkLift.moveMotor(gamepad2.right_trigger - gamepad2.left_trigger);
    }
}
