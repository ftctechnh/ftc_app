package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
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
    private ElapsedTime runtime = new ElapsedTime();
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
    public int testOutMoving(double inchesYouThink){
        stopAndResetify();
        toPosition(inchesYouThink, .4);
        return leftBackMotor.getCurrentPosition();
    }
    //
    public void startAuto(int key){
        //<editor-fold desc="Startify">
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
        stopAndResetify();
        //
        configureMotors();
        //
        pengwinWing.left.setPosition(.6);
        //
        pengwinFin.moveFinUp();
        //
        telemetry.update();
        waitForStartify();
        //
        pengwinFin.moveFinUp();
        //
        pengwinWing.setServos(false, false);
        //
        //<editor-fold desc="Jewel">
        strafeToPosition(3.5, .5);
        sleep(200);
        //
        pengwinFin.moveFinDown();
        sleep(1000);
        //
        int blueJewel = ((pengwinFin.doesColorSensorSeeBlueJewel()) ? 1 : -1) * key;
        //
        toPosition(2.5 * blueJewel, .4);
        pengwinFin.moveFinUp();
        toPosition(2.5 * -blueJewel, .4);
        sleep(1000);
        //
        //</editor-fold>
    }
    //
    public void autoNorth(int key){
        startAuto(key);
        //
        double distance = 10;
        if (key == -1){
            distance = distance + 11;
        }
        //
        telemetry.addLine("Distance: " + distance);
        //
        //<editor-fold desc="To CryptoBox">
        pengwinFin.moveFinSense();
        //
        runtime.reset();
        telemetry.addData("Off to find the box", key);
        telemetry.update();
        drive(.15 * key);
        while (!pengwinFin.approachCrypt(key) && runtime.seconds() < 10){}
        drive(0);
        sleep(100);
        //
        pengwinFin.moveFinUp();
        sleep(1000);
        //</editor-fold>
        //
        //<editor-fold desc="Place Glyph">
        toPosition(distance * key, .5);
        sleep(200);
        //
        turnWithGyro(90, .4);
        sleep(300);
        //
        pengwinWing.raiseArm();
        sleep(2000);
        //
        pengwinWing.setServos(true, true);
        sleep(1000);
        //
        toPosition(-6, .3);
        sleep(1000);
        //
        toPosition(4, .3);
        sleep(1000);
        //
        toPosition(-2, .2);
        //</editor-fold>
        //
        telemetry.addLine("Done!");
        sleep(1000);
    }
    //
    public void autoSouth(int key){
        startAuto(key);
        double distance = 10;
        //
        //<editor-fold desc="To CryptoBox">
        pengwinFin.moveFinUp();
        sleep(1000);
        //
        if (key == 1) {
            toPosition(34 * key, .2);
        }else {
            toPosition(40 * key, .2);
        }
        sleep(1000);
        //
        turnWithGyro(85, -.4 * key);
        sleep(1000);
        //
        pengwinFin.moveFinSense();
        sleep(1000);
        //
        runtime.reset();
        telemetry.addData("Off to find the box", key);
        telemetry.update();
        drive(.15 * key);
        while (!pengwinFin.approachCrypt(key) && runtime.seconds() < 5){}
        drive(0);
        sleep(100);
        //
        pengwinFin.moveFinUp();
        sleep(1000);
        //</editor-fold>
        //
        //<editor-fold desc="Place Glyph">
        //
        toPosition(distance * key, .5);
        sleep(200);
        //
        turnWithGyro(90, .4);
        sleep(300);
        //
        pengwinWing.raiseArm();
        sleep(2000);
        //
        pengwinWing.setServos(true, true);
        sleep(1000);
        //
        toPosition(-6, .3);
        sleep(1000);
        //
        toPosition(4, .3);
        sleep(1000);
        //
        toPosition(-2, .2);
        //</editor-fold>
        //
        telemetry.addLine("Done!");
    }
}

