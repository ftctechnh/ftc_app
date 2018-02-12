package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Deprecated.PixyCam;
import org.firstinspires.ftc.teamcode.PIDTesting.PIDTestInterface;
import org.firstinspires.ftc.teamcode.PIDTesting.PIDTestMashupPID;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


// This is NOT an opmode

public class NullbotHardware {
    // Alliance
    public Alliance color;
    public StartingPosition startingPad;
    boolean isTestChassis;

    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public PIDTestInterface[] driveInterface;

    public DcMotorEx lift;
    public DcMotorEx zType;

    public DcMotor intakeLeft;
    public DcMotor intakeRight;
    public IntakeTarget intakeTarget;

    // Servos
    public Servo leftWhipSnake;
    public Servo leftBlockClaw;
    public Servo rightBlockClaw;
    public Servo relicClaw;
    public Servo relicClawFlipper;
    public Servo leftIntakeFlipper;
    public Servo rightIntakeFlipper;

    public AnalogInput frontUltrasonic;
    public AnalogInput leftUltrasonic;
    public AnalogInput rightUltrasonic;
    public ColorSensor colorSensor;

    // Sensors
    public ManualHeadingAdjustmentController headingAdjuster;

    // Adjustment gamepad
    private Gamepad gp2;
    private Gamepad gp1;

    // Telemetry
    public Telemetry tel;
    public long currentTick;

    File file; // Our logfile
    FileOutputStream fOut;
    OutputStreamWriter oW;
    LinearOpMode opMode;
    ScheduledExecutorService loggerThread;
    AccelerationIntegrator integrator;

    // Utility mechanisms
    public DcMotor[] motorArr;
    public final int hz = 100;

    public BNO055IMU imu;
    double gyroError;

    // State used for updating telemetry
    public Orientation angles;

    public boolean initialized = false;

    // Local vars
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public NullbotHardware(){}
    private void init() {

        currentTick = 0;
        isTestChassis = false;

        // Motor initialization
        frontLeft = hwMap.dcMotor.get("frontLeft");
        frontRight = hwMap.dcMotor.get("frontRight");
        backLeft = hwMap.dcMotor.get("backLeft");
        backRight = hwMap.dcMotor.get("backRight");

        imu = hwMap.get(BNO055IMU.class, "primaryIMU");

        frontUltrasonic = hwMap.get(AnalogInput.class, "frontUltrasonic");
        leftUltrasonic = hwMap.get(AnalogInput.class, "leftUltrasonic");
        rightUltrasonic = hwMap.get(AnalogInput.class, "rightUltrasonic");
        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");

        if (!isTestChassis) {
            lift = (DcMotorEx) hwMap.dcMotor.get("lift");
            zType = (DcMotorEx) hwMap.dcMotor.get("zType");

            intakeLeft = hwMap.dcMotor.get("intakeLeft");
            intakeRight = hwMap.dcMotor.get("intakeRight");

            leftWhipSnake = hwMap.servo.get("leftGemHitter");

            leftBlockClaw = hwMap.servo.get("leftClaw");
            rightBlockClaw = hwMap.servo.get("rightClaw");

            relicClawFlipper = hwMap.servo.get("relicClawFlipper");
            retractFlipper();

            relicClaw = hwMap.servo.get("relicClaw");

            leftIntakeFlipper = hwMap.servo.get("leftIntakeFlipper");
            rightIntakeFlipper = hwMap.servo.get("rightIntakeFlipper");

            //raiseWhipSnake();
            openBlockClaw();
            flattenRelicClaw();
            relicFipperPosition = 80;
            retractFlipper();
        }

        headingAdjuster = new ManualHeadingAdjustmentController(gp2);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // MotorArr utility setup
        motorArr = new DcMotor[4];

        motorArr[0] = frontLeft;
        motorArr[1] = frontRight;
        motorArr[2] = backLeft;
        motorArr[3] = backRight;

        driveInterface = new PIDTestInterface[4];

        for (int i = 0; i < motorArr.length; i++) {
            driveInterface[i] = new PIDTestMashupPID(motorArr[i], tel);
        }

        initialized = true;

    }

    public void init(HardwareMap ahwMap, LinearOpMode oM, Gamepad gp2, Gamepad gp1) {
        hwMap = ahwMap;
        tel = oM.telemetry;
        opMode = oM;
        this.gp2 = gp2;
        this.gp1 = gp1;
        init();
        calibrate();
    }

    private void calibrate() {
        // Calibration
        ElapsedTime calibrationTimer = new ElapsedTime();

        tel.log().add("Gyro Calibrating. Do Not Move!!");
        //gyro.calibrate();

        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (!isTestChassis) {
            setLiftMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            zType.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        integrator = new AccelerationIntegrator();

        parameters.accelerationIntegrationAlgorithm = integrator;

        imu.initialize(parameters);

        ///imu.startAccelerationIntegration(new Position(), new Velocity(), 100);

        updateReadings();

        //sleep(500);

        tel.log().add("Gyro calibration complete");
        //initialCompassHeading = Math.toRadians(compass.getDirection());
        gyroError = 0;
        tel.log().add("Compass heading locked");
        tel.update();
        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (!isTestChassis) {
            setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
            zType.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            setIntakeMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    /**
     * Takes an array of motor speeds, and then uses
     * built-in PID control to run motors at those speeds
     *
     * @param speeds   The speeds for the motors to run at, between -1.0 and 1.0.
     *                 Motor order is front left, front right, back left, back right.
     */
    public void setMotorSpeeds(double[] speeds) {
        for (int i = 0; i < motorArr.length; i++) {
            motorArr[i].setPower(clamp(speeds[i]));
        }
    }

    /**
     * Clamps inputted value between -1.0 and 1.0, for use with DcMotor.setPower
     *
     * @param val   The value to be clamped
     * @return      The clamped value as a double between -1.0 and 1.0
     * @see          DcMotor
     */
    public static double clamp(double val) {
        return Math.max(-1, Math.min(1, val));
    }

    /**
     * Intelligently waits a given number of milliseconds. If program is terminated, sleeping
     * will end immediately.
     *
     * @param milliseconds   The amount of time to wait
     */
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(Math.abs(milliseconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Most op modes are set to run a certain number of times per second. waitForTick() is
     * called at the end of an op mode, in order to perform clean-up steps. It sets msSinceLastMoved,
     * updates the gyroscope error with respect to the compass, and then sleeps for the requested
     * amount of time.
     *
     * @param periodMs   The amount of time to wait
     */
    public void waitForTick(long periodMs) {

        currentTick += 1;

        long remaining = periodMs - (long) period.milliseconds();

        // Sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();

        updateReadings();
    }

    public void updateReadings() {
        //integrator.update(imu.getLinearAcceleration());
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    /**
     * Writes the current logs out to a file
     *
     */

    final static String[] headings = new String[]{"Tick", "Heading",
            "Gamepad 1 Left Stick X", "Gamepad 1 Left Stick Y",
            "Gamepad 1 Right Stick X", "Gamepad 1 Right Stick Y",
            "Front Left Motor Power", "Front Left Motor Position",
            "Front Right Motor Power", "Front Right Motor Position",
            "Back Left Motor Power", "Back Left Motor Position",
            "Back Right Motor Power", "Back Right Motor Position",
            "Z-Type Motor Power", "Z-Type Motor Position", "Z-Type Motor Mode",
            "Lift Motor Power", "Lift Motor Position", "Lift Motor Mode",
            "Left Block Claw Position", "Right Block Claw Position",
            "Left Whip Snake Position", "Right Whip Snake Position",
            "Relic Claw Position", "Relic Claw Flipper Position"};

    public void openLogFile() {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + "/PixyCamLogs/"
                        );
        if (!path.exists()) {path.mkdirs();}

        String DTS = DateFormat.getDateTimeInstance().format(new Date());
        file = new File(path, "Pixycam-log-" + DTS + "-" + opMode.getClass().getSimpleName() + ".txt");

        try
        {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            oW = new OutputStreamWriter(fOut);
            for (String h : headings) {
                oW.append(h);
                oW.append(",");
            }
            oW.append("\n");
        }
        catch (IOException e)
        {}

        loggerThread = Executors.newSingleThreadScheduledExecutor();
        loggerThread.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    flushLogs();
                } catch (IOException e) {}
            }
        }, 0, 1, TimeUnit.SECONDS);

    }
    public void writeLogTick() {
        LogTick l = new LogTick(gp1,this);
        try {
            oW.append(l.toString());
            oW.append("\n");
        } catch (IOException e) {tel.log().add("Failed to write log tick!");}
    }

    public void writePixyCamTick(PixyCam.Block blue, PixyCam.Block red) {
        try {
            oW.append("Blue: " + blue.toString() + " ----- Red: " + red.toString());
            oW.append("\n");
        } catch (IOException e) {tel.log().add("Failed to write log tick!");}
    }

    public void flushLogs() throws IOException {
        oW.flush();
        fOut.flush();
    }
    public void closeLog() throws IOException {
        flushLogs();
        oW.close();
        fOut.close();
    }

    /**
     * Translates inputted angle between 0 and 2 pi radians
     *
     * @param a   The angle to be translated
     * @return    The translated angle from 0 to 2 pi
     */
    public static double normAngle(double a) {
        a = a % (Math.PI * 2);

        if (a < 0) {
            a += Math.PI * 2;
        }
        return a;
    }

    /**
     *
     * @param d1  The first angle
     * @param d2  The second angle
     * @return    The difference between the angles, between 0 and 2Pi
     */



    public static double getAngleDifference(double d1, double d2) {
        double diff = Math.abs(d2 - d1);
        diff = diff % (Math.PI * 2);
        return diff > Math.PI ? (Math.PI * 2) - diff : diff;
    }

    public double getSignedAngleDifference(double d1, double d2) {
        double zeroed = normAngle(d2 - d1);

        if (zeroed >= Math.PI) { // If it's in the pI to 2PI range
            return -((Math.PI * 2) - zeroed);
        } else {
            return zeroed;
        }

    }
    /**
     * @return    The error adjusted gyro position, between 0 and 2pi radians
     */
    public double getGyroHeading() {
        return normAngle(angles.firstAngle - gyroError);
    }

    public void raiseWhipSnake() {
        raiseLeftWhipSnake();
    }
    public void lowerWhipSnake() {
        lowerLeftWhipSnake();
    }
    public void lowerLeftWhipSnake() {leftWhipSnake.setPosition(0.45);}
    public void raiseLeftWhipSnake() {leftWhipSnake.setPosition(0.02);}
    public void almostRaiseWhipSnake() {leftWhipSnake.setPosition(0.09);}
    public void almostLowerWhipSnake() {leftWhipSnake.setPosition(0.44);}

    public void crunchBlockClaw() {
        leftBlockClaw.setPosition(200.0/255.0);
        rightBlockClaw.setPosition(100.0/255.0);
    }

    public void openBlockClaw() {
        leftBlockClaw.setPosition(0.26);
        rightBlockClaw.setPosition(0.85);
    }

    public void closeBlockClaw() {
        leftBlockClaw.setPosition(0.4);
        rightBlockClaw.setPosition(0.65);

    }

    public final double RELIC_CLAW_OPEN_POSITION = 0.29;
    public final double RELIC_CLAW_CLOSED_POSITION = 0;
    public boolean RELIC_CLAW_IS_OPEN = false;

    public void flattenRelicClaw() {relicClaw.setPosition(0.65); RELIC_CLAW_IS_OPEN = true;}

    public void openRelicClaw() {relicClaw.setPosition(RELIC_CLAW_OPEN_POSITION); RELIC_CLAW_IS_OPEN = true;}
    public void closeRelicClaw() {relicClaw.setPosition(RELIC_CLAW_CLOSED_POSITION); RELIC_CLAW_IS_OPEN = false;}

    public void toggleRelicClaw() {
        if (RELIC_CLAW_IS_OPEN) {
            closeRelicClaw();
        } else {
            openRelicClaw();
        }
    }

    /*public double relicFipperPosition = 80;
    public final double RELIC_CLAW_FLIPPER_EXTENDED_POSITION = 83.0/255.0;
    public final double RELIC_CLAW_FLIPPER_RETRACTED_POSITION = 74.0/255.0;*/

    public double relicFipperPosition = 0.33;
    public final double RELIC_CLAW_FLIPPER_EXTENDED_POSITION = 0.79;
    public final double RELIC_CLAW_FLIPPER_RETRACTED_POSITION = 0.12;

    public void extendFlipper() {
        relicClawFlipper.setPosition(RELIC_CLAW_FLIPPER_EXTENDED_POSITION);
        relicFipperPosition = RELIC_CLAW_FLIPPER_EXTENDED_POSITION;
    }

    public void retractFlipper() {
        relicClawFlipper.setPosition(RELIC_CLAW_FLIPPER_RETRACTED_POSITION);
        relicFipperPosition = RELIC_CLAW_FLIPPER_RETRACTED_POSITION;
    }

    public void toggleRelicClawFlipper() {
        if (relicFipperPosition == RELIC_CLAW_FLIPPER_EXTENDED_POSITION) {
            retractFlipper();
        } else {
            extendFlipper();
        }
    }

    public void updateFlipperPos() {
        relicClawFlipper.setPosition(relicFipperPosition);
    }

    public enum IntakeTarget {
        RAISED, LOWERED
    }

    public void lowerIntake() {
        leftIntakeFlipper.setPosition(0.7);
        rightIntakeFlipper.setPosition(0.0);
        intakeTarget = IntakeTarget.LOWERED;
    }

    public void raiseIntake() {
        leftIntakeFlipper.setPosition(0.7 - (1.0/3.0) - (37.0/270.0));
        rightIntakeFlipper.setPosition((1.0/3.0) + (27.0/270.0));
        intakeTarget = IntakeTarget.RAISED;
    }

    public void setLiftMode(DcMotor.RunMode m) {
        lift.setMode(m);
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motorArr) {
            if (motor.getMode() != mode) {
                motor.setMode(mode);
            }
        }
    }

    public void setMotorMode(DcMotor m, DcMotor.RunMode mode) {
        if (m.getMode() != mode) {
            m.setMode(mode);
        }
    }

    public void setIntakeMode(DcMotor.RunMode m) {
        intakeLeft.setMode(m);
        intakeRight.setMode(m);
    }

    public void setIntakeSpeed(double s) {
        intakeLeft.setPower(s);
        intakeRight.setPower(s);
    }

    public double[] getDrivePowersFromAngle(double angle) {
        double[] unscaledPowers = new double[4];
        unscaledPowers[0] = Math.sin(angle + Math.PI / 4);
        unscaledPowers[1] = Math.cos(angle + Math.PI / 4);
        unscaledPowers[2] = unscaledPowers[1];
        unscaledPowers[3] = unscaledPowers[0];
        return unscaledPowers;
    }

}

