package org.firstinspires.ftc.teamcode;

import android.graphics.Color;


import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
//import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Jeremy on 11/19/17.
 * Basically the BIG ROBOT CLASS
 */

public class NewRobotFinal
{
    //This block is currently used for our lift
    final int liftLevels[] = {0, 250, 769, 1500, 1538}; //Specifies heights at where the robot should stop
    private short currentLvl = 0;//0 represents the first level and init at first level
    private short liftTargetPos;
    private short liftDir;
    private final static short UP_L = 1; //We use final shorts with variable names to help with specifying direction as we use numbers
    private final static short DOWN_L = 2;
    private final static short STOP_L = 0;

    final double defaultTurnPow = .5; //Default powers for motors
    final double defaultLiftSpeed = 1;
    public double balPow = .25;

    private ColorSensor floorColorSens;
    private ColorSensor rightWingColorSens;
    private ColorSensor leftWingColorSens;

    private DcMotorImplEx driveLeftOne;
    private DcMotorImplEx driveRightOne;
    private DcMotorImplEx wingMotor;

    private Servo leftDoorWall;
    private Servo rightDoorWall;
    private DcMotorImplEx liftMotor;
    private double leftDoorPos;
    private double rightDoorPos;

    private DcMotorImplEx tailRelease;
    private Servo grabber;
    private Servo grabberRotator;

    //Vuforia variables
    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key for Vuforia; need it for it to work
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    private static final double rotatorDown =0.91;
    private static final double rotatorUp = 0.00;
    private double rotatorPos = rotatorDown;
    private static final double grabberOpen = 0.5;
    private static final double grabberClosed = 1.00;
    private double grabberPos = grabberClosed;

    private BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    DigitalChannel wingTouchSens;
    DigitalChannel bottomLiftMagSwitch;
    DigitalChannel topLiftMagSwitch;

    private boolean touchedBottomMag = false; //used for stopping lift w/ mag switch

    private LinearOpMode opMode;//used to stop autonomous functions when stopped

    //Also to note: The front wheels to the back wheels is 13.5 apart in terms of center distance
    public final int neverrestEncCountsPerRev = 1120; //Based on Nevverest 40 motors
    //public final float roboDiameterCm = (float) (38.7 * Math.PI); // can be adjusted
    public final float wheelCircIn = (float) (Math.PI * 4); //Circumference of wheels used
    //public final float wheelCircCm = (float) (10.168 * Math.PI);//Converted to CM

    public NewRobotFinal(HardwareMap hardwareMap) //Initialize or set values to instance variables or give each variable a speicfic sensor/motor
    {
        liftMotor = hardwareMap.get(DcMotorImplEx.class, "liftMotor");
        //imu = (hardwareMap.get(BNO055IMU.class, "imu"));
       // initIMU();
       // updateIMUValues();

        driveLeftOne = hardwareMap.get(DcMotorImplEx.class, "driveLeftOne");
        driveRightOne = hardwareMap.get(DcMotorImplEx.class, "driveRightOne");

        wingMotor = hardwareMap.get(DcMotorImplEx.class, "wingMotor");

        leftDoorWall = hardwareMap.servo.get("leftDoorWall");
        leftDoorWall.scaleRange(.56f, .91f); //Set range of whhere the servo goes
        rightDoorWall = hardwareMap.servo.get("rightDoorWall");
        rightDoorWall.scaleRange(.11f, .51f);

        wingTouchSens = hardwareMap.digitalChannel.get("wingTouchSens");
        bottomLiftMagSwitch = hardwareMap.digitalChannel.get("bottomLiftMagSwitch");
        topLiftMagSwitch = hardwareMap.digitalChannel.get("topLiftMagSwitch");
        wingTouchSens.setMode(DigitalChannel.Mode.INPUT);//Have those touch sensors take in info
        bottomLiftMagSwitch.setMode(DigitalChannel.Mode.INPUT);
        topLiftMagSwitch.setMode(DigitalChannel.Mode.INPUT);

        resetDriveEncoders();
        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //When power is 0, motor will brake to stop drag
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveRightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        driveLeftOne.setDirection(DcMotorSimple.Direction.FORWARD);

        initEndGame(hardwareMap);
        initMouthAndWings();
    }

    public void initEndGame(HardwareMap hardwareMap)
    {
        tailRelease = hardwareMap.get(DcMotorImplEx.class, "tailRelease");
        tailRelease.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER); //Resets and stop motor
        tailRelease.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER); //Use this after stop and reset encoders to make it run
        tailRelease.setZeroPowerBehavior(DcMotorImplEx.ZeroPowerBehavior.BRAKE);
        tailRelease.setDirection(DcMotorImplEx.Direction.FORWARD);

        grabberRotator = hardwareMap.get(Servo.class, "grabberRotator");
        //grabberRotator.scaleRange(0, 1f);
        //grabberRotator.setPosition(rotatorPos);
        grabber = hardwareMap.get(Servo.class, "grabber");
        //grabber.setPosition(grabberPos);
    }

    public void initAutoFunctions(HardwareMap hardwareMap, LinearOpMode opMode_IN) //Initializes and gives variables values needed for autonomous
    {
        opMode = opMode_IN;
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

    public void initMouthAndWings()
    {
        liftMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        wingMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        wingMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        wingMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        wingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wingMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightDoorWall.setDirection(Servo.Direction.FORWARD);
        leftDoorWall.setDirection(Servo.Direction.FORWARD);
    }
/*
    public void initIMU() //Gives IMU of what to read
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
    //Since we use angles and gravity to get angles, we refresh the values by setting them equal to imu's values
    //Use this wehn using IMU as the values won't change
    public void updateIMUValues()
    {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();
    }

    public void selfBal()
    {
        updateIMUValues();
        double y = gravity.yAccel;
        double z = gravity.zAccel;
        double angle = Math.atan(y/z) * 180/Math.PI;
        //Find angle perpendicular to gravity

        if(angle> 4)
        {
            driveLeftOne.setPower(balPow);
            driveRightOne.setPower(-balPow);
        }
        else if (angle < -4)
        {
            driveLeftOne.setPower(-balPow);
            driveRightOne.setPower(balPow);
        }
        else
        {
            stopDriveMotors();
        }
    }*/

    public char getGlyphCipher()
    {
        //Get current value or picture of what vuforia is seeing
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        //Check if it equals a glyph
        if (vuMark.equals(RelicRecoveryVuMark.CENTER))
            return 'c';
        else if (vuMark.equals(RelicRecoveryVuMark.LEFT))
            return 'l';
        else if (vuMark.equals(RelicRecoveryVuMark.RIGHT))
            return 'r';
        else
            return '?';
    }

    //Use this as a reference http://www.tadpolewebworks.com/web/atomic/images/H02-HSV.jpg
    public float getHueValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        //Above function is used to conert from RGB to HSV
        return hsvValues[0];
    }

    public float getSatValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        return hsvValues[1];
    }

    public float getValueValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        return hsvValues[2];
    }

    public char getColor(ColorSensor in_ColorSens)
    {
        float hue = getHueValue(in_ColorSens);
        float value = getValueValue(in_ColorSens);

        if (value < .06)
            return 'k';
        else if (hue < 71 || hue > 310)
            return 'r';
        else if (hue > 150 && hue < 271)
            return 'b';
        else
            return '?';
    }

    public void driveMotors(double lPow, double rPow)
    {
        driveRightOne.setPower(-rPow);
        driveLeftOne.setPower(lPow);
    }

    public void driveMotorsAuto(double lPow, double rPow)
    {
        driveMotors(-lPow, -rPow);
    }

    private void resetDriveEncoders()
    {
        driveRightOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);//Also stops the motor
        driveLeftOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Allows motor to run after stop
        driveLeftOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveStraight_In(float inches, double pow)
    {
        stopDriveMotors();
        float encTarget = neverrestEncCountsPerRev / wheelCircIn * inches;
        //We calc encoder target by calculating how many encoder counts are needed per inch

        float absPow = (float) Math.abs(pow);
        resetDriveEncoders();

        if (pow < 0) //Check pow is neg
        {
            inches *= -1; //negate inches
        }

        if (inches < 0)//Check if distance is negative
        {
            driveMotorsAuto(-absPow, -absPow); //set power to go backwards
            //Have it check when it reaches encoder target
            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget && !opMode.isStopRequested())
            {

            }
        }
        else
        {
            driveMotorsAuto(absPow, absPow);

            while (driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget && !opMode.isStopRequested())
            {
            }
        }

        stopDriveMotors();
    }


    public void driveStraight_In(float inches)
    {
        driveStraight_In(inches, .75);
    }

    public void driveStraight_In_Stall(float inches, double pow)
    {
        int loops = 0;
        float encTarget = 1120 / wheelCircIn * inches;

        float absPow = (float) Math.abs(pow);
        resetDriveEncoders();

        if (pow < 0)
        {
            inches *= -1;
        }
        if (inches < 0)
        {
            driveMotorsAuto(-absPow, -absPow);

            while ((driveLeftOne.getCurrentPosition() < -encTarget) && (driveRightOne.getCurrentPosition() > encTarget) && !opMode.isStopRequested())
            {
                if (loops > 5 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }
        }
        else
        {
            driveMotorsAuto(absPow, absPow);

            while (driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget && !opMode.isStopRequested())
            {
                if (loops > 5 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }

            stopDriveMotors();
        }
    }

    public void driveStraight_In_Stall(float inches, double pow, Telemetry telemetry)
    {
        if(Math.abs(pow) < .5)
        {
            if(pow >= 0)
                pow = .5;
            else
                pow = -.5;
        }
        double velocitiesR = 0;
        double velocitiesL = 0;
        int loops = 0;
        float encTarget = 1120 / wheelCircIn * inches;

        float absPow = (float) Math.abs(pow);
        resetDriveEncoders();
        if (pow < 0)
        {
            inches *= -1;
        }
        if (inches < 0)
        {
            driveMotorsAuto(-absPow, -absPow);

            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget)
            {
                double rVel =  getDriveRightOne().getVelocity(AngleUnit.DEGREES);
                double lVel = getDriveLeftOne().getVelocity(AngleUnit.DEGREES);
                loops++;
                velocitiesR += rVel;
                velocitiesL += lVel;
                telemetry.addData("RightVel ",rVel);
                telemetry.addData("LeftVel ",lVel);
                telemetry.addData("Average", null);
                telemetry.addData("LAvg ",velocitiesL/loops);
                telemetry.addData("RAvg ",velocitiesR/loops);
                telemetry.update();
                if (loops > 5 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }
        }
        else
        {
            driveMotorsAuto(absPow, absPow);

            while (driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget)
            {
                double rVel =  getDriveRightOne().getVelocity(AngleUnit.DEGREES);
                double lVel = getDriveLeftOne().getVelocity(AngleUnit.DEGREES);
                loops++;
                velocitiesR += rVel;
                velocitiesL += lVel;
                telemetry.addData("RightVel ",rVel);
                telemetry.addData("LeftVel ",lVel);
                telemetry.addData("Average", null);
                telemetry.addData("LAvg ",velocitiesL/loops);
                telemetry.addData("RAvg ",velocitiesR/loops);
                telemetry.update();
                if (loops > 5 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }

            stopDriveMotors();
        }
    }

    public void pivot(float degrees)
    {
        pivot(degrees, defaultTurnPow);
    }

    public void pivot(float degrees, double pow)
    {
        float encTarget = (float) (degrees * 11.79712 - 50.29669);

        resetDriveEncoders();

        if (degrees < 0) //Pivot Clockwise
        {
            driveMotorsAuto(Math.abs(pow), -Math.abs(pow));

            while ((driveLeftOne.getCurrentPosition() > encTarget && driveRightOne.getCurrentPosition() > encTarget) && !opMode.isStopRequested())
            {
            }
        }
        else //CounterClockwise
        {
            driveMotorsAuto(-Math.abs(pow), Math.abs(pow));

            while ((driveLeftOne.getCurrentPosition() < encTarget && driveRightOne.getCurrentPosition() < encTarget) && !opMode.isStopRequested())
            {
            }
        }

        stopDriveMotors();
    }

    public void oldMoveLift(int adjLevels)
    {
        float pow = 1f;
        if (adjLevels + currentLvl < 0)
            return;
        else if (adjLevels + currentLvl > 3)
            return;

        currentLvl += adjLevels;

        if (adjLevels > 0)
        {
            liftMotor.setPower(-Math.abs(pow));
            while (-liftMotor.getCurrentPosition() < liftLevels[currentLvl] && !opMode.isStopRequested())
            {
            }
        } else
        {
            liftMotor.setPower(Math.abs(pow));
            while (-liftMotor.getCurrentPosition() > liftLevels[currentLvl] && !opMode.isStopRequested())
            {
            }
        }

        liftMotor.setPower(0);
    }

    public void moveLift(int adjLevels)
    {
        moveLift(adjLevels, defaultLiftSpeed);
    }

    public void moveLift(int adjLevels, double pow)
    {
        CalcLiftTarget(adjLevels);
        while (liftDir != STOP_L)
        {
            AdjLiftDir();
        }
    }

    public void CalcLiftTarget(int adjLevels)
    {
        final int liftLevels[] = {0, 150, 769, 1500, 1538};
        final int liftDeadzone = 0;

        if (adjLevels + currentLvl < 0)
        {
            currentLvl = 0;
        }
        else if (adjLevels + currentLvl >= liftLevels.length)
        {
            currentLvl = (short) (liftLevels.length - 1);
        }
        else
        {
            currentLvl += adjLevels;
        }

        //Calculate target
        if (adjLevels > 0)
        {
            liftTargetPos = (short) (liftLevels[currentLvl] - liftDeadzone);
            liftDir = UP_L;
        }
        else
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
        else if (liftDir == DOWN_L && -liftMotor.getCurrentPosition() > liftTargetPos)
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
        if(!bottomLiftMagSwitch.getState())
        {
            if (!touchedBottomMag)
            {
                liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
            }
            else
                touchedBottomMag = true;
            if (y > 0)
                y = 0;
            liftDir = STOP_L;
        }
        else
            touchedBottomMag = false;

        if (!topLiftMagSwitch.getState())
        {
            if (y < 0)
                y = 0;

            liftDir = STOP_L;
        }

        if (y > .3)
        {
            liftDir = STOP_L;
            liftMotor.setPower(Math.abs(y * factor));
        }
        else if (y < -.3)
        {
            liftDir = STOP_L;
            liftMotor.setPower(-Math.abs(factor * y));
        }
        else
        {
            AdjLiftDir();
        }
    }

    public void moveXEncoderCounts(int xEnc, float pow, boolean up)
    {
        int initPos = liftMotor.getCurrentPosition();
        if (up)
        {
            liftMotor.setPower(-Math.abs(pow));
            while (-liftMotor.getCurrentPosition() < -initPos + xEnc)
            {
            }
        } else
        {
            liftMotor.setPower(Math.abs(pow));
            while (-liftMotor.getCurrentPosition() > -initPos - xEnc)
            {
            }
        }
        liftMotor.setPower(0);
    }

    public void moveWing(boolean moveDown)
    {
        if (moveDown)
        {
            wingMotor.setPower(-1f);
            while ((wingMotor.getCurrentPosition() > -2725) && (wingTouchSens.getState()) && !opMode.isStopRequested())
            {
                if(!wingTouchSens.getState())
                    break;
            }
        }
        else
        {
            wingMotor.setPower(1f);
            while (((wingMotor.getCurrentPosition() < 0) && (wingTouchSens.getState())) && !opMode.isStopRequested())
            {
                if(!wingTouchSens.getState())
                    break;
            }
        }
        wingMotor.setPower(0);
    }

    public void openOrCloseDoor(boolean close)
    {
        if (close)
        {
            leftDoorWall.setPosition(1f);
            rightDoorWall.setPosition(0f);
        } else
        {
            leftDoorWall.setPosition(0f);
            rightDoorWall.setPosition(1f);
        }

    }

    public void fineAdjDoors(double in) //Note: Check and see if it goes past 0 or 1
    {
        leftDoorPos = leftDoorWall.getPosition() + in;
        rightDoorPos = rightDoorWall.getPosition() - in;
        leftDoorWall.setPosition(leftDoorPos);
        rightDoorWall.setPosition(rightDoorPos);
    }

    public void fineAdjGrabber(float in)
    {
        grabberPos += in;
        if (grabberPos > grabberClosed)
            grabberPos = grabberClosed;
        else if (grabberPos < grabberOpen)
            grabberPos = grabberOpen;

        grabber.setPosition(grabberPos);
    }

    public void fineAdjGrabberRotator(double in)
    {
        rotatorPos += in;
        if (rotatorPos > rotatorDown )
            rotatorPos = rotatorDown;
        else if (rotatorPos < rotatorUp)
            rotatorPos = rotatorUp;

        grabberRotator.setPosition(rotatorPos);
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

    public ColorSensor getleftWingColorSens()
    {
        return leftWingColorSens;
    }

    public ColorSensor getrightWingColorSens()
    {
        return rightWingColorSens;
    }

    public ColorSensor getFloorColorSens()
    {
        return floorColorSens;
    }

    public DcMotorImplEx getDriveLeftOne()
    {
        return driveLeftOne;
    }

    public DcMotorImplEx getDriveRightOne()
    {
        return driveRightOne;
    }

    public DcMotorImplEx getLiftMotor()
    {
        return liftMotor;
    }

    public DcMotorImplEx getWingMotor()
    {
        return wingMotor;
    }

    public Servo getLeftDoorWall()
    {
        return leftDoorWall;
    }

    public Servo getRightDoorWall()
    {
        return rightDoorWall;
    }

    public Servo getGrabber()
    {
        return grabber;
    }

    public Servo getGrabberRotator()
    {
        return grabberRotator;
    }

    public DcMotorImplEx getTailRelease()
    {
        return tailRelease;
    }

    public short getLiftDir()
    {
        return liftDir;
    }

    public DigitalChannel getWingTouchSens(){return wingTouchSens;}

    public DigitalChannel getBottomLiftMagSwitch(){return bottomLiftMagSwitch;}

    public DigitalChannel getTopLiftMagSwitch(){return topLiftMagSwitch;}
}