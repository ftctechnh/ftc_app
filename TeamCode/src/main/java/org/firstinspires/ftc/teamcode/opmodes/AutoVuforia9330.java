package org.firstinspires.ftc.teamcode.opmodes;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.PictographScan9330;

/**
 * Created by robot on 9/25/2017.
 */

@Autonomous(name="AutoVuforia9330", group="Opmode")  // @Autonomous(...) is the other common choice
public class AutoVuforia9330 extends LinearOpMode {

    PictographScan9330 PictographScan = new PictographScan9330();

    public void log(String message) {
        telemetry.clear();
        telemetry.addData("Program",message);
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Initializing. Please wait.");
        VuforiaTrackable info = PictographScan.init(hardwareMap,true);
        log("Initialized. Press start when ready.");
        waitForStart();
        while(opModeIsActive()) {
            log(PictographScan.checkPosition(info).toString());
        }
    }

}
