//TODO Dis i' da wight 1
package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by thad on 10/24/2017.
 */

@TeleOp(name="WindMotor",group="mm" )

public class WindMotor extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        while (opModeIsActive()) {
            telemetry.addData("hi","you");
            telemetry.update();
        }


    }
}