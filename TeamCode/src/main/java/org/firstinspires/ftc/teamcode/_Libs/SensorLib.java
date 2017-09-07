package org.firstinspires.ftc.teamcode._Libs;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

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
        private float mKp = 0;
        private float mKi = 0;
        private float mKd = 0;
        private float mKiCutoff = 0;     // max error value for which we integrate error over time

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
            if (Math.abs(error) < mKiCutoff)      // only integrate small errors (< 3 degrees for now)
                mIntegral += error*dt;
            float derivative = (dt > 0) ? (error - mPrevError)/dt : 0;
            float output = mKp*error + mKi*mIntegral + mKd*derivative;
            mPrevError = error;
            return output;
        }

    }

    // class that tries to correct systemic errors in ModernRoboticsI2cGyro output
    public static class CorrectedMRGyro implements HeadingSensor {

        ModernRoboticsI2cGyro mGyro;            // the underlying physical MR Gyro
        float mCorrection = (360.0f/376.0f);    // default correction factor = ~16 degrees per revolution

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
            float heading = intZ % 360.0f;          // wrap to [0..360) range <<< ??? Java % operator preserves sign of first arg ...

            return heading;         // unlike Gyro interface, we return this as float, not int
        }

        public boolean haveHeading()
        {
            return !mGyro.isCalibrating();  // data is always available from this device once it's calibrated
        }

        public void stop()
        {
            mGyro.close();
        }
    }

    // class that wraps UltrasonicSensor in a DistanceSensor interface
    static public class UltrasonicDistanceSensor implements DistanceSensor {

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
}