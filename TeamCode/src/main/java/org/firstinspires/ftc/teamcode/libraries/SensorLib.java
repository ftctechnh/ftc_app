package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;

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
            float derivative = (dt > 0) ? Math.abs(error - mPrevError)/dt : 0;
            float output = mKp*error + mKi*mIntegral;
            if(output >= 0) output += mKd*derivative;
            else output -= mKd*derivative;
            mPrevError = error;
            return output;
        }

        public void reset(){
            mPrevError = 0;
            mIntegral = 0;
        }
    }

    // class that tries to correct systemic errors in ModernRoboticsI2cGyro output
    public static class CorrectedMRGyro implements HeadingSensor {

        ModernRoboticsI2cGyro mGyro;

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

        public float getIntegratedZValue()
        {
            // return the raw (uncorrected) integrated Z value from the underlying physical gyro
            return mGyro.getIntegratedZValue();
        }

        // implements HeadingSensor interface
        public float getHeading()
        {
            // since the physical gyro appears to have a small (~5%) error in the angles it reports
            // so now we just scale the cumulative integrated Z reported by the gyro using one
            // constant (360/376) for both directions and use that integrated Z to compute "heading".

            float intZ = getIntegratedZValue();     // positive CCW

            final float k = (360.0f/376.0f);          // correction factor: ~16 degrees / revolution
            intZ *= k;
            
            // wrap to [0..360) range
            float heading = intZ % 360.0f;

            return heading;         // unlike Gyro interface, we return this as float, not int
        }

        public void stop()
        {
            mGyro.close();
        }
    }

}