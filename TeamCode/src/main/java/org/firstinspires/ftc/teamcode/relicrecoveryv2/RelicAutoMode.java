package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by christinecarroll on 12/14/17.
 */

public class RelicAutoMode extends LinearOpMode {
    PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    @Override
    public void runOpMode() throws InterruptedException {

    }
    public void startify (){
        pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
    }


}
