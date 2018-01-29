
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
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
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import com.qualcomm.robotcore.hardware.CRServo;


@Autonomous(name="Preciousss: Red1wGyro", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/17.
 *
 */
public class Red1wGyro extends LinearOpMode {

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

    Orientation angles;
    Acceleration gravity;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;
    boolean iAmBlue = false;
    boolean iAmRed = true;
    boolean isBoxSide = true;


    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        // V u f o r i a  s e t u p



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

        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            colors = colorSensor.getNormalizedColors();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

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
            //colors = colorSensor.getNormalizedColors();
            float redValue = colors.red * 10000;
            float blueValue = colors.blue * 10000;
            telemetry.addLine()
                    .addData("r", "%.3f", colors.red*10000)
                    .addData("b", "%.3f", colors.blue*10000);

            //telemetry.update();


        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;

            telemetry.update();
        } else {
            iSeeBlue = true;

            iSeeRed = false;
        }
        telemetry.addLine()
                .addData("r", "%.3f", redValue)
                .addData("b", "%.3f", blueValue)
                .addData("iSeeRed", "%b", iSeeRed)
                .addData("iSeeBlue", "%b", iSeeBlue)
                .addData ( "iSeeRed && iAmRed", "%b", (iSeeRed && iAmRed))
                .addData ( "iSeeBlue && iAmBlue", "%b", (iSeeBlue && iAmBlue))
                .addData ( "Final Boolean", "%b", ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)));

        telemetry.update();

        Wait(10f);

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
        telemetry.update();

        move(0f, -0.5f, .47f);

        Wait(.5);

        pivotTo(-90);

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

    void pivotTo(int target) {
        //Pivot to counterclockwise is positive.
        //Pivot to clockwise is negative.
        float fudgeFactor = .25f;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentHeading = angles.firstAngle;
        double wheelPower = .3;


        while ((currentHeading < (target - fudgeFactor)) || (currentHeading > (target + fudgeFactor))) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                currentHeading = angles.firstAngle;
                if (target - currentHeading <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                0) {
                    motorFL.setPower(-wheelPower);
                    motorBL.setPower(-wheelPower);
                    motorFR.setPower(wheelPower);
                    motorBR.setPower(wheelPower);
                } else {
                    motorFL.setPower(wheelPower);
                    motorBL.setPower(wheelPower);
                    motorFR.setPower(-wheelPower);
                    motorBR.setPower(-wheelPower);
                }
            }
        }

        sR();
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
}
