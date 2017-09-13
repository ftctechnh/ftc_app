package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDirectionalDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDriveTrain;

/**
 * This class implements general algorithms that aren't specific to any particular FTC season.
 * The dependencies of this class are injected as abstract interfaces which all extend
 * {@link org.firstinspires.ftc.teamcode.mechanism.IMechanism}. As such, this
 * class doesn't depend on the implementation of the actual robot.
 */
public abstract class RobotBase {
    private LinearOpMode opMode;
    private IDriveTrain driveTrain;

    private GyroSensor gyroSensor;
    private DistanceSensor distanceSensor;

    private static final double GYRO_ERROR_THRESHOLD = 1.0;
    private static final double P_GYRO_TURN_COEFF = 0.01;

    /**
     * Constructs an instance of this class with a reference to the calling op-mode
     * to provide access to the hardware map object and the telemetry object for use
     * by the various functions made available in this class.
     * <p>
     * This constructor will call {@code initialize()} on the
     * {@code driveTrain} parameter to initialize the robot drive train.
     *
     * @param opMode the op-mode using this RobotBase object
     * @param driveTrain an instance of the actual robot drive train implementation
     */
    public RobotBase(LinearOpMode opMode, IDriveTrain driveTrain) {
        this.opMode = opMode;
        this.driveTrain = driveTrain;

        driveTrain.initialize(opMode);
    }

    /**
     * Register a {@link HardwareDevice} (usually a sensor) to be
     * used by the algorithms implemented in {@link RobotBase}.
     *
     * @param device the device to be registered
     */
    public void registerDevice(HardwareDevice device) {
        if(device instanceof IntegratingGyroscope) {
            gyroSensor = (GyroSensor)device;

            // reset gyro Z axis
            gyroSensor.resetZAxisIntegrator();

        } else if(device instanceof DistanceSensor) {
            distanceSensor = (DistanceSensor)device;
        }
    }

    /**
     * Drive to the target distance detected using the {@link DistanceSensor}.
     * This may result in the robot driving forward or backward,
     * depending on the actual distance.
     *
     * @param targetDistance the distance in centimeters the robot should drive to
     * @param speed the speed at which the robot should drive
     */
    protected void rangeSensorDrive(int targetDistance, double speed) {
        double distance;

        if(distanceSensor == null) {
            throw new UnsupportedOperationException("distance sensor not enabled");
        }

        driveTrain.enableEncoders(true);

        while(opMode.opModeIsActive()) {
            distance = distanceSensor.getDistance(DistanceUnit.CM);

            if(distance > targetDistance) {
                driveTrain.drive(speed, 0);
            } else if(distance < targetDistance) {
                driveTrain.drive(-speed, 0);
            } else {
                break;
            }
        }

        driveTrain.stopDriveMotors();
    }

    private double getGyroError(double targetAngle) {
        // adding because of negative number
        return targetAngle + gyroSensor.getHeading();
    }

    /**
     * Pivot the robot to the specified degree angle using the gyro sensor.
     *
     * @param speed the speed of the drive motors while pivoting
     * @param angle the degree angle to pivot to; a negative value is
     *              counter clockwise and a positive value is clockwise
     * @param absolute whether or not the angle should be relative to where the
     *                 gyro sensor last calibrated or the robot's current rotational position
     */
    public void gyroPivot(double speed, double angle, boolean absolute) {
        double steer;
        double proportionalSpeed;
        double error;

        if(gyroSensor == null) {
            throw new UnsupportedOperationException("gyro sensor must be enabled to call gyroPivot()");
        } else if(!(driveTrain instanceof IDirectionalDriveTrain)) {
            throw new UnsupportedOperationException("drive train must be omni-directional");
        }

        error = getGyroError(angle);

        if(!absolute) {
            error += getGyroError(0);
        }

        // just return if the error is too low
        if(Math.abs(error) <= 2) {
            return;
        }

        while(opMode.opModeIsActive() && Math.abs(error) > GYRO_ERROR_THRESHOLD) {

            opMode.telemetry.addData("error", error);
            opMode.telemetry.addData("Z axis difference from target angle", error);
            opMode.telemetry.update();

            error = getGyroError(angle);

            if(!absolute) {
                error += getGyroError(0);
            }

            steer = Range.clip(error * P_GYRO_TURN_COEFF , -1, 1);

            proportionalSpeed = speed * steer;

            driveTrain.pivot(proportionalSpeed);
        }

        opMode.telemetry.addData(">", "stop");
        opMode.telemetry.update();

        // when we're on target, stop the robot
        driveTrain.stopDriveMotors();
    }
}
