package org.firstinspires.ftc.teamcode;
/* *
 * Created by ftcrobotics on 11/19/17.
 * Concept by Howard
 * First Coding by Jeffrey and Alexis
 **/
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@SuppressWarnings("WeakerAccess")
@TeleOp(name = "KP Driver Cmd Mecanum", group = "k9Bot")

public class KPDriverCmdMecanum extends LinearOpMode {
    /* Declare OpMode members. */
    private RobotKP robot = new RobotKP();
    final Drive2 myDrive = new Drive2();
    //hardwarePushBotKP robot   = new hardwarePushBotKP();   // Use a Pushbot's hardware
    Gamepad g1 = new Gamepad();
    Gamepad g2 = new Gamepad();

    @Override

    public void runOpMode() {

    /*
     * Initialize the drive system variables.
     * The init() method of the hardware class does all the work here
     */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        long currentTime = System.currentTimeMillis();
        long lastSensor = currentTime;
        long lastEncoderRead = currentTime + 5;
        long lastServo = currentTime + 10;
        long lastNav = currentTime + 15;
        long lastMotor = currentTime + 20;
        long lastController = currentTime + 7;
        long lastTelemetry = currentTime + 17;

        // variables for controller inputs.
        g1 = gamepad1;
        g2 = gamepad2;

        int g2Acounts = 0;
        int g2DUcounts = 0;
        BotMotors mPwr = new BotMotors();

        double leftClampCmd = Clamps.LEFT_UNCLAMPED;
        double rightClampCmd = Clamps.RIGHT_UNCLAMPED;

        float leftDriveCmd = 0;
        float rightDriveCmd = 0;
        float leftRearCmd = 0;
        float rightRearCmd = 0;
        float riserCmd = 0;

        float turtleScalar = 1;  //Initially full power
        float turtleSpeed = 4;  // Divider

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();

        //A Timing System By Katherine, Jeffrey, and Alexis

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
    /* ******************************************************************************************
     *****************************              CODE            *********************************
     *                           Everything below here  \\ press START                          *
     ********************************************************************************************/

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            currentTime = System.currentTimeMillis();
            //Loop For Timing System
            /* ***************************************************
             *                SENSORS
             *        INPUTS: Raw Sensor Values
             *       OUTPUTS: parameters containing sensor values*
             ****************************************************/
            if (currentTime - lastSensor > RobotKP.MINOR_FRAME) {
                lastSensor = currentTime;

                // no sensors at this time.  If we add some, change this comment.
            }

            /* ***************************************************
             *                ENCODERS                          *
             ****************************************************/
            if (currentTime - lastEncoderRead > RobotKP.MINOR_FRAME) {
                lastEncoderRead = currentTime;
                // We want to READ the Encoders here  Not currently using data, invalid
                //    ONLY set the motors in motion in ONE place.
                // NO ENCODERS AT THIS TIME
                //  rightMotorPos = robot.rightDrive.getCurrentPosition()
                //lefMotorPos   = robot.leftDrive.getCurrentPosition()
                //riserMotorPos = robot.pulleyDrive.getCurrentPosition()

            }
            /* **************************************************
             *                Controller INPUT                  *
             *  INPUTS: raw controller values                   *
             ****************************************************/
            if (currentTime - lastController > RobotKP.MINOR_FRAME) {
                lastController = currentTime;
                g1 = gamepad1;
                g2 = gamepad2;

                // do a little debounce on gamepad1.a so we don't drop the block accidentally
                // 6 counts at 30 milliseconds will delay things by 180 ms, and that allows
                // a flaky controller or a jittery operator. Splitting out the sensor "reads"
                // from the rest of the logic let's us do this here, rather than muddling up
                // the main logic of a more abstract, more complicated piece of code.
                g2Acounts =
                   Range.clip((gamepad2.a) ? g2Acounts + 1 : g2Acounts - 1, 0, 12);
                g2.a = (g2Acounts >= 6);

                g2DUcounts =
                   Range.clip((gamepad2.dpad_up) ? g2DUcounts + 1 : g2DUcounts - 1, 0, 12);
                g2.dpad_up = (g2DUcounts >= 6);
            }

            /* **************************************************
             *                NAV
             *      Inputs:  Gamepad positions
             *               Sensor Values (as needed)
             *      Outputs: Servo and Motor position commands
             *                         motor
             ****************************************************/
            if (currentTime - lastNav > RobotKP.MINOR_FRAME) {
                lastNav = currentTime;

                // init drive min and max to default values.  We'll reset them to other numbers
                // if conditions demand it.

                //Control riser motor
                riserCmd = 0;
                if (g2.right_trigger > 0) {    //UP
                    riserCmd = Drive2.MOTORMAX;
                }
                if (g2.left_trigger > 0) {    //DOWN
                    riserCmd = Drive2.MOTORMIN;
                }

                //Control Servos for Clamp
                if (g2.b) {
                    leftClampCmd = Clamps.LEFT_CLAMPED;
                    rightClampCmd = Clamps.RIGHT_CLAMPED;
                }
                if (g2.a) {
                    leftClampCmd = Clamps.LEFT_UNCLAMPED;
                    rightClampCmd = Clamps.RIGHT_UNCLAMPED;
                }
                if (g2.x) {  // Bias to left
                    leftClampCmd = leftClampCmd - Clamps.SERVO_TWEAK;
                    rightClampCmd = rightClampCmd + Clamps.SERVO_TWEAK;
                }
                if (g2.y) {  // Bias to right
                    leftClampCmd = leftClampCmd + Clamps.SERVO_TWEAK;
                    rightClampCmd = rightClampCmd - Clamps.SERVO_TWEAK;
                }
                if (g2.dpad_up) { // Mostly Clamped
                    leftClampCmd = Clamps.LEFT_MOSTLY_CLAMPED;
                    rightClampCmd = Clamps.RIGHT_MOSTLY_CLAMPED;
                }
                if (g2.dpad_down) { // Extra Clamped
                    leftClampCmd = Clamps.LEFT_TIGHT_CLAMPED;
                    rightClampCmd = Clamps.RIGHT_TIGHT_CLAMPED;
                }

                //Turtle Mode toggle
                if ((g1.right_bumper) || (g1.right_trigger > 0)) {  //Turtle
                    turtleScalar = turtleSpeed;
                }
                if ((g1.left_bumper) || (g1.left_trigger > 0)) {  // Exit Turtle
                    turtleScalar = Drive2.MOTORMAX;
                }

                // mapping inputs to motor commands - cube them to desensitize them around
                // the 0,0 point.
                g1.left_stick_y = (float) Math.pow((double) g1.left_stick_y, (double) 3);
                g1.left_stick_y = g1.left_stick_y / turtleScalar;

                g1.right_stick_y = (float) Math.pow((double) g1.left_stick_y, (double) 3);
                g1.right_stick_y = g1.right_stick_y / turtleScalar;

                if (g1.x)
                {
                    mPwr.leftFront = g1.left_stick_y;
                    mPwr.rightFront = g1.right_stick_y;
                    mPwr.leftRear = mPwr.leftFront;
                    mPwr.rightRear = mPwr.rightFront;
                }
                else
                {
                    mPwr = myDrive.driveIt(g1);
                }
                // The ONLY place we set the motor power variables. Set them here, and
                // we will never have to worry about which set is clobbering the other.

                // motor commands: Clipped & clamped.
                leftDriveCmd = Range.clip(mPwr.leftFront, Drive2.MOTORMIN, Drive2.MOTORMAX);
                rightDriveCmd = Range.clip(mPwr.rightFront, Drive2.MOTORMIN, Drive2.MOTORMAX);
                leftRearCmd = Range.clip(mPwr.leftRear, Drive2.MOTORMIN, Drive2.MOTORMAX);
                rightRearCmd = Range.clip(mPwr.rightRear,Drive2.MOTORMIN, Drive2.MOTORMAX);
                riserCmd = Range.clip(riserCmd, Drive2.MOTORMIN, Drive2.MOTORMAX);
            }                    // END NAVIGATION

            /* **************************************************
             *                SERVO OUTPUT
             *                Inputs: leftClamp position command
             *                        rightClamp position command *
             *                Outputs: Physical write to servo interface.
             ****************************************************/
            if (currentTime - lastServo > RobotKP.MINOR_FRAME) {
                lastServo = currentTime;

                // Move both servos to new position.
                robot.leftClamp.setPosition(leftClampCmd);
                robot.rightClamp.setPosition(rightClampCmd);
            }

            /* ***************************************************
             *                MOTOR OUTPUT
             *       Inputs:  Motor power commands
             *       Outputs: Physical interface to the motors
             ****************************************************/
            if (currentTime - lastMotor > RobotKP.MINOR_FRAME) {
                lastMotor = currentTime;
                // Yes, we'll set the power each time, even if it's zero.
                // this way we don't accidentally leave it somewhere.  Just simpler this way.
                robot.leftDrive.setPower(-1 * rightDriveCmd);
                robot.rightDrive.setPower(-1 * leftDriveCmd);
                robot.leftRear.setPower(-1*rightRearCmd);
                robot.rightRear.setPower(-1*leftRearCmd);
                robot.liftMotor.setPower(riserCmd);
            }

            /* ***************************************************
             *                TELEMETRY
             *       Inputs:  telemetry structure
             *       Outputs: command telemetry output to phone
             ****************************************************/
            if (currentTime - lastTelemetry > RobotKP.TELEMETRY_PERIOD) {
                lastTelemetry = currentTime;
                telemetry.update();
            }
            telemetry.addData("Left Motor Power     ", leftDriveCmd);
            telemetry.addData("Right Motor Power    ", rightDriveCmd);
            telemetry.addData("Riser Motor Power    ", riserCmd);
            telemetry.addData("Left Clamp Command   ", leftClampCmd);
            telemetry.addData("Right Clamp Command  ", rightClampCmd);
            telemetry.update();
        }

        //SAFE EXIT OF RUN OPMODE, stop motors, leave servos????
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftRear.setPower(0);
        robot.rightRear.setPower(0);
        robot.liftMotor.setPower(0);
    }
}
