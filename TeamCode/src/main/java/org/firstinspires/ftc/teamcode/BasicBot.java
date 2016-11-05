package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.BasicBotHardware;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Basic Bot", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class BasicBot extends OpMode {
    BasicBotHardware robot = new BasicBotHardware();

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