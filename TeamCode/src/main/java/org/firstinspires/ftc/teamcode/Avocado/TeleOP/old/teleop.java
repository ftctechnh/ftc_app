package org.firstinspires.ftc.teamcode.Avocado.TeleOP.old;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Team Inspiration 11128
 * This file provides code for a sample IterativeOpMode that pertains to a possible range of viable robots for the ROVER RUCKUS challenge.
 */


@TeleOp(name="Basic Tank Drive", group="Test")


public class teleop extends OpMode{

    /* Define hardware */
    hardwareprofile robot       = new hardwareprofile(); // use the class created to define a Pushbot's hardware

    // double          clawOffset  = 0.0 ;                  // Servo mid position
    // final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo

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

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;

        robot.topLeftMotor.setPower(left);
        robot.bottomLeftMotor.setPower(left);
        robot.topRightMotor.setPower(right);
        robot.bottomRightMotor.setPower(right);

        if (gamepad1.a) {
            robot.linearLift.setPower(0.8);
        } else if (gamepad1.y) {
            robot.linearLift.setPower(-1);
        }

        if (gamepad1.x) {
            robot.claw.setPower(0.5);
        } else if (gamepad1.b) {

            robot.claw.setPower(1);

        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}

