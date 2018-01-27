package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Nora on 1/19/2018.
 */

//@Autonomous(name="SouthRed", group="AutoMode")
public class SouthRed extends RelicAutoMode {
    @Override
    public void runOpMode() {
        //
        waitForStartify();
        //
        int someVariable = testOutMoving(10);
        //
        while (opModeIsActive()){
            telemetry.addData("result", someVariable);
            telemetry.update();
        }
    }
}
