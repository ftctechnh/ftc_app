package org.firstinspires.ftc.team8745;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by SuperSneasel12 on 11/21/16.
 */
@Autonomous(name = "IMU_8745First")

public class IMU_8745 extends LinearOpMode {

    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;


    private static final int TICS_PER_REV = 1120;

    private double WHEEL_DIAMETER = 4;

    DcMotor left_f;
    DcMotor right_f;
    DcMotor left_b;
    DcMotor right_b;

    DcMotor shooter_l;
    DcMotor shooter_r;

    Servo shooterServo;


    public ElapsedTime runtime = new ElapsedTime();


    final double kServoNullPosition = 0.8;
    final double kServoRange = 0.6;
    final double kShooterEnginePower = 0.8;

    private int ticsForInches(double inches) {
        return (int) ((inches * TICS_PER_REV) / (Math.PI * WHEEL_DIAMETER));

    }


    // 4 Inches
    public void initmybot() {
        //Front Motors
        left_f = hardwareMap.dcMotor.get("motor-left");
        right_f = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        left_b = hardwareMap.dcMotor.get("motor-leftBACK");
        right_b = hardwareMap.dcMotor.get("motor-rightBACK");


        //Shooter Motors
        shooter_l = hardwareMap.dcMotor.get("shooter-left");
        shooter_r = hardwareMap.dcMotor.get("shooter-right");

        //servos
        shooterServo = hardwareMap.servo.get("shooter-servo");

        //Running with encoder
        shooter_r.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter_l.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //stopping with Encoder
        right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //setting direction
        right_f.setDirection(DcMotorSimple.Direction.FORWARD);
        right_b.setDirection(DcMotorSimple.Direction.FORWARD);
        left_f.setDirection(DcMotorSimple.Direction.REVERSE);
        left_b.setDirection(DcMotorSimple.Direction.REVERSE);








        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        runtime.reset();
    }

    public void waitNSeconds(int secondsToWait) {
        double startTime = runtime.time();
        while (runtime.time() - startTime < secondsToWait) {

        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initmybot();
        waitForStart();
        /*
        if (!opModeIsActive()){
            stop();
            return;
        }
        */
        // Shoot Loaded Balls
        shooter_r.setPower(kShooterEnginePower);
        shooter_l.setPower(kShooterEnginePower);

        for (int i = 1; i <= 3; i++) {
            waitNSeconds(1);
            shooterServo.setPosition((kServoNullPosition + (-kServoRange)));
            waitNSeconds(1);
            shooterServo.setPosition(kServoNullPosition);

            int ticks = ticsForInches(12);

            //Run to posiiton
            right_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Our ticks for the motors

            right_f.setTargetPosition(ticks);
            right_b.setTargetPosition(ticks);
            left_f.setTargetPosition(ticks);
            left_b.setTargetPosition(ticks);


            //Waiting for robot to reach position.
            while (super.opModeIsActive()) {
                telemetry.addData("Ticks:", right_f.getCurrentPosition());
                telemetry.addData("Target:", right_f.getTargetPosition());
                telemetry.addData("Time elapsed:", runtime);
                telemetry.update();
                if (runtime.time() > 10) {
                    right_f.setPower(.5);
                    right_b.setPower(.5);
                    left_f.setPower(.5);
                    left_b.setPower(.5);
                }

                if (left_f.getCurrentPosition() > ticks) {
                    break;
                }

            }// end while

            right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            turnimu(-90);

            //////end turning//
            right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ticks = ticsForInches(24);

            //Run to posiiton
            right_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Our ticks for the motors

            right_f.setTargetPosition(ticks);
            right_b.setTargetPosition(ticks);
            left_f.setTargetPosition(ticks);
            left_b.setTargetPosition(ticks);


            //Waiting for robot to reach position.
            while (super.opModeIsActive()) {
                telemetry.addData("Ticks:", right_f.getCurrentPosition());
                telemetry.addData("Target:", right_f.getTargetPosition());
                telemetry.addData("Time elapsed:", runtime);
                telemetry.update();
                if (runtime.time() > 10) {
                    right_f.setPower(.5);
                    right_b.setPower(.5);
                    left_f.setPower(.5);
                    left_b.setPower(.5);
                }

                if (left_f.getCurrentPosition() > ticks) {
                    break;
                }

            }// end while

            right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
///////// turn 45

            turnimu(-45);

            //////end turning//


            ///////end turn 45
            right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ticks = ticsForInches(24);

            //Run to posiiton
            right_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Our ticks for the motors

            right_f.setTargetPosition(ticks);
            right_b.setTargetPosition(ticks);
            left_f.setTargetPosition(ticks);
            left_b.setTargetPosition(ticks);


            //Waiting for robot to reach position.
            while (super.opModeIsActive()) {
                telemetry.addData("Ticks:", right_f.getCurrentPosition());
                telemetry.addData("Target:", right_f.getTargetPosition());
                telemetry.addData("Time elapsed:", runtime);
                telemetry.update();
                if (runtime.time() > 10) {
                    right_f.setPower(.5);
                    right_b.setPower(.5);
                    left_f.setPower(.5);
                    left_b.setPower(.5);
                }

                if (left_f.getCurrentPosition() > ticks) {
                    break;
                }

            }// end while

            right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            while (super.opModeIsActive()) {
                Thread.sleep(10);
                Thread.yield();
            }
        }
    }



    public double getcurrentheading() {
        return AngleUnit.DEGREES.normalize(imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle);

    }

    public static double calculateDelta(double targetheading, double currentheading) {
        double ih =  AngleUnit.DEGREES.normalize(targetheading);
        double ch =  AngleUnit.DEGREES.normalize(currentheading);

        return   AngleUnit.DEGREES.normalize(ih - ch);
    }


    public void turnimu(double turnangle) {
        double initialHeading = getcurrentheading();


        double targetAngle = initialHeading + turnangle;
        targetAngle = AngleUnit.DEGREES.normalize(targetAngle);
        telemetry.addData("heading", initialHeading);
        telemetry.update();

        right_f.setPower(.10);
        right_b.setPower(.10);
        left_f.setPower(-.10);
        left_b.setPower(-.10);

        while (super.opModeIsActive()) {

            double currentHeading = getcurrentheading();
            double delta = calculateDelta(targetAngle,currentHeading);
            telemetry.addData("initial", initialHeading);
            telemetry.addData("heading", currentHeading);
            telemetry.addData("target", targetAngle);
            telemetry.addData("delta", delta);
            telemetry.update();
            right_f.setPower(Math.signum(delta)*-.10);
            right_b.setPower(Math.signum(delta)*-.10);
            left_f.setPower(Math.signum(delta)*.10);
            left_b.setPower(Math.signum(delta)*.10);
            if (Math.abs(delta) < 2) {
                right_f.setPower(0);
                right_b.setPower(0);
                left_f.setPower(0);
                left_b.setPower(0);
                return;

            }
        }

    }

}



