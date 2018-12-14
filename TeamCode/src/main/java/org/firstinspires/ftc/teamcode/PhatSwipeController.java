package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name="PhatSwipe Master", group="AAA")
public class PhatSwipeController extends OpMode {


    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime dropTime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor lifter;


    //Bulldozer contraption in front of robot
    private Servo bull;
    private Servo dozer;


    //teammarkerservo
    private Servo flagHolder;
    private double angleHand;

    // Hack stuff.
    private boolean useGyroscope = false;
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useLifter = true;
    private boolean useBulldozer =true;
    private boolean useDropper =true;
    private boolean reverseDrive = true;

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

            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");


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
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
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

        if(useBulldozer){
            bull = hardwareMap.get(Servo.class, "servo3");
            dozer = hardwareMap.get(Servo.class, "servo2");
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

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (gamepad1.start) {
            reverseDrive = !reverseDrive;
            sleep(250);
        }

        if (useMotors) {
            // Control the wheel motors.
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double driveNormal = gamepad1.left_stick_y;
            double driveStrafe = gamepad1.left_stick_x;
            if (Math.abs(driveNormal) < 0.1)
                driveNormal = 0.0; // Prevent the output from saying "-0.0".
            if (Math.abs(driveStrafe) < 0.1)
                driveStrafe = 0.0; // Prevent the output from saying "-0.0".

            if (reverseDrive) {
                driveNormal = -driveNormal;
                driveStrafe = -driveStrafe;
            }

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

        if(gamepad1.left_bumper && gamepad1.right_bumper && gamepad1.right_stick_button && gamepad1.left_stick_button) {
            swiggle(3);
        }

        if(useLifter){
            //control lifter (temporary)
            boolean liftUp = gamepad1.y;
            boolean lowerDown = gamepad1.a;
            double liftPower = 0.0;
            if (liftUp) {
                liftPower = 0.8;
            } else if (lowerDown) {
                liftPower = -0.8;
            }
            lifter.setPower(liftPower);
            telemetry.addData("Lifter", "curr: %d", lifter.getCurrentPosition());
        }

        if(useBulldozer) {
            //control Bulldozer
            float bullUp = gamepad1.right_trigger;
            bull.setPosition(1.0 - bullUp);
            dozer.setPosition(bullUp);

            telemetry.addData("BullDoze", "set: %f, curr: %f, %f", bullUp, bull.getPosition(), dozer.getPosition());
        }

        if(useDropper) {
            flagHolder.setPosition(gamepad1.left_trigger);
        }

        if (useGyroscope) {
            reportGyroscope();
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
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
    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    protected void swiggle(long secondsToSwiggle) {
        for(int lit = 0; lit < secondsToSwiggle; lit++) {
            turnLeft(500);
            turnRight(500);
        }
    }
    protected void turnLeft(int numberOfMillis) {
        float power = 0.3f;
        motorBackLeft.setPower(-power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(-power);
        motorFrontRight.setPower(power);

        sleep(numberOfMillis);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);

    }

    protected void turnRight(int numberOfMillis) {
        float power = 0.3f;
        motorBackLeft.setPower(power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(power);
        motorFrontRight.setPower(-power);

        sleep(numberOfMillis);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
    }

}