package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Trackable;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

/**
 * Created by TPR on 12/14/17.
 * IMPORTANT
 * DEFAULT IS RED
 */

public abstract class RelicAutoMode extends MeccyAutoMode {
    PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    ModernRoboticsI2cRangeSensor rangeSensor;

    //
    static double countify = 678;
    double inches;
    double speed;
    //
    VuforiaLocalizer vuforia;
    OpenGLMatrix lastLocation = null;
    //
    public void startify() {
        pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");
    }
    //
    public void goForward (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
    }

    public void strafeRight (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + -move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + -move);
    }

    public void strafeLeft (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -move);
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
    }


    //
    public int testOutMoving(double inchesYouThink){
        stopAndResetify();
        toPosition(inchesYouThink, .4);
        return leftBackMotor.getCurrentPosition();
    }
    //
    public double startAuto(int key){
        //<editor-fold desc="Startify">
        //<editor-fold desc="Hardware Map">
        initGyro();

        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        //
        pengwinWing = new PengwinWing(hardwareMap);
        pengwinFin = new PengwinFin(hardwareMap);
        //</editor-fold>
        //
        //<editor-fold desc="Vuforia">
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //
        parameters.vuforiaLicenseKey = "AbxR5+T/////AAAAGR1YlvU/6EDzrJvG5EfPnXSFutoBr1aCusr0K3pKqPuWTBQsUb0mv5irjoX2Xf/GFvAvHyw8v1GBYgHwE+hNTcNj05kw3juX+Ur4l3HNnp5SfXV/8fave0xB7yVYZ/LBDraNnYXiuT+D/5iGfQ99PVVao3LI4uGUOvL9+3vbPqtTXLowqFJX5uE7R/W4iLmNqHgTCSzWcm/J1CzwWuOPD252FDE9lutdDVRri17DBX0C/D4mt6BdI5CpxhG6ZR0tm6Zh2uvljnCK6N42V5x/kXd+UrBgyP43CBAACQqgP6MEvQylUD58U4PeTUWe9Q4o6Xrx9QEwlr8v+pmi9nevKnmE2CrPPwQePkDUqradHHnU";
        //
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;//set camera (front)
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        //
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        //</editor-fold>
        //</editor-fold>
        //
        stopAndResetify();
        //
        relicTrackables.activate();
        //
        configureMotors();
        //
        waitForStartify();
        //
        pengwinFin.moveFinUp();
        //
        pengwinWing.setServos(false, false);
        //
        //<editor-fold desc="Jewel">
        strafeToPosition(5, .5);
        sleep(200);
        //
        pengwinFin.moveFinDown();
        sleep(1000);
        //
        int blueJewel = ((pengwinFin.doesColorSensorSeeBlueJewel()) ? 1 : -1) * key;
        //
        turnWithGyro(15, .4 * blueJewel);
        sleep(1000);
        turnWithGyro(15, .4 * -blueJewel);
        sleep(1000);
        //
        pengwinFin.moveFinUp();
        //</editor-fold>
        //
        //<editor-fold desc="Vuforia">
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        drive(.4);
        while (vuMark == RelicRecoveryVuMark.UNKNOWN){
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
        }
        drive(0);
        //
        double distance = 1;
        if (vuMark == RelicRecoveryVuMark.LEFT){
            distance = 5;
        }else if (vuMark == RelicRecoveryVuMark.CENTER){
            distance = 10;
        }else if (vuMark == RelicRecoveryVuMark.RIGHT){
            distance = 15;
        }
        return distance;
        //</editor-fold>
    }
    //
    public void autoNorth(int key){
        double distance = startAuto(key);
        //
        //<editor-fold desc="To CryptoBox">
        pengwinFin.moveFinSense();
        //
        drive(.4 * key);
        while (!pengwinFin.approachCrypt(key)){}
        drive(0);
        sleep(100);
        //
        pengwinFin.moveFinUp();
        sleep(100);
        //</editor-fold>
        //
        //<editor-fold desc="Place Glyph">
        toPosition(distance * key, .5);
        sleep(200);
        //
        turnWithGyro(90, .4);
        sleep(300);
        //
        pengwinWing.lowerArm();
        sleep(200);
        //
        pengwinWing.setServos(true, true);
        sleep(100);
        //
        toPosition(-3, .3);
        sleep(100);
        //</editor-fold>
        //
        telemetry.addLine("Done!");
    }
    //
    public void autoSouth(int key){
        double distance = startAuto(key);
        //
        //<editor-fold desc="To CryptoBox">
        pengwinFin.moveFinUp();
        sleep(100);
        //
        toPosition(10 * (-key/2 + .5), .4);
        sleep(100);
        //
        strafeToPosition(-(10 + distance) * key, .4);
        sleep(100);
        //</editor-fold>
        //
        //<editor-fold desc="Place Glyph">
        //
        turnWithGyro(90 * ((-key/2) + .5), .4);
        sleep(300);
        //
        pengwinWing.lowerArm();
        sleep(200);
        //
        pengwinWing.setServos(true, true);
        sleep(100);
        //
        toPosition(-3, .3);
        sleep(100);
        //</editor-fold>
        //
        telemetry.addLine("Done!");
    }
}

