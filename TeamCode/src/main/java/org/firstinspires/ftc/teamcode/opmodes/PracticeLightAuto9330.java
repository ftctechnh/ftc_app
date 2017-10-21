package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 10/21/2017.
 */
@Autonomous(name="PracticeLightAuto", group = "Opmode")
public class PracticeLightAuto9330 extends LinearOpMode {

    Hardware9330 robotMap = new Hardware9330();
    ColorDistance9330 colorDistance = new ColorDistance9330(robotMap);
    String Distance;
    Integer colorAlpha;
    Integer colorRed;
    Integer colorBlue;
    Integer colorGreen;
    int hahaUseless;

    @Override
    public void runOpMode() throws InterruptedException {
       robotMap.init(hardwareMap);
        while(!isStopRequested()){
            updateColorDistance(colorDistance.getInfo());
        }
    }

    public void updateColorDistance(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Distance") Distance = (String)me.getValue();
                else if (me.getKey() == "Alpha") colorAlpha = (Integer)me.getValue();
                else if (me.getKey() == "Red") colorRed = (Integer)me.getValue();
                else if (me.getKey() == "Green") colorGreen = (Integer)me.getValue();
                else if (me.getKey() == "Blue") colorBlue = (Integer)me.getValue();
                telemetry.addData(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
            telemetry.update();
        } else hahaUseless = 1;//log("Info", "404 - Color not found :'(");
    }
}
