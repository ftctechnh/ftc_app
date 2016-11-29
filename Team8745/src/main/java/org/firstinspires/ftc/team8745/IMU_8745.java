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

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;

    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo shooterServo;


    public ElapsedTime runtime = new ElapsedTime();




    private int ticsForInches(double inches) {
        return (int) ((inches * TICS_PER_REV) / (Math.PI * WHEEL_DIAMETER));
    }


    // 4 Inches
    public void initmybot() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");


        //Running with encoder
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //stopping with Encoder
        rightFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //setting direction
        rightFRONT.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBACK.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
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


        int ticks = ticsForInches(24);

            //Run to posiiton
            rightFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Our ticks for the motors

            rightFRONT.setTargetPosition(ticks);
            rightBACK.setTargetPosition(ticks);
            leftFRONT.setTargetPosition(ticks);
            leftBACK.setTargetPosition(ticks);




        //Waiting for robot to reach position.
        while (true) {
            telemetry.addData("Ticks:", rightFRONT.getCurrentPosition());
            telemetry.addData("Target:", rightFRONT.getTargetPosition());
            telemetry.addData("Time elapsed:", runtime);
            if (runtime.time() > 10) {
                rightFRONT.setPower(.5);
                rightBACK.setPower(.5);
                leftFRONT.setPower(.5);
                leftBACK.setPower(.5);
            }

            if (leftFRONT.getCurrentPosition() > ticks) {
                break;
            }

        }// end while
        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        angles   = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);

        float initialHeading = angles.firstAngle;
        telemetry.addData("heading",initialHeading);
        telemetry.update();
        rightFRONT.setPower(-.5);
        rightBACK.setPower(-.5);
        leftFRONT.setPower(.5);
        leftBACK.setPower(.5);



    }
}



