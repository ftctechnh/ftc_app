package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.libraries.interfaces.FinishSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.LocationSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;

import java.util.ArrayList;

/**
 * Created by Noah on 4/2/2017.
 * Some autolib based steps and functions designed for meccanum wheels
 * Will have little use on standard drivetrains
 */

public class SquirrelyLib {

    // some utilities to support "Squirrely Wheels" that move the robot sideways
    // when front and back wheels turn in opposite directions

    // linear interpolation
    private static double lerp (double x, double x0, double x1, double y0, double y1)
    {
        return ((x-x0)/(x1-x0))*(y1-y0) + y0;
    }

    // example of a class used to return multiple values from a function call
    public static class MotorPowers {
        public double mFront;
        public double mBack;
        public MotorPowers(double front, double back) {
            mFront = front;
            mBack = back;
        }
        public double Front() { return mFront; }
        public double Back() { return mBack; }
    }

    // this function computes the relative front/back power settings needed to move along a given
    // heading, relative to the current orientation of the robot.
    public static MotorPowers GetSquirrelyWheelMotorPowers(
            double heading    // in degrees, zero = straight ahead, positive CCW, range +-180
    )
    {
        // wrap heading around to acceptable range
        heading = SensorLib.Utils.wrapAngle(heading);

        // compute front and back wheel relative speeds needed to go in desired direction
        double front = 0.0f;
        double back = 0.0f;
        if (heading < 0) {
            if (heading > -90) {
                front = 1.0;
                back = lerp(heading, 0, -90, 1, -1);
            } else {
                front = lerp(heading, -90, -180, 1, -1);
                back = -1.0;
            }
        }
        else {
            if (heading < 90) {
                front = lerp(heading, 0, 90, 1, -1);
                back = 1.0;
            }
            else {
                front = -1.0;
                back = lerp(heading, 90, 180, 1, -1);
            }
        }

        // return results
        return new MotorPowers(front, back);
    }

    // a Step that drives 4 "squirrely wheels" to move the robot in a given direction
    // relative to where it's facing, for a given time.
    static public class MoveSquirrelyByTimeStep extends AutoLib.ConcurrentSequence {

        public MoveSquirrelyByTimeStep(
                DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl,     // motors
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            _MoveSquirrelyByTimeStep(fr, br, fl, bl, heading, power, seconds, stop);
        }

        public MoveSquirrelyByTimeStep(
                DcMotor motors[],  // motors -- assumed order is fr, br, fl, bl
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            _MoveSquirrelyByTimeStep(motors[0], motors[1], motors[2], motors[3], heading, power, seconds, stop);
        }


        // internal function that actually does the constructor stuff for the two different ctors
        protected void _MoveSquirrelyByTimeStep(
                DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl,     // motors
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            // compute relative front and back motor powers needed to move on the desired heading
            MotorPowers mp = GetSquirrelyWheelMotorPowers(heading);

            // create TimedMotorSteps to control the 4 motors
            if (fr != null)
                this.add(new AutoLib.TimedMotorStep(fr, mp.Front()*power, seconds, stop));
            if (br != null)
                this.add(new AutoLib.TimedMotorStep(br, mp.Back()*power, seconds, stop));
            if (fl != null)
                this.add(new AutoLib.TimedMotorStep(fl, mp.Front()*power, seconds, stop));
            if (bl != null)
                this.add(new AutoLib.TimedMotorStep(bl, mp.Back()*power, seconds, stop));
        }
    }

    static public class SquirrleyGuideStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mDirection;                             // compass heading to steer for (-180 .. +180 degrees
        private float mHeading;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private SensorLib.PID mPid;                         // proportional–integral–derivative controller (PID controller)
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public SquirrleyGuideStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                  ArrayList<SetPower> motorsteps, float power) {
            mOpMode = mode;
            mDirection = direction;
            mHeading = heading;
            mGyro = gyro;
            mPid = pid;
            mMotorSteps = motorsteps;
            mPower = power;
        }

        public boolean loop() {
            super.loop();
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            final float heading = mGyro.getHeading();     // get latest reading from direction sensor
            // convention is positive angles CCW, wrapping from 359-0

            final float error = SensorLib.Utils.wrapAngle(heading - mHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            // compute delta time since last call -- used for integration time of PID step
            final double time = mOpMode.getRuntime();
            final double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            final float correction = -mPid.loop(error, (float) dt);

            //calculate motor powers for fancy wheels
            MotorPowers mp = GetSquirrelyWheelMotorPowers(mDirection);

            final float leftPower = correction;
            final float rightPower = -correction;

            //set the powers
            mMotorSteps.get(0).setPower((rightPower + mp.Front()) * mPower);
            mMotorSteps.get(1).setPower((rightPower + mp.Back()) * mPower);
            mMotorSteps.get(2).setPower((leftPower + mp.Front()) * mPower);
            mMotorSteps.get(3).setPower((leftPower + mp.Back()) * mPower);

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("front power ", mp.Front());
                mOpMode.telemetry.addData("back power ", mp.Back());
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }

    }

    // a Step that uses gyro input to drive in a given direction with the robot pointing in a given heading
    // uses a SquirleyGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class SquirrleyAzimuthTimedDriveStep extends AutoLib.ConcurrentSequence {

        public SquirrleyAzimuthTimedDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                              DcMotor motors[], float power, float time, boolean stop) {
            // add a concurrent Step to control each motor

            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.TimedMotorStep step = new AutoLib.TimedMotorStep(em, 0, time, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyGuideStep(mode, direction, heading, gyro, pid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    static public class SquirrleyAzimuthFinDriveStep extends AutoLib.ConcurrentSequence {

        public SquirrleyAzimuthFinDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                            DcMotor motors[], float power, FinishSensor fin, boolean stop) {
            // add a concurrent Step to control each motor

            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.DriveUntilStopMotorStep step = new AutoLib.DriveUntilStopMotorStep(em, 0, fin, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyGuideStep(mode, direction, heading, gyro, pid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    // a Step that provides Vuforia-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps assuming order fr, br, fl, bl driving Squirrely Wheels
    // (i.e. wheels that can move the robot in any direction without yawing the robot itself).
    // this step tries to keep the robot on course to a given location on the field.
    static public class VuforiaSquirrelyGuideStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...
        private VectorF mTargetPosition;                    // target position on field
        private LocationSensor mLocSensor;                  // sensor to use for field location information (e.g. Vuforia)
        private HeadingSensor mYawSensor;                   // sensor to use for robot orientation on field (may be same as LocationSensor)
        private float mError;                               // how close do we have to be to declare "done"

        public VuforiaSquirrelyGuideStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         ArrayList<SetPower> motorSteps, float power, float error)
        {
            mOpMode = mode;
            mTargetPosition = targetPosition;
            mLocSensor = locSensor;
            mYawSensor = yawSensor;
            mMotorSteps = motorSteps;
            mPower = power;
            mError = error;
        }

        public boolean loop()
        {
            super.loop();
            // compute absolute direction vector to target position on field
            VectorF position = mLocSensor.getLocation();
            if (position == null) {
                float searchPower = -0.1f;
                mMotorSteps.get(0).setPower(searchPower); //right
                mMotorSteps.get(1).setPower(searchPower);
                mMotorSteps.get(2).setPower(-searchPower); //left
                mMotorSteps.get(3).setPower(-searchPower);
                return false;       // not done
            }
            VectorF dirToTarget = mTargetPosition.subtracted(position);

            // compute absolute field heading to target: zero aligned with Y axis, positive CCW, degrees 0/359
            double headingToTarget = Math.atan2(-dirToTarget.get(0), dirToTarget.get(1));
            headingToTarget *= 180.0/Math.PI;       // to degrees

            // get current orientation of the robot on the field
            double robotYaw = mYawSensor.getHeading();

            // compute relative heading robot should move along
            double robotHeading = headingToTarget - robotYaw;

            // compute motor powers needed to go in that direction
            MotorPowers mp = GetSquirrelyWheelMotorPowers(robotHeading);
            double frontPower = Range.clip(mp.Front(), -mPower, mPower);
            double backPower = Range.clip(mp.Back(), -mPower, mPower);

            // reduce motor powers when we're very close to the target position
            //final double slowDist = 254.0;   // start slowing down when we're this close
            double distToTarget = dirToTarget.magnitude();
            //if (distToTarget < slowDist) {
            //    frontPower *= distToTarget/slowDist;
            //    backPower  *= distToTarget/slowDist;
            //}

            // output debug telemetry
            mOpMode.telemetry.addData("VSGS:", "abs heading: %4.1f  distance: %4.1f", headingToTarget, distToTarget);

            // update motors
            // assumed order is fr, br, fl, bl
            mMotorSteps.get(0).setPower(frontPower);
            mMotorSteps.get(1).setPower(backPower);
            mMotorSteps.get(2).setPower(frontPower);
            mMotorSteps.get(3).setPower(backPower);

            // are we there yet?
            boolean bDone = distToTarget < mError;     // within an inch of target position?

            if(bDone){
                double searchPower = 0.0;
                mMotorSteps.get(0).setPower(searchPower); //right
                mMotorSteps.get(1).setPower(searchPower);
                mMotorSteps.get(2).setPower(searchPower); //left
                mMotorSteps.get(3).setPower(searchPower);
            }

            return bDone;
        }
    }

    // a Step that uses Vuforia input to drive a SquirrelyWheel robot to a given position on the field.
    // uses a VuforiaSquirrelyGuideStep to adjust power to the 4 motors, assuming order fr, br, fl, bl.
    static public class VuforiaSquirrelyDriveStep extends AutoLib.ConcurrentSequence {

        public VuforiaSquirrelyDriveStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         DcMotor motors[], float power, float error)
        {
            // add a concurrent Step to control each motor
            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.TimedMotorStep step = new AutoLib.TimedMotorStep(em, power, 0, false);  // always set requested power and return "done"
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on Vuforia input
            this.preAdd(new VuforiaSquirrelyGuideStep(mode, targetPosition, locSensor, yawSensor, steps, power, error));

        }

        // the base class loop function does all we need --
        // since the motors always return done, the composite step will return "done" when
        // the GuideStep says it's done, i.e. we've reached the target location.

    }

}
