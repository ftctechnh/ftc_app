package org.firstinspires.ftc.teamcode.Avocado.TeleOP;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Avocado.Robot.Robot;

/* ----------------------------------------------------------------------------------------------- */

/**
 * New project structure - methods and hardware is defined in a seperate files. The master TeleOP
 * file exists only to invoke those methods and pass controls into the methods.
 * In order to initialize the methods, create an object that belongs to the robot file:
 * import org.firstinspires.ftc.teamcode.Avocado.Robot.Robot;
 * Robot robot = new Robot();
 * This will allow you to call methods that control aspects of movement.
 * Ex: robot.TankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y);
 * This will allow for clean and compartmentalized code
 */

/* ----------------------------------------------------------------------------------------------- */

@TeleOp(name = "Avacado TeleOp")


public class TeleOp_Avocado extends OpMode {

    Robot robot = new Robot();

    public void loop() {

        robot.TankDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.lift(gamepad2.left_stick_y);
        robot.strafe(gamepad1.dpad_left, gamepad1.dpad_right, gamepad1.dpad_up, gamepad1.dpad_down);

    }

    public void init() {
        robot.init(hardwareMap);
    }

}
