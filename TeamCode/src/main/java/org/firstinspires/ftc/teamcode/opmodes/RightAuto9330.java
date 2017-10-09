package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/9/2017.
 */
@Autonomous(name="RightAuto9330", group = "Opmode")
public class RightAuto9330 extends LinearOpMode {
   private Hardware9330 hwMap = new Hardware9330();

    @Override
    public void runOpMode() throws InterruptedException {

    }

    //Will rely on cs to detect side and turn accordingly
}
