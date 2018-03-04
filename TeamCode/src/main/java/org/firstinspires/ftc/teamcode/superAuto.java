package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

abstract public class superAuto extends LinearOpMode {
    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor motorConL;
    DcMotor motorConR;
    DcMotor motorFlip;
    DcMotor motorSlide;
    Servo servoTapper;
    Servo servoClaw;
    Servo servoWrist;
    Servo servoIntake;
    Servo servoFlicker;

    BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    Orientation angles;
    Acceleration gravity;

    ModernRoboticsI2cRangeSensor rangeSensor;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;

    boolean iAmRed;
    boolean iAmBlue = !iAmRed;

    private ElapsedTime runtime = new ElapsedTime();

    void setUp(){
        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        setUpVuforia();

    }



    void configureGyro() {
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
    }

    void mapHardware() {

        imu =hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");


        motorFR =hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL =hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL =hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR =hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorConL =hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR =hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoTapper =hardwareMap.servo.get("servoTapper");
        servoFlicker =hardwareMap.servo.get("servoFlicker");
        colorSensor =hardwareMap.get(NormalizedColorSensor.class,"colorSensor");
        if(colorSensor instanceof SwitchableLight)
        {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
    }
    void composeTelemetry() {

        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
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

    void findCrypto(int targetHeading, float ridgeDepth, float basePosx, float basePosy ){
        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //double currentHeading = angles.firstAngle;
        runtime.reset();
        float currentDist = rangeSensor.rawUltrasonic();
        float previousDist = currentDist;
        while ((currentDist-previousDist) < ridgeDepth){
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                double currentHeading = angles.firstAngle;
                double adjustPower =  (targetHeading - currentHeading) * .025;
                float addPower =(float) adjustPower;

                float FRBLPower = basePosy + basePosx;
                float FLBRPower = basePosy - basePosx;
                motorFR.setPower( FRBLPower - addPower);
                motorFL.setPower( FLBRPower + addPower);
                motorBR.setPower( FLBRPower - addPower );
                motorBL.setPower( FRBLPower + addPower );
            }
            previousDist = currentDist;
            currentDist = rangeSensor.rawUltrasonic();
            telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
            telemetry.update();
        }
        sR();
    }


    void followHeading(int targetHeading, double time, float basePosx, float basePosy ){
        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //double currentHeading = angles.firstAngle;
        runtime.reset();
        while (((runtime.seconds() < time))){
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            if (angles != null) {
                double currentHeading = angles.firstAngle;
                double adjustPower =  (targetHeading - currentHeading) * .025;
                float addPower =(float) adjustPower;

                float FRBLPower = basePosy + basePosx;
                float FLBRPower = basePosy - basePosx;
                motorFR.setPower( FRBLPower - addPower);
                motorFL.setPower( FLBRPower + addPower);
                motorBR.setPower( FLBRPower - addPower );
                motorBL.setPower( FRBLPower + addPower );
            }
        }
        sR();
    }



    void pivotTo(int target) {
        //Pivot to counterclockwise is positive.
        //Pivot to clockwise is negative.
        double baseWheelPower = .35;
        double minWheelPower = .2;
        double wheelPower = baseWheelPower;
        float fudgeFactor = .25f;
        double dif = target;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
       double currentHeading = angles.firstAngle;
        wheelPower = .3;

        while ((currentHeading < (target - fudgeFactor)) || (currentHeading > (target + fudgeFactor))) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                currentHeading = angles.firstAngle;
                dif = (target - currentHeading);
                telemetry.update();
                //wheelPower = ( ( ( dif / target ) - minWheelPower ) * baseWheelPower ) + minWheelPower ;
                if (target - currentHeading < 0) {
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

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            //Comment this out to avoid it overwriting other telemetry
            //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }

    void sR() {
        float power = 0.f;
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    void setUpVuforia(){

        VuforiaLocalizer vuforia;

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "Ac+mNz7/////AAAAGarZm7vF6EvSku/Zvonu0Dtf199jWYNFXcXymm3KQ4XngzMBntb" +
                    "NeMXt0qCPqACXugivtrYvwDU3VhMDRJwlwdMi4C2F6Su/8LZBrPIFtxUtr7MMagebQM/+4CSUIOQQdKNpdBttrX8yWM" +
                    "SrdyfnkNhh/vhXpQd7pXWwJ02UcnEVT1CiLeyTcl+bJUo1+xNonNaNEs8861zxmtO2TBtf9gyXhunlM6lpBJjC6nYWQ3" +
                    "BM2DOODFNz2EU3F3N1WxnOvCERQ+c934JKPajgCrNs5dquSo1wpcr0Kkf3u29hzK0DornR8s9j03g8Ea7q5cYN8WLn/e" +
                    "q1dUOFznng+6y2/7/fvw9wrzokOP9nP1QujkUN";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();
    }

    void jewel(){

        // J e w e l s

        boolean iSeeBlue = false;
        boolean iSeeRed = false;
        servoFlicker.setPosition(.5);
        servoTapper.setPosition(0.2d);
        Wait(.2f);
        servoTapper.setPosition(0.8d);
        Wait(1.5f);


        telemetry.update();
        colors = colorSensor.getNormalizedColors();
        float redValue = colors.red * 10000;
        float blueValue = colors.blue * 10000;

        if (redValue > blueValue) {
            iSeeRed = true;
            iSeeBlue = false;
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

        Wait(1f);

        if ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            servoFlicker.setPosition(.7);
        }
        else {
            telemetry.addData("1", "move left");
            servoFlicker.setPosition(.3);
        }
        Wait(1d);
        servoFlicker.setPosition(.5);
        servoTapper.setPosition(.2d);
        Wait(1d);
        servoFlicker.setPosition(0);

        telemetry.update();

    }
}
