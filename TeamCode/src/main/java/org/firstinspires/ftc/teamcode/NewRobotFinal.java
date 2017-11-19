package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Jeremy and Raghav on 10/18/2017.
 */

public class NewRobotFinal
{
    private int liftLevels[] = {0, 1696,2696,3696}; //Currently not levels or stops
    private int liftDeadzone = 169;
    private short currentLvl;

    private ColorSensor topColorSens = null;
    private ColorSensor forwardColorSens = null;
    private ColorSensor floorColorSens = null;

    private DcMotorImplEx liftMotor = null;
    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.
    private VuforiaLocalizer vuforia = null;
    private RelicRecoveryVuMark vuMark = null;
    private VuforiaTrackables relicTrackables = null;
    private VuforiaTrackable relicTemplate = null;

    private BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
    private float roboDiameterCm = (float)(45.7*Math.PI); // can be adjusted
    private float wheelCircIn = 4 * (float)Math.PI ; //Circumference of wheels used
    private float wheelCircCm = (float)(9.8* Math.PI);

    public NewRobotFinal(HardwareMap hardwareMap)
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //Comment out if you don't want camera view on robo phone
        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        floorColorSens = hardwareMap.colorSensor.get("floorColorSens");
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        liftMotor = hardwareMap.get(DcMotorImplEx.class, "liftMotor");
        zeroStuff();
        imu = (hardwareMap.get(BNO055IMU.class, "imu"));
        initIMU();

        driveLeftOne = hardwareMap.get(DcMotorImplEx.class, "driveLeftOne");
        driveRightOne = hardwareMap.get(DcMotorImplEx.class, "driveRightOne");

        resetEncoders();
        stopAllMotors();
        updateIMUValues();
    }

    public void initIMU()
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    public void updateIMUValues()
    {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
    }

    public char getGlyphCipher()
    {
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        if (vuMark.equals(RelicRecoveryVuMark.CENTER))
            return 'c';
        else if (vuMark.equals(RelicRecoveryVuMark.LEFT))
            return 'l';
        else if (vuMark.equals(RelicRecoveryVuMark.RIGHT))
            return 'r';
        else
            return '?';
    }

    public float getHueValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F,0F,0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        return hsvValues[0];
    }

    public char getColor(ColorSensor in_ColorSens)
    {
        float hue = getHueValue( in_ColorSens);

        if (hue < 5 || hue > 330)
            return 'r';
        else if (hue > 219 && hue < 241)
            return 'b';
        else
            return '?';
    }

    public void zeroStuff() //Methods sets motors at low power to put the motors to their resting positions
    {                       //basically sets up lift's step counts starting at its bottom position
        currentLvl = 0;
        resetEncoders();
    }

    private void resetEncoders()//sets encoders to 0 for motors
    {
        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setVelocity(0, AngleUnit.DEGREES);

        driveRightOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        driveRightOne.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveRightOne.setDirection(DcMotorSimple.Direction.REVERSE);
        driveRightOne.setVelocity(0, AngleUnit.DEGREES);

        driveLeftOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveLeftOne.setDirection(DcMotorSimple.Direction.REVERSE);
        driveLeftOne.setVelocity(0, AngleUnit.DEGREES);

    }

    public void moveLift(int adjLevels)
    {
        moveLift(adjLevels, .4f);
    }

    public void moveLift(int adjLevels, float pow) //For the lift, I'll use levels or encoders points that stop
    {
        if (adjLevels + currentLvl < 0)
            return;
        else if (adjLevels + currentLvl > 3)
            return;

        currentLvl += adjLevels;

        if (adjLevels > 0)
        {
            liftMotor.setPower(Math.abs(pow));
            while (liftMotor.getCurrentPosition() < liftLevels[currentLvl] - liftDeadzone){}
        }
        else
        {
            liftMotor.setPower(-Math.abs(pow));
            while (liftMotor.getCurrentPosition() > liftLevels[currentLvl] + liftDeadzone){}
        }

        liftMotor.setPower(0);
    }

    public void fineMoveLift(float y)
    {
        fineMoveLift(y, .15f);
    }

    public void fineMoveLift(float y, float factor)
    {
        if (y > .3)
        {
            liftMotor.setPower(Math.abs(y * factor));
        }
        else if (y < -.3)
        {
            liftMotor.setPower(-Math.abs(factor * y));
        }
        else
        {
            liftMotor.setPower(0);
        }
    }

    public void stopAllMotors()
    {
        liftMotor.setPower(0);
        driveLeftOne.setPower(0);
        driveRightOne.setPower(0);
    }

    public ColorSensor getTopColorSens()
    {
        return topColorSens;
    }

    public ColorSensor getForwardColorSens()
    {
        return forwardColorSens;
    }

    public ColorSensor getFloorColorSens()
    {
        return floorColorSens;
    }

    public DcMotorImplEx getLiftMotor()
    {
        return liftMotor;
    }
}
