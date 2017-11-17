package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/9/2017.
 */
@Autonomous(name="LeftAuto9330", group = "Opmode")
public class LeftAuto9330 extends LinearOpMode{
    Hardware9330 robotMap = new Hardware9330();

    @Override
    public void runOpMode() throws InterruptedException {
        robotMap.init(hardwareMap);
    }

    //Will rely on color sensor to detect side and then turn the right direction
}
