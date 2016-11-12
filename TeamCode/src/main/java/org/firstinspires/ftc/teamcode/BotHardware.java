package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class BotHardware
{

    /* Public OpMode members. */
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor lifterMotor = null;
    public DcMotor launcherMotor = null;

    public Servo leftServo = null;
    public Servo rightServo = null;

    public ColorSensor leftSensor = null;
    public ColorSensor rightSensor = null;
    public DeviceInterfaceModule cdim = null;

    /* local OpMode members. */
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public BotHardware() {}

    /* Initialize standard Hardware interfaces */
    public void init(OpMode opMode, boolean debug) {

        AutoLib.HardwareFactory hw;

        if (debug)
            hw = new AutoLib.TestHardwareFactory(opMode);
        else
            hw = new AutoLib.RealHardwareFactory(opMode);

        // Define and Initialize Motors
        frontLeftMotor = hw.getDcMotor("front_left");
        frontRightMotor = hw.getDcMotor("front_right");
        backLeftMotor = hw.getDcMotor("back_left");
        backRightMotor = hw.getDcMotor("back_right");
        lifterMotor = hw.getDcMotor("lifter");
        launcherMotor = hw.getDcMotor("launcher");

        leftServo = hw.getServo("servo_left");
        rightServo = hw.getServo("servo_right");

        try{
            cdim = opMode.hardwareMap.deviceInterfaceModule.get("dim");
            leftSensor = opMode.hardwareMap.colorSensor.get("sensor_left");
            rightSensor = opMode.hardwareMap.colorSensor.get("sensor_right");
        }
        catch (Exception e) {
            opMode.telemetry.addData("Color sensors failed to load!", "");
        }


        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        lifterMotor.setPower(0);
        launcherMotor.setPower(0);

        //set zero behavior
        //frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //set servo directions
        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.FORWARD);

        //set servos default positions
        leftServo.setPosition(0.5);
        rightServo.setPosition(0.5);
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

    //start crappy code!
    public void setRunMode(DcMotor.RunMode mode){
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
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

