package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Zone", group="Autonomous")
public class Zone extends baseAuto{
    @Override
    public void runOpMode() {
        dec(this.hardwareMap);
        Door.setPosition(.55);
        waitForStart();
        while(opModeIsActive()) {
            downSeq();
            forwards(1);

            break;
        }
    }
}
