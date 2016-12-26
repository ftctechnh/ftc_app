package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Peter on 9/22/2016.
 */
public class OmniDriveBot implements DriveTrainInterface
{
    private HardwareMap hardwareMap = null;
    private DcMotor fL = null;
    private DcMotor fR = null;
    private DcMotor bL = null;
    private DcMotor bR = null;
    private DcMotor lifterMotor = null;
    private DcMotor shooterMotorOne = null;
    private DcMotor shooterMotorTwo = null;
    private Servo scooperServo = null;
    private Servo shooterPitchServo = null;

    private float leftYIn;
    private float leftXIn;
    private float rightXIn;
    private float fRPower;
    private float fLPower;
    private float bRPower;
    private float bLPower;
    private float lifterPower;
    private float shooterPowerOne;
    private float shooterPowerTwo;
    private float shooterPitchServoPos;
    private float scooperServoPos;

    ColorSensor beaconColorSensor;
    DeviceInterfaceModule cdim;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    static final int LED_CHANNEL = 5;


    public OmniDriveBot()
    {

    }

    public void init(HardwareMap hwMap)
    {
        hardwareMap = hwMap;
        fL = hardwareMap.dcMotor.get("frontLeft");
        fR = hardwareMap.dcMotor.get("frontRight");
        bL = hardwareMap.dcMotor.get("backLeft");
        bR = hardwareMap.dcMotor.get("backRight");
        lifterMotor = hardwareMap.dcMotor.get("lifterMotor");
        shooterMotorOne = hardwareMap.dcMotor.get("shooterMotorOne");
        shooterMotorTwo = hardwareMap.dcMotor.get("shooterMotorTwo");
        scooperServo = hardwareMap.servo.get("scooperServo");
        shooterPitchServo = hardwareMap.servo.get("shooterPitchServo");

        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        fR.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);
        bR.setDirection(DcMotorSimple.Direction.REVERSE);

        fL.setPower(0);
        fR.setPower(0);
        bL.setPower(0);
        bR.setPower(0);

        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fR.setMaxSpeed(2400);
        fL.setMaxSpeed(2400);
        bR.setMaxSpeed(2400);
        bL.setMaxSpeed(2400);

        shooterMotorOne.setMaxSpeed(3000);
        shooterMotorTwo.setMaxSpeed(3000);

        shooterPitchServo.setPosition(1.0f);
        scooperServo.setPosition(.5f);

        //bLedOn represents the state of the LED.
        bLedOn = true;

        // get a reference to our DeviceInterfaceModule object.
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        beaconColorSensor = hardwareMap.colorSensor.get("beaconColorSensor");
        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
    }

    public void drive()
    {
        fRPower = leftYIn - leftXIn + rightXIn;
        fLPower = -leftYIn - leftXIn + rightXIn;
        bRPower = leftYIn + leftXIn + rightXIn;
        bLPower = -leftYIn + leftXIn + rightXIn;

        float scaleFactor = 1.0f;

        if (Math.abs(fRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fRPower);
        }
        if (Math.abs(fLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fLPower);
        }
        if (Math.abs(bRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bRPower);
        }
        if (Math.abs(bLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bLPower);
        }
        fLPower = fLPower / scaleFactor;
        bRPower = bRPower / scaleFactor;
        bLPower = bLPower / scaleFactor;
        fRPower = fRPower / scaleFactor;

        fR.setPower(fRPower);
        fL.setPower(fLPower);
        bR.setPower(bRPower);
        bL.setPower(bLPower);
        lifterMotor.setPower(lifterPower);
        shooterMotorOne.setPower(shooterPowerOne);
        shooterMotorTwo.setPower(shooterPowerTwo);
        scooperServo.setPosition(scooperServoPos);
        shooterPitchServo.setPosition(shooterPitchServoPos);

        // convert the RGB values to HSV values.
        Color.RGBToHSV((beaconColorSensor.red() * 255) / 800, (beaconColorSensor.green() * 255) / 800, (beaconColorSensor.blue() * 255) / 800, hsvValues);
    }

    public void stop()
    {
        fLPower = 0;
        fRPower = 0;
        bLPower = 0;
        bRPower = 0;
        lifterPower = 0;
        shooterPowerTwo = 0;
        shooterPowerOne = 0;
        shooterPitchServoPos = 0.5f;
    }

    public void resetEncoders()
    {
        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveStraight(double distanceInches, double degree)
    {
        //Resets Encoders
        resetEncoders();
        //Converts inputs appropriately if distance inches is negative
        if(distanceInches < 0)
        {
            distanceInches *= -1;
            degree += 180;
        }
        //Shift input degree between +-180
        while(degree > 180)
        {
            degree -= 360;
        }
        //degree is now guaranteed to be less than 180
        while(degree < -180)
        {
            degree += 360;
        }
        //degree is now guaranteed to be greater than -180
        double degToRad = (Math.PI/180) * degree;
        //Numbers in equation below came from a sinReg on a calculator using observed travel from 0 to 90 degrees at 12 inches
        //with increments of 15 degrees
        double scalingFactor = (0.157 * Math.sin(3.963 * (degToRad) + 1.755)) + 0.846;

        float leftXIn = -(float)Math.sin(degToRad);
        float leftYIn = (float)Math.cos(degToRad);
        float fRPower = leftYIn - leftXIn;
        float fLPower = -leftYIn - leftXIn;
        float bRPower = leftYIn + leftXIn;
        float bLPower = -leftYIn + leftXIn;

        float scaleFactor = 1.0f;

        if (Math.abs(fRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fRPower);
        }
        if (Math.abs(fLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fLPower);
        }
        if (Math.abs(bRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bRPower);
        }
        if (Math.abs(bLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bLPower);
        }
        fLPower = fLPower / scaleFactor;
        bRPower = bRPower / scaleFactor;
        bLPower = bLPower / scaleFactor;
        fRPower = fRPower / scaleFactor;

        double distanceTravel = distanceInches/scalingFactor;
        int fLDistanceEncoders = (int)(distanceTravel*57);
        int fRDistanceEncoders = (int)(distanceTravel*57);

        bL.setPower(bLPower);
        bR.setPower(bRPower);
        fL.setPower(fLPower);
        fR.setPower(fRPower);

        while(Math.abs(fL.getCurrentPosition()) < fLDistanceEncoders & Math.abs(fR.getCurrentPosition()) < fRDistanceEncoders)
        {
        }

        bL.setPower(0);
        bR.setPower(0);
        fL.setPower(0);
        fR.setPower(0);

     }

    public void driveStraightPow(double distanceInches, double degree, double power)
    {
        //Resets Encoders
        resetEncoders();
        //Converts inputs appropriately if distance inches is negative
        if(distanceInches < 0)
        {
            distanceInches *= -1;
            degree += 180;
        }
        //Shift input degree between +-180
        while(degree > 180)
        {
            degree -= 360;
        }
        //degree is now guaranteed to be less than 180
        while(degree < -180)
        {
            degree += 360;
        }
        //degree is now guaranteed to be greater than -180
        double degToRad = (Math.PI/180) * degree;
        //Numbers in equation below came from a sinReg on a calculator using observed travel from 0 to 90 degrees at 12 inches
        //with increments of 15 degrees
        double scalingFactor = (0.157 * Math.sin(3.963 * (degToRad) + 1.755)) + 0.846;

        float leftXIn = -(float)Math.sin(degToRad);
        float leftYIn = (float)Math.cos(degToRad);
        float fRPower = leftYIn - leftXIn;
        float fLPower = -leftYIn - leftXIn;
        float bRPower = leftYIn + leftXIn;
        float bLPower = -leftYIn + leftXIn;

        float scaleFactor = 1.0f;

        if (Math.abs(fRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fRPower);
        }
        if (Math.abs(fLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fLPower);
        }
        if (Math.abs(bRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bRPower);
        }
        if (Math.abs(bLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bLPower);
        }
        fLPower = (float)(power*fLPower) / scaleFactor;
        bRPower = (float)(power*bRPower) / scaleFactor;
        bLPower = (float)(power*bLPower) / scaleFactor;
        fRPower = (float)(power*fRPower) / scaleFactor;

        double distanceTravel = distanceInches/scalingFactor;
        int fLDistanceEncoders = (int)(distanceTravel*57);
        int fRDistanceEncoders = (int)(distanceTravel*57);

        bL.setPower(bLPower);
        bR.setPower(bRPower);
        fL.setPower(fLPower);
        fR.setPower(fRPower);

        while(Math.abs(fL.getCurrentPosition()) < fLDistanceEncoders & Math.abs(fR.getCurrentPosition()) < fRDistanceEncoders)
        {
        }

        bL.setPower(0);
        bR.setPower(0);
        fL.setPower(0);
        fR.setPower(0);

    }

    public void spin(double degree)
    {
        double robotCircumference = 84.1;
        int encoderTarget = (int)(degree*robotCircumference*65/360);
        int fLDistanceEncoders = fL.getCurrentPosition() + encoderTarget;
        int fRDistanceEncoders = fR.getCurrentPosition() + encoderTarget;

        if(degree > 0)
        {
            bL.setPower(1);
            bR.setPower(1);
            fL.setPower(1);
            fR.setPower(1);
            while(fL.getCurrentPosition() < fLDistanceEncoders & fR.getCurrentPosition() < fRDistanceEncoders);
        }
        else
        {
            bL.setPower(-1);
            bR.setPower(-1);
            fL.setPower(-1);
            fR.setPower(-1);
            while(fL.getCurrentPosition() > fLDistanceEncoders & fR.getCurrentPosition() > fRDistanceEncoders);
        }

        bL.setPower(0);
        bR.setPower(0);
        fL.setPower(0);
        fR.setPower(0);
    }

    public DcMotor getfL()
    {
        return fL;
    }

    public DcMotor getfR()
    {
        return fR;
    }

    public DcMotor getbL()
    {
        return bL;
    }

    public DcMotor getbR()
    {
        return bR;
    }

    public float getfRPower()
    {
        return fRPower;
    }

    public float getfLPower()
    {
        return fLPower;
    }

    public float getbRPower()
    {
        return bRPower;
    }

    public float getbLPower()
    {
        return bLPower;
    }

    public float getLifterPower() {return lifterPower; }

    public float getShooterPowerOne() {return shooterPowerOne; }

    public float getShooterPowerTwo() {return shooterPowerTwo; }

    public float getShooterPitchServoPos() { return shooterPitchServoPos; }

    public float getScooperServoPos() { return scooperServoPos; }

    public int getSensorRed()
    {
        return beaconColorSensor.red();
    }

    public int getSensorBlue()
    {
        return beaconColorSensor.blue();

    }

    public int getSensorGreen()
    {
        return beaconColorSensor.green();

    }

    public void setLifterPower(float pow)
    { lifterPower = pow; }

    public void setShooterPowerOne(float pow)
    { shooterPowerOne = pow; }

    public void setShooterPowerTwo(float pow)
    { shooterPowerTwo = pow;; }

    public void setShooterPitchServoPos(float pow)
    { shooterPitchServoPos = pow; }

    public void setScooperServoPos(float pow)
    { scooperServoPos = pow; }

    public void setLeftYIn(float lYI)
    {
        leftYIn = -lYI;
    }
    public void setLeftXIn(float lXI)
    {
        leftXIn = lXI;
    }
    public void setRightXIn(float rXI)
    {
        rightXIn = -rXI;
    }

    public void stopAttachments()
    {
        lifterPower = 0;
        shooterPowerOne = 0;
        shooterPowerTwo = 0;
        scooperServoPos = 0.5f;
    }

    public float getSensorHue()
    {
        Color.RGBToHSV((beaconColorSensor.red() * 255) / 800, (beaconColorSensor.green() * 255) / 800, (beaconColorSensor.blue() * 255) / 800, hsvValues);
        return hsvValues[0];
    }

    //Hue color value visual: https://i.stack.imgur.com/YOBFy.png
    public boolean isDetectingBlue()
    {
        if(getSensorHue() > 180 && getSensorHue() < 265) //if hue is between 180 and 265, it is blue
            return true;

        return false;
    }

    public boolean isDetectingRed()
    {
        if(getSensorHue() < 25 || getSensorHue() > 330 && getSensorHue() < 360) //hue is between 330 and 360 and if it's is less than 25, it's red
            return true;

        return false;
    }
}
