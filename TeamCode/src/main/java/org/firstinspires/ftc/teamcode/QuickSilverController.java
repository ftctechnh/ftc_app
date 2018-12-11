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

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    // arm motors
    private DcMotor extender;
    private DcMotor tacVac;
    private DcMotor shoulder;
    private DcMotor lifter;

    private int extenderStartPostion = 0;

    // servos in end scooper
    private Servo servoLeft;
    private Servo servoRight;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useArm = true;
    private boolean useLifter = true;

    //Movement State
    int armState;
    int extenderTarget;
    int shoulderTarget;
    int lifterState;
    int lifterExtenderTarget;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Init Movement state
        armState = 0;
        extenderTarget = 0;
        shoulderTarget = 0;

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

        // Init the arm.
        if (useArm) {
            shoulder = hardwareMap.get(DcMotor.class, "motor4");
            tacVac = hardwareMap.get(DcMotor.class, "motor7");
          
            extender = hardwareMap.get(DcMotor.class, "motor5");
            extender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (useLifter) {
            lifter = hardwareMap.get(DcMotor.class, "motor6");
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
            float pullUp = gamepad1.right_stick_y;
            float pullOut = -gamepad1.right_stick_y;
            double pullPower = 0.0;
            if ((pullUp > 0.0) && (pullOut == 0.0)) {
                pullPower = -1.0;
            } else if ((pullOut > 0.0) && (pullUp == 0.0)) {
                pullPower = 1.0;
            }
            shoulder.setPower(pullPower);

            // Control the extender: MANUAL CONTROL
            boolean extendOut = gamepad1.y;
            boolean extendIn = gamepad1.a;
            double extendManualPower = 0.0;
            if (extendOut) {
                extendManualPower = -0.5;
            }
            if (extendIn) {
                extendManualPower = 0.5;
            }
            if (extendManualPower != 0.0 || armState == 0) {
                extender.setPower(extendManualPower);
                armState = 0;
            }

            boolean extendAllIn = gamepad1.x;
            boolean extendAllOut = gamepad1.b;
            if (extendAllOut) {
                startArmMoving(extenderStartPostion + 4500,0);
            }
            else if (extendAllIn) {
                startArmMoving(0,0);
            }

            boolean extendCalibrate = gamepad1.dpad_right;
            if (extendCalibrate) {
                extenderStartPostion = extender.getCurrentPosition();
            }

            // Control the tacVac.
            float suckIn = gamepad1.right_trigger;
            float suckOut = gamepad1.left_trigger;
            double suckPower = 0.0;
            if ((suckOut > 0.0) && (suckIn == 0.0)) {
                suckPower = -1.0;
            } else if ((suckOut > 0.0) && (suckIn == 0.0)) {
                suckPower = 1.0;
            }
            tacVac.setPower(suckPower);
        }
      
       if(armState == 1 && !extender.isBusy()){
            armState = 0;
            extenderTarget = 0;
            telemetry.addLine("Position Moved!");
            extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            extender.setPower(0);

            /*armState = 2;
            shoulder.setTargetPosition(shoulderTarget);
            shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            shoulder.setPower(0.75); */
        }

        /*if(armState == 2 && !shoulder.isBusy()){
            armState = 0;
            telemetry.addLine("Position Moved!");
        } */

        telemetry.addData("Shoulder", "curr: %7d", shoulder.getCurrentPosition());
        telemetry.addData("Extender", "start: %d, curr: %d, target: %d, armState: %d",
                extenderStartPostion, extender.getCurrentPosition(), extenderTarget, armState);
        telemetry.addData("Lifter", "curr: %d",
                lifter.getCurrentPosition());
    }

    protected void startArmMoving(int eTarget, int sTarget){
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


    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}