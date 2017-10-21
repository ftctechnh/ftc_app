package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;
import org.firstinspires.ftc.teamcode.subsystems.Drive9330;
import org.firstinspires.ftc.teamcode.subsystems.PictographScan9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 9/25/2017.
 */

@Autonomous(name="AutoVuforia9330", group="Opmode")  // @Autonomous(...) is the other common choice
public class AutoVuforia9330 extends LinearOpMode {

    Hardware9330 robotMap = new Hardware9330();
    ColorDistance9330 colorDistance = new ColorDistance9330(robotMap);
    PictographScan9330 PictographScan = new PictographScan9330();
    Drive9330 drive = new Drive9330();
    Integer TurnError = 1;
    Integer TurnSpeed = 10;
    Integer hahaUseless;
    VuforiaTrackables info;
    Double PictoYRotation;
    Double PictoZTranslation;
    String PictoImageType;
    String  Distance;
    Integer ColorRed;
    Integer ColorGreen;
    Integer ColorBlue;
    Integer ColorAlpha;

    Orientation angles;

    public void log(String name, Object value) {
        telemetry.clear();
        telemetry.addData(name,value);
        telemetry.update();
    }

    public void checkStop() {
        if (isStopRequested()) stop();
    }

    public void updatePictogramInfo(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Y Rotation") PictoYRotation = (Double)me.getValue();
                else if (me.getKey() == "Z Translation (Distance)") PictoZTranslation = (Double)me.getValue();
                else if (me.getKey() == "Image Position") PictoImageType = (String)me.getValue();
                //log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        } else hahaUseless = 1; //log("Info", "404 - Image not found YET ;(");
    }

    public void updateColorDistance(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Distance") Distance = (String)me.getValue();
                else if (me.getKey() == "Alpha") ColorAlpha = (Integer)me.getValue();
                else if (me.getKey() == "Red") ColorRed = (Integer)me.getValue();
                else if (me.getKey() == "Green") ColorGreen = (Integer)me.getValue();
                else if (me.getKey() == "Blue") ColorBlue = (Integer)me.getValue();
                //log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        } else hahaUseless = 1;//log("Info", "404 - Color not found :'(");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        robotMap.init(hardwareMap);
        robotMap.gyro.initialize(parameters);
        info = PictographScan.init(hardwareMap,true);
        log("Info","Initialized. Press start when ready.");
        waitForStart();
        while(opModeIsActive()) {

            log("Info","Searching for image...");
            while (PictoImageType == null) {
                telemetry.clear();
                updatePictogramInfo(PictographScan.checkPosition(info));
                checkStop();
            }

            log("Info","Found image! Centering to the wall...");
            while (-TurnError > PictoYRotation || TurnError < PictoYRotation) {
                updatePictogramInfo(PictographScan.checkPosition(info));
                log("Rotation of pictogram", PictoYRotation.toString());
                if (PictoYRotation > 0)
                    drive.turnRight(TurnSpeed);
                else
                    drive.turnLeft(TurnSpeed);

                checkStop();
            }

            drive.stopDrive();
            log("Info","Centered to the wall! Checking team color...");

            while (ColorRed == null || ColorBlue == null) {
                updateColorDistance(colorDistance.getInfo()); // Check the color of the pad beneath you
                checkStop();
            }

                if (ColorRed > ColorBlue) {
                    //Knock down blue
                    log("Info", "We are red! Knocking down blue.");
                } else {
                    //Knock down red
                    log("Info", "We are blue! Knocking down red.");
                }

            angles   = robotMap.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //log("Gyro Angle", angles.firstAngle);
            // Diameter: 3.78
            // Width:
            while(!isStopRequested() && robotMap.touch.getState())
            {

            }
            stop();
        }

    }

}


