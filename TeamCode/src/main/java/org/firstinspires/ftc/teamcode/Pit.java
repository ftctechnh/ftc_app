package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Pit", group="Autonomous")
public class Pit extends baseAuto {
    @Override
    public void runOpMode() throws InterruptedException{
        dec(this.hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            downSeq();
            forwards(1);
            sleep(1000);
            forwards(0);
            pivOut(1);
            sleep(100);
            pivOut(0);
            sleep(50000);
            break;
        }
    }
}
