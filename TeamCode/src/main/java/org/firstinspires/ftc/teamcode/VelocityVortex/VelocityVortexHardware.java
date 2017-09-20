package org.firstinspires.ftc.teamcode.VelocityVortex;

import android.hardware.SensorManager;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;

/**
 * Created by spmce on 11/3/2016.
 */
public class VelocityVortexHardware extends OpMode {
    /**
     * Velocity Vortex Hardware Constructor
     */
    VelocityVortexHardware () {
        // Constructor Code
    }
    //------------Hardware Devices------------
    // ADD HARDWARE DEVICES HERE:
    // DcMotors
    protected DcMotor mFL; // Front Left Drive Motor
    protected DcMotor mFR; // Front Right Drive Motor
    protected DcMotor mBL; // Back Left Drive Motor
    protected DcMotor mBR; // Back Right Drive Motor
    protected DcMotor mSweeper; // Sweeper motor
    protected DcMotor mLauncher; // launcher motor

    // Servos
    protected Servo sLeftBeacon;
    protected Servo sRightBeacon;
    protected Servo sLoaderStopper;
    // Sensors
    protected TouchSensor touch;
    protected ColorSensor color1;
    protected ColorSensor color2;
    protected LightSensor light1;
    protected LightSensor light2;
    protected LightSensor light3;
    protected LightSensor light4;
    protected GyroSensor gyro;
    protected ModernRoboticsI2cRangeSensor range;
    protected OpticalDistanceSensor od;
    protected TouchSensorMultiplexer multi;
    protected SensorManager sm;

    //------------initial positions------------
    // ADD INITIAL POWER AND POSITIONS VARIABLES HERE:
    // DcMotors - Initial Power
    private double initLeftDrivePower = 0;
    private double initRightDrivePower = 0;
    private double initBackLeftPower = 0;
    private double initBackRightPower = 0;
    private double initSweeperPower = 0;
    private double initlauncherPower = 0;
    // Servos - Initial Positions
    protected double initLeftBeacon = 0.81;
    protected double initRightBeacon = 0.15;
    protected double initLoaderStopper = 0;
    protected double openLoaderStopper = 0.6;
    //------------loop positions------------
    // ADD LOOP POWER AND POSITION VARIABLES HERE:
    // DcMotors - Loop Power
    protected double leftDrivePower = 0;
    protected double rightDrivePower = 0;
    protected double backLeftPower = 0;
    protected double backRightPower = 0;
    protected double sweeperPower = 0;
    protected double launcherPower = 0;
    // Servos - Loop Positions
    protected double leftBeaconPosition;
    protected double rightBeaconPosition;
    protected double loaderStopperPosition;
    //------------Telemetry Warnings------------
    // Create message of warning if created
    protected String warningMessage = "";
    protected String motorWarningMessage = "";
    protected String driveWarningMessage = "";
    protected String servoWarningMessage = "";
    protected String sensorWarningMessage= "";


    DeviceInterfaceModule dim;
    //------------------------Init------------------------
    /**
     * Init
     */
    @Override
    public void init() {
        dim = hardwareMap.deviceInterfaceModule.get("DIM");
        dim.setLED(0,true);
        VelocityVortexTelemetry tele = new VelocityVortexTelemetry();
        tele.initTele();
        // Initialize Warnings Generated and ,Warning Messages
        Warning message = new Warning();
        message.initWarnings(); //Provide telemetry data to a class user
        //Hardware Map
        //Map hardware = new Map();
        // ADD HARDWARE MAP HERE;
        // DcMotors - Map
        /*mFL = hardware.map(mFL,initLeftDrivePower,"fl");
        mFR = hardware.map(mFR,initRightDrivePower,"fr",true); // "true" reverses motor direction
        mBL = hardware.map(mBL,initBackLeftPower,"bl");
        mBR = hardware.map(mBR,initBackRightPower,"br",true); // "true" reverses motor direction
        mSweeper = hardware.map(mSweeper,initSweeperPower,"swpr");*/
        // Servos - Map
        //sLeftBeacon = hardware.map(sLeftBeacon,initLeftBeacon);
        //sRightBeacon = hardware.map(sRightBeacon,initRightBeacon);
        // Sensors - Map

        //VelocityVortexMap hardware = new VelocityVortexMap();
        //hardware.map();
        mFL = hardwareMap.dcMotor.get("fl");
        mFR = hardwareMap.dcMotor.get("fr");
        mFR.setDirection(DcMotorSimple.Direction.REVERSE);
        mBL = hardwareMap.dcMotor.get("bl");
        mBR = hardwareMap.dcMotor.get("br");
        mBR.setDirection(DcMotorSimple.Direction.REVERSE);
        mSweeper = hardwareMap.dcMotor.get("swpr");
        mLauncher = hardwareMap.dcMotor.get("Launcher");
        sLeftBeacon = hardwareMap.servo.get("sLeftButt");
        sRightBeacon = hardwareMap.servo.get("sRightButt");
        sLoaderStopper = hardwareMap.servo.get("sls");
        sLeftBeacon.setPosition(initLeftBeacon);
        sRightBeacon.setPosition(initRightBeacon);
        sLoaderStopper.setPosition(initLoaderStopper);
        touch = hardwareMap.touchSensor.get("touch");
        color1 = hardwareMap.colorSensor.get("color1");
        color1.enableLed(false);
        //telemetry.addData("Color1 I2c address" ,color1.getI2cAddress());
        color2 = hardwareMap.colorSensor.get("color2");
        //color2.enableLed(false);
        //telemetry.addData("Color2 I2c address", color2.getI2cAddress());
        light1 = hardwareMap.lightSensor.get("light1");
        light1.enableLed(true);
        light2 = hardwareMap.lightSensor.get("light2");
        light2.enableLed(true);
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        od = hardwareMap.opticalDistanceSensor.get("od");
        od.enableLed(true);
        //range = hardwareMap.get("range");
        range = hardwareMap.get(ModernRoboticsI2cRangeSensor.class,"range");
        mFL.setPower(0);
        mFR.setPower(0);
        mBL.setPower(0);
        mBL.setPower(0);
        //tele.warningTele();
        telemetry.addData("gyro cal", gyro.isCalibrating());
        dim.setLED(1,true);
    }

    @Override
    public void init_loop() {
        super.init_loop();
        telemetry.addData("gyro cal", gyro.isCalibrating());
    }

    @Override
    public void start() {
        color1.setI2cAddress(I2cAddr.create8bit(0x6c));
        color1.enableLed(false);
        color2.setI2cAddress(I2cAddr.create8bit(0x3a));
        color2.enableLed(false);
    }

    //------------------------Loop------------------------
    /**
     * Loop
     */
    @Override
    public void loop() {

    }


    public String getDriveWarningMessage() {
        return driveWarningMessage;
    }

    public String getMotorWarningMessage() {
        return motorWarningMessage;
    }

    public String getSensorWarningMessage() {
        return sensorWarningMessage;
    }

    public String getServoWarningMessage() {
        return servoWarningMessage;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setmFL(DcMotor mFL) {
        this.mFL = mFL;
    }

    public void setmFR(DcMotor mFR) {
        this.mFR = mFR;
    }

    public void setmBL(DcMotor mBL) {
        this.mBL = mBL;
    }

    public void setmBR(DcMotor mBR) {
        this.mBR = mBR;
    }

    public void setmSweeper(DcMotor mSweeper) {
        this.mSweeper = mSweeper;
    }

    public void setsLeftBeacon(Servo sLeftBeacon) {
        this.sLeftBeacon = sLeftBeacon;
    }

    public void setsRightBeacon(Servo sRightBeacon) {
        this.sRightBeacon = sRightBeacon;
    }

    public void setInitLeftDrivePower(double initLeftDrivePower) {
        this.initLeftDrivePower = initLeftDrivePower;
    }

    public void setInitRightDrivePower(double initRightDrivePower) {
        this.initRightDrivePower = initRightDrivePower;
    }

    public void setInitBackLeftPower(double initBackLeftPower) {
        this.initBackLeftPower = initBackLeftPower;
    }

    public void setInitBackRightPower(double initBackRightPower) {
        this.initBackRightPower = initBackRightPower;
    }

    public void setInitSweeperPower(double initSweeperPower) {
        this.initSweeperPower = initSweeperPower;
    }

    public void setInitLeftBeacon(double initLeftBeacon) {
        this.initLeftBeacon = initLeftBeacon;
    }

    public void setInitRightBeacon(double initRightBeacon) {
        this.initRightBeacon = initRightBeacon;
    }

    public void setLeftDrivePower(double leftDrivePower) {
        this.leftDrivePower = leftDrivePower;
    }

    public void setRightDrivePower(double rightDrivePower) {
        this.rightDrivePower = rightDrivePower;
    }

    public void setBackLeftPower(double backLeftPower) {
        this.backLeftPower = backLeftPower;
    }

    public void setBackRightPower(double backRightPower) {
        this.backRightPower = backRightPower;
    }

    public void setSweeperPower(double sweeperPower) {
        this.sweeperPower = sweeperPower;
    }

    public void setLeftBeaconPosition(double leftBeaconPosition) {
        this.leftBeaconPosition = leftBeaconPosition;
    }

    public void setRightBeaconPosition(double rightBeaconPosition) {
        this.rightBeaconPosition = rightBeaconPosition;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public void setMotorWarningMessage(String motorWarningMessage) {
        this.motorWarningMessage = motorWarningMessage;
    }

    public void setDriveWarningMessage(String driveWarningMessage) {
        this.driveWarningMessage = driveWarningMessage;
    }

    public DcMotor getmBL() {
        return mBL;
    }

    public void setServoWarningMessage(String servoWarningMessage) {
        this.servoWarningMessage = servoWarningMessage;
    }

    public void setSensorWarningMessage(String sensorWarningMessage) {
        this.sensorWarningMessage = sensorWarningMessage;
    }

    public void setDrivePower (double fl,double fr, double bl,double br) {
        mFL.setPower(fl);
        mFR.setPower(fr);
        mBL.setPower(bl);
        mBR.setPower(br);
    }
    //------------------------------------Autonomous Methods-------------------------------------
    void allServosInitialPosition() {
        if (sRightBeacon != null)
            sRightBeacon.setPosition (initRightBeacon);
        if (sLeftBeacon != null)
            sLeftBeacon.setPosition(initLeftBeacon);
    }
    //------------ Set With Motor Wheel Encoders------------
    public void runUsingLeftDriveEncoder() {
        if (mFL != null)
            mFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runUsingRightDriveEncoder() {
        if (mFR != null)
            mFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runUsingBackLeftEncoder() {
        if (mBL != null)
            mBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runUsingBackRightEncoder() {
        if (mBR != null)
            mBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runUsingSweeperEncoder () {
        if (mSweeper != null)
            mSweeper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runUsingDriveEncoders () {
        runUsingLeftDriveEncoder();
        runUsingRightDriveEncoder();
        runUsingBackLeftEncoder();
        runUsingBackRightEncoder();
    }
    public void runUsingEncoders () {
        runUsingLeftDriveEncoder();
        runUsingRightDriveEncoder();
        runUsingBackLeftEncoder();
        runUsingBackRightEncoder();
        runUsingSweeperEncoder();
    }
    //------------Set Without Motor Wheel Encoders------------
    public void runWithoutLeftDriveEncoder () {
        if (mFL != null) {
            if (mFL.getMode () == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
                mFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void runWithoutRightDriveEncoder () {
        if (mFR != null) {
            if (mFR.getMode() == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
                mFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void runWithoutBackLeftEncoder () {
        if (mBL != null) {
            if (mBL.getMode() == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
                mBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void runWithoutBackRightEncoder () {
        if (mBR != null) {
            if (mBR.getMode() == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
                mBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void runWithoutSweeperEncoder () {
        if (mSweeper != null) {
            if (mSweeper.getMode () == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
                mSweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void runWithoutDriveEncoders () {
        runWithoutLeftDriveEncoder();
        runWithoutRightDriveEncoder();
        runWithoutBackLeftEncoder();
        runWithoutBackRightEncoder();
    }
    public void runWithoutMotorEncoders () {
        runWithoutLeftDriveEncoder();
        runWithoutRightDriveEncoder();
        runWithoutBackLeftEncoder();
        runWithoutBackRightEncoder();
        runWithoutSweeperEncoder();
    }
    //------------Reset Motor Wheel Encoders------------
    public void resetLeftDriveEncoder () {
        if (mFL != null)
            mFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void resetRightDriveEncoder () {
        if (mFR != null)
            mFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void resetBackLeftEncoder () {
        if (mBL != null)
            mBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void resetBackRightEncoder () {
        if (mBR != null)
            mBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void resetDriveEncoders () {
        resetLeftDriveEncoder();
        resetRightDriveEncoder();
        resetBackLeftEncoder();
        resetBackRightEncoder();
    }
    public void resetSweeperEncoder () {
        if (mSweeper != null)
            mSweeper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //------------Get Motor Wheel Encoder Count------------
    int getLeftEncoderCount () {
        if (mFL != null)
            return mFL.getCurrentPosition ();
        return 0;
    }
    int getRightEncoderCount () {
        if (mFR != null)
            return mFR.getCurrentPosition ();
        return 0;
    }
    int getBackLeftEncoderCount () {
        if (mBL != null)
            return mBL.getCurrentPosition ();
        return 0;
    }
    int getBackRightEncoderCount () {
        if (mBR != null)
            return mBR.getCurrentPosition ();
        return 0;
    }
    int getSweeperEncoderCount () {
        if (mSweeper != null)
            return mSweeper.getCurrentPosition ();
        return 0;
    }
    //------------Indicate Motor Wheel Encoders Value------------
    boolean hasLeftDriveEncoderReached (double count) {
        if (mFL != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (mFL.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasRightDriveEncoderReached (double count) {
        if (mFR != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (mFR.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasBackLeftEncoderReached (double count) {
        if (mBL != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (mBL.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasBackRightEncoderReached (double count) {
        if (mFR != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (mFR.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean haveDriveEncodersReached (double leftCount, double rightCount) {
        return hasLeftDriveEncoderReached(leftCount)
            && hasRightDriveEncoderReached(rightCount);}
    boolean haveDriveEncodersReached (double leftCount, double rightCount,double backLeft, double backRight) {
        return hasLeftDriveEncoderReached(leftCount)
                && hasRightDriveEncoderReached(rightCount)
                && hasBackLeftEncoderReached(backLeft)
                && hasBackRightEncoderReached(backRight);}
    boolean hasSweeperEncoderReached (double count) {
        if (mSweeper != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (mSweeper.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    //------------Indicate whether the motor wheel encoders have reached a value------------
    boolean driveUsingEncoders (double leftPower, double rightPower,
                                double backLeftPower, double backRightPower,
                                double leftCount, double rightCount,
                                double backLeftCount, double backRightCount) {
        runUsingEncoders();
        setDrivePower(leftPower, rightPower, backLeftPower, backRightPower);
        if (haveDriveEncodersReached(leftCount, rightCount, backLeftCount, backRightCount)) {
            resetDriveEncoders();
            setDrivePower(0, 0, 0, 0);
            return true;
        }
        return false;
    }
    boolean runSweeperUsingEncoder (double sweeperPower, double backSweeperPower, double sweeperCount) {
        double sweep;
        if (sweeperPower != 0)
            sweep = sweeperPower;
        else {
            sweep = backSweeperPower;
        }
        runUsingEncoders();
        setSweeperPower(sweep);
        if (hasSweeperEncoderReached(sweeperCount)) {
            resetSweeperEncoder();
            setSweeperPower(0);
            return true;
        }
        return false;
    }
    //------------Indicate If Motor Wheel Encoder Has Reset------------
    boolean hasLeftDriveEncoderReset () {return getLeftEncoderCount() == 0;}
    boolean hasRightDriveEncoderReset() {return getRightEncoderCount() == 0;}
    boolean hasBackLeftEncoderReset () {return getBackLeftEncoderCount() == 0;}
    boolean hasBackRightEncoderReset() {return getBackRightEncoderCount() == 0;}
    boolean haveDriveEncodersReset   () {return hasLeftDriveEncoderReset() && hasRightDriveEncoderReset() &&
                                                hasBackLeftEncoderReset() && hasBackRightEncoderReset();}
    boolean hasSweeperEncoderReset   () {return (getSweeperEncoderCount() == 0);}
}
