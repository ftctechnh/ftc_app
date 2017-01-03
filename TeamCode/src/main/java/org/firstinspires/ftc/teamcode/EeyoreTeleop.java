package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.EeyoreHardware;

@TeleOp(name="Eeyore TeleOp", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class EeyoreTeleop extends OpMode {
    EeyoreHardware robot = new EeyoreHardware();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x, -1, 1);
        right = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x, -1, 1);
        robot.l1.setPower(left);
        robot.l2.setPower(left);
        robot.r1.setPower(right);
        robot.r2.setPower(right);

        // Collection control
        if(gamepad1.a) {
            if(robot.collection.getPower() == 0) {
                robot.collection.setPower(1);
            } else {
                robot.collection.setPower(0);
            }
        } else if(gamepad1.y) {
            if(robot.collection.getPower() == 0) {
                robot.collection.setPower(-1);
            } else {
                robot.collection.setPower(0);
            }
        }

        // Shooter control
        if(gamepad1.x) {
            if(robot.shooter1.getPower() == 0) {
                robot.shooter1.setPower(1);
                robot.shooter2.setPower(1);
            } else {
                robot.shooter1.setPower(0);
                robot.shooter2.setPower(0);
            }
        } else if(gamepad1.b) {
            if(robot.shooter1.getPower() == 0) {
                robot.shooter1.setPower(-1);
                robot.shooter2.setPower(-1);
            } else {
                robot.shooter1.setPower(0);
                robot.shooter2.setPower(0);
            }
        }

        // Send telemetry message to signify robot running;
        telemetry.addData("left", "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}