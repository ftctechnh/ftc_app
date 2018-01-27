package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by TPR on 12/14/17.
 * IMPORTANT
 * DEFAULT IS RED
 */
@Autonomous(name = "GyroAutoMode", group = "Auto")
public class GyroAutoTest extends MeccyAutoMode {
    //PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    //
    static double countify = 678;
    //
    public void runOpMode() {
        startify();
        //
        telemetry.addData("Why?", "");
        telemetry.update();
        //
        waitForStartify();
        //
        telemetry.addLine("Working!");
        telemetry.update();
        turnWithGyro(90, .4);
        telemetry.addLine("Not Working!");
        telemetry.update();
    }
    //
    public void startify (){
        //pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
    }
}

