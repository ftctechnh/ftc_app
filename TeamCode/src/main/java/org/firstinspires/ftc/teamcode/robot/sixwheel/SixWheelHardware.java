package org.firstinspires.ftc.teamcode.robot.sixwheel;


import android.os.Build;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.LoadTimer;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class SixWheelHardware {

    public Telemetry telemetry;

    private Telemetry.Item[] telEncoders;
    private Telemetry.Item[] telAnalog;
    private Telemetry.Item telDigital;
    private Telemetry.Item telLoopTime;
    private Telemetry.Item telHertz;
    private long lastTelemetryUpdate;

    public LynxModule chassisLynxHub;
    public ExpansionHubEx chassisHub;

    public DcMotorEx driveLeft;
    public DcMotorEx driveRight;
    public DcMotorEx PTOLeft;
    public DcMotorEx PTORight;

    public List<DcMotorEx> chassisMotors;
    public List<DcMotorEx> driveMotors;
    public List<DcMotorEx> PTOMotors;
    public List<DcMotorEx> leftChassisMotors;
    public List<DcMotorEx> rightChassisMotors;


    public SixWheelHardware(LinearOpMode opMode) {
        LoadTimer loadTime = new LoadTimer();
        RevExtensions2.init();

        driveLeft = opMode.hardwareMap.get(DcMotorEx.class, "frontLeft");
        driveRight = opMode.hardwareMap.get(DcMotorEx.class, "frontRight");
        PTOLeft = opMode.hardwareMap.get(DcMotorEx.class, "backLeft");
        PTORight = opMode.hardwareMap.get(DcMotorEx.class, "backRight");

        chassisLynxHub = opMode.hardwareMap.get(LynxModule.class, "frontHub");
        chassisHub = opMode.hardwareMap.get(ExpansionHubEx.class, "frontHub");

        // Reverse right hand motors
        driveRight.setDirection(DcMotor.Direction.REVERSE);
        PTORight.setDirection(DcMotor.Direction.REVERSE);

        // Set up fast access linked lists
        chassisMotors = Arrays.asList(driveLeft, driveRight, PTOLeft, PTORight);
        driveMotors = Arrays.asList(driveLeft, driveRight);
        PTOMotors = Arrays.asList(PTOLeft, PTORight);
        leftChassisMotors = Arrays.asList(driveLeft, PTOLeft);
        rightChassisMotors = Arrays.asList(driveRight, PTORight);

        // Perform calibration
        LoadTimer calTime = new LoadTimer();
        calTime.stop();

        // Set up telemetry
        this.telemetry = opMode.telemetry;
        initTelemetry();
        logBootTelemetry(opMode.hardwareMap, loadTime, calTime);
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
        Date buildDate = new Date(Build.TIME);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd at HH:mm:ss");
        String javaVersion = System.getProperty("java.version");
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
                " Servos; " + digital.size() + analog.size() + i2c.size() + " Sensors");

        lT.stop();

        // Load information
        log.add("Total time " + lT.millis() + " ms; Calibrate time " + cT.millis() + " ms");
        telemetry.update();
        lastTelemetryUpdate = System.nanoTime();
    }

    public void initBulkReadTelemetry() {
        Telemetry.Line encoderLine = telemetry.addLine();
        telEncoders = new Telemetry.Item[4];
        for (int i = 0; i < 4; i++) {
            telEncoders[i] = encoderLine.addData("E" + i, -1);
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

        // Adjust encoders and analog inputs
        for (int i = 0; i < 4; i++) {
            telEncoders[i].setValue(data.getMotorCurrentPosition(i));
            telAnalog[i].setValue(data.getAnalogInputValue(i));
        }

        // Adjust digital inputs
        String digitals = "";
        for (int i = 0; i < 8; i++) {
            digitals += (data.getDigitalInputState(i) ? 1 : 0) + " ";
        }
        telDigital.setValue(digitals);

        // Adjust elapsed time
        int elapsed = (int) ((System.nanoTime() - lastTelemetryUpdate) / 1000000);
        telLoopTime.setValue(elapsed);
        telHertz.setValue(1000 / elapsed);

        // Finalize telemetry update
        telemetry.update();
        lastTelemetryUpdate = System.nanoTime();
        return data;
    }
}
