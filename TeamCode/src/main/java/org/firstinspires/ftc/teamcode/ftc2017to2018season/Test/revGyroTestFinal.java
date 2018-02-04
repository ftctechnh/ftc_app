package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;


/**
 * Created by Team Inspiration on 1/21/18.
 */
@Autonomous(name = "Curious George REV Gyro Test")
public class revGyroTestFinal extends LinearOpMode {


    /*Delta_TeleOp is designed for and tested with the Tile Runner robot. If this program is used with another robot it may not worked.
* This is specificly made for the Tile Runner and not another pushbot or competiotion robot. However, this program is the basic design for
* simple program and could work on a different robot with simple debugging and configuration.*/

    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    double P_TURN_COEFF = 1./180;
    double TURN_THRESHOLD = 5;
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    Orientation angles;
    Acceleration gravity;
    public BNO055IMU revGyro;


    @Override
    public void runOpMode() {
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");


        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        revGyro = hardwareMap.get(BNO055IMU.class, "revGyro");
        revGyro.initialize(parameters);

        composeTelemetry();

        waitForStart();

        gyroTurnREV1(0.4,90);
    }

    public void gyroTurnREV1(double speed, double angle){

        telemetry.addData("starting gyro turn","-----");
        telemetry.update();

        while(opModeIsActive() && !onTargetAngleREV1(speed, angle, P_TURN_COEFF)){
            telemetry.update();
            idle();
            //telemetry.addData("-->","inside while loop :-(");
            //telemetry.update();
        }
        stopMotors();
        telemetry.addData("done with gyro turn","-----");
        telemetry.update();
    }
    boolean onTargetAngleREV1(double speed, double angle, double PCoeff){
        double error;
        double steer;
        boolean onTarget = false;
        double rightSpeed;
        double leftSpeed;

        //determine turm power based on error
        error = getErrorREV1(angle);

        if (Math.abs(error) <= TURN_THRESHOLD){

            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else{

            steer = getSteerREV1(error, PCoeff);
            leftSpeed = speed * steer;//normally, we would do rightspeed = speed*steer, and leftspeed = -rightspeed
            rightSpeed = -leftSpeed;//for the echo robot, however, the values are reversed (a negative motor power makes the motor go forward,
                                    //assuming that the forward direction is in the direction of the glyph manipulator
        }

        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        double weightConstant = 1;//this constant will depend on the robot. you need to test experimentally to see which is best


        leftWheelMotorBack.setPower(leftSpeed);
        leftWheelMotorFront.setPower(leftSpeed);
        rightWheelMotorBack.setPower(rightSpeed);
        rightWheelMotorFront.setPower(rightSpeed);




        telemetry.addData("Target angle","%5.2f",angle);
        telemetry.addData("Error/Steer", "%5.2f/%5.2f", error, steer);
        telemetry.addData("speed", "%5.2f/%5.2f", leftSpeed, rightSpeed);
        telemetry.addData("current angle", angles.firstAngle);

        return onTarget;
    }
    public double getErrorREV1(double targetAngle){

        double robotError;

        robotError = targetAngle - angles.firstAngle;
        //telemetry.addData("Zvalue","%5.2f",gyro.getIntegratedZValue());
        //telemetry.update();

        while(robotError > 180) robotError -= 360;

        while(robotError <= -180) robotError += 360;

        telemetry.addData("Robot Error","%5.2f",robotError);
        telemetry.update();

        return robotError;

    }
    public double getSteerREV1(double error , double PCoeff){
        return Range.clip(error * PCoeff, -1 , 1);
    }

    public void stopMotors() {
        rightWheelMotorFront.setPower(0);
        //idle();
        leftWheelMotorBack.setPower(0);
        //idle();
        rightWheelMotorBack.setPower(0);
        //idle();
        leftWheelMotorFront.setPower(0);
        //sleep(100);
        leftWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //idle();
        leftWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //idle();
        rightWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // idle();
        rightWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        sleep(100);
    }


    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = revGyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = revGyro.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return revGyro.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return revGyro.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}

