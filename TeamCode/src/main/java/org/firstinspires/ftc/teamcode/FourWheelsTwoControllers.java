package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


/**
 */
@TeleOp(name="2 Contollers", group="User")
public class FourWheelsTwoControllers extends OpMode {

    public static final String TAG = "Vuforia Navigation Sample";

    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    //constants from encoder sample
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.9375;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor extender;
    private DcMotor tacVac;
    private DcMotor shoulder;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useNavigation = true;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");
            extender = hardwareMap.get(DcMotor.class, "motor6");
            tacVac = hardwareMap.get(DcMotor.class, "motor4");
            shoulder = hardwareMap.get(DcMotor.class, "motor7");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
            motorBackRight.setDirection(DcMotor.Direction.FORWARD);
            motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
            motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

            if (useEncoders) {
                motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }


    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        if (useNavigation) {
            /** Stop tracking the data sets we care about. */
        }
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (useMotors) {
            // Control the wheel motors.
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = gamepad1.left_stick_y;
            if (Math.abs(drive) < 0.1)
                drive = 0.0; // Prevent the output from saying "-0.0".

            double turn = -gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -0.8, 0.8);
            double rightPower = Range.clip(drive - turn, -0.8, 0.8);
            double halfLeftPower = Range.clip(drive + turn, -0.45, 0.45);
            double halfRightPower = Range.clip(drive - turn, -0.45, 0.45);

            boolean halfSpeed = gamepad1.left_bumper && gamepad1.right_bumper;
            if (halfSpeed) {
                motorBackLeft.setPower(halfLeftPower);
                motorBackRight.setPower(halfRightPower);
                motorFrontLeft.setPower(halfLeftPower);
                motorFrontRight.setPower(halfRightPower);
            } else {
                motorBackLeft.setPower(leftPower);
                motorBackRight.setPower(rightPower);
                motorFrontLeft.setPower(leftPower);
                motorFrontRight.setPower(rightPower);
            }


            // Report motor telemetry: note that the left motor is mounted upside down compared to the first one,
            // so it counts in reverse. We multiply it by -1 to change the sign, and so both motors count the same way.

            boolean pullIn = gamepad2.dpad_down;
            boolean pushOut = gamepad2.dpad_up;
            double pullPower = 0.0;
            if (pullIn) {
                pullPower = -1.0;
            } else if (pushOut) {
                pullPower = 1.0;
            }
            extender.setPower(pullPower);

            // Control the shoulder arm.

            double shoulderPower = -gamepad2.left_stick_y;
            if (gamepad2.left_bumper && gamepad2.right_bumper) {
                shoulderPower = Range.clip(shoulderPower, -0.4, 0.4);
            } else {
                shoulderPower = Range.clip(shoulderPower, -0.8, 0.8);
            }
            shoulder.setPower(shoulderPower);

            // control the vacuum
            double suckPower = gamepad2.right_stick_y;

            if (gamepad2.left_bumper && gamepad2.right_bumper) {
                suckPower = Range.clip(suckPower, -0.5, 0.5);
                tacVac.setPower(suckPower);
            } else {
                tacVac.setPower(suckPower);

            }
        }
    }
}

