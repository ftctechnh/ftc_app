package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;

/**
 * Created by Noah on 12/6/2016.
 */

public final class PushyLib {

    //function which handles pushing of beacons
    //assumes robot is in front of the beacon

    public static class pushypushy extends AutoLib.LinearSequence{

        public pushypushy(OpMode mode, DcMotor[] motors, BotHardwareOld.MuxColor leftSensor, BotHardwareOld.MuxColor rightSensor, Servo leftServo, Servo rightServo,
                          double pushPos, double time, boolean red, int colorThresh, float drivePower, float driveTime, int maxDriveLoop){
            //run color detection and pushing
            this.add(new pushyDetect(mode, motors, leftSensor, rightSensor, leftServo, rightServo, pushPos, time, red, colorThresh, drivePower, driveTime, maxDriveLoop));

            //pull servos back to default position
            AutoLib.ConcurrentSequence servoDetract = new AutoLib.ConcurrentSequence();
            servoDetract.add(new AutoLib.TimedServoStep(leftServo, leftServo.getPosition(), time, false));
            servoDetract.add(new AutoLib.TimedServoStep(rightServo, rightServo.getPosition(), time, false));
            this.add(servoDetract);
        }

    }

    private static class pushyDetect extends AutoLib.Step {
        DcMotor[] mMotors;
        BotHardwareOld.MuxColor mLeftSensor;
        BotHardwareOld.MuxColor mRightSensor;
        Servo mLeftServo;
        Servo mRightServo;
        final double mPushPos;
        AutoLib.Timer mTime;
        final boolean mRed;
        final int mColorThresh;
        final float mDrivePower;
        final float mDriveTime;
        boolean mLeft;
        int mMaxDriveLoop;
        OpMode mMode;

        public pushyDetect(OpMode mode, DcMotor[] motors, BotHardwareOld.MuxColor leftSensor, BotHardwareOld.MuxColor rightSensor, Servo leftServo, Servo rightServo,
                           double pushPos, double time, boolean red, int colorThresh, float drivePower, float driveTime, int maxDriveLoop){
            //You know what, I think I'm missing some variables
            //oh never mind here they are
            mMotors = motors;
            mLeftSensor = leftSensor;
            mRightSensor = rightSensor;
            mLeftServo = leftServo;
            mRightServo = rightServo;
            mPushPos = pushPos;
            mTime = new AutoLib.Timer(time);
            mRed = red;
            mColorThresh = colorThresh;
            mDrivePower = drivePower;
            mDriveTime = driveTime;
            mMaxDriveLoop = maxDriveLoop;
            mMode = mode;
        }

        public boolean loop(){
            super.loop();
            if(firstLoopCall()){
                //compare sensor values
                if(mRed){
                    mLeft = mLeftSensor.red() > mRightSensor.red();
                }
                else{
                    mLeft = mLeftSensor.blue() > mRightSensor.blue();
                }

                if(mMode != null){
                    mMode.telemetry.addData("Left Red", mLeftSensor.red());
                    mMode.telemetry.addData("Right Red", mRightSensor.red());
                    mMode.telemetry.addData("Left Blue", mLeftSensor.blue());
                    mMode.telemetry.addData("Right Blue", mRightSensor.blue());
                }

                //if left side is color, push left, else push right
                if(mLeft) mLeftServo.setPosition(mPushPos);
                else mRightServo.setPosition(mPushPos);

                //start servo timer
                mTime.start();
            }

            if(mTime.done()){
                //check to make sure the pushy worked
                boolean done = false;

                if(mMaxDriveLoop == 1) done = false;
                else if(!mRed){
                    if(Math.abs(mLeftSensor.red() - mRightSensor.red()) < mColorThresh) done = true;
                }
                else{
                    if(Math.abs(mLeftSensor.blue() - mRightSensor.blue()) < mColorThresh) done = true;
                }

                //if it's done, stop the motors
                if(done || mMaxDriveLoop <= 0){
                    for (int i = 0; i < mMotors.length; i++) {
                        mMotors[i].setPower(0);
                    }
                    return true;
                }

                //else, clearly it hasn't, so we run the smash-n-push <code></code>

                mMaxDriveLoop--;

                for (int i = 0; i < mMotors.length; i++) {
                    mMotors[i].setPower(mDrivePower);
                }

                mTime = new AutoLib.Timer(mDriveTime);
                mTime.start();
            }

            return false;
        }
    }
}
