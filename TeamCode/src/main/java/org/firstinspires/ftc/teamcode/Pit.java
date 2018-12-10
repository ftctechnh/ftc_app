package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Pit", group="Autonomous")
public class Pit extends baseAuto {
    @Override
    public void runOpMode() throws InterruptedException{
        dec(this.hardwareMap);
        Door.setPosition(.55);
        waitForStart();
        while(opModeIsActive()) {
            downSeq();
            forwards(1);
            sleep(3000);
            forwards(0);
            break;
        }
    }
}
