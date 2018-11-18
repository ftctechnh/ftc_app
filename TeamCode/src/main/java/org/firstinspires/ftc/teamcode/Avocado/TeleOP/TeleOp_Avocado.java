package org.firstinspires.ftc.teamcode.Avocado.TeleOP;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Avocado.Robot.Robot;

/* ----------------------------------------------------------------------------------------------- */

/** Future master TeleOp file - NOT TESTED */

/* ----------------------------------------------------------------------------------------------- */

@TeleOp(name = "Avacado TeleOp")


public class TeleOp_Avocado extends OpMode {

    Robot robot = new Robot();

    public void loop() {

        robot.TankDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.lift_a(gamepad2.left_stick_y);
        robot.strafe(gamepad1.dpad_left, gamepad1.dpad_right, gamepad1.dpad_up, gamepad1.dpad_down);

    }

    public void init() {
        robot.init(hardwareMap);
    }

}
