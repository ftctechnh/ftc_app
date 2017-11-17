package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.Clamps9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor9330;
import org.firstinspires.ftc.teamcode.subsystems.Drive9330;
import org.firstinspires.ftc.teamcode.subsystems.Gyro9330;
import org.firstinspires.ftc.teamcode.subsystems.JewelArm9330;
import org.firstinspires.ftc.teamcode.subsystems.Vuforia9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 10/21/2017.
 */
@Autonomous(name="SensorTest9330", group = "Opmode")
public class SensorTest9330 extends LinearOpMode {

    Hardware9330 robotMap = new Hardware9330();
    ColorDistance9330 colorDistance;
    ColorSensor9330 cs9330;
    Vuforia9330 PictographScan = new Vuforia9330();
    Gyro9330 gyro = new Gyro9330(robotMap);
    Drive9330 drive;
    JewelArm9330 crystalarm;
    Clamps9330 clamps;
    Integer TurnError = 1;
    Double TurnSpeed = 0.2;
    VuforiaTrackables info;
    Double PictoYRotation;
    Double PictoZTranslation;
    String PictoImageType;
    Integer ColorRed;
    Integer ColorGreen;
    Integer ColorBlue;
    Integer ColorAlpha;
    boolean onRedTeam; // will be used to reverse the motor direction based off of alliance

    public void log(String name, Object value) {
        telemetry.addData(name,value);
    }

    public void checkStop() {
        if (isStopRequested()) stop();
    }

    public void updatePictographInfo(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Y Rotation") PictoYRotation = (Double) me.getValue();
                else if (me.getKey() == "Z Translation (Distance)")
                    PictoZTranslation = (Double) me.getValue();
                else if (me.getKey() == "Image Position") PictoImageType = (String) me.getValue();
                log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        }
    }

    public void updateColorDistance(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Alpha") ColorAlpha = (Integer)me.getValue();
                else if (me.getKey() == "Red") ColorRed = (Integer)me.getValue();
                else if (me.getKey() == "Green") ColorGreen = (Integer)me.getValue();
                else if (me.getKey() == "Blue") ColorBlue = (Integer)me.getValue();
                log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");
        telemetry.update();

        //Everything commented out at the top is only there if moving it to the bottom was a mistake

        robotMap.init(hardwareMap); //initializes hardware map
        cs9330 = new ColorSensor9330(robotMap);
        drive = new Drive9330(robotMap);
        crystalarm = new JewelArm9330(robotMap);
        colorDistance = new ColorDistance9330(robotMap);
        clamps = new Clamps9330(robotMap);
        gyro.init();    //initializes gyro
        info = PictographScan.init(hardwareMap,true);   //initializes Vuforia

        log("Info","Initialized. Press start when ready.");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            updatePictographInfo(PictographScan.checkPosition(info));
            updateColorDistance(colorDistance.getInfo());
            log("Gyro Pitch", gyro.getPitch());
            log("Gyro Roll", gyro.getRoll());
            log("Gyro Yaw", gyro.getYaw());
            telemetry.update();
            checkStop();
        }

        drive.stopDrive();

    }
}
