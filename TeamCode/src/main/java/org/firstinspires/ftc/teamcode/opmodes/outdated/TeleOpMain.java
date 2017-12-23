/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode.opmodes.outdated;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.SquirrelyLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;

@TeleOp(name = "Ye Olde TeleOpMain", group = "Main")
@Disabled
public class TeleOpMain extends OpMode {

    BotHardwareOld robot = new BotHardwareOld();

    boolean lastLeftBumperState = false;
    boolean lastRightBumperState = false;
    boolean lastAButtonState = false;
    boolean lastBButtonState = false;
    boolean isShooting = false;

    boolean isBacon = false;
    double leftPusherState = -1.0;
    double rightPusherState = -1.0;

    double mLastTime;
    double mLastEncoderCount;
    double mMaxEncoderCount = 0;
    double mLastAccelX = 0;
    double mLastAccelY = 0;

    double powerMult = 1.0;

    AutoLib.Sequence mShoot;
    AutoLib.Sequence mBeacon;

    private void initShoot(){
        mShoot = new AutoLib.LinearSequence();

        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0,  1500, true, this));
        mShoot.add(new AutoLib.TimedServoStep(robot.ballServo, 0.5, 0.4, true));
        //mShoot.add(new AutoLib.LogTimeStep(this, "WAIT", 0.2));
    }

    private void initBeacon() {
        final float Kp = 0.20f;
        final float Ki = 0.00f;
        final float Kd = 0.00f;
        final float KiCut = 3.0f;

        // parameters of the PID controller for the ultrasonic sensor driving
        final float Kp4 = 0.025f;
        final float Ki4 = 0.00f;
        final float Kd4 = 0;
        final float Ki4Cutoff = 0.00f;

        SensorLib.PID mHeadingPid = new SensorLib.PID(Kp, Ki, Kd, KiCut);
        SensorLib.PID mUltraPid = new SensorLib.PID(Kp4, Ki4, Kd4, Ki4Cutoff);

        //mBeacon = new AutoLib.LinearSequence();

        //mBeacon.add(new AutoLib.GyroTurnStep(this, 0, new UltraHeading(robot.distSensorLeft, robot.distSensorRight), robot.getMotorArray(), 1.0f, 5.0f, true));
        //mBeacon.add(new LineDrive.UltraSquirrleyAzimuthFinDriveStep(this, 0, 0, new UltraHeading(robot.distSensorLeft, robot.distSensorRight), new LineDrive.UltraCorrectedDisplacement(this, robot.distSensorLeft, 15), mHeadingPid, mUltraPid,
        //    robot.getMotorArray(), 1.0f, new LineDrive.UltraSensors(robot.distSensorLeft, 15, 3.0f), true));

        AutoLib.Sequence mPush = new AutoLib.ConcurrentSequence();

        mPush.add(new AutoLib.TimedServoStep(robot.leftServo, 1.0, 0.2, false));
        mPush.add(new AutoLib.TimedServoStep(robot.rightServo, 1.0, 0.2, false));

        mBeacon.add(mPush);
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // hardware maps
        robot.initTeleop(this, false);
        robot.setMaxSpeedAll(2500);

        robot.leftServo.setPosition(leftPusherState);
        robot.rightServo.setPosition(rightPusherState);

        initShoot();
    }

    //@Override
    //public void init_loop(){
    //    telemetry.addData("NavX Ready", robot.startNavX());
    //}

    @Override
    public void start() {
        mLastTime = getRuntime();
        mLastEncoderCount = robot.backLeftMotor.getCurrentPosition();
    }

    @Override
    public void loop() {
        /*
        if(mEncoderMeasure.done()) {
            double encode = robot.frontRightMotor.getCurrentPosition() - lastEncode;
            avgEncode = (avgEncode + encode)/2.0;
            telemetry.addData("Encoder Current", robot.frontRightMotor.getCurrentPosition());
            telemetry.addData("Encoders Per Second Current", encode);
            telemetry.addData("Encoders Per Second Average", avgEncode);

            lastEncode = robot.frontRightMotor.getCurrentPosition();
            mEncoderMeasure.start();
        }

        if (gamepad2.a) {
            //automation
            if(gamepad2.a && !isShooting){
                initShoot();
            }
            if(gamepad2.a || isShooting){
                robot.sweeperMotor.setPower(-1.0);
                if(mShoot.loop()){
                    isShooting = false;
                    initShoot();
                }
                else isShooting = true;
            }
        }
        else if (gamepad1.x){
            //automation
            if(gamepad1.x && !isBacon){
                initBeacon();
            }
            if(gamepad1.x || isBacon){
                if(mBeacon.loop()){
                    isBacon = false;
                    initBeacon();
                }
                else isBacon = true;
            }
        }

        else{
        */
            isShooting = false;
            /*

                             1,1,1,1
                                |
                     0,0,1,1    |    1,1,0,0
                                |
                -1,-1,1,1 ------------- 1,1,-1,-1
                                |
                   -1,-1,0,0    |    0,0,-1,-1
                                |
                           -1,-1,-1,-1

            float x = gamepad1.left_stick_x;
            float y = -1 * gamepad1.left_stick_y;

            float ySign = (y >= 0) ? 1 : -1;

            float frontPower = (ySign * x > 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);
            float backPower = (ySign * x < 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);

            */


            int heading = -1;
            // run drivetrain motors
            // dpad steering
        /*
            if(gamepad1.dpad_up && gamepad1.dpad_left) {
                heading = 45;
                //robot.setFrontPower(0.0);
                //robot.setBackPower(1.0);
            }
            else if(gamepad1.dpad_up && gamepad1.dpad_right) {
                heading = -45;
                //robot.setFrontPower(1.0);
                //robot.setBackPower(0.0);
            }
            else if(gamepad1.dpad_down && gamepad1.dpad_left) {
                heading = 135;
                //robot.setFrontPower(-1.0);
                //robot.setBackPower(0.0);
            }
            else if(gamepad1.dpad_down && gamepad1.dpad_right) {
                heading = -135;
                //robot.setFrontPower(0.0);
                //robot.setBackPower(-1.0);
            }
            */
            if(gamepad1.dpad_up) {
                heading = 0;
                //robot.setFrontPower(1.0);
                //robot.setBackPower(1.0);
            }
            else if(gamepad1.dpad_left) {
                heading = 90;
                //robot.setFrontPower(-1.0);
                //robot.setBackPower(1.0);
            }
            else if(gamepad1.dpad_right) {
                heading = -90;
                //robot.setFrontPower(1.0);
                //robot.setBackPower(-1.0);
            }
            else if(gamepad1.dpad_down) {
                heading = 180;
                //robot.setFrontPower(-1.0);
                //robot.setBackPower(-1.0);
            }
            else {
                // joystick tank steering
                robot.frontLeftMotor.setPower((-gamepad1.left_stick_y) * powerMult);
                robot.frontRightMotor.setPower((-gamepad1.right_stick_y) * powerMult);
                robot.backLeftMotor.setPower((-gamepad1.left_stick_y) * powerMult);
                robot.backRightMotor.setPower((-gamepad1.right_stick_y) * powerMult);
            }

            if(heading != -1){
                //if(!robot.isReversed()) heading = -heading;
                SquirrelyLib.MotorPowers mp = SquirrelyLib.GetSquirrelyWheelMotorPowers(heading);
                robot.setFrontPower(mp.Front());
                robot.setBackPower(mp.Back());
            }

            // run lifter motor
            if(gamepad1.left_trigger > 0.2 || gamepad2.left_trigger > 0.2) {
                robot.sweeperMotor.setPower(-1.0);
            }
            else if(gamepad1.right_trigger > 0.2 || gamepad2.right_trigger > 0.2) {
                robot.sweeperMotor.setPower(1.0);
            }
            else {
                robot.sweeperMotor.setPower(0.0);
            }

            // run launcher motor

            if(gamepad2.x) {
                robot.launcherMotor.setPower(1.0);
            }
            else {
                robot.launcherMotor.setPower(0.0);
            }

            if(gamepad2.b) {
                robot.ballServo.setPosition(0.6);
            }
            else {
                robot.ballServo.setPosition(0.0);
            }

            //halfspeed
            if(gamepad1.left_bumper || gamepad1.right_bumper){
                powerMult = 0.5;
            }
            else {
                powerMult = 1.0;
            }

            // toggle button pushers
            if(!lastBButtonState && gamepad1.b) {
                leftPusherState *= -1.0;
                rightPusherState *= -1.0;
            }

            robot.leftServo.setPosition(leftPusherState);
            robot.rightServo.setPosition(rightPusherState);

            lastBButtonState = gamepad1.b;

            //lastLeftBumperState = gamepad1.left_bumper;
            //lastRightBumperState = gamepad1.right_bumper;

            //toggle reversed steering
            if(!lastAButtonState && gamepad1.a) {
                robot.initMotors(this, false, !robot.isReversed());
                if(robot.isReversed()){
                    robot.dim.setLED(0, true);
                    telemetry.addData("Reversed", "True");
                }
                else{
                    robot.dim.setLED(0, false);
                    telemetry.addData("Reversed", "False");
                }
            }
            lastAButtonState = gamepad1.a;

        //some fun telemetery
        /*
        final double dt = getRuntime() - mLastTime;
        final double jerkX = (robot.navX.getWorldLinearAccelX() - mLastAccelX) / dt;
        final double jerkY = (robot.navX.getWorldLinearAccelY() - mLastAccelY) / dt;
        final double vel = (robot.backLeftMotor.getCurrentPosition() - mLastEncoderCount) / dt;
        if(mMaxEncoderCount < vel) mMaxEncoderCount = vel;
        mLastEncoderCount = robot.backLeftMotor.getCurrentPosition();
        mLastAccelX = robot.navX.getWorldLinearAccelX();
        mLastAccelY = robot.navX.getWorldLinearAccelY();
        telemetry.addData("X Axis Accel", robot.navX.getWorldLinearAccelX());
        telemetry.addData("Y Axis Accel", robot.navX.getWorldLinearAccelY());
        telemetry.addData("X Axis Jerk", jerkX);
        telemetry.addData("Y Axis Jerk", jerkY);
        telemetry.addData("Encode per second", vel);
        telemetry.addData("Max Encode per second", mMaxEncoderCount);
        */
        //}
    }

    public static class UltraHeading implements HeadingSensor {
        UltrasonicSensor mLeft;
        UltrasonicSensor mRight;

        UltraHeading(UltrasonicSensor left, UltrasonicSensor right){
            mLeft = left;
            mRight = right;
        }

        public float getHeading(){
            return (float)(mRight.getUltrasonicLevel() - mLeft.getUltrasonicLevel());
        }

    }

    private static class JerkCorrect {
        OpMode mMode;
        SensorLib.PID mPid;
        DcMotor mMotor;
        AHRS mAccel;
        double mThresh;
        double mLastEncoderCount = 0;
        double mLastTime = 0;
        double mLastAccel;

        JerkCorrect(OpMode mode, DcMotor motor, AHRS accel, double thresh, SensorLib.PID pid){
            mMode = mode;
            mMotor = motor;
            mAccel = accel;
            mPid = pid;
            mThresh = thresh;
        }

        public double loopCorrection(){
            if(mLastEncoderCount == 0){
                mLastEncoderCount = mMotor.getCurrentPosition();
                mLastTime = mMode.getRuntime();
                mLastAccel = mAccel.getRawAccelY();
            }

            //calculate delta time
            final double dt = mMode.getRuntime() - mLastTime;
            mLastTime = mMode.getRuntime();
            //calculate current velocity
            final double vel = (mMotor.getCurrentPosition() - mLastEncoderCount) / dt;
            mLastEncoderCount = mMotor.getCurrentPosition();
            //calculate current jerk, negative and y axis due to sensor mounting
            final double jerk = -(mAccel.getWorldLinearAccelY() - mLastAccel) / dt;
            mLastAccel = mAccel.getWorldLinearAccelY();


            //check if jerk is beyond thresh
            if(Math.abs(jerk) > mThresh){
                //attempt to correct speed towards actual value if so
                //final double error = vel - (mMotor.getMaxSpeed() * mMotor.getPower());
                final double error = vel - mMotor.getPower();
                //and return the correction
                return -mPid.loop((float)error, (float)dt);
            }

            //else return 0
            return 0;
        }
    }

    public static float[] getCorrectedSquirrleyMotorPowers(float dt, float mDirection, float mHeading, HeadingSensor mGyro, SensorLib.PID mPid, float mPower) {
        final float heading = mGyro.getHeading();     // get latest reading from direction sensor
        // convention is positive angles CCW, wrapping from 359-0

        final float error = SensorLib.Utils.wrapAngle(heading - mHeading);   // deviation from desired heading
        // deviations to left are positive, to right are negative

        // feed error through PID to get motor power correction value
        final float correction = -mPid.loop(error, dt);

        //calculate motor powers for fancy wheels
        SquirrelyLib.MotorPowers mp = SquirrelyLib.GetSquirrelyWheelMotorPowers(mDirection);

        final float leftPower = correction;
        final float rightPower = -correction;

        //fr, br, fl, bl
        final float[] ret = {
                (rightPower + (float)mp.Front()) * mPower,
                (rightPower + (float)mp.Back()) * mPower,
                (leftPower + (float)mp.Front()) * mPower,
                (leftPower + (float)mp.Back()) * mPower};

        return ret;
    }
}
