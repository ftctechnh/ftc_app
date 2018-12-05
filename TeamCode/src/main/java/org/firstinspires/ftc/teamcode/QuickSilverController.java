package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


/**
 *
 */
@TeleOp(name="QuickSilverController", group="MonsieurMallah")
public class QuickSilverController extends OpMode {

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
    //private DcMotor tacVac;
    private DcMotor shoulder;

    private Servo rightGate;
    private Servo leftGate;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useArm = true;

    //Movement State
    // int armState;
    //int extenderTarget;
    //int shoulderTarget;

    /**
     * Code to run ONCE when the driver hits INITh6
     */
    @Override
    public void init() {

        //Init Movement state
        // armState = 0;
        //extenderTarget = 0;
        //shoulderTarget = 0;

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);
            motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
            motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

            if (useEncoders) {
                motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

        if (useArm) {
            shoulder = hardwareMap.get(DcMotor.class, "motor4");
            extender = hardwareMap.get(DcMotor.class, "motor5");
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
            double driveNormal = -gamepad1.left_stick_y;
            double driveStrafe = -gamepad1.left_stick_x;
            if (Math.abs(driveNormal) < 0.1)
                driveNormal = 0.0; // Prevent the output from saying "-0.0".
            if (Math.abs(driveStrafe) < 0.1)
                driveStrafe = 0.0; // Prevent the output from saying "-0.0".

            double turn = gamepad1.right_stick_x;

            double leftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.8, 0.8);
            double rightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.8, 0.8);
            double leftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.8, 0.8);
            double rightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.8, 0.8);

            double halfLeftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.25, 0.25);
            double halfRightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.25, 0.25);
            double halfLeftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.25, 0.25);
            double halfRightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.25, 0.25);

            boolean halfSpeed = gamepad1.left_bumper && gamepad1.right_bumper;


            if (halfSpeed) {
                motorBackLeft.setPower(halfLeftBackPower);
                motorBackRight.setPower(halfRightBackPower);
                motorFrontLeft.setPower(halfLeftFrontPower);
                motorFrontRight.setPower(halfRightFrontPower);
            } else {
                motorBackLeft.setPower(leftBackPower);
                motorBackRight.setPower(rightBackPower);
                motorFrontLeft.setPower(leftFrontPower);
                motorFrontRight.setPower(rightFrontPower);
            }
        }

        if (useArm) {
            boolean pullUp = gamepad1.dpad_down;
            boolean pullOut = gamepad1.dpad_up;
            double pullPower = 0.0;
            if (pullUp) {
                pullPower = 1.0;
            } else if (pullOut) {
                pullPower = -1.0;
            }
            shoulder.setPower(pullPower);

            // Control the extender.
            boolean extendOut = gamepad1.y;
            boolean extendIn = gamepad1.a;
            double extendPower = 0.0;
            if (extendOut) {
                extendPower = 1.0;
            } else if (extendIn) {
                extendPower = -1.0;
            }
            extender.setPower(extendPower);

/*
         //Setting certain postions for arm & extender
            boolean jewelPickUp = gamepad1.a;
            boolean moving = gamepad1.b;
            boolean deposit = gamepad1.x;
            boolean landerAttach = gamepad1.y;
            if (jewelPickUp){
                startArmMoving(-3127, 16714);
            }else if (moving){
                startArmMoving(-2691, 9103);
            }else if (deposit){
                startArmMoving(3185, -8695);
            }else if (landerAttach){
                startArmMoving(1587, -7522);
            }
        }

        //Time to Move!

        if(armState == 1 && !extender.isBusy()){
            armState = 2;
            shoulder.setTargetPosition(shoulderTarget);
            shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            shoulder.setPower(0.75);
        }
        if(armState == 2 && !shoulder.isBusy()){
            armState = 0;
            telemetry.addLine("Position Moved!");
        }


    }

    protected void startArmMoving(int eTarget, int sTarget){
        // save the g;obal movement state

        // Get the current position.
        int extenderStart = extender.getCurrentPosition();
        telemetry.addData("StartingPos.", "Starting %7d", extenderStart);

        // Turn On RUN_TO_POSITION
        extender.setTargetPosition(eTarget);
        extender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extender.setPower(0.75);

        //Set global Movement State
        armState = 1;
        extenderTarget = eTarget;
        shoulderTarget = sTarget;

    }

            protected void sleep ( long milliseconds){
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                }
            }

*/
        }

    }
}