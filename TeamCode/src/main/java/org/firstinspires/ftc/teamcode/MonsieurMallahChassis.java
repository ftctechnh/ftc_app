package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

@TeleOp(name="Monsieur Mallah Chassis", group="MonsieurMallah")
public class MonsieurMallahChassis extends OpMode {

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor sweeper;
    private DcMotor arm;

    // Hand servo.
    private Servo servoHand;
    private double angleHand;

    // Hack stuff.
    private boolean useGyroscope = false;
    private boolean useMotors = true;

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
            sweeper = hardwareMap.get(DcMotor.class, "motor2");
            arm = hardwareMap.get(DcMotor.class, "motor3");

            servoHand = hardwareMap.get(Servo.class, "servo0");
            angleHand = (MAX_POS - MIN_POS) / 2; // Start at halfway position

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorLeft.setDirection(DcMotor.Direction.REVERSE);
            motorRight.setDirection(DcMotor.Direction.FORWARD);
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
        if (useMotors) {
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            motorLeft.setPower(leftPower);
            motorRight.setPower(rightPower);

            // Control the sweeper.
            boolean suckIn =  gamepad1.right_bumper;
            boolean suckOut =  gamepad1.left_bumper;
            double suckPower = 0.0;
            if (suckIn) {
                suckPower = -1.0;
            } else if(suckOut){
                suckPower = 1.0;
            }
            sweeper.setPower(suckPower);

            //control the arm
            float pullUp = gamepad1.right_trigger;
            float pullDown = gamepad1.left_trigger;
            double pullPower = 0.0;
            if ((pullUp > 0.0) && (pullDown == 0.0)) {
                pullPower = -1.0;
            } else if ((pullDown > 0.0) && (pullUp == 0.0)){
                pullPower = 1.0;
            }
            arm.setPower(pullPower);

            // control the hand
            if (gamepad1.dpad_up) {
                // Keep stepping up until we hit the max value.
                angleHand += INCREMENT ;
                angleHand = Math.min(angleHand, MAX_POS);
            }
            else if (gamepad1.dpad_down){
                // Keep stepping down until we hit the min value.
                angleHand -= INCREMENT ;
                angleHand = Math.max(angleHand, MIN_POS);
            }
            servoHand.setPosition(angleHand);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Motors", "drive (%.2f), turn (%.2f)", drive, turn);
            telemetry.addData("Sweeper", "sweep (%.2f)", suckPower);
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
        double linear_force  = Math.sqrt(la.xAccel*la.xAccel
                + la.yAccel*la.yAccel
                + la.zAccel*la.zAccel);
        telemetry.addData("Gyro", "la: " + la + "(" + linear_force + ")");
        Acceleration ga = bosch.getGravity();
        double gravity_force  = Math.sqrt(ga.xAccel*ga.xAccel
                + ga.yAccel*ga.yAccel
                + ga.zAccel*ga.zAccel);
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
}