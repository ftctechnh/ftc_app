package org.firstinspires.ftc.team11248.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team11248.Robot11248;
import org.firstinspires.ftc.team11248.Hardware.Vuforia_V2;

/**
 * Created by tonytesoriero on 9/11/17.
 */

@TeleOp( name = "Simplified VuForia Test")

public class VuforiaRelicOpMode extends OpMode {

    Robot11248 robot;
    Vuforia_V2 vuforia;

    @Override
    public void init() {
        robot = new Robot11248(hardwareMap, telemetry);
        robot.init();

        vuforia = new Vuforia_V2(hardwareMap);
        vuforia.init(true,true);

        vuforia.activateTracking();
    }

    @Override
    public void loop() {

        double[] pos = vuforia.getPosition();
        double[] rot = vuforia.getRotation();

        telemetry.addData("Column", vuforia.getLastImage().toString());
        telemetry.addData("Position", "{" + pos[0] + ", " + pos[1] + ", " + pos[2] +"}");
        telemetry.addData("Rotation", "{" + rot[0] + ", " + rot[1] + ", " + rot[2] +"}" );

    }
}
