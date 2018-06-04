package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MecanumTeleop", group = "Iterative Opmode")
//@Disabled
public class Teleop extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot = new Robot();


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        double xVelocity;
        double yVelocity;
        double wVelocity;

        yVelocity = -gamepad1.left_stick_y;
        xVelocity = gamepad1.left_stick_x;
        wVelocity = gamepad1.right_stick_x;
        yVelocity = Range.clip(yVelocity, -1.0, 1.0);
        xVelocity = Range.clip(xVelocity, -1.0, 1.0);
        wVelocity = Range.clip(wVelocity, -1.0, 1.0);

        robot.drive(xVelocity, yVelocity, wVelocity);

        // Show the elapsed game time and velocities.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "X (%.2f), Y (%.2f), W (%.2f)", xVelocity, yVelocity, wVelocity);
    }

    @Override
    public void stop() {
    }

}
