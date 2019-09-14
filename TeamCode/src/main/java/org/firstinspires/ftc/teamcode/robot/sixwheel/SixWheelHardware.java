package org.firstinspires.ftc.teamcode.robot.sixwheel;


import android.os.Build;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.BuildConfig;
import org.firstinspires.ftc.teamcode.autonomous.odometry.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.autonomous.odometry.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.common.AxesSigns;
import org.firstinspires.ftc.teamcode.common.BNO055IMUUtil;
import org.firstinspires.ftc.teamcode.common.LoadTimer;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SixWheelHardware {

    public Telemetry telemetry;

    private Telemetry.Item[] threeWheelOdometry;
    private Telemetry.Item[] twoWheelOdometry;
    private Telemetry.Item[] twoWheelRelOdometry;
    private Telemetry.Item[] telEncoders;
    private Telemetry.Item[] telEncodersDist;
    private Telemetry.Item[] telAnalog;

    private Telemetry.Item telDigital;
    private Telemetry.Item telLoopTime;
    private Telemetry.Item telHertz;
    private long lastTelemetryUpdate;

    private LynxModule chassisLynxHub;
    private ExpansionHubEx chassisHub;
    private BNO055IMU imu;

    private StandardTrackingWheelLocalizer threeWheelLocalizer;
    private TwoWheelTrackingLocalizer twoWheelLocalizer;

    public DcMotorEx driveLeft;
    public DcMotorEx driveRight;
    public DcMotorEx PTOLeft;
    public DcMotorEx PTORight;

    public List<DcMotorEx> chassisMotors;
    public List<DcMotorEx> driveMotors;
    public List<DcMotorEx> PTOMotors;
    public List<DcMotorEx> leftChassisMotors;
    public List<DcMotorEx> rightChassisMotors;

    // Inches
    public static double TRACK_WIDTH = 16.5;
    public static double WHEEL_DIAMETER = 4;

    public SixWheelHardware(OpMode opMode) {
        LoadTimer loadTime = new LoadTimer();
        RevExtensions2.init();

        driveLeft = opMode.hardwareMap.get(DcMotorEx.class, "driveLeft");
        driveRight = opMode.hardwareMap.get(DcMotorEx.class, "driveRight");
        PTOLeft = opMode.hardwareMap.get(DcMotorEx.class, "PTOLeft");
        PTORight = opMode.hardwareMap.get(DcMotorEx.class, "PTORight");

        chassisLynxHub = opMode.hardwareMap.get(LynxModule.class, "chassisHub");
        chassisHub = opMode.hardwareMap.get(ExpansionHubEx.class, "chassisHub");

        // Reverse left hand motors
        driveLeft.setDirection(DcMotor.Direction.REVERSE);
        PTOLeft.setDirection(DcMotor.Direction.REVERSE);

        // Set up fast access linked lists
        chassisMotors = Arrays.asList(driveLeft, driveRight, PTOLeft, PTORight);
        driveMotors = Arrays.asList(driveLeft, driveRight);
        PTOMotors = Arrays.asList(PTOLeft, PTORight);
        leftChassisMotors = Arrays.asList(driveLeft, PTOLeft);
        rightChassisMotors = Arrays.asList(driveRight, PTORight);

        // Perform calibration
        LoadTimer calTime = new LoadTimer();
        for (DcMotorEx m : chassisMotors) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        calTime.stop();

        // Set up localization with motor names the wheels are connected to
        threeWheelLocalizer = new StandardTrackingWheelLocalizer(0, 1, 2);
        twoWheelLocalizer = new TwoWheelTrackingLocalizer(0, 1);

        // Set up telemetry
        this.telemetry = opMode.telemetry;
        initTelemetry();
        logBootTelemetry(opMode.hardwareMap, loadTime, calTime);
    }

    public SixWheelHardware() {} // Used for debugging

    public Pose pose() {
        return twoWheelLocalizer.pose();
    }

    private void initTelemetry() {
        telemetry.setMsTransmissionInterval(50); // Update at 20 Hz
        telemetry.setAutoClear(false); // Force not to autoclear
        telemetry.setItemSeparator("; ");
        telemetry.setCaptionValueSeparator(" ");
    }

    private void logBootTelemetry(HardwareMap hardwareMap, LoadTimer lT, LoadTimer cT) {
        Telemetry.Log log = telemetry.log();
        log.clear();
        log.setCapacity(6);

        log.add("-- 8802 RC by Gavin Uberti --");

        // Build information
        Date buildDate = new Date(BuildConfig.TIMESTAMP);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        String javaVersion = System.getProperty("java.runtime.version");
        log.add("Built " + dateFormat.format(buildDate) + " with Java " + javaVersion);

        // Device information
        log.add(Build.MANUFACTURER + " " + Build.MODEL + " running Android " + Build.VERSION.SDK_INT);

        // Chassis information
        String firmware = chassisHub.getFirmwareVersion();
        int rev = chassisHub.getHardwareRevision();
        log.add(this.getClass().getSimpleName() + " with hub " + rev + " " + firmware);

        // Robot information
        List<LynxModule> revHubs = hardwareMap.getAll(LynxModule.class);
        List<DcMotor> motors = hardwareMap.getAll(DcMotor.class);
        List<Servo> servos = hardwareMap.getAll(Servo.class);
        List<DigitalChannel> digital = hardwareMap.getAll(DigitalChannel.class);
        List<AnalogInput> analog = hardwareMap.getAll(AnalogInput.class);
        List<I2cDevice> i2c = hardwareMap.getAll(I2cDevice.class);
        log.add(revHubs.size() + " Hubs; " + motors.size() + " Motors; " + servos.size() +
                " Servos; " + (digital.size() + analog.size() + i2c.size()) + " Sensors");

        lT.stop();

        // Load information
        log.add("Total time " + lT.millis() + " ms; Calibrate time " + cT.millis() + " ms");
        telemetry.update();
        lastTelemetryUpdate = System.nanoTime();
    }

    public void initBNO055IMU(HardwareMap hardwareMap) {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        BNO055IMUUtil.remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN);
    }

    public void initBulkReadTelemetry() {
        Telemetry.Line threeWheelOdometryLine = telemetry.addLine();
        threeWheelOdometry = new Telemetry.Item[3];
        threeWheelOdometry[0] = threeWheelOdometryLine.addData("X", "%.1f", "-1");
        threeWheelOdometry[1] = threeWheelOdometryLine.addData("Y", "%.1f", "-1");
        threeWheelOdometry[2] = threeWheelOdometryLine.addData("θ", "%.3f", "-1");

        Telemetry.Line twoWheelOdometryLine = telemetry.addLine();
        twoWheelOdometry = new Telemetry.Item[3];
        twoWheelOdometry[0] = twoWheelOdometryLine.addData("RX", "%.1f", "-1");
        twoWheelOdometry[1] = twoWheelOdometryLine.addData("RY", "%.1f", "-1");
        twoWheelOdometry[2] = twoWheelOdometryLine.addData("Rθ", "%.3f", "-1");
        twoWheelOdometry[3] = twoWheelOdometryLine.addData("IMU", "%.3f", "-1");

        Telemetry.Line twoWheelRelOdometryLine = telemetry.addLine();
        twoWheelRelOdometry = new Telemetry.Item[3];
        twoWheelRelOdometry[0] = twoWheelRelOdometryLine.addData("RX", "%.1f", "-1");
        twoWheelRelOdometry[1] = twoWheelRelOdometryLine.addData("RY", "%.1f", "-1");
        twoWheelRelOdometry[2] = twoWheelRelOdometryLine.addData("Rθ", "%.3f", "-1");

        Telemetry.Line encoderLine = telemetry.addLine();
        telEncoders = new Telemetry.Item[4];
        for (int i = 0; i < 4; i++) {
            telEncoders[i] = encoderLine.addData("E" + i, -1);
        }

        Telemetry.Line encoderLineDist = telemetry.addLine();
        telEncodersDist = new Telemetry.Item[4];
        for (int i = 0; i < 4; i++) {
            telEncodersDist[i] = encoderLineDist.addData("ED" + i, -1);
        }

        Telemetry.Line analogLine = telemetry.addLine();
        telAnalog = new Telemetry.Item[4];
        for (int i = 0; i < 4; i++) {
            telAnalog[i] = analogLine.addData("A" + i, -1);
        }

        telDigital = telemetry.addLine().addData("DIGITALS", "0 0 0 0 0 0 0 0");

        Telemetry.Line timingLine = telemetry.addLine("LOOP ");
        telHertz = timingLine.addData("Hertz", -1);
        telLoopTime = timingLine.addData("Millis", -1);

    }

    public RevBulkData performBulkRead() {
        RevBulkData data = chassisHub.getBulkInputData();

        // Update localizer
        threeWheelLocalizer.update(data);
        double heading = imu.getAngularOrientation().firstAngle;
        twoWheelLocalizer.update(data, heading);

        // Adjust telemetry localizer info
        threeWheelOdometry[0].setValue(String.format("%.2f", threeWheelLocalizer.x()));
        threeWheelOdometry[1].setValue(String.format("%.2f", threeWheelLocalizer.y()));
        threeWheelOdometry[2].setValue(String.format("%.3f", Math.toDegrees(threeWheelLocalizer.h())));

        twoWheelOdometry[0].setValue(String.format("%.2f", twoWheelLocalizer.x()));
        twoWheelOdometry[1].setValue(String.format("%.2f", twoWheelLocalizer.y()));
        twoWheelOdometry[2].setValue(String.format("%.3f", Math.toDegrees(twoWheelLocalizer.h())));
        twoWheelOdometry[3].setValue(String.format("%.3f", Math.toDegrees(heading)));

        twoWheelRelOdometry[0].setValue(String.format("%.2f", twoWheelLocalizer.relativeRobotMovement.x));
        twoWheelRelOdometry[1].setValue(String.format("%.2f", twoWheelLocalizer.relativeRobotMovement.y));
        twoWheelRelOdometry[2].setValue(String.format("%.3f", Math.toDegrees(twoWheelLocalizer.relativeRobotMovement.heading)));

        // Adjust encoders and analog inputs
        for (int i = 0; i < 4; i++) {
            telEncoders[i].setValue(data.getMotorCurrentPosition(i));
            telEncodersDist[0].setValue(Math.round(100 * StandardTrackingWheelLocalizer.encoderTicksToInches(
                    data.getMotorCurrentPosition(0), StandardTrackingWheelLocalizer.LEFT_WHEEL_RADIUS)) / 100.0);
            telEncodersDist[1].setValue(Math.round(100 * StandardTrackingWheelLocalizer.encoderTicksToInches(
                    data.getMotorCurrentPosition(1), StandardTrackingWheelLocalizer.RIGHT_WHEEL_RADIUS)) / 100.0);
            telEncodersDist[2].setValue(Math.round(100 * StandardTrackingWheelLocalizer.encoderTicksToInches(
                    data.getMotorCurrentPosition(2), StandardTrackingWheelLocalizer.LAT_WHEEL_RADIUS)) / 100.0);
            telAnalog[i].setValue(data.getAnalogInputValue(i));
        }

        // Adjust digital inputs
        StringBuilder digitals = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            digitals.append(data.getDigitalInputState(i) ? 1 : 0).append(" ");
        }
        telDigital.setValue(digitals.toString());

        // Adjust elapsed time
        double elapsed = ((System.nanoTime() - lastTelemetryUpdate) / 1000000.0);
        telLoopTime.setValue("%.1f", elapsed);
        telHertz.setValue("%.1f", 1000 / elapsed);

        // Finalize telemetry update
        telemetry.update();
        lastTelemetryUpdate = System.nanoTime();
        return data;
    }

    public void setWheelPowers(SixWheelPowers powers) {
        this.PTOLeft.setPower(powers.left);
        this.driveLeft.setPower(powers.left);
        this.driveRight.setPower(powers.right);
        this.PTORight.setPower(powers.right);
    }
}
