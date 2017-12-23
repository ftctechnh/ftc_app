package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

public class BotHardwareOld
{

    //comment
    private boolean isReversed;
    /* Public OpMode members. */
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor launcherMotor = null;
    public DcMotor sweeperMotor = null;

    public Servo leftServo = null;
    public Servo rightServo = null;
    public Servo ballServo = null;

    public MuxColor leftSensor = null;
    public MuxColor rightSensor = null;
    public UltrasonicSensor distSensorLeft = null;
    public UltrasonicSensor distSensorRight = null;
    public DeviceInterfaceModule dim;
    public AHRS navX;

    //private MultiplexColorSensor mux = null;

    public NavXHeading navXHeading;

    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    /* local OpMode members. */
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public BotHardwareOld() {}

    /* Initialize standard Hardware interfaces */
    public void init(OpMode opMode, boolean debug, boolean reverse) {

        initMotors(opMode, debug, reverse);

        try{

            final int[] ports = {6,7};
            final int milliSeconds = 48;
            //mux = new MultiplexColorSensor(opMode.hardwareMap, "mux", "color",
            //        ports, milliSeconds,
            //        MultiplexColorSensor.GAIN_16X);

            //mux.startPolling();

            //leftSensor = new MuxColor(ports[1], mux);
            //rightSensor = new MuxColor(ports[0], mux);


            //leftSensor = opMode.hardwareMap.colorSensor.get("sensor_left");
            //rightSensor = opMode.hardwareMap.colorSensor.get("sensor_right");
        }
        catch (Exception e) {
            opMode.telemetry.addData("Color sensors failed to load!", "");
        }

        try {
            distSensorLeft = opMode.hardwareMap.ultrasonicSensor.get("ultra_left");
            distSensorRight = opMode.hardwareMap.ultrasonicSensor.get("ultra_right");
        }
        catch (Exception e){
            opMode.telemetry.addData("Ultrasonic sensor fail to load!", "");
        }

        try {
            dim = opMode.hardwareMap.deviceInterfaceModule.get("dim");
        }
        catch (Exception e){
            opMode.telemetry.addData("Device interface module failed to load", "");
        }

        try{
            navX = AHRS.getInstance(dim, 5, AHRS.DeviceDataType.kProcessedData, NAVX_DEVICE_UPDATE_RATE_HZ);
            navXHeading = new NavXHeading();
        }
        catch (Exception e){
            opMode.telemetry.addData("NavX failed to load!", "");
        }
    }

    public void init(OpMode opMode, boolean debug){
        init(opMode, debug, false);
    }

    public void initTeleop(OpMode opMode, boolean debug){
        try {
            dim = opMode.hardwareMap.deviceInterfaceModule.get("dim");
        }
        catch (Exception e){
            opMode.telemetry.addData("Device interface module failed to load", "");
        }
        initMotors(opMode, debug);
    }


    public void initMotors(OpMode opMode, boolean debug, boolean reversed){
        AutoLib.HardwareFactory hw;

        if (debug)
            hw = new AutoLib.TestHardwareFactory(opMode);
        else
            hw = new AutoLib.RealHardwareFactory(opMode);

        if(!reversed){
            // Define and Initialize Motors
            frontLeftMotor = hw.getDcMotor("front_left");
            frontRightMotor = hw.getDcMotor("front_right");
            backLeftMotor = hw.getDcMotor("back_left");
            backRightMotor = hw.getDcMotor("back_right");
        }
        else{
            // swap motors so back of robot is now front
            frontLeftMotor = hw.getDcMotor("front_right");
            frontRightMotor = hw.getDcMotor("front_left");
            backLeftMotor = hw.getDcMotor("back_right");
            backRightMotor = hw.getDcMotor("back_left");
        }
        isReversed = reversed;

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        launcherMotor = hw.getDcMotor("launcher");
        sweeperMotor = hw.getDcMotor("sweeper");

        //launcherMotor.setDirection(DcMotor.Direction.REVERSE);
        sweeperMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        leftServo = hw.getServo("servo_left");
        rightServo = hw.getServo("servo_right");
        ballServo = hw.getServo("ball");

        // Set all motors to zero power
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        launcherMotor.setPower(0);
        sweeperMotor.setPower(0);

        //set zero behavior
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set servo directions
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.REVERSE);
        ballServo.setDirection(Servo.Direction.REVERSE);

        //set servos default positions
        leftServo.setPosition(0.0);
        rightServo.setPosition(0.0);
        ballServo.setPosition(-0.2); //negative is closed
    }

    public void initMotors(OpMode opMode, boolean debug){
        initMotors(opMode, debug, false);
    }

    public class NavXHeading implements HeadingSensor {

        public float getHeading(){
            //if(isReversed) return -navX.getYaw();
            //else return navX.getYaw();
            return navX.getYaw();
        }

    }

    public class MuxColor implements ColorSensor{
        private int mPort;
        //private MultiplexColorSensor mSensor;
        private int[] crgb = new int[4];
        private double lastTime;

        //MuxColor(int port, MultiplexColorSensor sensor){
        //    mPort = port;
        //    mSensor = sensor;
        //}

        private boolean updateColor(){
            if(period.milliseconds() - lastTime > 50){
               // crgb = mux.getCRGB(mPort);
                lastTime = period.milliseconds();
            }
            return crgb[0] != 65535;
        }

        public int alpha(){
            if(!updateColor()) return -1;
            return crgb[0];
        }

        public int red(){
            if(!updateColor()) return -1;
            return crgb[1];
        }

        public int green(){
            if(!updateColor()) return -1;
            return crgb[2];
        }

        public int blue(){
            if(!updateColor()) return -1;
            return crgb[3];
        }

        //below are functions that should not be used under any circumstance
        public int argb(){
            return -1;
        }

        public void enableLed(boolean duh){

        }

        public void setI2cAddress(I2cAddr newAddr){

        }

        public I2cAddr getI2cAddress(){
            return new I2cAddr(0);
        }

        public Manufacturer getManufacturer(){
            return Manufacturer.Adafruit;
        }

        public String getDeviceName(){
            return "Multiplexed Color Sensors";
        }

        public String getConnectionInfo(){
            return "Connected (I hope)";
        }

        public int getVersion(){
            return 9001;
        }

        public void resetDeviceConfigurationForOpMode(){
            //no
        }

        public void close(){
            //will only activate on a thursday
        }
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public boolean startNavX(){
        if (!navX.isCalibrating())
            navX.zeroYaw();
        else return false;
        return true;
    }

    public HeadingSensor getNavXHeadingSensor(){
        return navXHeading;
    }

    public boolean isReversed(){
        return isReversed;
    }

    //start crappy code!
    public void setRunMode(DcMotor.RunMode mode){
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setPowerAll(double power){
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
    }

    public void setTargetAll(int pos){
        frontLeftMotor.setTargetPosition(pos);
        frontRightMotor.setTargetPosition(pos);
        backLeftMotor.setTargetPosition(pos);
        backRightMotor.setTargetPosition(pos);
    }

    public void setNewTargetAll(int newPos){
        frontLeftMotor.setTargetPosition(newPos + frontLeftMotor.getCurrentPosition());
        frontRightMotor.setTargetPosition(newPos + frontRightMotor.getCurrentPosition());
        backLeftMotor.setTargetPosition(newPos + backLeftMotor.getCurrentPosition());
        backRightMotor.setTargetPosition(newPos + backRightMotor.getCurrentPosition());
    }

    public void setMaxSpeedAll(int encoderTicks){
        //frontLeftMotor.setMaxSpeed(encoderTicks);
        //frontRightMotor.setMaxSpeed(encoderTicks);
        //backLeftMotor.setMaxSpeed(encoderTicks);
        //backRightMotor.setMaxSpeed(encoderTicks);
    }

    public void setFrontPower(double power) {
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
    }

    public void setBackPower(double power) {
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
    }

    public DcMotor[] getMotorArray(){
        return new DcMotor[] {frontRightMotor, backRightMotor, frontLeftMotor, backLeftMotor};
    }
}

