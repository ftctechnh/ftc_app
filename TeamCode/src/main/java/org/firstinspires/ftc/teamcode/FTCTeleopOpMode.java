package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Controller OpMode", group="Iterative OpMode")
public class FTCTeleopOpMode extends OpMode{

    /* Declare OpMode members. */
    HardwarePushbot robot = new HardwarePushbot();
    double clawOffset  = 0.0 ; // Servo mid position
    final double CLAW_SPEED = 0.02 ; // sets rate to move servo

    /* Declare instances. */
    private DcMotor motorLauncher = null;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        this.motorLauncher = hardwareMap.dcMotor.get(FTCInterface.LAUNCHER_MOTOR);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Teleop initialized.");
        updateTelemetry(telemetry);
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

    private boolean lever1Executing = false;

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left = 0;
        double right = 0;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if(gamepad1.y || gamepad1.a || gamepad1.b || gamepad1.x) { // if one of the four main buttons are pushed
            if(this.lever1Executing) { // if executing is true
                this.motorLauncher.setPower(FTCInterface.LAUNCHER_MOTOR_POWER); // Launch!
            } else {
                this.lever1Executing = true; // signify the lever is now bring pressed.
                telemetry.addData("Status", "Launcher activated.");
                updateTelemetry(telemetry);
            }
        } else {
            if(this.lever1Executing) {
                this.lever1Executing = false;
                this.motorLauncher.setPower(0); // stop launcher motor
                telemetry.addData("Status", "Launcher deactivated.");
                updateTelemetry(telemetry);
            }
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status", "Teleop stopped.");
        updateTelemetry(telemetry);
    }

}
