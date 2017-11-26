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
 * Created by Jeremy on 11/19/17.
 * Basically the BIG ROBOT CLASS
 */

public class NewRobotFinal
{
    private int liftLevels[] = {0, 400,800,1200}; //Currently not levels or stops
    private int liftDeadzone = 76;
    private short currentLvl;

    private ColorSensor topColorSens = null;
    private ColorSensor forwardColorSens = null;
    private ColorSensor floorColorSens = null;

    private DcMotorImplEx liftMotor = null;
    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;
    private DcMotorImplEx wingMotor = null;

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.
    private VuforiaLocalizer vuforia = null;
    private RelicRecoveryVuMark vuMark = null;
    private VuforiaTrackables relicTrackables = null;
    private VuforiaTrackable relicTemplate = null;

    private BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    //Also to note: The front wheels to the back wheels is 13.5 apart in terms of center distance
    private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
    private float roboDiameterCm = (float)(38.1*Math.PI); // can be adjusted
    private float wheelCircIn = (float)(Math.PI * 4) ; //Circumference of wheels used
    private float wheelCircCm = (float)(10.168* Math.PI);

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
        wingMotor = hardwareMap.get(DcMotorImplEx.class, "wingMotor");
        zeroStuff();
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

    public float getYaw()
    {
        updateIMUValues();
        return AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
    }

    public double anglePerpToGrav()
    {
        updateIMUValues();
        return Math.atan(gravity.yAccel/gravity.zAccel);
    }

    public String getGravToString()
    {
        updateIMUValues();
        return gravity.toString();
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
        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setVelocity(0, AngleUnit.DEGREES);

        wingMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        wingMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        wingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wingMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wingMotor.setVelocity(0, AngleUnit.DEGREES);

        currentLvl = 0;
        resetDriveEncoders();
    }

    private void resetDriveEncoders()//sets encoders to 0 for motors
    {
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

    public void driveStraight_In(float inches, double pow)
    {
        float encTarget;
        if(inches > 1)
            encTarget = encCountsPerRev / wheelCircIn * (inches-1);
        else if (inches < -1)
            encTarget = encCountsPerRev / wheelCircIn * (inches+1);
        else
            encTarget = encCountsPerRev / wheelCircCm * inches;
        //You get the number of encoder counts per unit and multiply it by how far you want to go

        resetDriveEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if(inches < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));

            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget) {}
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));

            while(driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget){}
        }

        stopAllMotors();
    }

    public void spin_Right(float degrees)
    {
        spin_Right(degrees, 1);
    }

    public void spin_Right(float degrees, double pow)// Right Motor only moves!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetDriveEncoders();

        if (degrees < 0) //spins clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));

            while(driveRightOne.getCurrentPosition() > encTarget){}
        }
        else //spins cc
        {
            driveRightOne.setPower(Math.abs(pow));

            while(driveRightOne.getCurrentPosition() < encTarget){}
        }

        stopAllMotors();
    }

    public void spin_Right_IMU(float degrees, double pow)
    {
        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
        }

        stopAllMotors();
    }

    public void spin_Left(float degrees)
    {
        spin_Left(degrees, 1);
    }

    public void spin_Left(float degrees, double pow)//Left Motor only moves!!!!!!!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetDriveEncoders();

        if (degrees > 0) //This spins the robot counterclockwise
        {
            driveLeftOne.setPower(Math.abs(pow));

            while(driveLeftOne.getCurrentPosition() < encTarget){}

        }
        else //spins clockwise
        {
            driveLeftOne.setPower(-Math.abs(pow));

            while(driveLeftOne.getCurrentPosition() > encTarget){}
        }

        stopAllMotors();
    }

    public void spin_Left_IMU(float deg, double pow)
    {
        float degrees = deg;
        if (deg > 0)
        {
            degrees -= 5.2f;
        }
        else
        {
            degrees += 5.2f;
        }

        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveLeftOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveLeftOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
        }

        stopAllMotors();
    }

    public void pivot(float degrees, double pow)//Utilizes two motors at a time; spins in place
    {
        float degToRad = degrees * (float) Math.PI / 180.0f; // converts it to Radians

        float encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm)/2;
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter
        //we divide it by two at the end to compensate for using two motors

        resetDriveEncoders();

        //It pivots in the direction of how to unit circle spins
        if (degrees < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));

            while (driveLeftOne.getCurrentPosition() > encTarget && driveRightOne.getCurrentPosition() > encTarget) {}

        }
        else //CounterClockwise
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));

            while (driveLeftOne.getCurrentPosition() < encTarget && driveRightOne.getCurrentPosition() < encTarget) {}
        }

        stopAllMotors();
    }

    public void pivot_IMU(float degrees, double pow)
    {
        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();

        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
        }

        stopAllMotors();
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

    public void moveWing(float pow)
    {
        wingMotor.setPower(pow);
    }

    public void moveWing(boolean moveDown)
    {
        if(moveDown)
        {
            wingMotor.setPower(-.76);
            while(wingMotor.getCurrentPosition() > -1696){}
        }
        else
        {
            wingMotor.setPower(.76);
            while(wingMotor.getCurrentPosition() < 0){}
        }

        wingMotor.setPower(0);
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

    public DcMotorImplEx getDriveLeftOne()
    {
        return driveLeftOne;
    }

    public DcMotorImplEx getDriveRightOne() {return driveRightOne;}

    public DcMotorImplEx getLiftMotor()
    {
        return liftMotor;
    }

    public DcMotorImplEx getWingMotor()
    {
        return wingMotor;
    }
}
