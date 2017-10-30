package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/30/2017.
 */

@Autonomous(name="ServoTestRun", group = "Opmode")
public class ServoTestRun extends LinearOpMode {
    Hardware9330 robotMap = new Hardware9330();

    @Override
    public void runOpMode() throws InterruptedException {
        robotMap.init(hardwareMap);

    }
}