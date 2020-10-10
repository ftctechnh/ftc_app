package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

/**
 * library of utility classes supporting sensor inputs
 * Created by phanau on 1/1/16.
 */

public class SensorLib {


    public static class Utils {

        // wrap given angle into range (-180 .. +180)
        public static float wrapAngle(float angle) {
            while (angle > 180)
                angle -= 360;
            while (angle < -180)
                angle += 360;
            return angle;
        }

        // wrap given angle into range (-180 .. +180)
        public static double wrapAngle(double angle) {
            while (angle > 180)
                angle -= 360;
            while (angle < -180)
                angle += 360;
            return angle;
        }

    }

    public static class PID {

        private float mPrevError = 0;
        private float mIntegral = 0;
        float mKp = 0;
        float mKi = 0;
        float mKd = 0;
        float mKiCutoff = 0;     // max error value for which we integrate error over time

        public PID(float Kp, float Ki, float Kd, float KiCutoff) {
            setK(Kp, Ki, Kd, KiCutoff);
        }

        // set the parameters of the filter
        public void setK(float Kp, float Ki, float Kd, float KiCutoff)
        {
            // set filter coefficients
            mKp = Kp;
            mKi = Ki;
            mKd = Kd;

            // set threshold for errors we integrate -- integrating large errors causes instability
            mKiCutoff = KiCutoff;
        }

        // run one cycle of the PID filter given current error and delta-time since the previous call
        public float loop(float error, float dt) {
            if (Math.abs(error) < mKiCutoff)      // only integrate small errors to avoid wild over-correction
                mIntegral += error*dt;
            float derivative = (dt > 0) ? (error - mPrevError)/dt : 0;
            float output = mKp*error + mKi*mIntegral + mKd*derivative;
            mPrevError = error;
            return output;
        }

    }

    // handle interactive adjustment of PID parameters using controller
    public static class PIDAdjuster {
        OpMode mOpMode;
        PID mPID;
        Gamepad mGamepad;

        public PIDAdjuster(OpMode opmode, PID pid, Gamepad gamepad) {
            mOpMode = opmode;
            mPID = pid;
            mGamepad = gamepad;
        }

        public boolean loop() {
            // adjust PID parameters by joystick inputs - use thresholds to reduce cross-axis changes
            mPID.mKp -= (Math.abs(mGamepad.left_stick_y) < 0.1f ? 0 : mGamepad.left_stick_y * 0.0001f);
            mPID.mKi -= (Math.abs(mGamepad.right_stick_y) < 0.1f ? 0 : mGamepad.right_stick_y * 0.0001f);
            mPID.mKd += (Math.abs(mGamepad.left_stick_x) < 0.1f ? 0 : mGamepad.left_stick_x* 0.0001f);
            mPID.mKiCutoff += (Math.abs(mGamepad.right_stick_x) < 0.1f ? 0 : mGamepad.right_stick_x * 0.01f);

            // log updated values to the operator's console
            if (mOpMode != null) {
                mOpMode.telemetry.addData("Kp = ", mPID.mKp);
                mOpMode.telemetry.addData("Ki = ", mPID.mKi);
                mOpMode.telemetry.addData("Kd = ", mPID.mKd);
                mOpMode.telemetry.addData("KiCutoff = ", mPID.mKiCutoff);
            }
            return true;
        }
    }

    // class that tries to correct systemic errors in ModernRoboticsI2cGyro output
    public static class CorrectedGyro implements HeadingSensor {

        GyroSensor mGyro;            // the underlying physical Gyro
        float mCorrection = (360.0f/376.0f);    // default correction factor = ~16 degrees per revolution
        float mHeadingOffset = 0;

        public CorrectedGyro(GyroSensor gyro)
        {
            // remember the physical gyro we're using
            mGyro = gyro;
        }

        public void calibrate()
        {
            // start a calibration sequence on the gyro and wait for it to finish
            mGyro.calibrate();
            while (mGyro.isCalibrating()) {
                try {Thread.sleep(100);}
                catch (Exception e) {}
            }

            // reset the on-board z-axis integrator and wait for it to zero
            mGyro.resetZAxisIntegrator();
            while (mGyro.getHeading() != 0);
        }

        // override the default correction factor if your gyro is different
        public void setCorrection(float corr)
        {
            mCorrection = corr;
        }

        // implements HeadingSensor interface
        public float getHeading()
        {
            // since the physical gyro appears to have a small (~5%) error in the angles it reports,
            // we scale the cumulative integrated Z reported by the gyro and use that integrated Z to compute "heading".

            float intZ = mGyro.getHeading();     // our convention is positive CCW (right hand rule) -- need to check actual output <<< ???
            intZ *= mCorrection;                    // apply corrective scaling factor (empirically derived by testing)
            float heading = Utils.wrapAngle(intZ + mHeadingOffset);  // add angle offset and wrap to [-180..180) range

            return heading;         // unlike Gyro interface, we return this as float, not int
        }

        public float getHeadingVelocity()
        {
            return 0;
        }

        public boolean haveHeading()
        {
            return !mGyro.isCalibrating();  // data is always available from this device once it's calibrated
        }

        public void setHeadingOffset(float offset) { mHeadingOffset = offset; }

        public void stop()
        {
            mGyro.close();
        }
    }

    // class that tries to correct systemic errors in ModernRoboticsI2cGyro output
    public static class CorrectedMRGyro implements HeadingSensor {

        ModernRoboticsI2cGyro mGyro;            // the underlying physical MR Gyro
        float mCorrection = (360.0f/376.0f);    // default correction factor = ~16 degrees per revolution
        float mHeadingOffset = 0;

        public CorrectedMRGyro(ModernRoboticsI2cGyro gyro)
        {
            // remember the physical gyro we're using
            mGyro = gyro;
        }

        public void calibrate()
        {
            // start a calibration sequence on the gyro and wait for it to finish
            mGyro.calibrate();
            while (mGyro.isCalibrating()) {
                try {Thread.sleep(100);}
                catch (Exception e) {}
            }

            // reset the on-board z-axis integrator and wait for it to zero
            mGyro.resetZAxisIntegrator();
            while (mGyro.getIntegratedZValue() != 0);
        }

        // override the default correction factor if your gyro is different
        public void setCorrection(float corr)
        {
            mCorrection = corr;
        }

        public float getIntegratedZValue()
        {
            // return the raw (uncorrected) integrated Z value from the underlying physical gyro

            /* from MR Sensor Documentation:
            Integrated Z Value:
            The integrated gyro Z value returns the current value obtained by integrating the Z axis rate value, adjusted by the Z axis offset continuously.
            This value can also be used as a signed heading value where CW is in the positive direction and CCW is in the negative direction.
            */

            return mGyro.getIntegratedZValue();
        }

        // implements HeadingSensor interface
        public float getHeading()
        {
            // since the physical gyro appears to have a small (~5%) error in the angles it reports,
            // we scale the cumulative integrated Z reported by the gyro and use that integrated Z to compute "heading".

            float intZ = getIntegratedZValue();     // our convention is positive CCW (right hand rule) -- need to check actual output <<< ???
            intZ *= mCorrection;                    // apply corrective scaling factor (empirically derived by testing)
            float heading = Utils.wrapAngle(intZ + mHeadingOffset);  // add angle offset and wrap to [-180..180) range

            return heading;         // unlike Gyro interface, we return this as float, not int
        }

        public float getHeadingVelocity()
        {
            return 0;
        }

        public boolean haveHeading()
        {
            return !mGyro.isCalibrating();  // data is always available from this device once it's calibrated
        }

        public void setHeadingOffset(float offset) { mHeadingOffset = offset; }

        public void stop()
        {
            // mGyro.close();           // this causes big problems with REV hub ???
        }
    }

    // class that wraps UltrasonicSensor in a DistanceSensor interface
    public static class UltrasonicDistanceSensor implements DistanceSensor {

        private UltrasonicSensor mSensor;

        public UltrasonicDistanceSensor(UltrasonicSensor us)
        {
            mSensor = us;
        }

        public boolean haveDistance()
        {
            return mSensor.getUltrasonicLevel() < 255;
        }

        public float getDistance()
        {
            final float UUtoMM = 0.3f*25.4f;    // "Ultrasonic Units" appear to be ~0.3"
            return (float)mSensor.getUltrasonicLevel() * UUtoMM;
        }
    }

    // class that integrates movement and direction data to estimate a position on the field
    public static class PositionIntegrator {

        Position mPosition;

        public PositionIntegrator() {
            mPosition = new Position(DistanceUnit.INCH, 0,0,0,0);
        }

        public PositionIntegrator(double x, double y) {     // initial position
            mPosition = new Position(DistanceUnit.INCH, x, y, 0, 0);
        }

        public PositionIntegrator(Position position) {
            mPosition = position;
        }

        // move the position the given distance along the given bearing -
        // bearing is absolute in degrees with zero being along the field Y-axis, positive CCW
        public void move(double distance, double bearing) {
            double a = Math.toRadians(bearing);
            mPosition.x -= distance * Math.sin(a);
            mPosition.y += distance * Math.cos(a);
        }

        // move the position the given distances in vehicle X (right) and Y (forward) on the given bearing -
        // bearing is absolute in degrees with zero being along the field Y-axis, positive CCW
        public void move(double dx, double dy, double bearing) {
            double a = Math.toRadians(bearing);
            mPosition.x += dx * Math.cos(a) - dy * Math.sin(a);
            mPosition.y += dx * Math.sin(a) + dy * Math.cos(a);
        }

        // get the current position
        public double getX() { return mPosition.x; }
        public double getY() { return mPosition.y; }
        public double getZ() { return mPosition.z; }
        public Position getPosition() { return mPosition; }

        // set the current position (presumably from some other reliable source)
        public void setPosition(Position position) { mPosition = position; }
        public void setPosition(VectorF pos) {
            final float MMPERINCH = 25.4f;   // assume update position is from Vuforia (VectorF in mm)
            mPosition = new Position(DistanceUnit.INCH, pos.get(0)/MMPERINCH, pos.get(1)/MMPERINCH, pos.get(2)/MMPERINCH, 0);
        }
        // update current position by mixing current position with given position at given weight
        public void setPosition(VectorF pos, float weight) {
            final float MMPERINCH = 25.4f;   // assume update position is from Vuforia (VectorF in mm)
            mPosition = new Position(DistanceUnit.INCH,
                    (1-weight)*getX()+weight*(pos.get(0)/MMPERINCH),
                    (1-weight)*getY()+weight*(pos.get(1)/MMPERINCH),
                    (1-weight)*getZ()+weight*(pos.get(2)/MMPERINCH), 0);
        }
    }

    // use a set of motor encoders and gyro to track absolute field position --
    // this version supports normal, Mecanum and X-drives
    // assumes motors in order fr, br, fl, bl
    public static class EncoderGyroPosInt extends SensorLib.PositionIntegrator {
        OpMode mOpMode;
        HeadingSensor mGyro;
        DcMotor[] mEncoderMotors;    // set of motors whose encoders we will average to get net movement

        int mEncoderPrev[];		// previous readings of motor encoders
        boolean mFirstLoop;

        int mCountsPerRev;
        double mWheelDiam;

        public enum DriveType { NORMAL, MECANUM, XDRIVE };
        DriveType mDriveType = DriveType.NORMAL;

        double mFactor = 1.0;   // fudge factor to correct for <4 motors in Normal drive only

        // transform from wheel distance to robot movement for various drive types
        double mTWR[][];

        final double tWR_Normal[][] = new double[][] {
                {0, 0, 0, 0},
                {0.25, 0.25, 0.25, 0.25}
        };

        final double tWR_Mecanum[][] = new double[][] {
                {-0.25, 0.25, 0.25, -0.25},
                {0.25, 0.25, 0.25, 0.25}
        };

        final double sqrt2 = Math.sqrt(2);
        final double tWR_XDrive[][] = new double[][] {
                {-0.25*sqrt2, 0.25*sqrt2, 0.25*sqrt2, -0.25*sqrt2},
                {0.25*sqrt2, 0.25*sqrt2, 0.25*sqrt2, 0.25*sqrt2}
        };

        // this ctor is for backward compatibility -- defaults to DriveType.NORMAL
        public EncoderGyroPosInt(OpMode opmode, HeadingSensor gyro, DcMotor[] encoderMotors, int countsPerRev, double wheelDiam, Position initialPosn)
        {
            super(initialPosn);
            commonCtor(DriveType.NORMAL, opmode, gyro, encoderMotors, countsPerRev, wheelDiam, initialPosn);
        }

        // this ctor is for uses that want to specify the DriveType
        public EncoderGyroPosInt(DriveType type, OpMode opmode, HeadingSensor gyro, DcMotor[] encoderMotors, int countsPerRev, double wheelDiam, Position initialPosn)
        {
            super(initialPosn);
            commonCtor(type, opmode, gyro, encoderMotors, countsPerRev, wheelDiam, initialPosn);
        }

        private void commonCtor(DriveType type, OpMode opmode, HeadingSensor gyro, DcMotor[] encoderMotors, int countsPerRev, double wheelDiam, Position initialPosn)
        {
            mDriveType = type;
            switch (mDriveType) {
                case NORMAL:  mTWR = tWR_Normal; break;
                case MECANUM: mTWR = tWR_Mecanum; break;
                case XDRIVE:  mTWR = tWR_XDrive; break;
            }
            mOpMode = opmode;
            mGyro = gyro;
            mEncoderMotors = encoderMotors;
            mFirstLoop = true;
            mCountsPerRev = countsPerRev;
            mWheelDiam = wheelDiam;
            mEncoderPrev = new int[encoderMotors.length];
            if (type == DriveType.NORMAL)       // we try to deal with <4 encoders in Normal mode only
                mFactor = 4.0/encoderMotors.length;
        }

        public boolean loop() {
            // get initial encoder value
            if (mFirstLoop) {
                for (int i=0; i<mEncoderMotors.length; i++)
                    mEncoderPrev[i] = mEncoderMotors[i].getCurrentPosition();
                mFirstLoop = false;
            }

            // get current encoder values and compute deltas since last read
            int encoderDist[] = new int[4];
            for (int i=0; i<mEncoderMotors.length; i++) {
                int encoder = mEncoderMotors[i].getCurrentPosition();
                encoderDist[i] = encoder - mEncoderPrev[i];
                mEncoderPrev[i] = encoder;
            }

            // compute physical distance each wheel thinks it moved
            double dist[] = new double[4];
            for (int i=0; i<4; i++)
                dist[i] = (encoderDist[i] * mWheelDiam * Math.PI)/mCountsPerRev;

            // compute robot motion in relative x (across) and y(along) directions for Mecanum or X-drive
            double[] robotDeltaPos = new double[] {0,0};
            for (int i=0; i<2; i++){
                for (int j = 0; j<4; j++){
                    robotDeltaPos[i] += mTWR[i][j] * dist[j];
                }
            }
            double dxR = robotDeltaPos[0] * mFactor;    // adjust for possible <4 encoders in Normal mode
            double dyR = robotDeltaPos[1] * mFactor;

            // get bearing from IMU gyro
            double imuBearingDeg = mGyro.getHeading();

            // update accumulated field position
            this.move(dxR, dyR, imuBearingDeg);
            if (mOpMode != null) {
                mOpMode.telemetry.addData("EGPI position", String.format("%.2f", this.getX()) + ", " + String.format("%.2f", this.getY()));
            }

            return true;
        }

        public HeadingSensor getGyro() {
            return mGyro;
        }
    }

}