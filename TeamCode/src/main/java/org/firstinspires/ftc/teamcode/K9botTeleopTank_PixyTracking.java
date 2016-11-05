package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;

/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="K9bot: Teleop with PIXY", group="K9bot")
//@Disabled
public class K9botTeleopTank_PixyTracking extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareK9bot robot           = new HardwareK9bot();              // Use a K9'shardware
    double          xPosition     = robot.ARM_HOME;                   // Servo safe position
    double          clawPosition    = robot.CLAW_HOME;                  // Servo safe position
    final double    CLAW_SPEED      = 0.01 ;                            // sets rate to move servo
    final double    ARM_SPEED       = 0.01 ;                            // sets rate to move servo
    double          power = .5;
    double          pixyCenter = 2.2;
    double          deadband = .1;
    double          pixyMin = 0.22;
    double          pixyMax = 3.8;



    @Override
    public void runOpMode() {
        double left;
        double right;

        //OpticalDistanceSensor odsSensor;  // Hardware Device Object
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        //odsSensor = hardwareMap.opticalDistanceSensor.get("sensor_ods");
        robot.init(hardwareMap);

        AnalogInput pixySensor;
        pixySensor = hardwareMap.analogInput.get("pixy");
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.a) {
                power = .2;
            }
            if (gamepad1.y & gamepad1.left_bumper & gamepad1.left_stick_button) {
                power = 1;
            }
            if (gamepad1.x & gamepad1.left_bumper & gamepad1.left_stick_button) {
                power = .75;
            }
            if (gamepad1.b) {
                power = .375;
            }
            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            left = -gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;
            robot.leftMotor.setPower(left*power);
            robot.rightMotor.setPower(right*power);

            // Use gamepad Y & A raise and lower the arm
            if (gamepad1.dpad_down)
                xPosition += ARM_SPEED;
            else if (gamepad1.dpad_up)
                xPosition -= ARM_SPEED;

            // Use gamepad X & B to open and close the claw
            if (gamepad1.dpad_left)
                clawPosition += CLAW_SPEED;
            else if (gamepad1.dpad_right)
                clawPosition -= CLAW_SPEED;

            while (gamepad1.left_bumper & gamepad1.right_bumper) {
                robot.leftMotor.setPower(.2);
                robot.rightMotor.setPower(.2);
            }

            double voltreading = (float) pixySensor.getVoltage();


            //check to see if reading is out of range
            if (voltreading > pixyMax || voltreading < pixyMin) {
                telemetry.addData("Out of Range", "");
            } else if(voltreading>pixyCenter) {
                xPosition = xPosition - .01;
            } else if (voltreading<pixyCenter) {
                xPosition = xPosition + .01;

            }

            // Move both servos to new position.
            xPosition  = Range.clip(xPosition, robot.ARM_MIN_RANGE, robot.ARM_MAX_RANGE);
            robot.arm.setPosition(xPosition);
            clawPosition = Range.clip(clawPosition, robot.CLAW_MIN_RANGE, robot.CLAW_MAX_RANGE);
            robot.claw.setPosition(clawPosition);

            // Send telemetry message to signify robot running;
            telemetry.addData("arm",   "%.2f", xPosition);
            telemetry.addData("Volts",   "%.2f", voltreading);
            telemetry.addData("claw",  "%.2f", clawPosition);
            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.addData("power", "%.2f", power);
            //telemetry.addData("ods", "%.2f", odsSensor.getLightDetected());
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
