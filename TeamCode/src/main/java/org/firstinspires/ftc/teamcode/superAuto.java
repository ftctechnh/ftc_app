package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
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

    static final float ridgeDepth = 4;

    RelicRecoveryVuMark[] boxOrder = new RelicRecoveryVuMark[4];

    BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    Orientation angles;
    Acceleration gravity;

    ModernRoboticsI2cRangeSensor rangeSensor;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;
    RelicRecoveryVuMark vuMark;


    boolean iAmRed;
    boolean iAmBlue = !iAmRed;

    private ElapsedTime runtime = new ElapsedTime();

    void setUp() {

        iAmBlue = !iAmRed;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();
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

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");


        motorFlip = hardwareMap.dcMotor.get("motorFlip");
        motorFlip.setDirection(DcMotor.Direction.FORWARD);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoTapper = hardwareMap.servo.get("servoTapper");
        servoFlicker = hardwareMap.servo.get("servoFlicker");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
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
        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                gravity = imu.getGravity();
            }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override
                    public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override
                    public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel * gravity.xAccel
                                        + gravity.yAccel * gravity.yAccel
                                        + gravity.zAccel * gravity.zAccel));
                    }
                });
    }


    void cryptoState(int targetHeading, float basePosx, float basePosy) {
        runtime.reset();

        // Grab first distance
        double currentDist = rangeSensor.getDistance(DistanceUnit.CM);
        double previousDist = currentDist;

        // Red game
        if (iAmRed) {
            if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                vuMark = RelicRecoveryVuMark.RIGHT;
            }
            boxOrder[0] = RelicRecoveryVuMark.UNKNOWN;
            boxOrder[1] = RelicRecoveryVuMark.RIGHT;
            boxOrder[2] = RelicRecoveryVuMark.CENTER;
            boxOrder[3] = RelicRecoveryVuMark.LEFT;
        }
        // Blue game
        else {
            if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                vuMark = RelicRecoveryVuMark.LEFT;
            }
            boxOrder[0] = RelicRecoveryVuMark.LEFT;
            boxOrder[1] = RelicRecoveryVuMark.CENTER;
            boxOrder[2] = RelicRecoveryVuMark.RIGHT;
            boxOrder[3] = RelicRecoveryVuMark.UNKNOWN;
        }

        // Simple state machine
        int i = 0;
        boolean go = true;
        RelicRecoveryVuMark state = boxOrder[i];

        // While not our state do ...
        while (go) {

            // Move & take reading
            adjustHeading(targetHeading, basePosx, basePosy);
            previousDist = currentDist;
            currentDist = rangeSensor.getDistance(DistanceUnit.CM);

            // Found a divider, change state
            if ((currentDist - previousDist) >= 4) {
                i++;
                state = boxOrder[i];
                if (state == vuMark) {
                    go = false;
                }
            }

            // Debugging info
            telemetry.addData("Previous Distance after read: ", previousDist);
            telemetry.addData("Current Distance after read: ", currentDist);
            telemetry.addData("State", state);
            telemetry.addData("i", i);
            telemetry.update();

        }
        telemetry.addData("We have made it this far... ", state);
        // Stop the robot
        sR();

    }

    void followHeading(int targetHeading, double time, float basePosx, float basePosy) {
        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //double currentHeading = angles.firstAngle;
        runtime.reset();
        while (((runtime.seconds() < time))) {
            adjustHeading(targetHeading, basePosx, basePosy);
        }
        sR();
    }

    void adjustHeading(int targetHeading, float basePosx, float basePosy) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("First Angle", angles.firstAngle);
        if (angles != null) {
            double currentHeading = angles.firstAngle;
            double addPower = (targetHeading - currentHeading) * .025;
            telemetry.addData("add power", addPower);

            double FRBLPower = (-basePosy) - basePosx;
            double FLBRPower = (-basePosy) + basePosx;
            motorFR.setPower(FRBLPower + addPower);
            motorFL.setPower(FLBRPower - addPower);
            motorBR.setPower(FLBRPower + addPower);
            motorBL.setPower(FRBLPower - addPower);
        }
    }

    void flip() {
        distCorrector(20);
        motorFlip.setPower(.5d);
        Wait(1d);
        motorFlip.setPower(0d);
        Wait(1);
        motorFlip.setPower(-.5d);
        Wait(.5);
        motorFlip.setPower(0d);
        distCorrector(15);
        distCorrector(25);
    }
    void fancyGyroPivot (double target) {

        //First set up variables

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double raw = angles.firstAngle;
        float fudgeFactor = .25f;
        double wheelPower = .3;
        double convert = raw;
        double dflt = Math.abs(target - raw);
        double alt = (360 - dflt);
        boolean right;

        while ((convert < (target - fudgeFactor)) || (convert > (target + fudgeFactor)))
        {

            telemetry.addData("Raw: ", raw);
            telemetry.addData("Convert: ", convert);
            telemetry.addData("Default", dflt);
            telemetry.addData("Alt", alt);
            telemetry.update();

            // determine if we are moving right or left
            if (alt < dflt || dflt < 0)
            {
                right = false;
            }
            else
            {
                right = true;
            }

            //set power to motor
            if (right = true)
            {
                motorFL.setPower(wheelPower);
                motorBL.setPower(wheelPower);
                motorFR.setPower(-wheelPower);
                motorBR.setPower(-wheelPower);
            }
            else
            {
                motorFL.setPower(-wheelPower);
                motorBL.setPower(-wheelPower);
                motorFR.setPower(wheelPower);
                motorBR.setPower(wheelPower);
            }

            //read the heading and find the shortest path
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            raw = angles.firstAngle;
            dflt = (target - raw);
            alt = (360 - Math.abs(dflt));

            //determines if we need to convert to compass
            if (Math.abs(dflt) > 180)
            {
                convert = 360 - Math.abs(raw);

            }
            else
            {
                convert = raw;
            }
        }
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
                telemetry.update();
                if (target - currentHeading > 0) {
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

    public double compassConverter(double raw)
    {
           double compass = 3;

            if (raw < 0)
            {
                compass = 360 + raw;
            }

            else
            {
                compass = raw;
            }
        return compass;
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
        motorFL.setPower(0);
        motorBL.setPower(0);
        motorFR.setPower(0);
        motorBR.setPower(0);
    }

    void setUpVuforia(){

        VuforiaLocalizer vuforia;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

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
        int timeRecongnize=0;

        for(int t = 0; t<2e6; t++) {
            timeRecongnize =t;
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                break;
            }
        }
        telemetry.addData("VuMark", "%s visible", vuMark);
        telemetry.addData("Time Recognize", "%d", timeRecongnize);
        telemetry.update();
    }


    void distCorrector(double trgDistance) {

        double curDistance = rangeSensor.getDistance(DistanceUnit.CM);
        double fstDistance = curDistance - trgDistance;

        boolean go = true;
        int i = 0;

        // While not our state do ...
        while (go) {

            curDistance = rangeSensor.getDistance(DistanceUnit.CM);
            double chgDistance = curDistance - trgDistance;

            double direction;
            if (chgDistance > 0) {
                direction = 1;
            } else {
                direction = -1;
            }

            if (Math.abs(chgDistance) >= 2) {

                // Slow down
                double adjust = 0.20 * direction;

                //(Math.abs(chgDistance / fstDistance)) * direction;
                //if (adjust > 0.25) { adjust = 0.25; }

                telemetry.addData("current distance", curDistance);
                telemetry.addData("target distance", trgDistance);
                telemetry.addData("direction", direction);
                telemetry.addData("adjust", adjust);
                telemetry.addData("i", i);
                telemetry.update();

                // Power the motors
                  motorFL.setPower(adjust);
                  motorFR.setPower(adjust);
                  motorBL.setPower(adjust);
                  motorBR.setPower(adjust);
            } else
            {
                go = false;
            }
            i++;
        //    if (i > 30) {
        //        go = false;
        //    }
        }
        sR();
    }


    void jewel(){

        // J e w e l s

        boolean iSeeBlue = false;
        boolean iSeeRed = false;
        servoTapper.setPosition(0.2d);
        servoFlicker.setPosition(.3d);
        Wait(.2f);
        servoTapper.setPosition(0.76d);
        Wait(1f);

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
            servoFlicker.setPosition(.1);
        }
        else {
            telemetry.addData("1", "move left");
            servoFlicker.setPosition(.5);
        }
        Wait(1d);
        servoFlicker.setPosition(.3);
        servoTapper.setPosition(.2d);
        Wait(1d);
        servoFlicker.setPosition(.6);

        telemetry.update();

    }
}
