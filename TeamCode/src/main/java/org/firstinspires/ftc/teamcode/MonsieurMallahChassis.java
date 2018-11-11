package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name="Monsieur Mallah Chassis", group="MonsieurMallah")
public class MonsieurMallahChassis extends OpMode {

    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;     // period of each cycle
    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    //constants from encoder sample
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.7;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 4.7);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;


    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime dropTime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motorLeft;
    private DcMotor motorRight;
    // private DcMotor sweeper;
    // private DcMotor arm;
    // private DcMotor vacuum;
    // private DcMotor extender;


    // Hand servo.
    // private Servo servoHand;
    private Servo flagHolder;
    private double angleHand;

    // Hack stuff.
    private boolean useGyroscope = false;
    private boolean useMotors = true;
    private boolean useEncoders = true;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the gyoroscope.
        if (useGyroscope) {
            boolean boschInit = initGyroscope();
            telemetry.addData("Gyro", "bosch init=" + boschInit);
            telemetry.addData("Gyro", bosch.getCalibrationStatus().toString());
        }


        // Initialize the motors.
        if (useMotors) {
            motorLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorRight = hardwareMap.get(DcMotor.class, "motor1");
            // sweeper = hardwareMap.get(DcMotor.class, "motor2");
            //   arm = hardwareMap.get(DcMotor.class, "motor3");
            //  extender = hardwareMap.get(DcMotor.class, "motor5");
            // vacuum = hardwareMap.get(DcMotor.class, "motor6");

            // servoHand = hardwareMap.get(Servo.class, "servo0");
            flagHolder = hardwareMap.get(Servo.class, "servo1");
            angleHand = 0.75;
            flagHolder.setPosition(angleHand);

            //  angleHand = (MAX_POS - MIN_POS) / 2; // Start at halfway position

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorLeft.setDirection(DcMotor.Direction.REVERSE);
            motorRight.setDirection(DcMotor.Direction.FORWARD);

            if (useEncoders) {
                motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                // NOTE: only use RUN_IUSING_ENCODER when you want to run a certain amount of spins;
                // running it like this made the right motor run slower than the left one when using
                // a power that is less than max.
                /*motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); */

                motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }


        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        // Reset the game timer.
        runtime.reset();

        if (useGyroscope) {
            // Start the sampling thread.
            bosch.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        if (useGyroscope) {
            // Stop the sampling thread.
            bosch.stopAccelerationIntegration();
        }
    }

    public void dropFlag() {
        angleHand = 0;
        flagHolder.setPosition(angleHand);

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (useMotors) {
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            if (Math.abs(drive) < 0.1)
                drive = 0.0; // Prevent the output from saying "-0.0".
            double turn = gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            motorLeft.setPower(leftPower);
            motorRight.setPower(rightPower);
            telemetry.addData("Motors", "left:%.2f, right:%.2f, lpos:%d, rpos=%d",
                    leftPower, rightPower, motorLeft.getCurrentPosition(), motorRight.getCurrentPosition());
            telemetry.addData("Motors", "drive (%.2f), turn (%.2f)", drive, turn);

            telemetry.addData("DropTime", dropTime.seconds());


            if (gamepad1.x) {
                dropFlag();
                dropTime.reset();
            }

            if (dropTime.seconds() > 3.0) {
                angleHand = 0.75;
                flagHolder.setPosition(angleHand);
            }


            // Control the extender.

          /*  boolean extendOut = gamepad1.dpad_up;
            boolean extendIn = gamepad1.dpad_down;
            double extendPower = 0.0;
            if (extendOut) {
                extendPower = -1.0;
            } else if (extendIn) {
                extendPower = 1.0;
            }
            extender.setPower(extendPower);

            // Control the vacuum.
            boolean suckIn = gamepad1.right_bumper;
            boolean suckOut = gamepad1.left_bumper;
            double suckPower = 0.0;
            if (suckIn) {
                suckPower = -1.0;
            } else if (suckOut) {
                suckPower = 1.0;
            }
            vacuum.setPower(suckPower);

            //control the arm
            float pullUp = gamepad1.right_trigger;
            float pullDown = gamepad1.left_trigger;
            double pullPower = 0.0;
            if ((pullUp > 0.0) && (pullDown == 0.0)) {
                pullPower = -1.0;
            } else if ((pullDown > 0.0) && (pullUp == 0.0)) {
                pullPower = 1.0;
            }
            arm.setPower(pullPower);


            // control the hand
            if (gamepad1.dpad_up) {
                // Keep stepping up until we hit the max value.
                angleHand += INCREMENT;
                angleHand = Math.min(angleHand, MAX_POS);
            } else if (gamepad1.dpad_down) {
                // Keep stepping down until we hit the min value.
                angleHand -= INCREMENT;
                angleHand = Math.max(angleHand, MIN_POS);
            }
            servoHand.setPosition(angleHand);
*/

            // HACK: If driver presses the secret 'y' key, go forward 12 inches, to test encoder.
            if (useEncoders) {
                boolean encodertest = false;

                if (encodertest) {
                    double speed = 1;
                    encoderDrive(speed, 24, 24);
                }
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "time: " + runtime.toString());


            // telemetry.addData("Sweeper", "sweep (%.2f)", suckPower);
            telemetry.addData("Hand", " angle %5.2f", angleHand);
        }




        if (useGyroscope) {
            reportGyroscope();
        }
    }

    /******** GYROSCOPE STUFF **********/

    private boolean initGyroscope() {
        bosch = hardwareMap.get(BNO055IMU.class, "imu");
        telemetry.addData("Gyro", "class:" + bosch.getClass().getName());

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.loggingTag = "bosch";
        parameters.calibrationDataFile = "MonsieurMallahCalibration.json"; // see the calibration sample opmode
        boolean boschInit = bosch.initialize(parameters);
        return boschInit;
    }

    private void reportGyroscope() {
         /*AngularVelocity velocity = imu.getAngularVelocity(AngleUnit.DEGREES);
        String mesg = "x: " + velocity.xRotationRate + ", y:" + velocity.yRotationRate + ", z:" + velocity.zRotationRate;
        telemetry.addData("Gyro", mesg); */

        Orientation angles = bosch.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        telemetry.addData("Gyro", "angles: " + angles.firstAngle + "," + angles.secondAngle + "," + angles.thirdAngle);
        // Acceleration oa = bosch.getOverallAcceleration();
        //telemetry.addData("Gyro", "oa: " + oa);
        Acceleration la = bosch.getLinearAcceleration();
        double linear_force = Math.sqrt(la.xAccel * la.xAccel
                + la.yAccel * la.yAccel
                + la.zAccel * la.zAccel);
        telemetry.addData("Gyro", "la: " + la + "(" + linear_force + ")");
        Acceleration ga = bosch.getGravity();

        double gravity_force = Math.sqrt(ga.xAccel * ga.xAccel
                + ga.yAccel * ga.yAccel
                + ga.zAccel * ga.zAccel);
        telemetry.addData("Gyro", "ga: " + ga + "(" + gravity_force + ")");
        Position pos = bosch.getPosition();
        telemetry.addData("Gyro", "pos: " + pos);
        Velocity v = bosch.getVelocity();
        telemetry.addData("Gyro", "v: " + pos);
        Acceleration accel = bosch.getAcceleration();
        telemetry.addData("Gyro", "accel: " + accel);
        Temperature temp = bosch.getTemperature();
        telemetry.addData("Gyro", "temp: " + temp.temperature + " " + temp.unit.toString());
        MagneticFlux flux = bosch.getMagneticFieldStrength();
        telemetry.addData("Gyro", "flux: " + flux);
        BNO055IMU.SystemStatus status = bosch.getSystemStatus();
        telemetry.addData("Gyro", "status (" + bosch.isSystemCalibrated() + "): " + status);
        BNO055IMU.CalibrationStatus cstatus = bosch.getCalibrationStatus();
        telemetry.addData("Gyro", "cstatus: " + cstatus);
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive (double speed, double leftInches, double rightInches) {
        int newLeftTarget;
        int newRightTarget;

        // Determine new target position, and pass to motor controller
        newLeftTarget = motorRight.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newRightTarget = motorLeft.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        motorRight.setTargetPosition(newLeftTarget);
        motorLeft.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        motorRight.setPower(Math.abs(speed));
        motorLeft.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) &&
                (motorRight.isBusy() && motorLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    motorRight.getCurrentPosition(),
                    motorLeft.getCurrentPosition());
            telemetry.update();
           sleep(100);
        }

        // Stop all motion;
        motorRight.setPower(0);
        motorLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}