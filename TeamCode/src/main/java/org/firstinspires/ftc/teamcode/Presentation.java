package org.firstinspires.ftc.teamcode;

/**
 * Created by Peter on 12/21/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous(name = "Presentation Program", group = "Test")
@Disabled
public class Presentation extends OpMode {
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;

    public void init() {
        double speed = 0.5;

        drive = new DriveMecanum(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        ForkLift.init();
        RelicClaw = new RelicClaw(hardwareMap, telemetry);
        RelicClaw.init();

        //Forklift:
        ForkLift.openClaw();
        sleep((long) speed*2000);
        ForkLift.closeClaw();
        sleep((long) speed*2000);

        ForkLift.moveUntilUp(speed);
        sleep((long) speed*2000);
        ForkLift.moveUntilDown(speed);

        sleep((long) speed*4000);

        //Driving:
        RelicClaw.driving();
        RelicClaw.moveMotor(-speed, 1500);

    }
    public void loop() {
        drive.driveLeftRight(gamepad2.left_stick_x / 4, gamepad2.right_stick_x / 4, gamepad2.left_stick_y / 4, gamepad2.right_stick_y / 4);
        if (gamepad1.a) {
            RelicClaw.closeClaw();
        }
        if (gamepad1.b) {
            RelicClaw.openClaw();
        }
        if (gamepad1.dpad_up) {
            RelicClaw.up();
        }
        if (gamepad1.dpad_down) {
            RelicClaw.down();
        }
        if (gamepad1.x) {
            RelicClaw.pickup();
        }
        if (gamepad1.y) {
            RelicClaw.driving();
        }
        RelicClaw.moveMotor(gamepad1.left_trigger - gamepad1.right_trigger);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }
}
