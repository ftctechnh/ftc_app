package org.firstinspires.ftc.teamcode.seasons.relicrecovery.summer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by ftc6347 on 6/20/17.
 */

@Autonomous (name = "Pixy Cam Test", group = "Tests")
public class PixyTest extends LinearOpMode {
    AnalogInput pixy;
    @Override
    public void runOpMode() throws InterruptedException {
        pixy = hardwareMap.analogInput.get("pixy");

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("X position", pixy.getVoltage());
            telemetry.update();
        }
    }
}
