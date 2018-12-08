package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Iterator;
import java.util.Set;

@TeleOp(name="Monsieur Mallah Motor OpMode", group="User")
public class MonsieurMallahMotorTest extends OpMode {
    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motor0;
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;

    // Hack stuff.
    private boolean runMotors = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the Bosch IMU, the internal gyroscope/temperature/magnetometer
        bosch = hardwareMap.get(BNO055IMU.class, "imu");
        //telemetry.addData("Gyro", "class:" + bosch.getClass().getName());
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.loggingTag = "bosch";
        parameters.calibrationDataFile = "MonsieurMallahCalibration.json"; // see the calibration sample opmode
        boolean boschInit = bosch.initialize(parameters);
        telemetry.addData("Gyro", "bosch init=" + boschInit);

        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();



        // Initialize the motors.
        if (runMotors) {
            motor0 = hardwareMap.get(DcMotor.class, "motor0");
            motor1 = hardwareMap.get(DcMotor.class, "motor1");
            motor2 = hardwareMap.get(DcMotor.class, "motor2");
            motor3 = hardwareMap.get(DcMotor.class, "motor3");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motor1.setDirection(DcMotor.Direction.REVERSE);
            motor3.setDirection(DcMotor.Direction.REVERSE);
        }

        // Tell the driver that initialization is complete.
        telemetry.addData("Gyro", bosch.getCalibrationStatus().toString());
        telemetry.addData("Status", "Initialized");
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
        runtime.reset();

        // Start the sampling thread.
        bosch.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (runMotors) {
            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            //double drive = -gamepad1.left_stick_y;
            //double turn  =  gamepad1.right_stick_x;
            double drive = 1.0;
            double turn = 1.0;
            //leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            leftPower = 0.5;
            rightPower = -0.5;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            motor0.setPower(leftPower);
            motor1.setPower(rightPower);
            motor2.setPower(leftPower);
            motor3.setPower(rightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        }

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

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        bosch.stopAccelerationIntegration();
    }
}
