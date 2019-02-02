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
@TeleOp(name="QuickSilver TeleOp", group="AAA")
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

    // servos in end scooper
    private Servo servoLeft;
    private Servo servoRight;

    //teammarkerservo
    private Servo flagHolder;
    private double angleHand;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useArm = true; // HACK
    private boolean useLifter = true; // HACL
    private boolean useDropper = true;

    //Movement State
    private int armState;
    private int extenderTarget;
    private int lifterState;
    private int lifterExtenderTarget;
    private int extenderStartPostion = 0;
    private int lifterStartPosition = 0;
    private int shoulderTarget;
    private int shoulderStartPosition = 0;

    //Drive State
     private boolean switchFront = false;


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

            // Init Movement state
            armState = 0;
            extenderTarget = 0;
            shoulderTarget = 0;
        }

        if (useLifter) {
            lifter = hardwareMap.get(DcMotor.class, "motor6");
            lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }


        if (useDropper) {
            flagHolder = hardwareMap.get(Servo.class, "servo1");
            flagHolder.setPosition(0.5);
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
            // Switch the directions for driving!
            if (gamepad1.back){
                switchFront = !switchFront;
                sleep(500);
            }

            /*
            // Control the wheel motors.
            // POV Mode uses left stick to go forw ard, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double driveNormal = -gamepad1.left_stick_y;
            double driveStrafe = -gamepad1.left_stick_x;
            if (Math.abs(driveNormal) < 0.1)
                driveNormal = 0.0; // Prevent the output from saying "-0.0".
            if (Math.abs(driveStrafe) < 0.1)
                driveStrafe = 0.0; // Prevent the output from saying "-0.0".

            double turn = gamepad1.right_stick_x;

            if (switchFront){
                driveNormal = -driveNormal;
                driveStrafe = -driveStrafe;
            }

            double leftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.8, 0.8);
            double rightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.8, 0.8);
            double leftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.8, 0.8);
            double rightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.8, 0.8);

            double halfLeftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.25, 0.25);
            double halfRightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.25, 0.25);
            double halfLeftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.25, 0.25);
            double halfRightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.25, 0.25);
            */

            // Control the wheel motors.
            // POV Mode uses left stick to go forw ard, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double driveNormal = -gamepad1.left_stick_y;
            if (Math.abs(driveNormal) < 0.1)
                driveNormal = 0.0; // Prevent the output from saying "-0.0".

            double driveStrafe = -gamepad1.right_stick_x;
            if (Math.abs(driveStrafe) < 0.1)
                driveStrafe = 0.0; // Prevent the output from saying "-0.0".

            double turn = gamepad1.left_stick_x;

            if (switchFront){
                driveNormal = -driveNormal;
                driveStrafe = -driveStrafe;
            }
            telemetry.addData("Motor", "n:%02.1f, s:%02.1f, t:%02.1f", driveNormal, driveStrafe, turn);

            float cap = 1.0f;
            float backScale = 0.5f;
            double leftBackPower = Range.clip(driveNormal + turn + (driveStrafe * backScale), -cap, cap);
            double rightBackPower = Range.clip(driveNormal - turn - (driveStrafe * backScale), -cap, cap);
            double leftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -cap, cap);
            double rightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -cap, cap);

            double halfLeftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.25, 0.25);
            double halfRightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.25, 0.25);
            double halfLeftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.25, 0.25);
            double halfRightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.25, 0.25);

            boolean halfSpeed = gamepad1.left_stick_button || gamepad1.left_bumper;
            if (halfSpeed) {
                motorBackLeft.setPower(halfLeftBackPower);
                motorBackRight.setPower(halfRightBackPower);
                motorFrontLeft.setPower(halfLeftFrontPower);
                motorFrontRight.setPower(halfRightFrontPower);
                telemetry.addData("Motor", "half lb:%02.1f, rb:%02.1f, lf:%02.1f, rf:%02.1f", halfLeftBackPower, halfRightBackPower, halfLeftFrontPower, halfRightFrontPower);
            } else {
                motorBackLeft.setPower(leftBackPower);
                motorBackRight.setPower(rightBackPower);
                motorFrontLeft.setPower(leftFrontPower);
                motorFrontRight.setPower(rightFrontPower);
                telemetry.addData("Motor", "full left-back:%02.1f, %d", leftBackPower,  motorBackLeft.getCurrentPosition());
                telemetry.addData("Motor", "full rght-back:%02.1f, %d", rightBackPower, motorBackRight.getCurrentPosition());
                telemetry.addData("Motor", "full left-frnt:%02.1f, %d", leftFrontPower, motorFrontLeft.getCurrentPosition());
                telemetry.addData("Motor", "full rght-frnt:%02.1f, %d", rightFrontPower, motorFrontRight.getCurrentPosition());
            }
        }

        if (useDropper) {
            if (gamepad1.b) {
                flagHolder.setPosition(1.0);
            } else {
                flagHolder.setPosition(-1.0);
            }
        }

        if (useArm) {
            // shoulder MANUAL CONTROL
            boolean pullUpOne = gamepad1.dpad_up;
            boolean pullOutOne = gamepad1.dpad_down;
            boolean pullUpTwo = gamepad2.dpad_up;
            boolean pullOutTwo = gamepad2.dpad_down;
            double pullPower = 0.0;
            if (pullUpOne||pullUpTwo) {
                pullPower = -0.8;
            } else if (pullOutOne || pullOutTwo) {
                pullPower = 0.8;
            }else if(pullUpOne && pullOutTwo) {
                pullPower = 0.8;
            }else if(pullOutOne && pullUpTwo){
                pullPower = -0.8;
            }
            shoulder.setPower(pullPower);

            if (pullPower != 0.0 || armState == 0) {
                // if anyone uses manual reset presets and turn everything off
                if (armState != 0) {
                    armState = 0;
                    extenderTarget = 0;
                    extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    extender.setPower(0);
                    shoulderTarget = 0;
                    shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                shoulder.setPower(pullPower);
            }

            // Control the extender: MANUAL CONTROL
            boolean extendOut = gamepad1.a;
            boolean extendIn = gamepad1.y;
            double extendManualPower = 0.0;
            if (extendOut) {
                extendManualPower = -0.5;
            }
            if (extendIn) {
                extendManualPower = 0.5;
            }
            if (extendManualPower != 0.0 || armState == 0) {
                if (armState != 0) {
                    // if anyone uses manual reset presets and turn everything off
                    armState = 0;
                    extenderTarget = 0;
                    extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    shoulderTarget = 0;
                    shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    shoulder.setPower(0);
                }
                extender.setPower(extendManualPower);
            }

            // extender: presets
            /*boolean depositeState = gamepad2.y;
            boolean homeState = gamepad2.a;
            if (depositeState) {
                //All the way out + all the way up or depositeState
                startArmMoving(extenderStartPostion + 4000,shoulderStartPosition + 4500);
            }
            else if (homeState) {
                startArmMoving(extenderStartPostion,shoulderStartPosition);
            }

            boolean extendCalibrate = gamepad2.dpad_right && gamepad2.left_bumper;
            if (extendCalibrate) {
                extenderStartPostion = extender.getCurrentPosition();
            }

            boolean shoulderCalibrate = gamepad2.dpad_left && gamepad2.left_bumper;
            if (shoulderCalibrate) {
                shoulderStartPosition = shoulder.getCurrentPosition();
            }

            //Are we half way through the preset mode?
            if (armState == 1 && !extender.isBusy()){
               // turn off extender
                extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                extender.setPower(0);

                //Preset part 2: turn on shoulder!
                shoulder.setTargetPosition(shoulderTarget);
                shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                shoulder.setPower(1.0);

                //Set arm state to shoulder moving state
                armState = 2;
                telemetry.addLine("Position Moved!");
            }

            // Are we finishing a preset move?
            if(armState == 2 && !shoulder.isBusy()){
               //Preset mode is done! time to turn off all the arm motors
                shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                shoulder.setPower(0);

                //Now set the targets to 0 and reset the arm state
                extenderTarget = 0;
                shoulderTarget = 0;
                armState = 0;
            }*/

            // Control the tacVac.
            float suckOut = gamepad1.left_trigger;
            float suckIn = gamepad1.right_trigger;
            if (suckOut != 0){
                tacVac.setPower(-suckOut);
            } else if (suckIn != 0){
                tacVac.setPower(suckIn);
            } else {
                tacVac.setPower(0);
            }

            telemetry.addData("Shoulder", "start: %d, curr: %d, target: %d", shoulderStartPosition, shoulder.getCurrentPosition(), shoulderTarget);
            telemetry.addData("Extender", "start: %d, curr: %d, target: %d, armState: %d", extenderStartPostion, extender.getCurrentPosition(), extenderTarget, armState);
        }

        if (useLifter) {
            // lyfter MANUAL CONTROL
            boolean extendOut = gamepad1.right_bumper;
            boolean extendIn = gamepad1.left_bumper;
            double lifterManualPower = 0.0;
            if (extendOut) {
                lifterManualPower = -1.0;
            }
            if (extendIn) {
                lifterManualPower = 1.0;
            }
            if (lifterManualPower != 0.0 || lifterState == 0) {
                // we get here if we ar ealready in manual control, or if we are in the process of moving to a preset,
                // and someone hit a manual control, so we have to cancel the preset.
                lifterState = 0;
                lifterExtenderTarget = 0;
                lifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                lifter.setPower(lifterManualPower);
            }

            boolean liftAllIn = gamepad2.dpad_left;
            boolean liftAllOut = gamepad2.dpad_right;
            if (liftAllOut) {
                startLifterMoving(lifterStartPosition + 4500);
            } else if (liftAllIn) {
                startLifterMoving(lifterStartPosition);
            }

            boolean lyfterCalibrate = gamepad1.left_bumper && gamepad1.dpad_up;
            if (lyfterCalibrate) {
                lifterStartPosition = lifter.getCurrentPosition();
                sleep(500);
            }

            if (lifterState == 1 && !lifter.isBusy()) {
                lifterState = 0;
                lifterExtenderTarget = 0;
                lifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                lifter.setPower(0);
                telemetry.addLine("Position Moved!");
            }

            telemetry.addData("Lifter", "start: %d, curr: %d, target: %d, liftState: %d",
                    lifterStartPosition, lifter.getCurrentPosition(), lifterExtenderTarget, lifterState);
        }

        /*if(armState == 2 && !shoulder.isBusy()){
            armState = 0;
            telemetry.addLine("Position Moved!");
        } */


    }


    protected void startLifterMoving(int lTarget){
        // Get the current position.
        int lifterStart = lifter.getCurrentPosition();
        telemetry.addData("LifterStartingPos.", "Starting %7d", lifterStart);

        // Turn On RUN_TO_POSITION
        lifter.setTargetPosition(lTarget);
        lifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lifter.setPower(0.75);

        //Set global Movement State
        lifterState = 1;
        lifterExtenderTarget = lTarget;
    }

    protected void startArmMoving(int eTarget, int sTarget){
        // Get the current position.
        int extenderStart = extender.getCurrentPosition();
        int shoulderStart = shoulder.getCurrentPosition();
        telemetry.addData("ExtenderStartingPos.", "Starting %7d", extenderStart);
        telemetry.addData("ShoulderStartingPos.", "Starting %7d", shoulderStart);


        // Turn On RUN_TO_POSITION
        extender.setTargetPosition(eTarget);
        extender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extender.setPower(0.75);

        //shoulder.setTargetPosition(sTarget);
        //shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       // shoulder.setPower(0.75);

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