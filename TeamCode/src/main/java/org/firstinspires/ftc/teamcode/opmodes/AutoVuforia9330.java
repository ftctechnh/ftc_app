package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
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

    PictographScan9330 PictographScan = new PictographScan9330();
    HashMap hm = new HashMap();

    public void log(String name, Object value) {
        telemetry.addData(name,value);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");
        telemetry.update();
        VuforiaTrackables info = PictographScan.init(hardwareMap,true);
        log("Info","Initialized. Press start when ready.");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            hm = PictographScan.checkPosition(info);
            if (hm != null) {
                Set set = hm.entrySet();
                telemetry.clear();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    log(me.getKey().toString(), me.getValue());
                }
            } else
                log("Info","404 - Image not found ;(");
            telemetry.update();
        }
    }

}
