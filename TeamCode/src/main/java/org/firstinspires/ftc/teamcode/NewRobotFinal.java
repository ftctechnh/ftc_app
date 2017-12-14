package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
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
    //Currently not levels or stops
    private short currentLvl = 0;
    private short liftTargetPos;
    private short liftDir;
    private final short UP_L = 1;
    private final short DOWN_L = 2;
    private final short STOP_L = 0;

    private ColorSensor floorColorSens = null;
    private ColorSensor rightWingColorSens = null;
    private ColorSensor leftWingColorSens = null;

    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;
    private DcMotorImplEx wingMotor = null;

    private Servo leftDoorWall = null;
    private Servo rightDoorWall = null;
    private DcMotorImplEx liftMotor = null;
    //private DcMotorImplEx shiftLiftMotor = null;

    private DcMotorImplEx tailRelease  = null;
    private Servo grabber  = null;
    private Servo grabberRotator = null;

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.
    private VuforiaLocalizer vuforia  = null;
    private RelicRecoveryVuMark vuMark  = null;
    private VuforiaTrackables relicTrackables  = null;
    private VuforiaTrackable relicTemplate  = null;

    private BNO055IMU imu  = null;
    Orientation angles;
    Acceleration gravity;

    //Also to note: The front wheels to the back wheels is 13.5 apart in terms of center distance
    public final int neverrestEncCountsPerRev = 1120; //Based on Nevverest 40 motors
    public final float roboDiameterCm = (float)(38.1*Math.PI); // can be adjusted
    public final float wheelCircIn = (float)(Math.PI * 4) ; //Circumference of wheels used
    public final float wheelCircCm = (float)(10.168* Math.PI);

    public NewRobotFinal(HardwareMap hardwareMap)
    {
        liftMotor = hardwareMap.get(DcMotorImplEx.class, "liftMotor");

        imu = (hardwareMap.get(BNO055IMU.class, "imu"));

        driveLeftOne = hardwareMap.get(DcMotorImplEx.class, "driveLeftOne");
        driveRightOne = hardwareMap.get(DcMotorImplEx.class, "driveRightOne");

        wingMotor = hardwareMap.get(DcMotorImplEx.class, "wingMotor");

     //   leftDoorWall = hardwareMap.get(Servo.class, "leftDoorWall");
       // rightDoorWall = hardwareMap.get(Servo.class, "rightDoorWall");

        leftDoorWall = hardwareMap.servo.get("leftDoorWall");
        rightDoorWall = hardwareMap.servo.get("rightDoorWall");
        leftDoorWall.scaleRange(.5f, .95f);
        rightDoorWall.scaleRange(.05f, .5f);
        initEndGame(hardwareMap);

        zeroStuff();
        initIMU();
        updateIMUValues();
    }

    public void initEndGame(HardwareMap hardwareMap)
    {
        tailRelease = hardwareMap.get(DcMotorImplEx.class, "tailRelease");
        tailRelease.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        tailRelease.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        tailRelease.setZeroPowerBehavior(DcMotorImplEx.ZeroPowerBehavior.BRAKE);
        tailRelease.setDirection(DcMotorImplEx.Direction.FORWARD);
       // tailRelease.setVelocity(0, AngleUnit.RADIANS);

        grabberRotator = hardwareMap.get(Servo.class, "grabberRotator");
        grabber = hardwareMap.get(Servo.class, "grabber");
    }

    public void initVuforia(HardwareMap hardwareMap)
    {
        resetDriveEncoders();
        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveRightOne.setVelocity(0, AngleUnit.RADIANS);
        driveLeftOne.setVelocity(0, AngleUnit.RADIANS);
        driveRightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        driveLeftOne.setDirection(DcMotorSimple.Direction.FORWARD);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //Comment out if you don't want camera view on robo phone
        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        floorColorSens = hardwareMap.colorSensor.get("floorColorSens");
        rightWingColorSens = hardwareMap.colorSensor.get("rightWingColorSens");
        leftWingColorSens = hardwareMap.colorSensor.get("leftWingColorSens");
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
    }

    public void zeroStuff() //Methods sets motors at low power to put the motors to their resting positions
    {                       //basically sets up lift's step counts starting at its bottom position
        liftMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //liftMotor.setVelocity(0, AngleUnit.DEGREES);

        wingMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        wingMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        wingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wingMotor.setDirection(DcMotorSimple.Direction.REVERSE);
       // wingMotor.setVelocity(0, AngleUnit.DEGREES);

        //driveRightOne.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveRightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        //driveRightOne.setVelocity(1, AngleUnit.DEGREES);
        //driveLeftOne.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveLeftOne.setDirection(DcMotorSimple.Direction.FORWARD);
       // driveLeftOne.setVelocity(1, AngleUnit.DEGREES);
//        resetDriveEncoders();

        rightDoorWall.setDirection(Servo.Direction.FORWARD);
        leftDoorWall.setDirection(Servo.Direction.FORWARD);
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
        float hue = getHueValue(in_ColorSens);

        if (hue < 5 || hue > 330)
            return 'r';
        else if (hue > 219 && hue < 241)
            return 'b';
        else
            return '?';
    }

    public void driveMotors(float lPow, float rPow)
    {
        driveLeftOne.setPower(lPow);
        driveRightOne.setPower(rPow);
    }

    private void resetDriveEncoders()//sets encoders to 0 for motors
    {
        driveRightOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveStraight_In(float inches, double pow)
    {
        float encTarget = neverrestEncCountsPerRev / wheelCircCm * inches;
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

        stopDriveMotors();
    }

    public void driveStraight_In(float inches)
    {
        driveStraight_In(inches, .75);
    }

    /*public void spin_Right(float degrees)
    {
        spin_Right(degrees, .5);
    }

    public void spin_Right(float degrees, double pow)// Right Motor only moves!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (neverrestEncCountsPerRev / wheelCircCm);
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

        stopDriveMotors();
    }*/

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

        stopDriveMotors();
    }

    public void spin_Right_IMU(float degrees)
    {
        spin_Right_IMU(degrees, .5);
    }
/*
    public void spin_Left(float degrees)
    {
        spin_Left(degrees, .5);
    }

    public void spin_Left(float degrees, double pow)//Left Motor only moves!!!!!!!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (neverrestEncCountsPerRev / wheelCircCm);
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

        stopDriveMotors();
    }*/

    public void spin_Left_IMU(float degrees)
    {
        spin_Left_IMU(degrees, .5);
    }

    public void spin_Left_IMU(float degrees, double pow)
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
            driveLeftOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveLeftOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
        }
        stopDriveMotors();
    }
/*
    public void pivot(float degrees, double pow)//Utilizes two motors at a time; spins in place
    {
        float degToRad = degrees * (float) Math.PI / 180.0f; // converts it to Radians

        float encTarget = (roboDiameterCm / 2 * degToRad) * (neverrestEncCountsPerRev / wheelCircCm)/2;
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

        stopDriveMotors();
    }

    public void pivot(float degrees)
    {
        pivot(degrees, .23);
    }
*/
    public void pivot_IMU(float degrees)
    {
        pivot_IMU(degrees, .23);
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

        stopDriveMotors();
    }

    public void moveLift(int adjLevels)
    {
        moveLift(adjLevels, .66f);
    }

    public void moveLift(int adjLevels, float pow) //For the lift, I'll use levels or encoders points that stop
    {
        CalcLiftTarget(adjLevels);
        while(liftDir != STOP_L)
        {
            AdjLiftDir();
        }
    }

    public void CalcLiftTarget(int adjLevels) //For the lift, I'll use levels or encoders points that stop
    {
        final int liftLevels[] = {0, 100, 769, 1500, 1538};
        final int liftDeadzone = 0;

        if (adjLevels + currentLvl < 0)
        {
            currentLvl = 0;
        } else if (adjLevels + currentLvl >= liftLevels.length)
        {
            currentLvl = (short) (liftLevels.length - 1);
        } else
        {
            currentLvl += adjLevels;
        }

        //Calculate target
        if (adjLevels > 0)
        {
            liftTargetPos = (short) (liftLevels[currentLvl] - liftDeadzone);
            liftDir = UP_L;
        } else
        {
            liftTargetPos = (short) (liftLevels[currentLvl] + liftDeadzone);
            liftDir = DOWN_L;
        }
    }


    public void AdjLiftDir()
    {
        if (liftDir == UP_L && -liftMotor.getCurrentPosition() < liftTargetPos)
        {
            liftMotor.setPower(-.7);
        }
        else if(liftDir == DOWN_L && -liftMotor.getCurrentPosition() > liftTargetPos)
        {
            liftMotor.setPower(.7);
        }
        else
        {
            liftDir = STOP_L;
            liftMotor.setPower(0);
        }
    }

    public void fineMoveLift(float y)
    {
        fineMoveLift(y, .15f);
    }

    public void fineMoveLift(float y, float factor)
    {
        liftDir = STOP_L;

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

    public void moveXEncoderCounts(int xEnc, float pow, boolean up)
    {
        int initPos = liftMotor.getCurrentPosition();
        if(up)
        {
            liftMotor.setPower(-Math.abs(pow));
            while(-liftMotor.getCurrentPosition() < -initPos + xEnc){}
        }
        else
        {
            liftMotor.setPower(Math.abs(pow));
            while(-liftMotor.getCurrentPosition() > -initPos - xEnc){}
        }
        liftMotor.setPower(0);
    }

    public void moveWing(boolean moveDown)
    {
        if(moveDown)
        {
            wingMotor.setPower(-.3);
            while(wingMotor.getCurrentPosition() > -2500){}
        }
        else
        {
            wingMotor.setPower(.3);
            while(wingMotor.getCurrentPosition() < 0){}
        }

        wingMotor.setPower(0);
    }

    public void openOrCloseDoor(boolean close)
    {
        /**
         * NOTE NEED TO CHECK THE VALUES
         */
        if (!close)
        {
            leftDoorWall.setPosition(.5f);
            rightDoorWall.setPosition(.5f);
        }
        else
        {
            leftDoorWall.setPosition(.95f);
            rightDoorWall.setPosition(.05f);
        }
    }

    public void fineAdjDoors(double in)
    {
        leftDoorWall.setPosition(leftDoorWall.getPosition() + in);
        rightDoorWall.setPosition(rightDoorWall.getPosition() + in);
    }

    public void autoPark()
    {
        double angle = anglePerpToGrav();
        if (angle > 5)
        {
            driveLeftOne.setPower(.5);
            driveRightOne.setPower(-.5);
        }
        else if (angle < -5)
        {
            driveLeftOne.setPower(-.5);
            driveRightOne.setPower(.5);
        }
        else
            return;
    }

    public void OpenCloseGrabber(boolean close)
    {
        if(close)
        {
            grabber.setPosition(.6f);
        }
        else
        {
            grabberRotator.setPosition(.15f);
        }
    }

    public void fineAdjGrabber(float in)
    {
        grabber.setPosition(grabber.getPosition() + in);
    }

    public void fineAdjGrabberRotator(float in)
    {
        grabberRotator.setPosition(grabberRotator.getPosition() + in);
    }

    public void tiltGrabberRotator(boolean goUp)
    {
        /*
        .81 is pointing towards the robot
        .69 is over the relic, vertical
        .21 is horizontal and not pointing towards the robot
         0 is up.
         */
        if (goUp)
            grabberRotator.setPosition(0f);
        else
            grabberRotator.setPosition(.69f);
    }

    public void stopAllMotors()
    {
        stopDriveMotors();
        liftMotor.setPower(0);
        wingMotor.setPower(0);
    }

    public void stopDriveMotors()
    {
        driveLeftOne.setPower(0);
        driveRightOne.setPower(0);
    }

    public void kill()
    {
        leftDoorWall.close();
        rightDoorWall.close();
        driveRightOne.close();
        driveLeftOne.close();
        liftMotor.close();
        grabberRotator.close();
        imu.close();
        grabber.close();
        tailRelease.close();
        wingMotor.close();
    }

    public void killAuto()
    {
        leftWingColorSens.close();
        rightWingColorSens.close();
        floorColorSens.close();
    }

    public ColorSensor getleftWingColorSens() { return leftWingColorSens; }

    public ColorSensor getrightWingColorSens() { return rightWingColorSens; }

    public ColorSensor getFloorColorSens() { return floorColorSens; }

    public DcMotorImplEx getDriveLeftOne() { return driveLeftOne; }

    public DcMotorImplEx getDriveRightOne() {return driveRightOne;}

    public DcMotorImplEx getLiftMotor() { return liftMotor; }

    public DcMotorImplEx getWingMotor() { return wingMotor; }

    public Servo getLeftDoorWall() { return leftDoorWall; }

    public Servo getRightDoorWall() { return rightDoorWall; }

    public Servo getGrabber() { return grabber; }

    public Servo getGrabberRotator(){return grabberRotator;}

    public DcMotorImplEx getTailRelease(){return tailRelease;}

    public BNO055IMU getImu(){return imu;}

    public short getLiftDir(){return liftDir;}
}
