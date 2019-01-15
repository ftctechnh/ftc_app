package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Zone", group="Autonomous")
public class Zone extends baseAuto{
    @Override
    public void runOpMode() {
        dec(this.hardwareMap);
        gyro();
        waitForStart();
        while(opModeIsActive()) {
            nom.setPower(1);
            sleep(1000);
            break;
        }
    }
}
