package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by Jeremy on 6/11/2017.
 */
public class ParadeBot
{
    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;
    private BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    int loops = 0;
    double velocitiesR = 0;
    double velocitiesL = 0;

    private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
    private float roboDiameterCm = (float) (45.7 * Math.PI); // can be adjusted
    private float wheelCircIn = 4 * (float) Math.PI; //Circumference of wheels used
    private float wheelCircCm = (float) (9.8 * Math.PI);

    private DistanceSensor frontDistSens, frontRightDistSens;

    LinearOpMode linearOpMode;

    public ParadeBot(HardwareMap hMap, LinearOpMode linearOpModeIN)
    {
        linearOpMode = linearOpModeIN;
        initSensorsAndMotors(hMap);
    }

    public ParadeBot(HardwareMap hMap)
    {
        initSensorsAndMotors(hMap);
    }

    private void initSensorsAndMotors(HardwareMap hMap)
    {
        imu = (hMap.get(BNO055IMU.class, "imu"));
        initIMU();

        driveLeftOne = hMap.get(DcMotorImplEx.class, "driveLeftOne");
        driveRightOne = hMap.get(DcMotorImplEx.class, "driveRightOne");

        resetEncoders();

        driveRightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        driveLeftOne.setDirection(DcMotorSimple.Direction.FORWARD);

        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontDistSens = hMap.get(DistanceSensor.class, "frontLeftDistSens");
        frontRightDistSens = hMap.get(DistanceSensor.class, "frontRightDistSens");
        stopAllMotors();
    }

    public void driveStraight_In(float inches)
    {
        driveStraight_In(inches, .75);
    }

    public void driveStraight_In(float inches, double pow)
    {
        float encTarget = 88.3378f * Math.abs(inches) - 357.7886f;

        if (pow < 0)
            inches = -inches;

        resetEncoders();

        if (inches < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
        }

        while (Math.abs(driveLeftOne.getCurrentPosition()) < Math.abs(encTarget) && Math.abs(driveRightOne.getCurrentPosition()) < Math.abs(encTarget) && !linearOpMode.isStopRequested())
        {
        }

        stopAllMotors();
    }

    public void driveMotorsAuto(float lPow, float rPow)
    {
        driveMotors(-lPow, -rPow);
    }

    public void driveStraight_In_Stall(float inches, double pow, Telemetry telemetry)
    {
        float encTarget = 1120 / wheelCircIn * inches;
        //You get the number of encoder counts per unit and multiply it by how far you want to go

        float absPow = (float) Math.abs(pow);
        resetDriveEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if (pow < 0)
        {
            inches *= -1;
        }
        if (inches < 0)
        {
            driveMotorsAuto(-absPow, -absPow);

            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget)
            {
                // if (Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES) <  *.75 )
                double rVel = getDriveRightOne().getVelocity(AngleUnit.DEGREES);
                double lVel = getDriveLeftOne().getVelocity(AngleUnit.DEGREES);
                loops++;
                velocitiesR += rVel;
                velocitiesL += lVel;
                telemetry.addData("RightVel ", rVel);
                telemetry.addData("LeftVel ", lVel);
                telemetry.addData("Average", null);
                telemetry.addData("LAvg ", velocitiesL / loops);
                telemetry.addData("RAvg ", velocitiesR / loops);
                telemetry.update();
                if (loops > 3 && (Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }
        } else
        {
            driveMotorsAuto(absPow, absPow);

            while (driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget)
            {
                double rVel = getDriveRightOne().getVelocity(AngleUnit.DEGREES);
                double lVel = getDriveLeftOne().getVelocity(AngleUnit.DEGREES);
                loops++;
                velocitiesR += rVel;
                velocitiesL += lVel;
                telemetry.addData("RightVel ", rVel);
                telemetry.addData("LeftVel ", lVel);
                telemetry.addData("Average", null);
                telemetry.addData("LAvg ", velocitiesL / loops);
                telemetry.addData("RAvg ", velocitiesR / loops);
                telemetry.update();
                if (loops > 3 && (Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }

            stopDriveMotors();
        }
    }

    public void stopDriveMotors()
    {
        driveLeftOne.setPower(0);
        driveRightOne.setPower(0);
    }
/*
    public void driveStraight_Cm(float cm, double pow)
    {
        float encTarget = encCountsPerRev / wheelCircCm * cm;

        resetEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if(cm < 0)
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

        stopAllMotors();
    }
    */

    public void spin_Right(float degrees)
    {
        spin_Right(degrees, 1);
    }

    public void spin_Right(float degrees, double pow)// Right Motor only moves!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetEncoders();

        if (degrees < 0) //spins clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));

            while (driveRightOne.getCurrentPosition() > encTarget)
            {
            }
        } else //spins cc
        {
            driveRightOne.setPower(Math.abs(pow));

            while (driveRightOne.getCurrentPosition() < encTarget)
            {
            }
        }

        stopAllMotors();
    }

    public void spin_Right_IMU(float degrees, double pow)
    {
        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees < -180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            while (getYaw() > degrees)
            {
            }
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            while (getYaw() < degrees)
            {
            }
        }

        stopAllMotors();
    }

    public void spin_Left(float degrees)
    {
        spin_Left(degrees, 1);
    }

    public void spin_Left(float degrees, double pow)//Left Motor only moves!!!!!!!
    {
        double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

        double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetEncoders();

        if (degrees > 0) //This spins the robot counterclockwise
        {
            driveLeftOne.setPower(Math.abs(pow));

            while (driveLeftOne.getCurrentPosition() < encTarget)
            {
            }

        } else //spins clockwise
        {
            driveLeftOne.setPower(-Math.abs(pow));

            while (driveLeftOne.getCurrentPosition() > encTarget)
            {
            }
        }

        stopAllMotors();
    }

    public void spin_Left_IMU(float deg, double pow)
    {
        float degrees = deg;
        if (deg > 0)
        {
            degrees -= 5.2f;
        } else
        {
            degrees += 5.2f;
        }

        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees < -180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveLeftOne.setPower(-Math.abs(pow));
            while (getYaw() > degrees)
            {
            }
        } else
        {
            driveLeftOne.setPower(Math.abs(pow));
            while (getYaw() < degrees)
            {
            }
        }

        stopAllMotors();
    }
    public void pivot(double encoder)//Utilizes two motors at a time; spins in place
    {
        resetEncoders();

        //It pivots in the direction of how to unit circle spins
        if (encoder < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-.8);
            driveLeftOne.setPower(-.8);

        } else //CounterClockwise
        {
            driveRightOne.setPower(.8);
            driveLeftOne.setPower(.8);
        }

        stopAllMotors();
        stopDriveMotors();
    }
    public void pivot(float degrees)
    {
        pivot(degrees, .8);
    }
    public void pivot(double degrees, double pow)//Utilizes two motors at a time; spins in place
    {
        resetEncoders();
        double encTarget;
        encTarget = Math.abs(17.254 * Math.abs(degrees) + 367.295);

        //It pivots in the direction of how to unit circle spins
        if (degrees < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));

        } else //CounterClockwise
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
        }

        while (Math.abs(driveLeftOne.getCurrentPosition()) < encTarget && Math.abs(driveRightOne.getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
        {
        }
        stopAllMotors();
        stopDriveMotors();
    }

    public void pivot(float degrees, double pow)//Utilizes two motors at a time; spins in place
    {
        resetEncoders();
        double encTarget;
        encTarget = Math.abs(17.254 * Math.abs(degrees) + 367.295);

        //It pivots in the direction of how to unit circle spins
        if (degrees < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));

        } else //CounterClockwise
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
        }

        while (Math.abs(driveLeftOne.getCurrentPosition()) < encTarget && Math.abs(driveRightOne.getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
        {
        }
        stopAllMotors();
        stopDriveMotors();
    }

    public void pivot_IMU(float degrees_IN)
    {
        pivot_IMU(degrees_IN, .8);
    }

    public void pivot_IMU(float degrees_In, double pow)
    {
        float degreesToStopAt;

        if (degrees_In > 0)
            degreesToStopAt = Math.abs(1.0661f * Math.abs(degrees_In) - 21.0936f);// at .8 pow
        else
            degreesToStopAt = -Math.abs(1.0661f * Math.abs(degrees_In) - 21.0936f);

        initIMU();

        linearOpMode.sleep(100);
        if (degreesToStopAt < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
            while (getYaw() > degreesToStopAt && !linearOpMode.isStopRequested())
            {
                linearOpMode.sleep(160);
            }
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
            while (getYaw() < degreesToStopAt && !linearOpMode.isStopRequested())
            {
                linearOpMode.sleep(160);
            }
        }

        stopAllMotors();
    }

    public int getRightEncoderPos()
    {
        return driveRightOne.getCurrentPosition();
    }

    public int getLeftEncoderPos()
    {
        return driveLeftOne.getCurrentPosition();
    }

    public void driveMotors(float lPow, float rPow)
    {
        driveRightOne.setPower(-rPow);
        driveLeftOne.setPower(lPow);
    }

    public void stopAllMotors()
    {
        driveLeftOne.setPower(0);
        driveRightOne.setPower(0);
    }

    private void resetEncoders()
    {
        driveRightOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initIMU()
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //gravity  = imu.getGravity();
    }


    public double anglePerpToGrav()
    {
        return Math.atan(gravity.yAccel / gravity.zAccel);
    }

    public String getGravToString()
    {
        return gravity.toString();
    }

    public float getYaw()
    {
        return angles.firstAngle;
    }

    public double getDistFromFront_In()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
    }

    public double getDistFromRight_In()
    {
        return frontRightDistSens.getDistance(DistanceUnit.INCH);
    }

    private void resetDriveEncoders()//sets encoders to 0 for motors
    {
        driveRightOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public DcMotorImplEx getDriveLeftOne()
    {
        return driveLeftOne;
    }

    public DcMotorImplEx getDriveRightOne()
    {
        return driveRightOne;
    }

    public DistanceSensor getFrontLeftDistSens()
    {
        return frontDistSens;
    }

    public void setFrontLeftDistSens(DistanceSensor frontLeftDistSens)
    {
        this.frontDistSens = frontLeftDistSens;
    }

    public DistanceSensor getFrontRightDistSens()
    {
        return frontRightDistSens;
    }

    public void setFrontRightDistSens(DistanceSensor frontRightDistSens)
    {
        this.frontRightDistSens = frontRightDistSens;
    }

    /*
    public void turnAbsolute(int target)
    {
        double direction = angles.firstAngle;
        double minspeed = .1;
        double maxspeed = .3;
        double errorDegs = Math.abs(direction - target);
        double turnSpeed = maxspeed * (errorDegs / 180) + minspeed;
        while (Math.abs(direction - target) > 2 && opModeIsActive())
        {
            if (direction > target)
            {
                back_left.setPower(turnSpeed);
                front_left.setPower(turnSpeed);
                back_right.setPower(-turnSpeed);
                front_right.setPower(-turnSpeed);
            }
            if (direction < target)
            {
                back_left.setPower(-turnSpeed);
                front_left.setPower(-turnSpeed);
                back_right.setPower(turnSpeed);
                front_right.setPower(turnSpeed);
            }
            direction = angles.firstAngle;
            telemetry.addData("accu", String.format("%03d", zAccumulated));
            telemetry.update();
        }
        back_left.setPower(0);
        front_left.setPower(0);
        back_right.setPower(0);
        front_right.setPower(0);
    }
    */
}
