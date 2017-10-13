package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.Clamps9330;
import org.firstinspires.ftc.teamcode.subsystems.PictographScan9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 9/25/2017.
 */

@Autonomous(name="AutoVuforia9330", group="Opmode")  // @Autonomous(...) is the other common choice
public class AutoVuforia9330 extends LinearOpMode {

    Hardware9330 robotMap = new Hardware9330();
    Clamps9330 clamps = new Clamps9330(robotMap);
    PictographScan9330 PictographScan = new PictographScan9330();
    VuforiaTrackables info;
    HashMap hm = new HashMap();
    Double PictoYRotation;
    Double PictoZTranslation;
    String PictoImageType;

    public void log(String name, Object value) {
        telemetry.addData(name,value);
    }

    public void updatePictogramInfo() {
        hm = PictographScan.checkPosition(info);
        if (!hm.isEmpty()) {
            telemetry.clear();
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Y Rotation") PictoYRotation = (Double)me.getValue();
                else if (me.getKey() == "Z Translation (Distance)") PictoZTranslation = (Double)me.getValue();
                else if (me.getKey() == "Image Position") PictoImageType = (String)me.getValue();
                log(me.getKey().toString(), me.getValue());
            }
        } else log("Info", "404 - Image not found YET ;(");
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");
        telemetry.update();
        info = PictographScan.init(hardwareMap,true);
        log("Info","Initialized. Press start when ready.");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {

            //while (PictoYRotation == null || PictoZTranslation == null || PictoImageType == null) {
                updatePictogramInfo();
            //}

        }
    }

}
