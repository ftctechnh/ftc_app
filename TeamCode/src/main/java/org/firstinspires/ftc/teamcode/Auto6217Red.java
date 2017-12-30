
package org.firstinspires.ftc.teamcode;

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


@Autonomous(name="Preciousss: Autonomous6217Red", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/17.
 *
 */
public class Auto6217Red extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo servoTapper;

    NormalizedColorSensor colorSensor;
    static ModernRoboticsI2cGyro gyro;
    boolean iAmBlue = false;
    boolean iAmRed = true;
    boolean isBoxSide = true;

    private ElapsedTime runtime = new ElapsedTime();

    VuforiaLocalizer vuforia;
    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void runOpMode() {

        // V u f o r i a  s e t u p

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "Ac+mNz7/////AAAAGarZm7vF6EvSku/Zvonu0Dtf199jWYNFXcXymm3KQ4XngzMBntb" +
                "NeMXt0qCPqACXugivtrYvwDU3VhMDRJwlwdMi4C2F6Su/8LZBrPIFtxUtr7MMagebQM/+4CSUIOQQdKNpdBttrX8yWM" +
                "SrdyfnkNhh/vhXpQd7pXWwJ02UcnEVT1CiLeyTcl+bJUo1+xNonNaNEs8861zxmtO2TBtf9gyXhunlM6lpBJjC6nYWQ3" +
                "BM2DOODFNz2EU3F3N1WxnOvCERQ+c934JKPajgCrNs5dquSo1wpcr0Kkf3u29hzK0DornR8s9j03g8Ea7q5cYN8WLn/e" +
                "q1dUOFznng+6y2/7/fvw9wrzokOP9nP1QujkUN";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();

        // H a r d w a r e   M a p p i n g

        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        servoTapper = hardwareMap.servo.get("tapper");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");
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

        servoTapper.setPosition(0.0d);
        Wait(1);
        servoTapper.setPosition(0.6d);
        Wait(1);
        boolean iSeeBlue = false;
        boolean iSeeRed = false;

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        telemetry.addLine()
                .addData("r", "%.3f", colors.red)
                .addData("b", "%.3f", colors.blue);

        telemetry.update();

        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;
        } else {
            iSeeBlue = true;
            iSeeRed = false;
        }

        Wait(2.5f);

        if ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            move(0f, .2f, .25f);
            Wait(1);
            move(0f, -.2f, .25f);
        } else {
            telemetry.addData("1", "move left");
            move(0f, -.2f, .25f);
            Wait(1);
            move(0f, .2f, .25f);
        }
        telemetry.update();
        servoTapper.setPosition(0.1d);

        // R e a d   V u M a r k

        RelicRecoveryVuMark currentVumark = ReadPicto(relicTemplate);
        // Move off balance stone
        move(0f,.25f, 1f);
        // Put this back when gyro is working
        // if (isBoxSide) {pivotByZ(90);}

        if (currentVumark == RelicRecoveryVuMark.LEFT) {
            telemetry.addData("1", "LEFT");
            move(0f, .25f, 3f);
            Wait(1);
        }
        else if (currentVumark == RelicRecoveryVuMark.CENTER) {
            telemetry.addData("1", "CENTER");
            move(0f, .25f, 2f);
            Wait(1);
        }
        else if (currentVumark == RelicRecoveryVuMark.RIGHT) {
            telemetry.addData("1", "RIGHT");
            move(0f, .25f, 1f);
            Wait(1);
        }
        else {
            telemetry.addData("1", "UNKNOWN");
            move(0f, .25f, 02f);
            Wait(1);
        }
        telemetry.update();
        /*telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
        telemetry.addData("raw optical", rangeSensor.rawOptical());
        telemetry.addData("cm optical", "%.2f cm", rangeSensor.cmOptical());
        telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();*/
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

    void pivotRight(float waitTime) {

        motorFL.setPower(-1.f);
        motorBL.setPower(-1.f);
        motorFR.setPower(1.f);
        motorBR.setPower(1.f);
        Wait(waitTime);
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(1.f);
        motorBL.setPower(1.f);
        motorFR.setPower(-1.f);
        motorBR.setPower(-1.f);
        Wait(waitTime);
    }

    void pivotBy(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .25f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.97f;
        int initialHeading = 0;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        } else {
            initialHeading = 360;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount + 1;
            curHeading = Math.abs(gyro.getHeading() - initialHeading);
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    RelicRecoveryVuMark ReadPicto(VuforiaTrackable relicTemplate) {

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        return vuMark;

    }

    void pivotByZ(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .2f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.95f;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount+1;
            curHeading = Math.abs(gyro.getIntegratedZValue());
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }



    void pivotTo(int heading) {


        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .5f;
        float tolerance = 0.1f * heading;

        int initialHeading = gyro.getHeading();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (!((curHeading < heading + tolerance) && (curHeading > heading - tolerance))) {
            iCount = iCount++;
            curHeading = gyro.getHeading();
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", iCount);
            telemetry.update();
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














