package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
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
    private float roboDiameterCm = (float)(45.7*Math.PI); // can be adjusted
    private float wheelCircIn = 4 * (float)Math.PI ; //Circumference of wheels used
    private float wheelCircCm = (float)(9.8* Math.PI);

    private DistanceSensor frontLeftDistSens;
    private DistanceSensor frontRightDistSens;


    public ParadeBot(HardwareMap hMap)
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

        frontLeftDistSens = hMap.get(DistanceSensor.class, "frontLeftDistSens");
        frontRightDistSens = hMap.get(DistanceSensor.class, "frontRightDistSens");
        stopAllMotors();
        updateIMUValues();
    }

    public void driveStraight_In(float inches)
    {
        driveStraight_In(inches,.75);
    }


    public void driveStraight_In(float inches, double pow)
    {
        float encTarget = 88.3378f * inches - 357.7886f;

        if(inches > 0)
            encTarget = inches;
        else if (inches < 0)
            encTarget = inches;
        else
            encTarget = inches;
        //You get the number of encoder counts per unit and multiply it by how far you want to go

        resetEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if(inches < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
        }

        while (Math.abs(driveLeftOne.getCurrentPosition()) < Math.abs(encTarget) && Math.abs(driveRightOne.getCurrentPosition()) < Math.abs(encTarget)) {}

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
                if (loops > 3 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
                    break;
            }
        } else
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
                if (loops > 3 &&(Math.abs(driveRightOne.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES)) < 5))
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

            while(driveRightOne.getCurrentPosition() > encTarget){}
        }
        else //spins cc
        {
            driveRightOne.setPower(Math.abs(pow));

            while(driveRightOne.getCurrentPosition() < encTarget){}
        }

        stopAllMotors();
    }

    public void spin_Right_IMU(float degrees, double pow)
    {
        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
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

            while(driveLeftOne.getCurrentPosition() < encTarget){}

        }
        else //spins clockwise
        {
            driveLeftOne.setPower(-Math.abs(pow));

            while(driveLeftOne.getCurrentPosition() > encTarget){}
        }

        stopAllMotors();
    }

    public void spin_Left_IMU(float deg, double pow)
    {
        float degrees = deg;
        if (deg > 0)
        {
            degrees -= 5.2f;
        }
        else
        {
            degrees += 5.2f;
        }

        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveLeftOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveLeftOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
        }

        stopAllMotors();
    }

    public void pivot(float degrees, double pow)//Utilizes two motors at a time; spins in place
    {
        resetEncoders();

        float encTarget = degrees;
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter
        //we divide it by two at the end to compensate for using two motors

        //It pivots in the direction of how to unit circle spins
        if (degrees < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));

        }
        else //CounterClockwise
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
        }

        while (Math.abs(driveLeftOne.getCurrentPosition()) < Math.abs(encTarget) && Math.abs(driveRightOne.getCurrentPosition()) < Math.abs(encTarget)) {}

        stopAllMotors();
    }

    public void pivot_IMU(float degrees_IN)
    {
        pivot_IMU(degrees_IN, .8);
    }

    public void pivot_IMU(float degrees_In, double pow)
    {
        float degrees =  1.0661f * degrees_In - 21.0936f; // at .8 pow

        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees <-180)
        {
            degrees += 360;
        }

        initIMU();
        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
            while(getYaw() > degrees) {}
        }
        else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
            while(getYaw() < degrees) {}
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
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }


    public double anglePerpToGrav()
    {
        updateIMUValues();
        return Math.atan(gravity.yAccel/gravity.zAccel);
    }

    public String getGravToString()
    {
        updateIMUValues();
        return gravity.toString();
    }

    public float getYaw()
    {
        updateIMUValues();
        return AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
    }

    public void updateIMUValues()
    {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
    }

    public double getDistFromFrontLeft_In()
    {
        return frontLeftDistSens.getDistance(DistanceUnit.INCH);
    }
    public double getDistFromFrontRight_In()
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

    public DcMotorImplEx getDriveRightOne() {return driveRightOne;}

    public DistanceSensor getFrontLeftDistSens()
    {
        return frontLeftDistSens;
    }

    public void setFrontLeftDistSens(DistanceSensor frontLeftDistSens)
    {
        this.frontLeftDistSens = frontLeftDistSens;
    }

    public DistanceSensor getFrontRightDistSens()
    {
        return frontRightDistSens;
    }

    public void setFrontRightDistSens(DistanceSensor frontRightDistSens)
    {
        this.frontRightDistSens = frontRightDistSens;
    }
}
