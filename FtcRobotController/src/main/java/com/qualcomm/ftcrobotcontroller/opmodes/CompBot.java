package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.sql.Time;

/**
 * Created by Peter on 12/3/2015.
 */
public class CompBot implements DriverInterface, AttachmentInterface
{
    static DcMotor leftMotor;
    static DcMotor rightMotor;
    static DcMotor frontLeftMotor;
    static DcMotor frontRightMotor;
    static DcMotor backLeftMotor;
    static DcMotor backRightMotor;
    static DcMotor grabberMotor;
    static DcMotor winchMotor;
    static Servo diverterServo;
    static Servo dispenserServo;
    static Servo dispenserFlipperServo;
    static Servo tapeMeasureServo;
    static Servo climberReleaseServo;
    private static final String frontLeftMotorName = "frontLeft";
    private static final String frontRightMotorName = "frontRight";
    private static final String backLeftMotorName = "backLeft";
    private static final String backRightMotorName = "backRight";
    private static final String leftMotorName = "left";
    private static final String rightMotorName = "right";
    private static final String grabberMotorName = "grabber";
    private static final String winchMotorName = "winch";
    private static final String diverterServoName = "diverter";
    private static final String dispenserServoName = "dispenser";
    private static final String dispenserFlipperServoName = "dispenserFlipper";
    private static final String tapeMeasureServoName = "tapeMeasure";
    private static final String climberReleaseServoName = "climberRelease";
    //PhoneGyrometer gyro;
    boolean vendDeploy = false;
    boolean buttonArm = true;
    boolean speedSwitch = false;
    boolean buttonReady = true;
    boolean switchSides = false;
    boolean buttonStatus = true;


    public DcMotor getLeftMotor()
    {
        return leftMotor;
    }

    public DcMotor getRightMotor()
    {
        return rightMotor;
    }
    public DcMotor getFrontLeftMotor()
    {
        return frontLeftMotor;
    }
    public DcMotor getFrontRightMotor()
    {
        return frontRightMotor;
    }
    public DcMotor getBackLeftMotor()
    {
        return backLeftMotor;
    }
    public DcMotor getBackRightMotor()
    {
        return backRightMotor;
    }
    public DcMotor getGrabberMotor()
    {
        return  grabberMotor;
    }
    public DcMotor getWinchMotor()
    {
        return winchMotor;
    }
    public Servo getDiverterServo()
    {
        return diverterServo;
    }
    public Servo getDispenserServo()
    {
        return dispenserServo;
    }
    public Servo getDispenserFlipperServo()
    {
        return dispenserFlipperServo;
    }
    public Servo getTapeMeasureServo()
    {
        return tapeMeasureServo;
    }
    public Servo getClimberReleaseServo()
    {
        return climberReleaseServo;
    }

    public boolean flipBool(boolean bool)
    {
        return bool != bool;
    }

    public CompBot(HardwareMap hardwareMap)
    {
       // gyro = new PhoneGyrometer(hardwareMap);
        leftMotor = hardwareMap.dcMotor.get(leftMotorName);
        rightMotor = hardwareMap.dcMotor.get(rightMotorName);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor = hardwareMap.dcMotor.get(frontLeftMotorName);
        frontRightMotor = hardwareMap.dcMotor.get(frontRightMotorName);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor = hardwareMap.dcMotor.get(backLeftMotorName);
        backRightMotor = hardwareMap.dcMotor.get(backRightMotorName);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        grabberMotor = hardwareMap.dcMotor.get(grabberMotorName);
        grabberMotor.setDirection(DcMotor.Direction.REVERSE);
        winchMotor = hardwareMap.dcMotor.get(winchMotorName);
        diverterServo = hardwareMap.servo.get(diverterServoName);
        dispenserServo = hardwareMap.servo.get(dispenserServoName);
        dispenserFlipperServo = hardwareMap.servo.get(dispenserFlipperServoName);
        tapeMeasureServo = hardwareMap.servo.get(tapeMeasureServoName);
        climberReleaseServo = hardwareMap.servo.get(climberReleaseServoName);
        dispenserServo.setPosition(0.5f);
        dispenserFlipperServo.setPosition(1.0f);
        tapeMeasureServo.setPosition(0.5f);
        climberReleaseServo.setPosition(1.0f);
        diverterServo.setPosition(0.5f);
    }

    @Override
    public void releaseClimbers() {

    }

    @Override
    public void pushButton(boolean isButtonLeft) {

    }

    @Override
    public void moveStraightEncoders(float inches, float speed)
    {
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //makes sure the encoders reset
        while(leftMotor.getCurrentPosition() != 0){}
        while(rightMotor.getCurrentPosition() != 0){}
        /*leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftMotor.setTargetPosition((int)(131.65432 * inches));
        rightMotor.setTargetPosition((int)(131.65432 * inches));*///previous code to run to set positions with setTargetPosition function
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        if(speed < 0)
        {
            speed*=-1.0;
            inches*=-1.0;
        }
        int encoderTarget = ((int)(123.42844 * inches));
        if(encoderTarget > 0)
        {
            leftMotor.setPower(speed);
            rightMotor.setPower(speed);
            while (leftMotor.getCurrentPosition() < encoderTarget) {}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
        else
        {
            leftMotor.setPower(-speed);
            rightMotor.setPower(-speed);
            while (leftMotor.getCurrentPosition() > encoderTarget) {}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
    }

    @Override
    public void pivotTurn(float degrees, float speed)
    {

            leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            //makes sure the encoders reset
            while (leftMotor.getCurrentPosition() != 0) {
            }
            while (rightMotor.getCurrentPosition() != 0) {
            }
                /*leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                leftMotor.setTargetPosition((int)(131.65432 * inches));
                rightMotor.setTargetPosition((int)(131.65432 * inches));*///previous code to run to set positions with setTargetPosition function
            leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);//15.75 inc diameter
            int encoderPivotTarget = ((int) (123.42844 * 148.44024 * degrees / 360));//148.44024 is different than the calculation of the original turn diameter due to the robot jiggling and turning only half of its intended turn
            if (encoderPivotTarget > 0 && speed > 0) {
                rightMotor.setPower(speed);
                while (rightMotor.getCurrentPosition() < encoderPivotTarget) {
                }
                rightMotor.setPower(0.0f);
            } else if (encoderPivotTarget < 0 && speed > 0) {
                leftMotor.setPower(speed);
                while (leftMotor.getCurrentPosition() < (encoderPivotTarget * -1)) {
                }
                leftMotor.setPower(0.0f);
            } else if (encoderPivotTarget > 0 && speed < 0) {
                leftMotor.setPower(speed);
                while (leftMotor.getCurrentPosition() > encoderPivotTarget * -1) {
                }
                leftMotor.setPower(0.0f);
            } else if (encoderPivotTarget < 0 && speed < 0) {
                rightMotor.setPower(speed);
                while (rightMotor.getCurrentPosition() > (encoderPivotTarget)) {
                }
                rightMotor.setPower(0.0f);
            }


           /* if(degrees > 0)
            {
                float startGyro = gyro.getPitch();
                float endGyro = startGyro + degrees;
                leftMotor.setPower(speed);
                while(gyro.getPitch() < endGyro)
                {

                }
                leftMotor.setPower(0.0f);
            }
            if(degrees < 0)
            {
                float startGyro = gyro.getPitch();
                float endGyro = startGyro - degrees;
                rightMotor.setPower(speed);
                while(gyro.getPitch() > endGyro)
                {

                }
                rightMotor.setPower(0.0f);
            }*/

    }

    @Override
    public void spinOnCenter(float degrees, float speed)
    {

            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
            leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            //makes sure the encoders reset
            while (leftMotor.getCurrentPosition() != 0) {
            }
            while (rightMotor.getCurrentPosition() != 0) {
            }
            leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            int encoderSpinTarget = ((int) (123.42844 * 98.96 * degrees / 360));//98.96 is different than the calculation of the original turn diameter due to the robot jiggling and turning only half of its intended tur
            if (encoderSpinTarget > 0 && speed > 0) {
                leftMotor.setPower(speed);
                rightMotor.setPower(-speed);
                while (leftMotor.getCurrentPosition() < encoderSpinTarget) {
                }
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            } else if (encoderSpinTarget > 0 && speed < 0) {
                leftMotor.setPower(speed);
                rightMotor.setPower(-speed);
                while (rightMotor.getCurrentPosition() < encoderSpinTarget) {
                }
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            } else if (encoderSpinTarget < 0 && speed > 0) {
                leftMotor.setPower(-speed);
                rightMotor.setPower(speed);
                while (rightMotor.getCurrentPosition() < -encoderSpinTarget) {
                }
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            } else if (encoderSpinTarget < 0 && speed < 0) {
                leftMotor.setPower(-speed);
                rightMotor.setPower(speed);
                while (leftMotor.getCurrentPosition() < -encoderSpinTarget) {
                }
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            }


            /*if(degrees > 0)
            {
                float startGyro = gyro.getPitch();
                float endGyro = startGyro + degrees;
                leftMotor.setPower(speed);
                rightMotor.setPower(-speed);
                while(gyro.getPitch() < endGyro)
                {

                }
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            }
            if(degrees < 0)
            {
                float startGyro = gyro.getPitch();
                float endGyro = startGyro - degrees;
                rightMotor.setPower(speed);
                leftMotor.setPower(-speed);
                while(gyro.getPitch() > endGyro)
                {

                }
                rightMotor.setPower(0.0f);
                leftMotor.setPower(0.0f);
            }*/

    }
    public void checkAndFlipDispenserServo(boolean button) {
        if (button) {
            vendDeploy ^= buttonArm;
            buttonArm = false;
        } else {
            buttonArm = true;
        }
        if (vendDeploy)
        {
            getDispenserFlipperServo().setPosition(0.0f);
        }
        else
        {
            getDispenserFlipperServo().setPosition(1.0f);
        }
    }

    public void toggleRotorSpeed(boolean button, float speed) {
        if (button) {
            speedSwitch ^= buttonReady;
            buttonReady = false;
        } else {
            buttonReady = true;
        }
        if (speedSwitch)
        {
            speed = 0.85f;
        }
        else
        {
            speed = 0.65f;
        }
    }

    public void toggleSides(boolean button, boolean correct) {
        if (button) {
            switchSides ^= buttonStatus;
            buttonStatus = false;
        } else {
            buttonStatus = true;
        }
        if (switchSides)
        {
            correct = false;
        }
        else
        {
            correct = true;
        }
    }


    @Override
    public void pitchAllTracks(float time, float speed)
    {
        double timeStart = System.currentTimeMillis();
        double timeEnd = timeStart + (1000 * time);
        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);
        while (System.currentTimeMillis() < timeEnd)
        {

        }
        frontLeftMotor.setPower(0.0f);
        frontRightMotor.setPower(0.0f);
        backLeftMotor.setPower(0.0f);
        backRightMotor.setPower(0.0f);

    }

    @Override
    public void pitchAllTracksAndMoveRobot(float time, float speed)
    {
        double timeStart = System.currentTimeMillis();
        double timeEnd = timeStart + (1000 * time);
        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
        while (System.currentTimeMillis() < timeEnd)
        {

        }
        frontLeftMotor.setPower(0.0f);
        frontRightMotor.setPower(0.0f);
        backLeftMotor.setPower(0.0f);
        backRightMotor.setPower(0.0f);
        leftMotor.setPower(0);
        rightMotor. setPower(0);
    }

    @Override
    public void stop()
    {
        leftMotor.setPower(0.0f);
        rightMotor.setPower(0.0f);
    }

    @Override
    public void pitchFrontTracks(float time, float speed)
    {
        double timeStart = System.currentTimeMillis();
        double timeEnd = timeStart + (1000 * time);
        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        while (System.currentTimeMillis() < timeEnd)
        {

        }
        frontLeftMotor.setPower(0.0f);
        frontRightMotor.setPower(0.0f);
    }

    @Override
    public void pitchBackTracks(float time, float speed)
    {
        double timeStart = System.currentTimeMillis();
        double timeEnd = timeStart + (1000 * time);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);
        while (System.currentTimeMillis() < timeEnd)
        {

        }
        backLeftMotor.setPower(0.0f);
        backRightMotor.setPower(0.0f);
    }

    public void kill()
    {
        rightMotor.close();
        leftMotor.close();
        frontLeftMotor.close();
        frontRightMotor.close();
        backLeftMotor.close();
        backRightMotor.close();
        grabberMotor.close();
        winchMotor.close();
        diverterServo.close();
        dispenserFlipperServo.close();
        dispenserServo.close();
        tapeMeasureServo.close();
        climberReleaseServo.close();
    }

}
