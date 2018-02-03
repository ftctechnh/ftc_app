
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import com.qualcomm.robotcore.hardware.CRServo;


@Autonomous(name="Preciousss: Autonomous6217Red1", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/17.
 *
 */
public class Autonomous6217Red1 extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo servoTapper;
    CRServo servoConL;
    CRServo servoConR;
    DcMotor motorConL;
    DcMotor motorConR;


    BNO055IMU imu;
    // State used for updating telemetry
    ModernRoboticsI2cRangeSensor rangeSensor;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;
    Orientation angles;
    Acceleration gravity;
    boolean iAmBlue = false;
    boolean iAmRed = true;
    boolean isBoxSide = true;


    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // V u f o r i a  s e t u p

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
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        // H a r d w a r e   M a p p i n g

        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoConL = hardwareMap.crservo.get("servoConL");
        servoConL.setDirection(CRServo.Direction.FORWARD);
        servoConR = hardwareMap.crservo.get("servoConR");
        servoConR.setDirection(CRServo.Direction.REVERSE);
        servoTapper = hardwareMap.servo.get("tapper");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }

        // S t a r t

        waitForStart();

        // J e w e l s
        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

        boolean iSeeBlue = false;
        boolean iSeeRed = false;
        servoTapper.setPosition(0.2d);
        Wait(.2f);
        servoTapper.setPosition(0.675d);
        Wait(2f);

        telemetry.update();

        Wait ( 10f);

        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;
        } else {
            iSeeBlue = true;

            iSeeRed = false;
        }


        if ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            move(0f, .2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, -.2f, .3f);
        } else {
            telemetry.addData("1", "move left");
            move(0f, -.2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, .2f, .3f);
        }

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        telemetry.update();

        move(0f, -0.5f, .47f);

        Wait(.5);

        pivotRight(.7f);

        Wait(1);

        move(0f,-.25f,.35f);

        Wait(1);

        Conveyor(3f);

        Wait(1);

        move(0f,-.25f,.5f);

        Wait(1);

        move(0f,.25f,.25f);



    }

    void move(float posx, float posy, float waitTime) {
        float FRBLPower = posy + posx;
        float FLBRPower = posy - posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(waitTime);
        sR();
    }

    void Conveyor (float waitTime)   {
        servoConL.setPower(1);
        servoConR.setPower(1);
        motorConL.setPower(1);
        motorConR.setPower(1);
        Wait(waitTime);


    }

    void pivotRight(float waitTime) {

        motorFL.setPower(-.5f);
        motorBL.setPower(-.5f);
        motorFR.setPower(.5f);
        motorBR.setPower(.5f);
        Wait(waitTime);
        sR();
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(.25f);
        motorBL.setPower(.25f);
        motorFR.setPower(-.25f);
        motorBR.setPower(-.25f);
        Wait(waitTime);
    }



    void letsGo(float posx, float posy) {
        float FRBLPower = posy - posx;
        float FLBRPower = posy + posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(0.1f);
    }

    void sR() {
        float power = 0.f;
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            //Comment this out to avoid it overwriting other telemetry
            //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }

    double lightLevel(double odsVal) {
        /*
         * This method adjusts the light sensor input, read as an ODS,
         * to obtain values that are large enough to use to drive
         * the robot.  It does this by taking the third decimal values
         * and beyond as the working values.
         */
        // LB = left bumper, RB = right bumper.
        int prefix = (int) (odsVal * 10);
        odsVal = Math.pow(10, odsVal) * prefix;

        return (odsVal);
    }
}














