package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.relicrecovery.JeffThePengwin;
import org.firstinspires.ftc.teamcode.relicrecovery.PengwinArm;
import org.firstinspires.ftc.teamcode.relicrecovery.PengwinFin;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="GyroOtherTest", group="Autonomisisisisis")
@Disabled
public class GyroOtherTest extends MeccyAutoMode {
    private ElapsedTime runtime = new ElapsedTime();
    //
    public void runOpMode() {
        //
        telemetry.addData("Hello", "World");
        //
        waitForStartify();
        //
        telemetry.addLine("Hello World!");
        //
        while (opModeIsActive()){
            telemetry.addData("Hello", "World");
            telemetry.update();
        }
    }
    //
}