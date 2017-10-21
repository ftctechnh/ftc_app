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
        telemetry.addData(name,value);
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
                log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        } else log("Info", "404 - Image not found YET ;(");
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
                log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        } else log("Info", "404 - Color not found :'(");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        robotMap.init(hardwareMap);
        robotMap.gyro.initialize(parameters);
        info = PictographScan.init(hardwareMap,true);
        log("Info","Initialized. Press start when ready.");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {

            //while (PictoYRotation == null || PictoZTranslation == null || PictoImageType == null) {
                telemetry.clear();
                updatePictogramInfo(PictographScan.checkPosition(info));
                log("","");
                updateColorDistance(colorDistance.getInfo());

            angles   = robotMap.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            telemetry.addData("Gyro Angle", angles.firstAngle);

            // Diameter: 3.78
            // Width:

            telemetry.update();
        }
            //}

    }

}


