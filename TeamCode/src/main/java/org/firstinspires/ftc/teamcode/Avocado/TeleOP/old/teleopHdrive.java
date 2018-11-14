package org.firstinspires.ftc.teamcode.Avocado.TeleOP.old;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Team Inspiration 11128
 */


@TeleOp(name="Basic H Drive (2 Wheel)", group="Test")


public class teleopHdrive extends OpMode{

    /* Define hardware */
    hardwareprofileHdrive robot       = new hardwareprofileHdrive(); // use the class created to define a Pushbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Ready");
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
        loop();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;
        double middle;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;

        robot.topLeftMotor.setPower(left);

        robot.topRightMotor.setPower(right);
        if (gamepad1.dpad_left && !gamepad1.dpad_right) {
            robot.middleMotor.setPower(1);

        } else if (!gamepad1.dpad_left && !gamepad1.dpad_right) {

            robot.middleMotor.setPower(0);
        } else if (gamepad1.dpad_right && !gamepad1.dpad_left) {
            robot.middleMotor.setPower(-1);

        }
        telemetry.addData("Joystick value", left);
        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}

