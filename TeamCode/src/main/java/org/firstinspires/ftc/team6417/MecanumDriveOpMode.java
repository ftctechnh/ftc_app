package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.team6417.MecanumDrive;

@TeleOp(name = "mechanum driving opmode", group = "ftc6417")
public class MecanumDrivingOpMode extends OpMode {
    private MecanumDrive mecanumDrive = new MecanumDrive();

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        mecanumDrive.init(hardwareMap);
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        double forward = gamepad1.left_stick_y * -1; //The y direction on the gamepad is reversed idk why
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        mecanumDrive.driveMecanum(forward, strafe, rotate);

    }
}