package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="Startify", group="Autonomisisisisis")
@Disabled
public class Startify extends LinearOpMode {
    PengwinArm pengwinArm;
    PengwinFin pengwinFin;
    JeffThePengwin jeffThePengwin;
    private ElapsedTime runtime = new ElapsedTime();
    private void startify() {
        if(!jeffThePengwin.touchy.getState()){
            pengwinArm.setUpPower(-.4);
            while (!jeffThePengwin.touchy.getState() && opModeIsActive()) {
                //TODO Do nothing
            }
            pengwinArm.setUpPower(0);
            //pengwinArm.setHome();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        int forever = 10;//segundos
        int theAnswerToLifeTheUniverseAndEverything = 42;
        startify();
        runtime.reset();
        while(getRuntime()<forever){
            //do Stuff(nothing)
            telemetry.addData("touchy", jeffThePengwin.touchy.getState());
            telemetry.update();
        }
    }
}