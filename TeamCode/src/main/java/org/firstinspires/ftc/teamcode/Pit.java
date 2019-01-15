package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Pit", group="Autonomous")
public class Pit extends baseAuto {
    @Override
    public void runOpMode() throws InterruptedException{
        dec(this.hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            downSeq();
            forwards(-.4);
            sleep(1000);
            counter(.7);
            sleep(800);
            counter(0);
            pivOut(-.6);
            sleep(2000);
            pivOut(0);
            clock(.7);
            sleep(1500);
            forwards(.6);
            sleep(600);
            forwards(0);
            break;
        }
    }
}
