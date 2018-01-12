package org.firstinspires.ftc.teamcode.relicrecoveryv2;

/**
 * Created by Eric on 1/6/2018.
 */

public class NorthRed extends RelicAutoMode {
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
