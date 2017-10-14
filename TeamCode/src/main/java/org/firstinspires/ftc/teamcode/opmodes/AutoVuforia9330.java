package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;
import org.firstinspires.ftc.teamcode.subsystems.PictographScan9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    List<Integer> RGB;
    Double PictoYRotation;
    Double PictoZTranslation;
    String PictoImageType;
    String  Distance;
    Integer ColorRed;
    Integer ColorGreen;
    Integer ColorBlue;
    Integer ColorAlpha;

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
        } else log("Info", "404 - Image not found YET ;(");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");
        telemetry.update();
        robotMap.init(hardwareMap);
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

            telemetry.update();
        }
            //}

    }
}


